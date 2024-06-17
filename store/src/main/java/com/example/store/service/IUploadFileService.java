package com.example.store.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUploadFileService {
    String uploadFile(MultipartFile multipartFile) throws IOException;
}
