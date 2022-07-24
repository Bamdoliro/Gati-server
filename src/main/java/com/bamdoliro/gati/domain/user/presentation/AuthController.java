package com.bamdoliro.gati.domain.user.presentation;

import com.bamdoliro.gati.domain.user.presentation.dto.request.LoginUserRequestDto;
import com.bamdoliro.gati.domain.user.presentation.dto.response.TokenResponseDto;
import com.bamdoliro.gati.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public TokenResponseDto loginUser(
            @RequestBody @Valid LoginUserRequestDto dto
    ) {
        return authService.loginUser(dto);
    }

    @DeleteMapping
    public void logoutUser() {
        authService.logoutUser();
    }

    @PutMapping
    public TokenResponseDto getNewAccessToken(
            @RequestHeader(value = "Refresh-Token") String refreshToken
    ) {
        return authService.getNewAccessToken(refreshToken);
    }
}
