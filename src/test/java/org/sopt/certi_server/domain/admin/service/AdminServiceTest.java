package org.sopt.certi_server.domain.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.CertificationJob;
import org.sopt.certi_server.domain.certification.entity.CertificationMajor;
import org.sopt.certi_server.domain.certification.repository.CertificationJobRepository;
import org.sopt.certi_server.domain.certification.repository.CertificationMajorRepository;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.job.repository.JobRepository;
import org.sopt.certi_server.domain.major.entity.Major;
import org.sopt.certi_server.domain.major.service.MajorService;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class AdminServiceTest {

	@InjectMocks
	private AdminService adminService;

	@Mock private MajorService majorService;
	@Mock private CertificationService certificationService;
	@Mock private CertificationMajorRepository certificationMajorRepository;
	@Mock private JobRepository jobRepository;
	@Mock private CertificationJobRepository certificationJobRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("createMajor: CertificationMajor의 major 업데이트")
	void createMajor_success() {
		Long certId = 1L;
		String majorName = "컴퓨터공학";

		Certification certification = mock(Certification.class);
		Major major = mock(Major.class);
		CertificationMajor certificationMajor = mock(CertificationMajor.class);

		given(majorService.getMajorByName(majorName)).willReturn(major);
		given(certificationService.getCertification(certId)).willReturn(certification);
		given(certificationService.getCertificationMajor(certification, major)).willReturn(certificationMajor);

		adminService.createMajor(certId, majorName);

		verify(certificationMajor).updateMajor(major);
	}

	@Test
	@DisplayName("deleteMajor: CertificationMajor 삭제 성공")
	void deleteMajor_success() {
		Long certId = 1L;
		String majorName = "경영학";

		Certification certification = mock(Certification.class);
		Major major = mock(Major.class);
		CertificationMajor certificationMajor = mock(CertificationMajor.class);

		given(majorService.getMajorByName(majorName)).willReturn(major);
		given(certificationService.getCertification(certId)).willReturn(certification);
		given(certificationService.getCertificationMajor(certification, major)).willReturn(certificationMajor);

		adminService.deleteMajor(certId, majorName);

		verify(certificationMajorRepository).delete(certificationMajor);
	}

	@Test
	@DisplayName("createJob: Job이 없으면 NotFoundException 발생")
	void createJob_jobNotFound() {
		Long certId = 1L;
		String jobName = "UX디자이너";
		Certification certification = mock(Certification.class);

		given(certificationService.getCertification(certId)).willReturn(certification);
		given(jobRepository.findByName(jobName)).willReturn(Optional.empty());

		assertThatThrownBy(() -> adminService.createJob(certId, jobName))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining(ErrorCode.DATA_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("deleteJob: 정상 삭제")
	void deleteJob_success() {
		Long certId = 1L;
		String jobName = "백엔드개발자";

		Certification certification = mock(Certification.class);
		Job job = mock(Job.class);
		CertificationJob certificationJob = mock(CertificationJob.class);

		given(certificationService.getCertification(certId)).willReturn(certification);
		given(jobRepository.findByName(jobName)).willReturn(Optional.of(job));
		given(certificationJobRepository.findByCertificationAndJob(certification, job)).willReturn(Optional.of(certificationJob));

		adminService.deleteJob(certId, jobName);

		verify(certificationJobRepository).delete(certificationJob);
	}
}
