package hana.simple.boardservice.api.reply.controller;

import hana.simple.boardservice.api.reply.controller.request.ReplyCreate;
import hana.simple.boardservice.api.reply.controller.request.ReplyUpdate;
import hana.simple.boardservice.api.reply.controller.response.ReplyInformation;
import hana.simple.boardservice.api.reply.service.ReplyService;
import hana.simple.boardservice.global.response.APIResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping("/v2/reply/{boardId}")
    public APIResponse<List<ReplyInformation>> getReplys(
            @PathVariable Long boardId
    ) {
        return APIResponse.success(replyService.getReplys(boardId));
    }

    @PostMapping("/v2/reply")
    public APIResponse<Long> replyCrete(
            HttpServletRequest request,
            @RequestBody ReplyCreate replyCreate
    ) {
        String userId = request.getHeader("userId");
        return APIResponse.success(replyService.create(userId, replyCreate));
    }

    @PatchMapping("/v2/reply")
    public APIResponse<Long> replyUpdate(
            HttpServletRequest request,
            @RequestBody ReplyUpdate replyUpdate
    ) {
        String userId = request.getHeader("userId");
        return APIResponse.success(replyService.update(userId, replyUpdate));
    }

    @DeleteMapping("/v2/reply/{replyId}")
    public APIResponse<Long> replyUpdate(
            HttpServletRequest request,
            @PathVariable Long replyId
    ) {
        String userId = request.getHeader("userId");
        return APIResponse.success(replyService.delete(userId, replyId));
    }
}
