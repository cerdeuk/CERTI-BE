package org.sopt.certi_server.global.s3;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

import java.net.URL;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Template s3Template;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * S3에서 파일을 업로드하기 위한 Presigned URL 생성
     * @param key 파일 경로 및 이름 (예: "images/profile.png")
     * @return 생성된 URL
     */
    public String getPreSignedUrlForUpload(String key) {
        // 10분 동안 유효한 업로드용 URL 생성
        URL preSignedUrl = s3Template.createSignedPutURL(bucketName, key, Duration.ofMinutes(10));
        return preSignedUrl.toString();
    }

//    public String getPublicKey(String key){
//        return String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucketName, key);
//    }

    public String getPublicKey(String key){
        return s3Client.utilities()
                .getUrl(GetUrlRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build())
                .toString();
    }

    /**
     * S3에 있는 파일을 조회하기 위한 Presigned URL 생성
     */
    public String getPreSignedUrlForDownload(String key) {
        // 10분 동안 유효한 조회용 URL 생성
        URL preSignedUrl = s3Template.createSignedGetURL(bucketName, key, Duration.ofMinutes(10));
        return preSignedUrl.toString();
    }
}
