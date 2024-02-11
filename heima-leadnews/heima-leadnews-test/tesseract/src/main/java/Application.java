import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Application {
    public static void main(String[] args) throws TesseractException {
        Tesseract instance = new Tesseract();
        instance.setDatapath("/usr/local/tessdata");
        instance.setLanguage("chi_sim");
        File file = new File("/usr/local/tessdata/helloworld.png");
        String result = instance.doOCR(file);
        System.out.println(result);
    }
}
