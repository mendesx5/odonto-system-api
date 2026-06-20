package com.odonto.odonto_system.shared.storage;

import com.odonto.odonto_system.shared.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final S3Client s3Client;

    @Value("${R2_BUCKET_NAME:odonto-exams}")
    private String bucketName;

    @Value("${R2_PUBLIC_URL:https://sua-url-publica.r2.dev}")
    private String publicUrl;

    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024;
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/png", "image/webp", "application/pdf"
    );

    public StorageResult upload (MultipartFile file, UUID patientId) {
        // Validação de segurança e tamanho
        if (file.isEmpty()) {
            throw new ConflictException("Não é possível fazer upload de um arquivo vazio.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ConflictException("O arquivo excede o limite máximo permitido de 20MB.");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new ConflictException("Formato de arquivo não suportado. Permita apenas JPEG, PNG, WEBP ou PDF.");
        }

        try {
            // Estrutura inteligente de pastas
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";

            String storageKey = "patients/" + patientId + "/exams/" + UUID.randomUUID() + extension;

            // Preparar a requisição de upload
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // URL final
            String filePublicUrl = publicUrl + "/" + storageKey;

            return new StorageResult(storageKey, filePublicUrl);

        } catch (IOException e) {
            throw new RuntimeException("Falha crítica ao ler os bytes do arquivo para upload.", e);
        }
    }

    public void delete (String storageKey) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao remover o arquivo físico do provedor de armazenamento.", e);
        }
    }

}
