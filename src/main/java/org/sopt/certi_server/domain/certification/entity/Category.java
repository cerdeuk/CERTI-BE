package org.sopt.certi_server.domain.certification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

@Entity
@Getter
@Table(name = "category")
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;
}
