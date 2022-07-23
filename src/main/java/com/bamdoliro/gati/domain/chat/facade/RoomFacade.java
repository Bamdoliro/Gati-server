package com.bamdoliro.gati.domain.chat.facade;

import com.bamdoliro.gati.domain.chat.domain.Room;
import com.bamdoliro.gati.domain.chat.domain.repository.RoomRepository;
import com.bamdoliro.gati.domain.chat.domain.type.RoomStatus;
import com.bamdoliro.gati.domain.chat.exception.RoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomFacade {

    private final RoomRepository roomRepository;

    public Room findRoomById(Long id) {
        return roomRepository.findByIdAndStatus(id, RoomStatus.ACTIVATED)
                .orElseThrow(() -> RoomNotFoundException.EXCEPTION);
    }
}
