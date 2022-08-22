package com.sns.mcc.service;

import com.sns.mcc.dto.request.*;
import com.sns.mcc.dto.response.JwtResponse;
import com.sns.mcc.dto.response.UserDuplicateResponse;
import com.sns.mcc.dto.response.UserLoginResponse;
import com.sns.mcc.entity.Auth;
import com.sns.mcc.entity.Concern;
import com.sns.mcc.entity.User;
import com.sns.mcc.enums.ErrorCode;
import com.sns.mcc.enums.UserRole;
import com.sns.mcc.repository.AuthRepository;
import com.sns.mcc.repository.ConcernRepository;
import com.sns.mcc.repository.UserRepository;
import com.sns.mcc.security.UserDetailsImpl;
import com.sns.mcc.security.jwt.JwtTokenProvider;
import com.sns.mcc.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final ConcernRepository concernRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Transactional
    public ResponseEntity<Object> userSignup(UserSignUpRequest userSignUpRequest) {
        boolean usernameCheck = this.duplicateCheckUsername(userSignUpRequest.getUsername()).isExist();
        if (usernameCheck) {
            return new ResponseEntity<>("사용 불가능한 아이디 입니다.", HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<Object> userLogin(UserLoginRequest userLoginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword()));
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorCode.USERNAME_OR_PASSWORD_NOTFOUND.getMessage(), ErrorCode.USERNAME_OR_PASSWORD_NOTFOUND.getStatus());
        }

        Optional<User> user = userRepository.findByUsername(userLoginRequest.getUsername());
        if (user.isEmpty()) {
            return new ResponseEntity<>("등록되지 않은 회원입니다.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(jwtTokenCreate(user.get()), HttpStatus.CREATED);
    }

    //회원정보 수정
    @Transactional
    public ResponseEntity<Object> userEdit(UserDetailsImpl userDetails, UserEditRequest userEditRequest) {
        boolean nicknameCheck = this.duplicateCheckUsername(userEditRequest.getNickname()).isExist();
        if (nicknameCheck) {
            return new ResponseEntity<>("사용 불가능한 닉네임 입니다.", HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        if (user.isEmpty()) {
            return new ResponseEntity<>("등록되지 않은 회원 입니다.", HttpStatus.BAD_REQUEST);
        }

        if (user.get().getConcern() == null) {
            final Concern emptyConcern = concernRepository.save(new Concern());
            user.get().newUserUpdate(userEditRequest.getNickname(), emptyConcern);
        } else {
            user.get().userUpdate(userEditRequest.getNickname());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Access 토큰 재발급
    public ResponseEntity<?> newAccessToken(NewTokenRequest newTokenRequest) {
        JwtResponse jwtResponse = new JwtResponse();

        Optional<Auth> refreshToken = authRepository.findByRefreshToken(newTokenRequest.getRefreshtoken());
        if (refreshToken.isEmpty()) {
            jwtResponse.setCode(ErrorCode.UNAUTHORIZED.getCode());
            jwtResponse.setMessage(ErrorCode.UNAUTHORIZED.getMessage());
            jwtResponse.setErrorStstus(ErrorCode.UNAUTHORIZED.getStatus());
            return new ResponseEntity<>(jwtResponse, HttpStatus.UNAUTHORIZED);
        }

        if (jwtTokenProvider.validateToken(refreshToken.get().getRefreshToken())) {

            Optional<User> user = userRepository.findByUsername(refreshToken.get().getUsername());
            if (user.isEmpty()) {
                return new ResponseEntity<>(" 등록되지 않은 회원 입니다.", HttpStatus.BAD_REQUEST);
            }

            String accessToken = jwtTokenProvider.createAccessToken(user.get());
            jwtResponse.setAccessToken(accessToken);
            jwtResponse.setRefreshToken(newTokenRequest.getRefreshtoken());
            jwtResponse.setMessage("새로운 AccessToken이 발급 되었습니다.");
        }

        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }

    //JWT 토큰 생성기
    public UserLoginResponse jwtTokenCreate(User user) {
        UserLoginResponse userLoginResponse = new UserLoginResponse();

        if (user.getConcern() == null) {
            String accessToken = jwtTokenProvider.createAccessToken(user);
            String refreshToken = jwtTokenProvider.createRefreshToken(user);

            Auth auth = new Auth(user.getUsername(), refreshToken);
            authRepository.save(auth);

            userLoginResponse.setAccessToken(accessToken);
            userLoginResponse.setRefreshToken(refreshToken);
            userLoginResponse.setFirst(true);
        } else {
            String accessToken = jwtTokenProvider.createAccessToken(user);
            String refreshToken = jwtTokenProvider.createRefreshToken(user);

            Auth auth = new Auth(user.getUsername(), refreshToken);
            authRepository.save(auth);

            userLoginResponse.setAccessToken(accessToken);
            userLoginResponse.setRefreshToken(refreshToken);
        }
        return userLoginResponse;
    }
}
