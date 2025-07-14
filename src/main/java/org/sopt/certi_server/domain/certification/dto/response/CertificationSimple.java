package org.sopt.certi_server.domain.certification.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.sopt.certi_server.domain.certification.entity.Certification;

import java.util.List;

@Getter
public class CertificationSimple {

    private Long certificationId;
    private String certificationName;
    private String certificationType;
    private String testType;
    private List<String> tags;
    @JsonProperty(value = "isFavorite")
    private boolean favorite;

    public CertificationSimple(
            Certification certification,
            boolean favorite
    ) {
        this.certificationId = certification.getId();
        this.certificationName = certification.getName();
        this.certificationType = certification.getCertificationType() != null ?
                certification.getCertificationType().getKoreanName() : null;
        this.testType = certification.getTestType() != null ?
                certification.getTestType().getType() : null;
        this.tags = certification.getTags();
        this.favorite = favorite;
    }
}
