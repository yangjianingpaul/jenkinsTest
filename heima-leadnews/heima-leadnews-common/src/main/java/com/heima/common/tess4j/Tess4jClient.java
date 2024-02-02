package com.heima.common.tess4j;

import lombok.Getter;
import lombok.Setter;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "tess4j")
public class Tess4jClient {

    private String dataPath;
    private String language;

    public String doOCR(BufferedImage image) throws TesseractException {
        //Create a Tesseract object
        ITesseract tesseract = new Tesseract();
        //Set the font library path
        tesseract.setDatapath(dataPath);
        //Chinese recognition
        tesseract.setLanguage(language);
        //Perform ocr identification
        String result = tesseract.doOCR(image);
        //Replace the return and tal keys to make the result a row
        result = result.replaceAll("\\r|\\n", "-").replaceAll(" ", "");
        return result;
    }
}