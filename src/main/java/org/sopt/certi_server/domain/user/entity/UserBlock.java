package org.sopt.certi_server.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_blocks",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"blocker_id", "blocked_id"})
        },
        indexes = {
                // 복합 인덱스 설정
                @Index(name = "idx_blocker_blocked", columnList = "blocker_id, blocked_id"),
                // 나를 차단한 사람을 찾을 때를 위한 인덱스
                @Index(name = "idx_blocked_id", columnList = "blocked_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserBlock extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 차단을 하는 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id", nullable = false)
    private User blocker;

    // 차단을 당하는 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id", nullable = false)
    private User blocked;
}
