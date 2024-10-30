package hana.simple.boardservice.api.reply.repository;

import hana.simple.boardservice.api.reply.controller.response.ReplyInformation;
import hana.simple.boardservice.api.reply.domain.ReplyEntity;

public class ReplyMapper {

    public static ReplyInformation replyInformation(ReplyEntity reply) {

        return ReplyInformation.builder()
                .content(reply.getContent())
                .sequence(reply.getSequence())
                .createdId(reply.getCreateId())
                .createdAt(reply.getCreatedAt())
                .build();

    }

}
