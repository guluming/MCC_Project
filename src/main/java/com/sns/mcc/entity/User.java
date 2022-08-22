package com.sns.mcc.entity;

import com.sns.mcc.dto.request.UserSignUpRequest;
import com.sns.mcc.enums.UserRole;
import com.sns.mcc.utill.TimeStamped;
import com.sns.mcc.validator.UserValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concern_id")
    private Concern concern;

    @Column(unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    public User(UserSignUpRequest param, UserRole role) {

        UserValidator.validateUsername(param.getUsername());
        UserValidator.validatePassword(param.getPassword());

        this.username = param.getUsername();
        this.password = param.getPassword();
        this.role = role;
    }

    public void newUserUpdate(String nickName, Concern concern) {
        this.nickname = nickName;
        this.concern = concern;
    }

    public void userUpdate(String nickName) {
        this.nickname = nickName;
    }

    public void encryptPassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }
}
