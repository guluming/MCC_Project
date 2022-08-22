package com.sns.mcc.service;

import com.sns.mcc.dto.request.*;
import com.sns.mcc.dto.response.UserDuplicateResponse;
import com.sns.mcc.entity.User;
import com.sns.mcc.enums.UserRole;
import com.sns.mcc.repository.UserRepository;
import com.sns.mcc.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional
    public ResponseEntity<Object> userSignup(UserSignUpRequest userSignUpRequest) {
        boolean usernameCheck = this.duplicateCheckUsername(userSignUpRequest.getUsername()).isExist();
        boolean nicknameCheck = this.duplicateCheckNickname(userSignUpRequest.getNickname()).isExist();
        if (usernameCheck) {
            return new ResponseEntity<>("사용 불가능한 아이디 입니다.", HttpStatus.BAD_REQUEST);
        } else if (nicknameCheck) {
            return new ResponseEntity<>("사용 불가능한 닉네임 입니다.", HttpStatus.BAD_REQUEST);
        } else if (!userSignUpRequest.getPassword().equals(userSignUpRequest.getPasswordChk())) {
            return new ResponseEntity<>("비밀번호와 재확인 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        } else {
            UserRole role = UserRole.USER;

            User beforeSaveUser = new User(userSignUpRequest, role);
            beforeSaveUser.encryptPassword(passwordEncoder);
            userRepository.save(beforeSaveUser);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //아이디 중복 확인
    public UserDuplicateResponse duplicateCheckUsername(String username) {
        Optional<User> usernameCheck = userRepository.findByUsername(username);
        UserDuplicateResponse userDuplicateResponse = new UserDuplicateResponse();

        if (usernameCheck.isPresent()) {
            userDuplicateResponse.setExist(true);
            userDuplicateResponse.setMessage("중복된 아이디 입니다.");
        } else {
            UserValidator.validateUsername(username);
            userDuplicateResponse.setExist(false);
            userDuplicateResponse.setMessage("사용 가능한 아이디 입니다.");
        }
        return userDuplicateResponse;
    }

    //닉네임 중복 확인
    public UserDuplicateResponse duplicateCheckNickname(String nickname) {
        Optional<User> nicknameCheck = userRepository.findByNickname(nickname);
        UserDuplicateResponse userDuplicateResponse = new UserDuplicateResponse();

        if (nicknameCheck.isPresent()) {
            userDuplicateResponse.setExist(true);
            userDuplicateResponse.setMessage("중복된 닉네임 입니다.");
        } else {
            UserValidator.validateNickname(nickname);
            userDuplicateResponse.setExist(false);
            userDuplicateResponse.setMessage("사용 가능한 닉네임 입니다.");
        }
        return userDuplicateResponse;
    }

    //로그인
    public ResponseEntity<?> userLogin(UserLoginRequest userLoginRequest) {

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //회원정보 수정
    @Transactional
    public ResponseEntity<?> userEdit(UserEditRequest userEditRequest) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
