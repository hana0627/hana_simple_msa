package hana.simple.boardservice.api.reply.controller;

import hana.simple.boardservice.api.reply.controller.request.ReplyCreate;
import hana.simple.boardservice.api.reply.controller.request.ReplyUpdate;
import hana.simple.boardservice.api.reply.controller.response.ReplyInformation;
import hana.simple.boardservice.api.reply.service.ReplyService;
import hana.simple.boardservice.global.response.APIResponse;
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
            @RequestBody ReplyCreate replyCreate
    ) {
        return APIResponse.success(replyService.create(replyCreate));
    }

    @PatchMapping("/v2/reply")
    public APIResponse<Long> replyUpdate(
            @RequestBody ReplyUpdate replyUpdate
    ) {
        return APIResponse.success(replyService.update(replyUpdate));
    }

    @DeleteMapping("/v2/reply/{replyId}")
    public APIResponse<Long> replyUpdate(
            @PathVariable Long replyId
    ) {
        return APIResponse.success(replyService.delete(replyId));
    }
}
