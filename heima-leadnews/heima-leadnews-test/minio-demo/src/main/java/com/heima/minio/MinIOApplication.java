package com.heima.minio;

import com.heima.file.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootApplication
public class MinIOApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinIOApplication.class, args);
    }
}
