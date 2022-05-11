package org.minsirv.restApi.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.minsirv.restApi.controller.response.ValidationSummary;
import org.minsirv.restApi.entity.NationalId;
import org.minsirv.restApi.entity.ValidationError;
import org.minsirv.restApi.enumerators.Messages;
import org.minsirv.restApi.exceptions.InvalidNationalIdException;
import org.minsirv.restApi.repository.filters.NationalIdFilter;
import org.minsirv.restApi.service.NationalIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/nationalId")
public class NationalIdController {

    private final Logger logger = LogManager.getLogger(NationalIdController.class);

    @Autowired
    NationalIdService service;

    @GetMapping("/get/{id}")
    public NationalId getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping("/validate/{nationalId}")
    public String validateSingle(@PathVariable String nationalId) {
        NationalId entity = service.validateNationalId(nationalId);
        if (!CollectionUtils.isEmpty(entity.getValidationErrors())) {
            throw new InvalidNationalIdException(entity.getValidationErrors().stream().map(ValidationError::getErrorMessage).collect(Collectors.toList()));
        }
        return Messages.ID_VALID.getValue();
    }

    @PostMapping("/validate/list")
    public ValidationSummary validateList(@RequestBody List<String> nationalIdList) {
        return service.validateBatch(nationalIdList);
    }

    @PostMapping("/validate/file")
    public ValidationSummary validateFile(@RequestParam("file") MultipartFile file) {
        return service.readNationalIdFile(file);
    }

    @PostMapping("/search")
    public List<NationalId> searchNationalId(@RequestBody NationalIdFilter filter) {
        return service.search(filter);
    }
}
