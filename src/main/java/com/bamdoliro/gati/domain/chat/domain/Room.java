package com.bamdoliro.gati.domain.chat.domain;

import com.bamdoliro.gati.domain.chat.domain.type.RoomStatus;
import com.bamdoliro.gati.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private RoomStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    private List<RoomMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @Builder
    public Room(String name) {
        this.name = name;
        this.status = RoomStatus.ACTIVATED;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void endRoom() {
        this.status = RoomStatus.ENDED;
    }
}
