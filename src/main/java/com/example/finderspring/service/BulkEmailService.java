package com.example.finderspring.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BulkEmailService {
    public String bulkEmailVerify(MultipartFile file) throws IOException;
}
