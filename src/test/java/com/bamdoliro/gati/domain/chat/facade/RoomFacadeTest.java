package com.bamdoliro.gati.domain.chat.facade;

import com.bamdoliro.gati.domain.chat.domain.Room;
import com.bamdoliro.gati.domain.chat.domain.repository.RoomRepository;
import com.bamdoliro.gati.domain.chat.domain.type.RoomStatus;
import com.bamdoliro.gati.domain.chat.exception.RoomNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Facade] Room")
class RoomFacadeTest {

    @InjectMocks
    private RoomFacade roomFacade;

    @Mock
    private RoomRepository roomRepository;

    private final Room defaultRoom = Room.builder()
            .name("귀요미팟")
            .build();

    @Test
    @DisplayName("[Facade] Room id로 찾기")
    void givenRoomId_whenFindingRoom_thenReturnsRoom() {
        // given
        given(roomRepository.findByIdAndStatus(any(), any(RoomStatus.class))).willReturn(Optional.of(defaultRoom));

        // when
        Room foundRoom = roomFacade.findRoomById(defaultRoom.getId());

        // then
        verify(roomRepository, times(1)).findByIdAndStatus(defaultRoom.getId(), RoomStatus.ACTIVATED);
        assertEquals(defaultRoom.getName(), foundRoom.getName());
        assertEquals(defaultRoom.getMembers(), foundRoom.getMembers());
        assertEquals(defaultRoom.getMessages(), foundRoom.getMessages());
        assertEquals(defaultRoom.getStatus(), foundRoom.getStatus());
    }

    @Test
    @DisplayName("[Facade] Room id로 찾기 - 없는 경우")
    void givenInvalidRoomId_whenFindingRoom_thenThrowsRoomNotFoundException() {
        // given
        given(roomRepository.findByIdAndStatus(any(), any(RoomStatus.class))).willReturn(Optional.empty());

        // when and then
        assertThrows(RoomNotFoundException.class,
                () -> roomFacade.findRoomById(defaultRoom.getId()));
        verify(roomRepository, times(1)).findByIdAndStatus(defaultRoom.getId(), RoomStatus.ACTIVATED);
    }
}