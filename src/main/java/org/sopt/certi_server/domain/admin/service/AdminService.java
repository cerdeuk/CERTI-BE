package org.sopt.certi_server.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
import org.sopt.certi_server.domain.admin.dto.response.*;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.CertificationCategory;
import org.sopt.certi_server.domain.certification.entity.CertificationJob;
import org.sopt.certi_server.domain.certification.entity.CertificationMajor;
import org.sopt.certi_server.domain.certification.repository.CertificationCategoryRepository;
import org.sopt.certi_server.domain.certification.repository.CertificationJobRepository;
import org.sopt.certi_server.domain.certification.repository.CertificationMajorRepository;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.favorite.repository.FavoriteRepository;
import org.sopt.certi_server.domain.userprecertification.repository.UserPreCertificationRepository;
import org.sopt.certi_server.domain.admin.repository.AdminRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
	private final AdminRepository adminRepository;
	private final MajorService majorService;
	private final CertificationService certificationService;
	private final CertificationMajorRepository certificationMajorRepository;
	private final JobRepository jobRepository;
	private final CertificationJobRepository certificationJobRepository;
    private final CertificationRepository certificationRepository;
    private final AcquisitionRepository acquisitionRepository;
    private final FavoriteRepository favoriteRepository;
    private final CertificationCategoryRepository certificationCategoryRepository;
    private final UserPreCertificationRepository userPreCertificationRepository;




    @Transactional
	public void createMajor(Long certificationId, String majorName) {
		Major major = majorService.getMajorByName(majorName);
		Certification certification = certificationService.getCertification(certificationId);
		CertificationMajor certificationMajor = certificationService.getCertificationMajor(certification, major);
		certificationMajor.updateMajor(major);
	}

	@Transactional
	public void deleteMajor(Long certificationId, String majorName) {
		Major major = majorService.getMajorByName(majorName);
		Certification certification = certificationService.getCertification(certificationId);
		CertificationMajor certificationMajor = certificationService.getCertificationMajor(certification, major);
		certificationMajorRepository.delete(certificationMajor);
	}

	@Transactional
	public void createJob(Long certificationId, String jobName) {
		Certification certification = certificationService.getCertification(certificationId);
		Job job = jobRepository.findByName(jobName)
			.orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));
		CertificationJob certificationJob = certificationJobRepository.findByCertificationAndJob(certification, job)
			.orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));
		certificationJob.updateJob(job);
	}

	@Transactional
	public void deleteJob(Long certificationId, String jobName) {
		Certification certification = certificationService.getCertification(certificationId);
		Job job = jobRepository.findByName(jobName)
			.orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));
		CertificationJob certificationJob = certificationJobRepository.findByCertificationAndJob(certification, job)
			.orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));
		certificationJobRepository.delete(certificationJob);
	}
}
