package com.odonto.odonto_system.shared.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class S3Config {

    @Value("${R2_ACCOUNT_ID:account_id}")
    private String accountId;

    @Value("${R2_ACCOUNT_KEY:access_key}")
    private String accessKey;

    @Value("${R2_SECRET_KEY:secret_key}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        String endpoint = "https://" + accountId + ".r2.cloudflarestorage.com";

        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .region(Region.US_EAST_1)
                .build();
    }
}
