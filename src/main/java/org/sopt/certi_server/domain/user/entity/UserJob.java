package org.sopt.certi_server.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.domain.job.entity.Job;

@Entity
@Getter
@Table(
        name = "user_job",
        indexes = {
                @Index(name = "user_id_idx", columnList = "user_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Job.class)
    @JoinColumn(name = "job_id")
    private Job job;

    private UserJob(User user, Job job) {
        this.user = user;
        this.job = job;
    }

    @Builder
    public UserJob(Long id, User user, Job job) {
        this.id = id;
        this.user = user;
        this.job = job;
    }

    public static UserJob createUserJob(User user, Job job) {
        return new UserJob(user, job);
    }
}
