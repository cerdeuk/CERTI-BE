package org.sopt.certi_server.global.error.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * API 응답을 위한 공용 페이징 DTO
 *
 * @param <T>         the type of content
 * @param content     실제 데이터 리스트
 * @param totalPages  전체 페이지 수
 * @param totalElements 총 데이터 수
 * @param isLast      마지막 페이지 여부
 */
public record PageResponse<T>(
        List<T> content,
        int totalPages,
        long totalElements,
        boolean isLast
) {
    // Spring의 Page 객체를 이 DTO로 변환하는 정적 팩토리 메서드, 플랫폼 독립적인 DTO를 생성하기 위함
    public static <T> PageResponse<T> from(Page<T> page){
        return new PageResponse<>(
                page.getContent(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast()
        );
    }
}
