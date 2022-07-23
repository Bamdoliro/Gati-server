package com.bamdoliro.gati.domain.chat.presentation.dto.response;

import com.bamdoliro.gati.domain.chat.domain.Room;
import lombok.Getter;

@Getter
public class RoomResponseDto {

    private final Long id;
    private final int numberOfMembers;
    private final String name;

    public RoomResponseDto(Room room) {
        this.id = room.getId();
        this.numberOfMembers = room.getMembers().size();
        this.name = room.getName();
    }
}
