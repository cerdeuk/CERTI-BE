package org.sopt.certi_server.domain.user.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_job")
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

	public static UserJob createUserJob(User user, Job job){
		return new UserJob(user, job);
	}

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
}
