package org.sopt.certi_server.domain.career.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.certi_server.domain.career.dto.request.CreateCareerRequest;
import org.sopt.certi_server.domain.career.dto.response.CareerDetailResponse;
import org.sopt.certi_server.domain.career.entity.Career;
import org.sopt.certi_server.domain.career.repository.CareerRepository;
import org.sopt.certi_server.domain.user.entity.User;
import org.sopt.certi_server.domain.user.service.UserService;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.*;

class CareerServiceTest {

	@Mock
	CareerRepository careerRepository;

	@Mock
	UserService userService;

	@InjectMocks
	CareerService careerService;

	private User user;
	private Career career;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = User.builder()
			.id(1L)
			.email("test@test.com")
			.nickname("성민")
			.build();

		career = Career.builder()
			.id(10L)
			.user(user)
			.name("sopt")
			.place("여기에서")
			.startAt(LocalDate.of(2022, 1, 1))
			.endAt(LocalDate.of(2023, 1, 1))
			.description("썰티화이팅")
			.build();
	}

	@Nested
	@DisplayName("getCareerList")
	class GetCareerList {
		@Test
		@DisplayName("성공적으로 리스트 반환")
		void getCareerList_success() {
			// given
			given(userService.getUser(1L)).willReturn(user);
			given(careerRepository.findByUserId(1L)).willReturn(List.of(career));

			// when
			List<CareerDetailResponse> result = careerService.getCareerList(1L);

			// then
			assertThat(result).hasSize(1);
			assertThat(result.get(0).name()).isEqualTo("sopt");
		}
	}

	@Nested
	@DisplayName("createCareer")
	class CreateCareer {
		@Test
		@DisplayName("성공적으로 경력 생성")
		void createCareer_success() {
			// given
			CreateCareerRequest request = new CreateCareerRequest(
				LocalDate.of(2022,1,1),
				LocalDate.of(2023,1,1),
				"여기에서",
				"sopt",
				"썰티화이팅"
			);
			given(userService.getUser(1L)).willReturn(user);
			given(careerRepository.save(any(Career.class))).willReturn(career);

			// when & then
			assertThatCode(() -> careerService.createCareer(1L, request))
				.doesNotThrowAnyException();

			then(careerRepository).should().save(any(Career.class));
		}
	}

	@Nested
	@DisplayName("deleteCareer")
	class DeleteCareer {
		@Test
		@DisplayName("성공적으로 경력 삭제")
		void deleteCareer_success() {
			// given
			given(userService.getUser(1L)).willReturn(user);
			given(careerRepository.findByUserIdAndId(1L, 10L)).willReturn(Optional.of(career));

			// when & then
			assertThatCode(() -> careerService.deleteCareer(1L, 10L))
				.doesNotThrowAnyException();

			then(careerRepository).should().delete(career);
		}

		@Test
		@DisplayName("존재하지 않는 경력 삭제 시 NotFoundException")
		void deleteCareer_notFound() {
			// given
			given(userService.getUser(1L)).willReturn(user);
			given(careerRepository.findByUserIdAndId(1L, 10L)).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> careerService.deleteCareer(1L, 10L))
				.isInstanceOf(NotFoundException.class)
				.hasMessage(ErrorCode.DATA_NOT_FOUND.getMessage());
		}
	}
}
