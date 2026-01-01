package org.sopt.certi_server.domain.acquisition.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.domain.acquisition.entity.enums.CardType;
import org.sopt.certi_server.domain.acquisition.entity.enums.SmallCardType;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "acquisition",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "certification_id"})
        },
        indexes = {
                @Index(name = "user_id_idx", columnList = "user_id")
        }
)
public class Acquisition extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acquisition_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    @Enumerated(value = EnumType.STRING)
    private CardType cardType;

    @Enumerated(value = EnumType.STRING)
    private SmallCardType smallCardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "grade")
    private String grade;

    @Column(name = "acquisition_date")
    private LocalDate acquisitionDate;

    @Builder
    public Acquisition(Certification certification, CardType cardType, User user, SmallCardType smallCardType, String grade) {
        this.certification = certification;
        this.smallCardType = smallCardType;
        this.cardType = cardType;
        this.user = user;
        this.acquisitionDate = LocalDate.now();
    }

    public void changeAcquisition(LocalDate acquisitionDate, String grade) {
        this.acquisitionDate = acquisitionDate;
        this.grade = grade;
    }
}
