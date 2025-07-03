package org.sopt.certi_server.domain.Major.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "major")
public class Major {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "major_id", nullable = false)
	private Long id;

	@Column(nullable = false)
	private String name;
}
