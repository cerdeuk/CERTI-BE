package org.sopt.certi_server.domain.certification.repository;

import org.sopt.certi_server.domain.certification.dto.response.CertificationSimple;
import org.sopt.certi_server.domain.user.entity.User;

import java.util.List;

public interface CertificationRepositoryCustom {
    List<CertificationSimple> findByJobAndFavorite(User user, boolean isFavorite, Long jobId);
}
