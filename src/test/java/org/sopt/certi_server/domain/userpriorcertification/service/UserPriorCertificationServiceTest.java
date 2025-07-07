package org.sopt.certi_server.domain.userpriorcertification.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.Category;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.domain.userpriorcertification.dto.response.GetPriorCertificationDetailResponse;
import org.sopt.certi_server.domain.userpriorcertification.dto.response.GetPriorCertificaitonResponse;
import org.sopt.certi_server.domain.userpriorcertification.entity.UserPriorCertification;
import org.sopt.certi_server.domain.userpriorcertification.repository.UserPriorCertificationRepository;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserPriorCertificationServiceTest {

	@Mock
	UserPriorCertificationRepository userPriorCertificationRepository;
	@Mock
	UserService userService;
	@Mock
	CertificationService certificationService;
	@Mock
	CertificationRepository certificationRepository;

	@InjectMocks
	UserPriorCertificationService userPriorCertificationService;

	User user;
	Certification certification;
	UserPriorCertification userPriorCertification;

	@BeforeEach
	void setUp() {
		user = User.builder()
			.email("test@test.com")
			.nickname("테스트유저")
			.build();
		certification = Certification.builder()
			.id(1L)
			.name("자격증")// 추가
			.build();
		certificationRepository.save(certification);
		userPriorCertification = UserPriorCertification.builder()
			.user(user)
			.certification(certification)
			.build();
	}

	@Nested
	@DisplayName("createPriorCertification")
	class CreatePriorCertification {

		@Test
		@DisplayName("정상적으로 생성")
		void create_success() {
			given(userService.getUser(anyLong())).willReturn(user);
			given(certificationService.getCertification(anyLong())).willReturn(certification);
			given(userPriorCertificationRepository.save(any(UserPriorCertification.class)))
				.willReturn(userPriorCertification);

			String result = userPriorCertificationService.createPriorCertification(1L, 1L);

			assertThat(result).isEqualTo("자격증");
		}
	}

	@Nested
	@DisplayName("getPriorCertificationDetail")
	class GetPriorCertificationDetail {

		@Test
		@DisplayName("상세 조회 성공")
		void detail_success() {
			given(userService.getUser(anyLong())).willReturn(user);
			given(userPriorCertificationRepository.findByUserAndId(any(), anyLong()))
				.willReturn(Optional.of(userPriorCertification));
			given(certificationRepository.getCategoriesByCertificationId(anyLong()))
				.willReturn(List.of(Category.builder().name("IT").build()));

			GetPriorCertificationDetailResponse result = userPriorCertificationService.getPriorCertificationDetail(1L, 1L);

			assertThat(result.categories()).contains("IT");
		}
	}

	@Nested
	@DisplayName("getPriorCertificaitonList")
	class GetPriorCertificationList {

		@Test
		@DisplayName("리스트 조회 성공")
		void list_success() {
			given(userService.getUser(anyLong())).willReturn(user);
			given(userPriorCertificationRepository.findByUserOrderByIdAsc(any()))
				.willReturn(List.of(userPriorCertification));

			List<GetPriorCertificaitonResponse> result = userPriorCertificationService.getPriorCertificaitonList(1L);

			assertThat(result).hasSize(1);
			assertThat(result.get(0).name()).isEqualTo("자격증");
		}
	}

	@Nested
	@DisplayName("deletePriorCertification")
	class DeletePriorCertification {

		@Test
		@DisplayName("삭제 성공")
		void delete_success() {
			given(userService.getUser(anyLong())).willReturn(user);
			given(userPriorCertificationRepository.findByUserAndId(any(), anyLong()))
				.willReturn(Optional.of(userPriorCertification));

			assertThatCode(() -> userPriorCertificationService.deletePriorCertification(1L, 1L))
				.doesNotThrowAnyException();

			then(userPriorCertificationRepository).should().delete(userPriorCertification);
		}
	}
}
