package org.sopt.certi_server.domain.userprecertification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_pre_certification")
public class UserPreCertification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public UserPreCertification(User user, Certification certification) {
        this.user = user;
        this.certification = certification;
    }

    public static UserPreCertification create(User user, Certification certification) {
        return UserPreCertification.builder()
                .user(user)
                .certification(certification)
                .build();
    }
}
