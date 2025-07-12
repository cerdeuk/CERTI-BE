package org.sopt.certi_server.domain.certification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.domain.job.entity.Job;

@Entity
@Getter
@Table(
        name = "certification_job",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"certification_id", "job_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CertificationJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_job_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    private double weight;

    @Builder
    public CertificationJob(Certification certification, Job job, double weight) {
        this.job = job;
        this.certification = certification;
        this.weight = weight;
    }

    public static CertificationJob create(Certification certification, Job job, double weight) {
        return new CertificationJob(certification, job, weight);
    }

    public void updateJob(Job job) {
        this.job = job;
    }
}
