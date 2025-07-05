package org.sopt.certi_server.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.sopt.certi_server.domain.user.entity.enums.CollegeType;
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
    private String track;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private CollegeType collegeType;

    public User(String nickname, String email, String profileImageUrl){
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public static User createUser(String nickname, String email, String profileImageUrl){
        return new User(nickname, email, profileImageUrl);
    }

    @Builder
    public User(Long id, String universityName, String track, String phoneNumber, String nickname, String email,
        String profileImageUrl, CollegeType collegeType) {
        this.id = id;
        this.universityName = universityName;
        this.track = track;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.collegeType = collegeType;
    }
}
