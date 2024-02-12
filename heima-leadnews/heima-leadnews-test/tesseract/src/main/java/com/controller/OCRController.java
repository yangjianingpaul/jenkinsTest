package com.controller;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

@RestController
public class OCRController {

    @GetMapping("ocr")
    public ResponseEntity<String> performOCR(@RequestParam String url2) throws IOException, TesseractException {
        Tesseract instance = new Tesseract();
        instance.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
        instance.setLanguage("chi_sim");
        URL url = new URL(url2);
        BufferedImage image = ImageIO.read(url);
        String result = instance.doOCR(image);
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }
}
