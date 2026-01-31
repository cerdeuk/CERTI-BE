package org.sopt.certi_server.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.sopt.certi_server.domain.comment.entity.QCertificationComment;
import org.sopt.certi_server.domain.user.entity.QUserBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class CertificationCommentRepositoryCustomImpl implements CertificationCommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CertificationComment> findByCertificationId(Long certificationId, Long userId, Pageable pageable) {

        QUserBlock block = QUserBlock.userBlock;
        QCertificationComment comment = QCertificationComment.certificationComment;

        List<CertificationComment> content = jpaQueryFactory
                .selectFrom(comment)
                .join(comment.user).fetchJoin()
                .leftJoin(block).on(
                        // 내가 상대를 차단했거나, 상대가 나를 차단했거나
                        (block.blocker.id.eq(userId).and(block.blocked.id.eq(comment.user.id)))
                                .or(block.blocked.id.eq(userId).and(block.blocker.id.eq(comment.user.id)))
                )
                .where(
                        comment.certification.id.eq(certificationId),
                        block.id.isNull() // JOIN 결과가 Null이면 차단 관계가 없다는 뜻
                )
                .orderBy(comment.createdTime.desc()) // 최신순 정렬 등 추가
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. 카운트 쿼리 (최적화를 위해 별도 작성)
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .leftJoin(block).on(
                        (block.blocker.id.eq(userId).and(block.blocked.id.eq(comment.user.id)))
                                .or(block.blocked.id.eq(userId).and(block.blocker.id.eq(comment.user.id)))
                )
                .where(
                        comment.certification.id.eq(certificationId),
                        block.id.isNull()
                );

        // 3. Page 객체로 반환 (카운트 쿼리가 필요 없을 땐 생략하는 최적화 포함)
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
