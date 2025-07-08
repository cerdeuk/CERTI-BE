package org.sopt.certi_server.domain.job.entity;

import org.sopt.certi_server.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "jobs")
public class Job {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

}
