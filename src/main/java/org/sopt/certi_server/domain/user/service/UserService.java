package org.sopt.certi_server.domain.user.service;

import lombok.RequiredArgsConstructor;

import org.sopt.certi_server.domain.major.entity.Major;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.major.repository.MajorImplRepository;
import org.sopt.certi_server.domain.user.dto.response.GetUserResponse;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.entity.UserMajorImpl;
import org.sopt.certi_server.domain.user.repository.UserMajorImplRepository;
import org.sopt.certi_server.domain.user.repository.UserRepository;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMajorImplRepository userMajorImplRepository;
    private final MajorImplRepository majorImplRepository;

    public User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public GetUserResponse getHomeUser(final Long userId){
        User user = getUser(userId);
        UserMajorImpl userMajor = userMajorImplRepository.findByUserId(userId);
        MajorImpl majorImpl = majorImplRepository.findById(userMajor.getMajorImpl().getId())
            .orElseThrow(()-> new NotFoundException(ErrorCode.DATA_NOT_FOUND));
        return GetUserResponse.from(user, majorImpl);
    }
}
