package org.sopt.certi_server.domain.user.entity;

import static org.sopt.certi_server.domain.user.service.UserService.*;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.user.entity.enums.Grade;
import org.sopt.certi_server.domain.user.entity.enums.TrackType;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "university_name")
    private String universityName;

    @Column(name = "track")
    @Enumerated(value = EnumType.STRING)
    private TrackType track;

    @Column(name = "grade")
    @Enumerated(value = EnumType.STRING)
    private Grade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_impl_id")
    private MajorImpl major;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "profile_image_url")
    private String profileImageUrl;


    public User(String nickname, String email, String profileImageUrl){
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public static User createUser(String nickname, String email, String profileImageUrl){
        return new User(nickname, email, profileImageUrl);
    }

    @Builder
    public User(Long id, String universityName, String track, String grade, MajorImpl major, String nickname, String email,
                String profileImageUrl) {
        this.id = id;
        this.universityName = universityName;
        this.track = TrackType.from(track);
        this.grade = Grade.from(grade);
        this.major = major;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }


}
