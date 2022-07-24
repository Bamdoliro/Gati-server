package com.bamdoliro.gati.domain.user.service;

import com.bamdoliro.gati.domain.user.domain.User;
import com.bamdoliro.gati.domain.user.domain.repository.UserRepository;
import com.bamdoliro.gati.domain.user.facade.UserFacade;
import com.bamdoliro.gati.domain.user.presentation.dto.request.CreateUserRequestDto;
import com.bamdoliro.gati.domain.user.presentation.dto.request.DeleteUserRequestDto;
import com.bamdoliro.gati.domain.user.presentation.dto.request.UpdateUserNameRequestDto;
import com.bamdoliro.gati.domain.user.presentation.dto.request.UpdateUserPasswordRequestDto;
import com.bamdoliro.gati.domain.user.presentation.dto.response.GetUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFacade userFacade;

    @Transactional
    public void createUser(CreateUserRequestDto dto) {
        userFacade.checkUserByEmail(dto.getEmail());

        userRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
    }

    @Transactional(readOnly = true)
    public GetUserResponseDto getUserInformation() {
        User user = userFacade.getCurrentUser();

        return GetUserResponseDto.of(user);
    }

    @Transactional
    public void updateUserPassword(UpdateUserPasswordRequestDto dto) {
        User user = userFacade.getCurrentUser();

        userFacade.checkUserPassword(user, dto.getCurrentPassword());
        user.updatePassword(passwordEncoder.encode(dto.getPassword()));
    }

    @Transactional
    public void updateUserName(UpdateUserNameRequestDto dto) {
        User user = userFacade.getCurrentUser();

        user.updateName(dto.getName());
    }

    @Transactional
    public void deleteUser(DeleteUserRequestDto dto) {
        User user = userFacade.getCurrentUser();

        userFacade.checkUserPassword(user, dto.getPassword());
        userRepository.delete(user);
    }
}
