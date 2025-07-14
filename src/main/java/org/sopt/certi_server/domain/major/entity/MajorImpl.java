package org.sopt.certi_server.domain.major.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "major_impl",
        indexes = {
                @Index(name = "major_impl_name_idx", columnList = "name")
        }
)
public class MajorImpl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_impl_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    public MajorImpl(Major major, String majorImplName) {
        this.major = major;
        this.name = majorImplName;
    }

    public static MajorImpl create(Major major, String majorImplName) {
        return new MajorImpl(major, majorImplName);
    }
}
