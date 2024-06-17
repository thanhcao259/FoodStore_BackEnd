package com.example.store.service.implement;

import com.cloudinary.Cloudinary;
import com.example.store.service.IUploadFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

    private final Cloudinary cloudinary;

    public UploadFileServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Transactional
    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url").toString();
    }
}
