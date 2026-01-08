package org.sopt.certi_server.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.user.entity.enums.Grade;
import org.sopt.certi_server.domain.user.entity.enums.SocialType;
import org.sopt.certi_server.domain.user.entity.enums.TrackType;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(targetEntity = University.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @Column(name = "track")
    @Enumerated(value = EnumType.STRING)
    private TrackType track;

    @Column(name = "grade")
    @Enumerated(value = EnumType.STRING)
    private Grade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_impl_id")
    private MajorImpl major;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "social_id")
    private Long socialId;

    @Column(name = "birth_date")
    private LocalDate birthDate;


    public User(String nickname, String email, String profileImageUrl) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    @Builder
    public User(Long id, University university, String track, String grade, MajorImpl major, String nickname, String name, String email,
                String profileImageUrl, SocialType socialType, Long socialId) {
        this.id = id;
        this.university = university;
        this.track = TrackType.from(track);
        this.grade = Grade.from(grade);
        this.major = major;
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public void changeUser(String name, String nickname, String email, LocalDate birthDate){
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.birthDate = birthDate;
    }

    public void changeUniversity(University university) {
        this.university = university;
    }

    public void changeMajor(MajorImpl mi) {
        this.major = mi;
    }
}
