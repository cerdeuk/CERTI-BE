package org.sopt.certi_server.domain.certification.dto.response;

import lombok.Getter;
import org.sopt.certi_server.domain.certification.entity.Certification;

import java.util.List;

@Getter
public class CertificationSimple{

    private Long certificationId;
    private String certificationName;
    private String certificationType;
    private String testType;
    private List<String> tags;
    private boolean isFavorite;

    public CertificationSimple(
            Certification certification,
            boolean isFavorite
    ){
        this.certificationId = certification.getId();
        this.certificationName = certification.getName();
        this.certificationType = certification.getCertificationType().getKoreanName();
        this.testType = certification.getTestType().getType();
        this.tags = certification.getTags();
        this.isFavorite = isFavorite;
    }
}
