package com.bamdoliro.gati.domain.chat.service;

import com.bamdoliro.gati.domain.chat.domain.RoomMember;
import com.bamdoliro.gati.domain.chat.domain.Message;
import com.bamdoliro.gati.domain.chat.domain.Room;
import com.bamdoliro.gati.domain.chat.domain.repository.RoomRepository;
import com.bamdoliro.gati.domain.chat.domain.type.RoomStatus;
import com.bamdoliro.gati.domain.chat.facade.RoomFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] Chat Room")
class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomFacade roomFacade;

    private final Room defaultRoom = Room.builder()
            .name("귀요미팟")
            .build();

    @DisplayName("[Service] Chat Room 생성")
    @Test
    void givenRoomName_whenCreatingRoom_thenCreatesRoom() {
        // given
        given(roomRepository.save(any())).willReturn(defaultRoom);
        ArgumentCaptor<Room> captor = ArgumentCaptor.forClass(Room.class);

        // when
        roomService.createRoom("귀요미팟");

        // then
        verify(roomRepository, times(1)).save(captor.capture());
        Room savedRoom = captor.getValue();
        assertEquals("귀요미팟", savedRoom.getName());
        assertEquals(new ArrayList<RoomMember>(), savedRoom.getMembers());
        assertEquals(new ArrayList<Message>(), savedRoom.getMessages());
    }

    @Test
    @DisplayName("[Service] Room 정보 수정")
    void givenRoomIdAndUpdateName_whenUpdatingRoom_thenUpdatesRoomName() {
        // given
        given(roomFacade.findRoomById(any())).willReturn(defaultRoom);

        // when
        roomService.updateRoom(defaultRoom.getId(), "변경된 이름");

        // then
        assertEquals("변경된 이름", defaultRoom.getName());
    }

    @Test
    @DisplayName("[Service] Room 종료")
    void givenRoomId_whenEndingRoom_thenUpdatesStatusEnd() {
        // given
        given(roomFacade.findRoomById(any())).willReturn(defaultRoom);

        // when
        roomService.endRoom(defaultRoom.getId());

        // then
        assertEquals(RoomStatus.ENDED, defaultRoom.getStatus());
    }
}