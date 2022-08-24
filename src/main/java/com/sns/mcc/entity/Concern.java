package com.sns.mcc.entity;

import com.sns.mcc.utill.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
public class Concern extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int game;

    @Column
    private int movie;

    @Column
    private int music;

    @Column
    private int love;

    @Column
    private int book;

    public Concern() {
        this.game = 0;
        this.movie = 0;
        this.music = 0;
        this.love = 0;
        this.book = 0;
    }
}
