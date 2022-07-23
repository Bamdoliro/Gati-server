package com.bamdoliro.gati.domain.chat.presentation;

import com.bamdoliro.gati.domain.chat.presentation.dto.response.RoomResponseDto;
import com.bamdoliro.gati.domain.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PutMapping("/{roomId}")
    public void updateRoom(@PathVariable Long roomId, @RequestParam String name) {
        roomService.updateRoom(roomId, name);
    }

    @GetMapping
    public List<RoomResponseDto> getUserRoom() {
        return roomService.getUserRoom();
    }

    @DeleteMapping("/{roomId}")
    public void endRoom(@PathVariable Long roomId) {
        roomService.endRoom(roomId);
    }
}
