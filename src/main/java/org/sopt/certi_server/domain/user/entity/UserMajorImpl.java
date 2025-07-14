package org.sopt.certi_server.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.certi_server.domain.major.entity.MajorImpl;

@Getter
@Entity
@Table(name = "user_major_impl")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMajorImpl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MajorImpl.class)
    @JoinColumn(name = "major_impl_id")
    private MajorImpl majorImpl;

    public UserMajorImpl(User user, MajorImpl major) {
        this.user = user;
        this.majorImpl = major;
    }

    public static UserMajorImpl createUserMajorImpl(User user, MajorImpl major) {
        return new UserMajorImpl(user, major);
    }
}
