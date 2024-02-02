package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.SensitiveDto;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.wemedia.service.WmSensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sensitive")
public class WmSensitiveController {

    @Autowired
    private WmSensitiveService wmSensitiveService;

    /**
     * get the sensitive list
     * @param dto
     * @return
     */
    @PostMapping("/list")
    public ResponseResult sensitiveList(@RequestBody SensitiveDto dto) {
        return wmSensitiveService.sensitiveList(dto);
    }

    /**
     * create a sensitive word
     * @param wmSensitive
     * @return
     */
    @PostMapping("/save")
    public ResponseResult sensitiveSave(@RequestBody WmSensitive wmSensitive) {
        return wmSensitiveService.sensitiveSave(wmSensitive);
    }

    /**
     * delete the sensitive word
     * @param id
     * @return
     */
    @DeleteMapping("/del/{id}")
    public ResponseResult sensitiveDelete(@PathVariable String id) {
        Integer sensitiveId = Integer.parseInt(id);
        return wmSensitiveService.sensitiveDelete(sensitiveId);
    }

    /**
     * update the sensitive word
     * @param wmSensitive
     * @return
     */
    @PostMapping("/update")
    public ResponseResult sensitiveUpdate(@RequestBody WmSensitive wmSensitive) {
        return wmSensitiveService.sensitiveUpdate(wmSensitive);
    }
}
