//package com.codechallengebackend.demo.bank.application;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Slf4j
//@ControllerAdvice
//@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8)
//public class ExceptionController {
//
//    private final ObjectMapper objectMapper;
//
//    public ExceptionController(ObjectMapper objectMapper) {
//
//        this.objectMapper = objectMapper;
//    }
//
//    @ExceptionHandler(GcpAuthorisationException.class)
//    public ResponseEntity<String> gcpAuthorisationException(final GcpAuthorisationException exception) throws JsonProcessingException {
//
//        HttpStatus statusCode = exception.getStatusCode();
//        if(statusCode == null) {
//            statusCode = HttpStatus.FORBIDDEN;
//        }
//
//        return parseError(new ApiErrorResponse(ErrorCode.GCP_AUTHORISATION_EXCEPTION.code(), exception.getMessage()), statusCode);
//    }
//
//    private ResponseEntity<String> parseError(final ApiErrorResponse errorResponse, final HttpStatus status) throws JsonProcessingException {
//
//        final String exceptionAsString = objectMapper.writeValueAsString(errorResponse);
//        log.error("The service threw an exception:\n".concat(exceptionAsString));
//
//        return new ResponseEntity<>(exceptionAsString, status);
//    }
//}
