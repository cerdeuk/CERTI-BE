package org.sopt.certi_server.domain.acquisition.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Acquisition {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
