package com.bamdoliro.gati.domain.community.presentation;

import com.bamdoliro.gati.domain.community.domain.type.Authority;
import com.bamdoliro.gati.domain.community.presentation.dto.request.JoinCommunityRequestDto;
import com.bamdoliro.gati.domain.community.presentation.dto.request.ChangeCommunityLeaderRequestDto;
import com.bamdoliro.gati.domain.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/community/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public void joinCommunity(@RequestBody @Valid JoinCommunityRequestDto dto) {
        memberService.joinCommunity(dto, Authority.MEMBER);
    }

    @PutMapping("/leader")
    public void updateCommunityLeader(@RequestBody ChangeCommunityLeaderRequestDto dto) {
        memberService.changeCommunityLeader(dto);
    }

    @DeleteMapping("/{communityId}")
    public void leaveCommunity(@PathVariable Long communityId) {
        memberService.leaveCommunity(communityId);
    }
}
