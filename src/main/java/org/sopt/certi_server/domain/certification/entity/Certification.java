package org.sopt.certi_server.domain.certification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.domain.agency.entity.Agency;
import org.sopt.certi_server.domain.certification.dto.request.CertificationCreateRequest;
import org.sopt.certi_server.domain.certification.entity.enums.TestType;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "certification")
public class Certification extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private TestType testType;

    private String averagePeriod;

    private Long charge;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String testDate;

    @Column(columnDefinition = "TEXT")
    private String applicationMethod;

    private String cardImageUrl;

    private String applicationUrl;

    @Builder
    public Certification(
            Agency agency,
            String name,
            TestType testType,
            String averagePeriod,
            Long charge,
            String description,
            String testDate,
            String applicationMethod,
            String cardImageUrl,
            String applicationUrl
    ) {
        this.agency = agency;
        this.name = name;
        this.testType = testType;
        this.averagePeriod = averagePeriod;
        this.charge = charge;
        this.description = description;
        this.testDate = testDate;
        this.applicationMethod = applicationMethod;
        this.cardImageUrl = cardImageUrl;
        this.applicationUrl = applicationUrl;
    }


}

