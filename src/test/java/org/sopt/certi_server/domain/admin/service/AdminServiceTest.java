package org.sopt.certi_server.domain.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.sopt.certi_server.domain.admin.dto.request.CreateJobRequest;
import org.sopt.certi_server.domain.admin.dto.request.CreateMajorRequest;
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
import org.sopt.certi_server.global.error.exception.InvalidValueException;
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
	@DisplayName("createMajor - CertificationMajor가 이미 존재하면 예외 발생")
	void createMajor_alreadyExists() {
		// given
		Long certId = 1L;
		CreateMajorRequest request = new CreateMajorRequest("컴공", 0.5f);
		Major major = mock(Major.class);
		Certification certification = mock(Certification.class);
		CertificationMajor existing = mock(CertificationMajor.class);

		given(majorService.getMajorByName("컴공")).willReturn(major);
		given(certificationService.getCertification(certId)).willReturn(certification);
		given(certificationService.getCertificationMajor(certification, major)).willReturn(existing);

		// when & then
		assertThatThrownBy(() -> adminService.createMajor(certId, request))
			.isInstanceOf(InvalidValueException.class);
	}

	@Test
	@DisplayName("createMajor - 정상 저장")
	void createMajor_success() {
		Long certId = 1L;
		CreateMajorRequest request = new CreateMajorRequest("심리학", 0.3f);
		Major major = mock(Major.class);
		Certification certification = mock(Certification.class);

		given(majorService.getMajorByName("심리학")).willReturn(major);
		given(certificationService.getCertification(certId)).willReturn(certification);
		given(certificationService.getCertificationMajor(certification, major)).willReturn(null);

		// when
		adminService.createMajor(certId, request);

		// then
		verify(certificationMajorRepository).save(any(CertificationMajor.class));
	}

	@Test
	@DisplayName("deleteMajor - 정상 삭제")
	void deleteMajor_success() {
		Long certId = 1L;
		String majorName = "철학";
		Major major = mock(Major.class);
		Certification certification = mock(Certification.class);
		CertificationMajor certificationMajor = mock(CertificationMajor.class);

		given(majorService.getMajorByName(majorName)).willReturn(major);
		given(certificationService.getCertification(certId)).willReturn(certification);
		given(certificationService.getCertificationMajor(certification, major)).willReturn(certificationMajor);

		// when
		adminService.deleteMajor(certId, majorName);

		// then
		verify(certificationMajorRepository).delete(certificationMajor);
	}

	@Test
	@DisplayName("createJob - 이미 존재하는 CertificationJob이면 예외")
	void createJob_alreadyExists() {
		Long certId = 1L;
		CreateJobRequest request = new CreateJobRequest("기획자", 0.6f);
		Certification certification = mock(Certification.class);
		Job job = mock(Job.class);
		CertificationJob existing = mock(CertificationJob.class);

		given(certificationService.getCertification(certId)).willReturn(certification);
		given(jobRepository.findByName("기획자")).willReturn(Optional.of(job));
		given(certificationJobRepository.findByCertificationAndJob(certification, job))
			.willReturn(Optional.of(existing));

		// when & then
		assertThatThrownBy(() -> adminService.createJob(certId, request))
			.isInstanceOf(InvalidValueException.class);
	}

	@Test
	@DisplayName("createJob - 정상 저장")
	void createJob_success() {
		Long certId = 1L;
		CreateJobRequest request = new CreateJobRequest("백엔드개발자", 0.7f);
		Certification certification = mock(Certification.class);
		Job job = mock(Job.class);

		given(certificationService.getCertification(certId)).willReturn(certification);
		given(jobRepository.findByName("백엔드개발자")).willReturn(Optional.of(job));
		given(certificationJobRepository.findByCertificationAndJob(certification, job)).willReturn(Optional.empty());

		// when
		adminService.createJob(certId, request);

		// then
		verify(certificationJobRepository).save(any(CertificationJob.class));
	}

	@Test
	@DisplayName("deleteJob - CertificationJob 정상 삭제")
	void deleteJob_success() {
		Long certId = 1L;
		String jobName = "데이터사이언티스트";
		Certification certification = mock(Certification.class);
		Job job = mock(Job.class);
		CertificationJob certificationJob = mock(CertificationJob.class);

		given(certificationService.getCertification(certId)).willReturn(certification);
		given(jobRepository.findByName(jobName)).willReturn(Optional.of(job));
		given(certificationJobRepository.findByCertificationAndJob(certification, job))
			.willReturn(Optional.of(certificationJob));

		// when
		adminService.deleteJob(certId, jobName);

		// then
		verify(certificationJobRepository).delete(certificationJob);
	}
}
