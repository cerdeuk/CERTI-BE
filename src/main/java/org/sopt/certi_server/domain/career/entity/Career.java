package org.sopt.certi_server.domain.career.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "career")
public class Career {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "career_id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "created_at", nullable = false)
	private LocalDate startAt;

	@Column(name = "end_at", nullable = false)
	private LocalDate endAt;

	@Column(name = "place", nullable = false)
	private String place;

	@Column(name = "description", nullable = false)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Builder
	public Career(Long id, String name, LocalDate startAt, LocalDate endAt, String place, String description,
		User user) {
		this.id = id;
		this.name = name;
		this.startAt = startAt;
		this.endAt = endAt;
		this.place = place;
		this.description = description;
		this.user = user;
	}
}
