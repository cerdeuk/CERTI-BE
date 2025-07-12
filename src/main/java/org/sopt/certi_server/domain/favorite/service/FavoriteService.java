package org.sopt.certi_server.domain.favorite.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.favorite.dto.response.FavoriteCertificationSimple;
import org.sopt.certi_server.domain.favorite.dto.response.FavoriteCertificationSimpleListResponse;
import org.sopt.certi_server.domain.favorite.entity.Favorite;
import org.sopt.certi_server.domain.favorite.repository.FavoriteRepository;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FavoriteService {

    private final UserService userService;
    private final CertificationService certificationService;
    private final FavoriteRepository favoriteRepository;
    private final CertificationRepository certificationRepository;

    public FavoriteCertificationSimpleListResponse getFavoriteCertificationsByUserId(Long userId) {
        return new FavoriteCertificationSimpleListResponse(favoriteRepository.findFavoriteCertificationsByUserId(userId).stream()
                .map(FavoriteCertificationSimple::from)
                .toList());
    }

    @Transactional
    public boolean toggleFavorite(Long userId, Long certificationId) {
        log.info("userId = " + userId + ", certificationId = " + certificationId);
        User user = userService.getUser(userId);
        Certification certification = certificationService.getCertification(certificationId);

        Optional<Favorite> favoriteOpt = favoriteRepository.findByUserAndCertification(user, certification);

        if (favoriteOpt.isPresent()) {
            favoriteRepository.delete(favoriteOpt.get());
            return false; // 해제됨
        }
        favoriteRepository.save(Favorite.create(user, certification));
        return true; // 추가됨

    }
}
