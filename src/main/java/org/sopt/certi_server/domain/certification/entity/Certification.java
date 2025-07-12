package org.sopt.certi_server.domain.certification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.domain.certification.entity.enums.CertificationType;
import org.sopt.certi_server.domain.certification.entity.enums.TestType;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "certification")
public class Certification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private CertificationType certificationType;

    @Enumerated(value = EnumType.STRING)
    private TestType testType;

    private String averagePeriod;

    private String charge;

    @ElementCollection
    @CollectionTable(name = "tags")
    private List<String> tags = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String testDateInformation;

    private LocalDate nearestTestDate;

    @Column(columnDefinition = "TEXT")
    private String applicationMethod;

    @Column(columnDefinition = "TEXT")
    private String applicationUrl;

    @Column(name = "expiration_period", nullable = false)
    private String expirationPeriod;

    @Builder
    public Certification(Long id, Agency agency, String name, CertificationType certificationType, TestType testType, String averagePeriod, String charge,
                         List<String> tags, String description, String testDateInformation, LocalDate nearestTestDate,
                         String applicationMethod, String applicationUrl, String expirationPeriod) {
        this.id = id;
        this.agency = agency;
        this.name = name;
        this.certificationType = certificationType;
        this.testType = testType;
        this.averagePeriod = averagePeriod;
        this.charge = charge;
        this.tags = tags;
        this.description = description;
        this.testDateInformation = testDateInformation;
        this.nearestTestDate = nearestTestDate;
        this.applicationMethod = applicationMethod;
        this.applicationUrl = applicationUrl;
        this.expirationPeriod = expirationPeriod;
    }
}

