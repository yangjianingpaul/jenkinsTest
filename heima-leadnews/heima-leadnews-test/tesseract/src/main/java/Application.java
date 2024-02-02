import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Application {
    public static void main(String[] args) throws TesseractException {
        Tesseract instance = new Tesseract();
        instance.setDatapath("/Users/paulyang/Desktop/tessdata");
        instance.setLanguage("chi_sim");
        File file = new File("/Users/paulyang/Desktop/test.png");
        String result = instance.doOCR(file);
        System.out.println(result);
    }
}
