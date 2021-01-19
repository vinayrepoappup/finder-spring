package com.example.finderspring.endpoint;

import com.example.finderspring.service.BulkEmailService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;


@RestController
@RequestMapping(value = "/finder")
public class BulkeEmailController {

    @Autowired
    BulkEmailService bulkEmailService;

    @PostMapping(value = "/verify/bulk",produces = {"application/json"})
    public String bulkEmailVerify(@RequestParam("file")MultipartFile file ) throws IOException {
        String result = bulkEmailService.bulkEmailVerify(file);
        return result;
    }
}
