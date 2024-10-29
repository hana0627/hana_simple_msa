package hana.simple.boardservice.api.reply.service.impl;

import hana.simple.boardservice.api.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyService implements hana.simple.boardservice.api.reply.service.ReplyService {
    private ReplyRepository replyRepository;

}
