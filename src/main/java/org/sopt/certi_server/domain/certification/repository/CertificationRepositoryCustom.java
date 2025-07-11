package org.sopt.certi_server.domain.certification.repository;

import java.util.List;

import org.sopt.certi_server.domain.certification.dto.response.CertificationSimple;
import org.sopt.certi_server.domain.user.entity.User;

public interface CertificationRepositoryCustom {
	List<CertificationSimple> findByJobAndFavorite(User user, boolean isFavorite, Long jobId);
}
