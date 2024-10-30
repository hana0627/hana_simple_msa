package hana.simple.boardservice.api.reply.controller;

import hana.simple.boardservice.api.reply.controller.request.ReplyCreate;
import hana.simple.boardservice.api.reply.controller.request.ReplyUpdate;
import hana.simple.boardservice.api.reply.controller.response.ReplyInformation;
import hana.simple.boardservice.api.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping("/v2/reply/{boardId}")
    public Map<String, Object> getReplys(
            @PathVariable Long boardId
    ) {
        List<ReplyInformation> result = replyService.getReplys(boardId);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",result);
        return resultMap;
    }

    @PostMapping("/v2/reply")
    public Map<String, Object> replyCrete(
            @RequestBody ReplyCreate replyCreate
    ) {
        Long result = replyService.create(replyCreate);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",result);
        return resultMap;
    }

    @PatchMapping("/v2/reply")
    public Map<String, Object> replyUpdate(
            @RequestBody ReplyUpdate replyUpdate
    ) {
        Long result = replyService.update(replyUpdate);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",result);
        return resultMap;
    }

    @DeleteMapping("/v2/reply/{replyId}")
    public Map<String, Object> replyUpdate(
            @PathVariable Long replyId
    ) {
        Long result = replyService.delete(replyId);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",result);
        return resultMap;
    }
}
