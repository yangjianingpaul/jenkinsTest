import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Application {
    public static void main(String[] args) throws TesseractException {
        Tesseract instance = new Tesseract();
        instance.setDatapath("/usr/share/tesseract-ocr/5/tessdata");
        instance.setLanguage("chi_sim");
        File file = new File("/usr/local/tessdata/test.jpg");
        String result = instance.doOCR(file);
        System.out.println(result);
    }
}
