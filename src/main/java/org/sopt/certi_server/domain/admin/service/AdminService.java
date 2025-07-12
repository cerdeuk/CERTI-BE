package org.sopt.certi_server.domain.admin.service;

import org.sopt.certi_server.domain.admin.dto.request.CertificationMajorCreateRequest;
import org.sopt.certi_server.domain.admin.dto.request.*;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.acquisition.repository.AcquisitionRepository;
import org.sopt.certi_server.domain.admin.dto.response.*;
import org.sopt.certi_server.domain.certification.entity.Agency;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.CertificationJob;
import org.sopt.certi_server.domain.certification.entity.CertificationMajor;
import org.sopt.certi_server.domain.certification.repository.AgencyRepository;
import org.sopt.certi_server.domain.certification.repository.CertificationJobRepository;
import org.sopt.certi_server.domain.certification.repository.CertificationMajorRepository;
import org.sopt.certi_server.domain.certification.repository.CertificationRepository;
import org.sopt.certi_server.domain.favorite.repository.FavoriteRepository;
import org.sopt.certi_server.domain.major.entity.MajorImpl;
import org.sopt.certi_server.domain.major.repository.MajorImplRepository;
import org.sopt.certi_server.domain.major.repository.MajorRepository;
import org.sopt.certi_server.domain.userprecertification.repository.UserPreCertificationRepository;
import org.sopt.certi_server.domain.certification.service.CertificationService;
import org.sopt.certi_server.domain.job.entity.Job;
import org.sopt.certi_server.domain.job.repository.JobRepository;
import org.sopt.certi_server.domain.major.entity.Major;
import org.sopt.certi_server.domain.major.service.MajorService;
import org.sopt.certi_server.global.error.code.ErrorCode;
import org.sopt.certi_server.global.error.exception.InvalidValueException;
import org.sopt.certi_server.global.error.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
	private final MajorService majorService;
	private final CertificationService certificationService;
	private final CertificationMajorRepository certificationMajorRepository;
	private final JobRepository jobRepository;
	private final CertificationJobRepository certificationJobRepository;
    private final CertificationRepository certificationRepository;
    private final AcquisitionRepository acquisitionRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserPreCertificationRepository userPreCertificationRepository;
	private final MajorRepository majorRepository;
	private final MajorImplRepository majorImplRepository;
	private final AgencyRepository agencyRepository;

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
        userPreCertificationRepository.deleteAllByCertification(certification);

        certificationRepository.delete(certification);
    }

	@Transactional
	public void addMajor(MajorCreateRequest request) {
		try{
			majorRepository.save(Major.create(request.majorName()));
		}catch (DataIntegrityViolationException e){
			throw new DataIntegrityViolationException("이미 존재하는 중분류 학과입니다. \n" + e);
		}
	}

	@Transactional
	public void addMajorImpl(MajorImplCreateRequest request){
		Major major = majorRepository.findByName(request.majorName())
				.orElseThrow(() -> new NotFoundException(ErrorCode.MAJOR_NOT_FOUND));
		try{
			majorImplRepository.save(MajorImpl.create(major, request.majorImplName()));
		}catch (DataIntegrityViolationException e){
			throw new DataIntegrityViolationException("이미 존재하는 세부 학과입니다. \n" + e);
		}
	}

	@Transactional
	public void addCertificationMajor(CertificationMajorCreateRequest request) {

		Major major = majorRepository.findByName(request.majorName())
				.orElseThrow(() -> new NotFoundException(ErrorCode.MAJOR_NOT_FOUND));
		Certification certification = certificationRepository.findByName(request.certificationName())
				.orElseThrow(() -> new NotFoundException(ErrorCode.CERTIFICATION_NOT_FOUND));

		try{
			certificationMajorRepository.save(CertificationMajor.create(certification, major, request.weight()));
		}catch (DataIntegrityViolationException e){
			throw new DataIntegrityViolationException("이미 존재하는 (자격증 - 학과) 가중치 매핑입니다. \n" + e);
		}
	}

	@Transactional
	public void addJob(JobCreateRequest request) {
		try{
			jobRepository.save(Job.create(request.jobName()));
		}catch (DataIntegrityViolationException e){
			throw new DataIntegrityViolationException("이미 존재하는 직무입니다. \n" + e);
		}
	}

	@Transactional
	public void addCertificationJob(CertificationJobCreateRequest request){

		Job job = jobRepository.findByName(request.jobName())
				.orElseThrow(() -> new NotFoundException(ErrorCode.JOB_NOT_FOUND));
		Certification certification = certificationRepository.findByName(request.certificationName())
				.orElseThrow(() -> new NotFoundException(ErrorCode.CERTIFICATION_NOT_FOUND));

		try{
			certificationJobRepository.save(CertificationJob.create(certification, job, request.weight()));
		}catch (DataIntegrityViolationException e){
			throw new DataIntegrityViolationException("이미 존재하는 (자격증 - 직무) 가중치 매핑입니다. \n" + e);
		}
	}

	@Transactional
	public void addAgency(AgencyCreateRequest request) {
		try{
			agencyRepository.save(Agency.create(request.agencyName()));
		}catch (DataIntegrityViolationException e){
			throw new DataIntegrityViolationException("이미 존재하는 인증기관입니다.");
		}
	}
}
