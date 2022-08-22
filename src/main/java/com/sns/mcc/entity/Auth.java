package com.sns.mcc.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Auth {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String refreshToken;

    public Auth(String username, String refreshToken){
        this.username = username;
        this.refreshToken = refreshToken;
    }
}
