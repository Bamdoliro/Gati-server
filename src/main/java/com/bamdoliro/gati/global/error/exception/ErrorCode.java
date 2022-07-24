package com.bamdoliro.gati.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "서버에 오류가 발생했습니다."),
    BAD_REQUEST(400, "잘못된 요청입니다."),

    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(422, "사용자가 이미 존재합니다."),
    PASSWORD_MISMATCH(401, "비밀번호가 틀렸습니다."),

    EXPIRED_TOKEN(401, "만료된 토큰입니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),

    COMMUNITY_NOT_FOUND(404, "공동체를 찾을 수 없습니다."),
    COMMUNITY_PASSWORD_MISMATCH(401, "커뮤니티 비밀번호가 틀렸습니다."),
    USER_NOT_COMMUNITY_MEMBER(422, "사용자가 해당 커뮤니티 멤버가 아닙니다."),
    MEMBER_NOT_FOUND(404, "멤버를 찾을 수 없습니다."),
    AUTHORITY_MISMATCH(401, "권한이 없습니다."),
    CANNOT_DELETE_COMMUNITY(401, "멤버가 2 명 이하일 때만 삭제할 수 있습니다."),

    BOARD_NOT_FOUND(404,"게시물을 찾을 수 없습니다."),
    LIKE_OVERLAP(422, "좋아요는 한 번만 가능합니다."),
    REPORT_OVERLAP(422, "신고는 한 게시물 당 한 번만 가능합니다."),

    ROOM_NOT_FOUND(404, "채팅방을 찾을 수 없습니다."),

    DDO_NOT_FOUND(404, "뚜를 찾을 수 없습니다."),
    ALREADY_JOIN(422, "이미 참여중입니다."),
    EXCESS_OF_CAPACITY(422, "정원이 초과되었습니다.");

    ;

    private final int status;
    private final String message;
}
