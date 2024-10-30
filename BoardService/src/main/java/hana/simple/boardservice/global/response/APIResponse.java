package hana.simple.boardservice.global.response;

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

}
