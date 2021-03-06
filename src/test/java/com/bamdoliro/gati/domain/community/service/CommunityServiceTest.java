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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Service] Community")
class CommunityServiceTest {

    @InjectMocks
    private CommunityService communityService;

    @Mock private CommunityRepository communityRepository;
    @Mock private MemberService memberService;
    @Mock private CommunityFacade communityFacade;
    @Mock private UserFacade userFacade;
    @Mock private MemberFacade memberFacade;

    private final Community defaultCommunity = Community.builder()
            .name("?????????")
            .introduction("???")
            .numberOfPeople(100)
            .code("1A2B3C")
            .isPublic(true)
            .build();

    private final Community defaultPrivateCommunity = Community.builder()
            .name("????????????")
            .introduction("??????")
            .numberOfPeople(200)
            .code("A1B2C3")
            .isPublic(false)
            .password("1234")
            .build();

    private final User defaultUser = User.builder()
            .name("?????????")
            .email("gati@bamdoliro.com")
            .build();

    private final Member defaultMember = Member.builder()
            .community(defaultCommunity)
            .user(defaultUser)
            .authority(Authority.MEMBER)
            .build();

    private final Member defaultMemberInPrivate = Member.builder()
            .community(defaultPrivateCommunity)
            .user(defaultUser)
            .authority(Authority.MEMBER)
            .build();

    private final User leaderUser = User.builder()
            .name("?????????")
            .email("leader@bamdoliro.com")
            .build();

    private final Member leaderMember = Member.builder()
            .community(defaultCommunity)
            .user(leaderUser)
            .authority(Authority.LEADER)
            .build();


    @DisplayName("[Service] Community detail ??????")
    @Test
    void givenCommunityId_whenGettingCommunityDetail_thenGetsCommunityDetailResponseDto() {
        // given
        given(communityFacade.findCommunityById(defaultCommunity.getId())).willReturn(defaultCommunity);

        // when
        CommunityDetailResponseDto dto = communityService.getCommunityDetail(defaultCommunity.getId());

        // then
        verify(communityFacade, times(1)).findCommunityById(defaultCommunity.getId());
        assertEquals(defaultCommunity.getId(), dto.getId());
        assertEquals(defaultCommunity.getName(), dto.getName());
        assertEquals(defaultCommunity.getIntroduction(), dto.getIntroduction());
        assertEquals(defaultCommunity.getNumberOfPeople(), dto.getNumberOfPeople());
        assertEquals(defaultCommunity.getCode(), dto.getCode());
        assertEquals(defaultCommunity.getIsPublic(), dto.getIsPublic());
    }

    @DisplayName("[Service] Community code??? ??????")
    @Test
    void givenCommunityCode_whenFindingCommunity_thenGetsCommunity() {
        // given
        given(communityFacade.findCommunityByCode(defaultCommunity.getCode())).willReturn(defaultCommunity);

        // when
        CommunityResponseDto dto = communityService.getCommunityByCode(defaultCommunity.getCode());

        // then
        verify(communityFacade, times(1)).findCommunityByCode(defaultCommunity.getCode());
        assertEquals(dto.getId(), defaultCommunity.getId());
        assertEquals(dto.getName(), defaultCommunity.getName());
        assertEquals(dto.getMaxNumberOfPeople(), defaultCommunity.getNumberOfPeople());
    }

    @DisplayName("[Service] Public Community ??????")
    @Test
    void givenCreateCommunityRequestDto_whenCreatingPublicCommunity_thenCreatesCommunity() {
        // given
        given(communityRepository.save(any())).willReturn(defaultCommunity);
        given(communityFacade.checkCode(anyString())).willReturn(true);
        ArgumentCaptor<Community> captor = ArgumentCaptor.forClass(Community.class);
        willDoNothing().given(memberService).joinCommunity(defaultCommunity, Authority.LEADER);

        // when
        communityService.createCommunity(
                new CreateCommunityRequestDto(
                        "?????????",
                        "???",
                        100,
                        true,
                        null
                )
        );

        // then
        verify(communityRepository, times(1)).save(captor.capture());
        verify(memberService, times(1)).joinCommunity(defaultCommunity, Authority.LEADER);
        Community savedCommunity = captor.getValue();
        assertEquals("?????????", savedCommunity.getName());
        assertEquals("???", savedCommunity.getIntroduction());
        assertEquals(100, savedCommunity.getNumberOfPeople());
        assertEquals(6, savedCommunity.getCode().length());
        assertEquals(true, savedCommunity.getIsPublic());
        assertNull(savedCommunity.getPassword());
    }

    @DisplayName("[Service] Private Community ??????")
    @Test
    void givenCreateCommunityRequestDto_whenCreatingPrivateCommunity_thenCreatesCommunity() {
        // given
        given(communityRepository.save(any())).willReturn(defaultPrivateCommunity);
        given(communityFacade.checkCode(anyString())).willReturn(true);
        ArgumentCaptor<Community> captor = ArgumentCaptor.forClass(Community.class);
        willDoNothing().given(memberService).joinCommunity(defaultPrivateCommunity, Authority.LEADER);

        // when
        communityService.createCommunity(
                new CreateCommunityRequestDto(
                        "????????????",
                        "??????",
                        200,
                        false,
                        "1234"
                )
        );

        // then
        verify(communityRepository, times(1)).save(captor.capture());
        verify(memberService, times(1)).joinCommunity(defaultPrivateCommunity, Authority.LEADER);
        Community savedCommunity = captor.getValue();
        assertEquals("????????????", savedCommunity.getName());
        assertEquals("??????", savedCommunity.getIntroduction());
        assertEquals(200, savedCommunity.getNumberOfPeople());
        assertEquals(6, savedCommunity.getCode().length());
        assertEquals(false, savedCommunity.getIsPublic());
        assertEquals("1234", savedCommunity.getPassword());
    }

    @DisplayName("[Service] Community ??????")
    @Test
    void givenUpdateCommunityRequestDto_whenUpdatingCommunity_thenUpdatesCommunity() {
        // given
        given(communityFacade.findCommunityById(defaultCommunity.getId())).willReturn(defaultCommunity);
        given(userFacade.getCurrentUser()).willReturn(leaderUser);

        // when
        communityService.updateCommunity(
                new UpdateCommunityRequestDto(
                defaultCommunity.getId(),
                "???????????????",
                "??????????????????",
                false,
                "1234"
        ));

        // then
        verify(communityFacade, times(1)).findCommunityById(defaultMember.getId());
        verify(userFacade, times(1)).getCurrentUser();
        assertEquals("???????????????", defaultCommunity.getName());
        assertEquals("??????????????????", defaultCommunity.getIntroduction());
        assertFalse(defaultCommunity.getIsPublic());
        assertEquals("1234", defaultCommunity.getPassword());
    }

    @DisplayName("[Service] Community ??????")
    @Test
    void givenCommunityId_whenDeletingCommunity_thenDeletesCommunity() {
        // given
        given(communityFacade.findCommunityById(defaultCommunity.getId())).willReturn(defaultCommunity);
        given(userFacade.getCurrentUser()).willReturn(leaderUser);
        given(memberFacade.findMemberByUserAndCommunity(leaderUser, defaultCommunity)).willReturn(leaderMember);

        // when
        communityService.deleteCommunity(defaultMember.getId());

        // then
        assertEquals(CommunityStatus.DELETED, defaultCommunity.getCommunityStatus());
    }
}