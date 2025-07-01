package org.sopt.certi_server.domain.category.entity;

import jakarta.persistence.*;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

@Entity
@Table(name = "category")
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;
}
