package org.minsirv.restApi;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.minsirv.enumerators.ResponseCodes;
import org.minsirv.restApi.controller.response.ValidationSummary;
import org.minsirv.restApi.entity.NationalId;
import org.minsirv.restApi.enumerators.Messages;
import org.minsirv.restApi.repository.filters.NationalIdFilter;
import org.minsirv.restApi.util.builder.NationalIdFilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class NationalIdControllerTest {

    private final Logger logger = LogManager.getLogger(NationalIdControllerTest.class);

    @Value("classpath:in_test.txt")
    private Resource testFile;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void validateAndGetTest(){
        String nationalIdValid = "35610261630";
        String nationalIdInvalid = "67303142133";

        String validResponse = restTemplate.postForObject("https://localhost:" + port + "/nationalId/validate/" + nationalIdValid, null, String.class);
        String invalidResponse = restTemplate.postForObject("https://localhost:" + port + "/nationalId/validate/" + nationalIdInvalid, null, String.class);
        assertEquals(Messages.ID_VALID.getValue(), validResponse);
        assertEquals(ResponseCodes.INVALID_CONTROL_CODE.toString(), invalidResponse);

        NationalId valid = restTemplate.getForObject("https://localhost:" + port + "/nationalId/get/" + nationalIdValid, NationalId.class);
        NationalId invalid = restTemplate.getForObject("https://localhost:" + port + "/nationalId/get/" + nationalIdInvalid, NationalId.class);
        assertTrue(valid.getGender() != null && valid.getDateOfBirth() != null);
        assertEquals(1, invalid.getValidationErrors().size());
    }

    @Test
    public void notFoundTest() {
        ResponseEntity<String> response = restTemplate.getForEntity("https://localhost:" + port + "/nationalId/get/1", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void validateListTest() {
        List<String> listToValidate = new ArrayList<>();
        listToValidate.addAll(Arrays.asList("48010016799", "45902209168","49001165600","52509152095","39403027690"));

        ValidationSummary validationSummary = restTemplate.postForObject("https://localhost:" + port + "/nationalId/validate/list", listToValidate, ValidationSummary.class);

        assertEquals(Messages.SOME_INVALID.getValue(), validationSummary.getSummary());
        assertEquals(3, validationSummary.getValidNationalIdList().size());
        assertEquals(2, validationSummary.getInvalidNationalIdList().size());
    }

    @Test
    public void validateFileTest() {
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", testFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ValidationSummary validationSummary = restTemplate.postForObject("https://localhost:" + port + "/nationalId/validate/file", requestEntity, ValidationSummary.class);
        assertEquals(Messages.SOME_INVALID.getValue(), validationSummary.getSummary());
        assertEquals(87, validationSummary.getValidNationalIdList().size());
        assertEquals(911, validationSummary.getInvalidNationalIdList().size());
    }

    @Test
    public void searchTest() throws ParseException {
        Date from = DateUtils.parseDate("1946-01-01", "yyyy-MM-dd");
        Date to = DateUtils.parseDate("1947-01-01", "yyyy-MM-dd");
        List<String> listToValidate = new ArrayList<>();

//        1944-09-15
//        1946-02-20
//        1946-08-05
//        2054-04-19
//        2057-10-19
//        2066-09-04
//        2068-05-09
        listToValidate.addAll(Arrays.asList("34409157850", "44602205109", "34608056440", "65404197310", "55710193990", "66609044130", "66805096280"));
        restTemplate.postForObject("https://localhost:" + port + "/nationalId/validate/list", listToValidate, ValidationSummary.class);
        NationalIdFilter filter = new NationalIdFilterBuilder().from(from).to(to).build();
        NationalId[] valid = restTemplate.postForObject("https://localhost:" + port + "/nationalId/search", filter, NationalId[].class);
        for(NationalId nationalId : valid) {
            assertTrue(nationalId.getDateOfBirth().after(from) && nationalId.getDateOfBirth().before(to));
        }
    }
}
