package org.sopt.certi_server.domain.favorite.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.favorite.dto.response.FavoriteCertificationSimple;
import org.sopt.certi_server.domain.favorite.dto.response.FavoriteCertificationSimpleListResponse;
import org.sopt.certi_server.domain.favorite.entity.Favorite;
import org.sopt.certi_server.domain.favorite.repository.FavoriteRepository;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;

    public FavoriteCertificationSimpleListResponse getFavoriteCertificationsByUserId(Long userId){
        return new FavoriteCertificationSimpleListResponse(favoriteRepository.findFavoriteCertificationsByUserId(userId).stream()
                .map(FavoriteCertificationSimple::from)
                .collect(Collectors.toList()));
    }

    public boolean toggleFavorite(Long userId, Long certificationId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND)
                );
        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND)
                );

        Optional<Favorite> favoriteOpt = favoriteRepository.findByUserAndCertification(user, certification);

        if (favoriteOpt.isPresent()) {
            favoriteRepository.delete(favoriteOpt.get());
            return false; // 해제됨
        } else {
            favoriteRepository.save(Favorite.create(user, certification));
            return true; // 추가됨
        }
    }
}
