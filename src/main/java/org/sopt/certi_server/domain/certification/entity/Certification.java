package org.sopt.certi_server.domain.certification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "certification")
public class Certification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long averagePeriod;

    private Long charge;

    private String description;

    private String testDate;

    private String applicationMethod;

    private String cardImageUrl;

    private String applicationUrl;

}

