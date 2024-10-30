package hana.simple.boardservice.global.exception.constant;

import hana.simple.boardservice.global.exception.ApplicationException;
import hana.simple.boardservice.global.response.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
@Slf4j
public class RestControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    private APIResponse<String> exceptionHandler(ApplicationException error) {

        log.info("=== Application Exception occurred !! error : {}", error);
        return APIResponse.error(error);

    }

}
