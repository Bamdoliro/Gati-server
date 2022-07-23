package com.bamdoliro.gati.domain.chat.domain.repository;

import com.bamdoliro.gati.domain.chat.domain.Room;
import com.bamdoliro.gati.domain.chat.domain.type.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByIdAndStatus(Long id, RoomStatus roomStatus);
}
