package org.sopt.certi_server.domain.admin.service;

import org.sopt.certi_server.domain.admin.dto.request.CreateJobRequest;
import org.sopt.certi_server.domain.admin.dto.request.CreateMajorRequest;
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
import org.sopt.certi_server.global.error.exception.InvalidValueException;
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
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
	public void createMajor(Long certificationId, CreateMajorRequest request) {
		Major major = majorService.getMajorByName(request.majorName());
		Certification certification = certificationService.getCertification(certificationId);


		if(certificationService.getCertificationMajor(certification, major)!=null) {
			throw new InvalidValueException(ErrorCode.BAD_REQUEST_DATA);
		}

		CertificationMajor certificationMajor = new CertificationMajor(major, certification, request.weight());
		certificationMajorRepository.save(certificationMajor);
	}

	@Transactional
	public void deleteMajor(Long certificationId, String majorName) {
		Major major = majorService.getMajorByName(majorName);
		Certification certification = certificationService.getCertification(certificationId);
		CertificationMajor certificationMajor = certificationService.getCertificationMajor(certification, major);
		certificationMajorRepository.delete(certificationMajor);
	}

	@Transactional
	public void createJob(Long certificationId, CreateJobRequest createJobRequest) {
		Certification certification = certificationService.getCertification(certificationId);
		Job job = jobRepository.findByName(createJobRequest.jobName())
			.orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));
		if(certificationJobRepository.findByCertificationAndJob(certification, job).isPresent()){
			throw new InvalidValueException(ErrorCode.BAD_REQUEST_DATA);
		}
		CertificationJob certificationJob = new CertificationJob(job, certification, createJobRequest.weight());
		certificationJobRepository.save(certificationJob);
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
    public AdminCertificationDetailResponse getCertificationDetail(Long certificationId) {

        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));

        List<CertificationMajor> certificationMajorList = certificationMajorRepository.findAllByCertification(certification);
        List<CertificationJob> certificationJobList = certificationJobRepository.findAllByCertification(certification);

        return AdminCertificationDetailResponse.of(
                certificationId,
                certificationMajorList.stream()
                        .map(CertificationMajorSimple::from)
                        .toList(),
                certificationJobList.stream()
                        .map(CertificationJobSimple::from)
                        .toList()
        );
    }

    public AdminCertificationListResponse getAllCertifications() {

        List<Certification> allCertifications = certificationRepository.findAll();

        return AdminCertificationListResponse.of(
                allCertifications.stream()
                        .map(AdminCertificationResponse::from)
                        .toList()
        );
    }

    @Transactional
    public void deleteCertification(Long certificationId) {

        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DATA_NOT_FOUND));

        certificationJobRepository.deleteAllByCertification(certification);
        certificationMajorRepository.deleteAllByCertification(certification);
        acquisitionRepository.deleteAllByCertification(certification);
        favoriteRepository.deleteAllByCertification(certification);
        certificationCategoryRepository.deleteAllByCertification(certification);
        userPreCertificationRepository.deleteAllByCertification(certification);

        certificationRepository.delete(certification);
    }
}
