package com.bamdoliro.gati.domain.community.service;

import com.bamdoliro.gati.domain.community.domain.Community;
import com.bamdoliro.gati.domain.community.domain.Member;
import com.bamdoliro.gati.domain.community.domain.repository.CommunityRepository;
import com.bamdoliro.gati.domain.community.domain.type.Authority;
import com.bamdoliro.gati.domain.community.domain.type.CommunityStatus;
import com.bamdoliro.gati.domain.community.facade.CommunityFacade;
import com.bamdoliro.gati.domain.community.facade.MemberFacade;
import com.bamdoliro.gati.domain.community.presentation.dto.request.CreateCommunityRequestDto;
import com.bamdoliro.gati.domain.community.presentation.dto.request.UpdateCommunityRequestDto;
import com.bamdoliro.gati.domain.community.presentation.dto.response.CommunityDetailResponseDto;
import com.bamdoliro.gati.domain.community.presentation.dto.response.CommunityResponseDto;
import com.bamdoliro.gati.domain.user.domain.User;
import com.bamdoliro.gati.domain.user.facade.UserFacade;
import com.bamdoliro.gati.global.utils.RandomCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.bamdoliro.gati.domain.community.domain.property.CommunityProperty.MAX_NUMBER_OF_MEMBER_WHEN_DELETE;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final MemberService memberService;
    private final CommunityFacade communityFacade;
    private final UserFacade userFacade;
    private final MemberFacade memberFacade;

    @Transactional(readOnly = true)
    public Page<CommunityResponseDto> getPagingCommunity(Pageable pageable) {
        return communityRepository.findAll(pageable)
                .map(CommunityResponseDto::of);
    }

    @Transactional(readOnly = true)
    public List<CommunityResponseDto> getMyCommunity() {
        User user = userFacade.getCurrentUser();

        return user.getCommunities().stream()
                .map(Member::getCommunity)
                .map(CommunityResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommunityDetailResponseDto getCommunityDetail(Long id) {
        return CommunityDetailResponseDto.of(
                communityFacade.findCommunityById(id));
    }

    @Transactional(readOnly = true)
    public List<CommunityResponseDto> searchCommunity(String name) {
        return communityRepository.findByNameContainingAndCommunityStatus(name, CommunityStatus.EXISTED).stream()
                .map(CommunityResponseDto::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommunityResponseDto getCommunityByCode(String code) {
        return CommunityResponseDto.of(communityFacade.findCommunityByCode(code));
    }

    @Transactional
    public void createCommunity(CreateCommunityRequestDto dto) {
        communityFacade.checkPasswordAndCommunityType(dto.getPassword(), dto.getIsPublic());

        memberService.joinCommunity(
                communityRepository.save(
                        dto.toEntity(createUniqueCode())
                ),
                Authority.LEADER
        );
    }


    private String createUniqueCode() {
        String code = RandomCodeUtil.make(6);
        while (!communityFacade.checkCode(code)) {
            code = RandomCodeUtil.make(6);
        }

        return code;
    }

    @Transactional
    public void updateCommunity(UpdateCommunityRequestDto dto) {
        Community community = communityFacade.findCommunityById(dto.getId());
        memberFacade.checkMemberAuthority(
                memberFacade.findMemberByUserAndCommunity(userFacade.getCurrentUser(), community),
                Authority.LEADER
        );
        communityFacade.checkPasswordAndCommunityType(dto.getPassword(), dto.getIsPublic());

        community.updateCommunity(dto.getName(), dto.getIntroduction(), dto.getIsPublic(), dto.getPassword());
    }
  
    @Transactional
    public void deleteCommunity(Long id) {
        Community community = communityFacade.findCommunityById(id);
        communityFacade.checkNumberOfMembers(community, MAX_NUMBER_OF_MEMBER_WHEN_DELETE);
        memberFacade.checkMemberAuthority(
                memberFacade.findMemberByUserAndCommunity(userFacade.getCurrentUser(), community),
                Authority.LEADER
        );

        community.deleteCommunity();
    }

}