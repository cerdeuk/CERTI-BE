package org.sopt.certi_server.domain.comment.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.comment.entity.CertificationComment;
import org.sopt.certi_server.domain.comment.entity.QCertificationComment;
import org.sopt.certi_server.domain.user.entity.QUserBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
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
                .orderBy(getOrderSpecifiers(pageable, comment)) // 최신순 정렬 등 추가
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

    /**
     * 정렬 조건을 생성하는 헬퍼 메서드
     */
    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, QCertificationComment comment) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "likeCount": // 좋아요 순
                        orders.add(new OrderSpecifier<>(direction, comment.likeCount));
                        // 좋아요 수가 같을 경우 최신순으로 정렬하는 보조 조건 추가 가능
                        orders.add(new OrderSpecifier<>(Order.DESC, comment.createdTime));
                        break;
                    case "createdTime": // 최신순
                    default:
                        orders.add(new OrderSpecifier<>(direction, comment.createdTime));
                        break;
                }
            }
        } else {
            // 기본 정렬값: 최신순
            log.info("기본 정렬 호출");
            orders.add(new OrderSpecifier<>(Order.DESC, comment.createdTime));
        }

        return orders.toArray(OrderSpecifier[]::new);
    }


}
