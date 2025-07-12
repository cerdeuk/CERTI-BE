package org.sopt.certi_server.domain.certification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agency extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agency_id")
    private Long id;

    @Column(unique = true)
    private String name;

    private String agencyUrl;

    public Agency(String agencyName, String agencyUrl) {
        this.name = agencyName;
        this.agencyUrl = agencyUrl;
    }

    public static Agency create(String agencyName, String agencyUrl) {
        return new Agency(agencyName, agencyUrl);
    }
}
