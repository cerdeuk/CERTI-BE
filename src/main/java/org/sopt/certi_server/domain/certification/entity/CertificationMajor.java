package org.sopt.certi_server.domain.certification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.major.entity.Major;

@Entity
@Getter
@Table(
        name = "certification_major",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"certification_id", "major_id"})
        }
)

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CertificationMajor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_major_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    private double weight;

    public static CertificationMajor create(Certification certification, Major major, double weight) {
        return new CertificationMajor(certification, major, weight);
    }

    public void updateMajor(Major major) {
        this.major = major;
    }

    public CertificationMajor(Certification certification, Major major, double weight) {
        this.major = major;
        this.certification = certification;
        this.weight = weight;
    }

}
