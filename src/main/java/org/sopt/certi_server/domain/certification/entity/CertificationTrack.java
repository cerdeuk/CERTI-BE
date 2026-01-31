package org.sopt.certi_server.domain.certification.entity;

import lombok.*;
import org.sopt.certi_server.domain.user.entity.enums.TrackType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Builder
@Table(name = "certification_track_map",
	uniqueConstraints = @UniqueConstraint(columnNames = {"certification_id", "track"}))
public class CertificationTrack {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "certification_id", nullable = false)
	private Certification certification;

	@Enumerated(EnumType.STRING)
	@Column(name = "track", nullable = false)
	private TrackType track;
}
