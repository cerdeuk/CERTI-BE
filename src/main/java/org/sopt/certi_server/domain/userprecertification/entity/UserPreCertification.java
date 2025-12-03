package org.sopt.certi_server.domain.userprecertification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.userprecertification.entity.enums.IconType;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "user_pre_certification",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "certification_id"})
        },
        indexes = {
                @Index(name = "user_id_idx", columnList = "user_id")
        }
)
public class UserPreCertification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    private IconType iconType;

    @Column(name = "test_date", nullable = false)
    private LocalDate testDate;


    @Builder
    public UserPreCertification(User user, Certification certification, IconType iconType) {
        this.user = user;
        this.certification = certification;
        this.iconType = iconType;
    }

    public static UserPreCertification create(User user, Certification certification, IconType iconType) {
        return UserPreCertification.builder()
                .user(user)
                .certification(certification)
                .iconType(iconType)
                .build();
    }
}
