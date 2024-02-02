package com.heima.freemarker;

import com.heima.freemarker.entity.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SpringBootApplication
public class FreemarkerDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(FreemarkerDemoApplication.class, args);
    }
}
