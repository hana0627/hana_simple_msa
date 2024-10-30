package hana.simple.boardservice.global.response;

import hana.simple.boardservice.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class APIResponse<T> {
    private String resultCode;
    private T result;


    private APIResponse(String resultCode, T result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public String getResultCode() {
        return resultCode;
    }

    public T getResult() {
        return result;
    }

    public static <T> APIResponse<T> success(T result) {
        return new APIResponse<>(HttpStatus.OK.name(), result);
    }

    public static APIResponse<String> error(Object result) {
        if (result instanceof ApplicationException exception) {
            return new APIResponse<>(exception.getErrorCode().getStatus().name(), exception.getMessage());
        } else {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.name(), "알 수 없는 예외가 발생했습니다.");
        }
    }

}
