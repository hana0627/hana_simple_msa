package hana.simple.boardservice.api.feignclinet;

import hana.simple.boardservice.global.response.APIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/v3/writeable")
    APIResponse<Boolean> writeable(@RequestHeader("authorization") String authorization, @RequestHeader("userId") String userId);
}
