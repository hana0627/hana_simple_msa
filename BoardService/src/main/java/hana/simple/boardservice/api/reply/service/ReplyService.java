package hana.simple.boardservice.api.reply.service;


import hana.simple.boardservice.api.reply.controller.request.ReplyCreate;
import hana.simple.boardservice.api.reply.controller.request.ReplyUpdate;
import hana.simple.boardservice.api.reply.controller.response.ReplyInformation;

import java.util.List;

public interface ReplyService {

    List<ReplyInformation> getReplys(Long boardId);

    Long create(String userId, ReplyCreate replyCreate);

    Long update(String userId, ReplyUpdate replyUpdate);

    Long delete(String userId, Long replyId);
}
