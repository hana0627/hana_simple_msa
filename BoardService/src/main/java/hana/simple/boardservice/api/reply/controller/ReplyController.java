package hana.simple.boardservice.api.reply.controller;

import hana.simple.boardservice.api.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReplyController {
    private ReplyService replyService;
}
