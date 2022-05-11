package org.minsirv.restApi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minsirv.JarValidatorFactory;
import org.minsirv.JarValidator;
import org.minsirv.restApi.controller.response.ValidationSummary;
import org.minsirv.restApi.entity.NationalId;
import org.minsirv.restApi.entity.ValidationError;
import org.minsirv.restApi.enumerators.Gender;
import org.minsirv.restApi.exceptions.NationalIdNotFoundException;
import org.minsirv.restApi.exceptions.ProcessingNationalIdException;
import org.minsirv.restApi.exceptions.ReadFileException;
import org.minsirv.restApi.repository.NationalIdRepository;
import org.minsirv.restApi.repository.filters.NationalIdFilter;
import org.minsirv.restApi.util.builder.NationalIdFilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class NationalIdService {
    private final Logger logger = LogManager.getLogger(NationalIdService.class);

    @Autowired
    NationalIdRepository repository;

    public NationalId getById(String id) {
        NationalId entity = repository.getById(id);
        if (entity == null)
            throw new NationalIdNotFoundException();
        return entity;
    }

    public List<NationalId> findAll() {
        return repository.findAll();
    }

    public NationalId save(NationalId nationalId) {
        return repository.save(nationalId);
    }

    public ValidationSummary validateBatch(List<String> nationalIdList) {
        ValidationSummary summary = new ValidationSummary();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Callable<NationalId>> tasks = new ArrayList<>();
        for (String object: nationalIdList) {
            Callable<NationalId> c = () -> validateNationalId(object);
            tasks.add(c);
        }
        try {
            long start = Instant.now().getEpochSecond();
            List<Future<NationalId>> results = executor.invokeAll(tasks);
            awaitTerminationAfterShutdown(executor);
            for (Future<NationalId> fr : results) {
                NationalId nationalIdEntity = fr.get();
                if (CollectionUtils.isEmpty(nationalIdEntity.getValidationErrors())) {
                    summary.getValidNationalIdList().add(nationalIdEntity);
                } else {
                    summary.getInvalidNationalIdList().add(nationalIdEntity);
                }
            }
            long elapsed = Instant.now().getEpochSecond() - start;
            logger.info(String.format("Processed %d items in %d s", nationalIdList.size(), elapsed));

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Failed to process national Id's in parallel!");
            e.printStackTrace();
            throw new ProcessingNationalIdException("");
        }
        return summary;
    }

    public void awaitTerminationAfterShutdown(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


    public NationalId validateNationalId(String nationalId) {
        JarValidator validator = JarValidatorFactory.createJarValidator();
        List<String> isValid = validator.validateVerbose(nationalId);

        NationalId nationalIdEntity = new NationalId();
        nationalIdEntity.setId(nationalId);
        if (CollectionUtils.isEmpty(isValid)) {
            nationalIdEntity.setDateOfBirth(validator.toDate(nationalId));
            nationalIdEntity.setGender(validator.isMale(nationalId) ? Gender.MALE : Gender.FEMALE);
        } else {
            nationalIdEntity.setValidationErrors(isValid.stream().map(e -> new ValidationError(nationalId, e, e)).collect(Collectors.toList()));
        }

        repository.save(nationalIdEntity);

        return nationalIdEntity;
    }

    @Scheduled(cron = "${cleanupJob.schedule:-}")
    private void cleanupJob() {
        NationalIdFilter filter = new NationalIdFilterBuilder().hasErrors(true).build();
        List<NationalId> list = repository.search(filter);
        list.forEach(repository::remove);
        logger.info("executing cleanup job. cleared record no.: " + list.size());
    }

    public ValidationSummary readNationalIdFile(MultipartFile file) {
        ValidationSummary summary;
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            List<String> nationalIdList = new ArrayList<>();
            String line;
            while((line = reader.readLine()) != null) {
                nationalIdList.add(line);
            }
            summary = validateBatch(nationalIdList);
        } catch (IOException e) {
            logger.error("failed to read file!");
            e.printStackTrace();
            throw new ReadFileException("");
        }
        return summary;
    }

    public List<NationalId> search(NationalIdFilter filter) {
        return repository.search(filter);
    }
}
