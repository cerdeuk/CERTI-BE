package org.sopt.certi_server.domain.certification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "certification_category")
public class CertificationCategory extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    private CertificationCategory(Certification certification, Category category){
        this.certification = certification;
        this.category = category;
    }

    public static CertificationCategory create(Certification certification, Category category){
        return CertificationCategory.builder()
                .certification(certification)
                .category(category)
                .build();
    }
}
