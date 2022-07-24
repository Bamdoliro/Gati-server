package com.bamdoliro.gati.domain.community.presentation;

import com.bamdoliro.gati.domain.community.presentation.dto.request.CreateCommunityRequestDto;
import com.bamdoliro.gati.domain.community.presentation.dto.request.UpdateCommunityRequestDto;
import com.bamdoliro.gati.domain.community.presentation.dto.response.CommunityDetailResponseDto;
import com.bamdoliro.gati.domain.community.presentation.dto.response.CommunityResponseDto;
import com.bamdoliro.gati.domain.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping
    public Page<CommunityResponseDto> getPagingCommunity(Pageable pageable) {
        return communityService.getPagingCommunity(pageable);
    }

    @GetMapping("/my")
    public List<CommunityResponseDto> getUserCommunity() {
        return communityService.getMyCommunity();
    }

    @GetMapping("/{id}")
    public CommunityDetailResponseDto getCommunityDetail(@PathVariable Long id) {
        return communityService.getCommunityDetail(id);
    }

    @GetMapping("/search")
    public List<CommunityResponseDto> searchCommunity(@RequestParam String name) {
        return communityService.searchCommunity(name);
    }

    @GetMapping("/code")
    public CommunityResponseDto getCommunityByCode(@RequestParam String code) {
        return communityService.getCommunityByCode(code);
    }

    @PostMapping
    public void createCommunity(@RequestBody @Valid CreateCommunityRequestDto dto) {
        communityService.createCommunity(dto);
    }

    @PutMapping
    public void updateCommunity(@RequestBody @Valid UpdateCommunityRequestDto dto) {
        communityService.updateCommunity(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCommunity(@PathVariable Long id) {
        communityService.deleteCommunity(id);
    }
}
