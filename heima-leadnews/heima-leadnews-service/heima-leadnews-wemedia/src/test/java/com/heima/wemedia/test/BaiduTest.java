package com.heima.wemedia.test;

import com.heima.common.baidu.GreenImageScan;
import com.heima.common.baidu.GreenTextScan;
import com.heima.file.service.FileStorageService;
import com.heima.wemedia.WemediaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Map;

@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class BaiduTest {
    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private GreenImageScan greenImageScan;
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 测试文本内容审核
     */
    @Test
    public void testScanText() throws Exception {
        Map<String, String> map = greenTextScan.textScan("我是一个冰毒");
        System.out.println(map);
    }

    /**
     * 测试图片内容审核
     */
    @Test
    public void testScanImage() throws Exception {
        byte[] bytes = fileStorageService.downLoadFile("http://192.168.31.71:9000/leadnews/2023/12/05/08c81e9f98c6478a840b05b25829ce00.jpeg");
        Map map = greenImageScan.imageScan(bytes);
        System.out.println(map);
    }
}
