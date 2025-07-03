package org.sopt.certi_server.domain.favorite.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.domain.acquisition.entity.Acquisition;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "favorite", indexes = {
        @Index(name = "idx_favorite_user_id", columnList = "user_id")},
        uniqueConstraints = @UniqueConstraint(name = "uq_user_cert", columnNames = {"user_id", "certification_id"})
)
public class Favorite extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Favorite create(User user, Certification certification) {
        Favorite favorite = new Favorite();
        favorite.user = user;
        favorite.certification = certification;
        return favorite;
    }
}
