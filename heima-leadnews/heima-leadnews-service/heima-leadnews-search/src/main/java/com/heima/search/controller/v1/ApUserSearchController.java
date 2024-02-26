package com.heima.search.controller.v1;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.HistorySearchDto;
import com.heima.search.service.ApUserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/history")
public class ApUserSearchController {

    @Autowired
    private ApUserSearchService apUserSearchService;

    /**
     * search history
     *
     * @return
     */
    @PostMapping("/load")
    public ResponseResult findUserSearch() {
        return apUserSearchService.findUserSearch();
    }

    /**
     * delete search history
     *
     * @param keyWordId
     * @return
     */
    @PostMapping("/del")
    public ResponseResult delUserSearch(@RequestBody String keyWordId) {
        JSONObject jsonObject = JSON.parseObject(keyWordId);
        String id = jsonObject.getString("id");
        HistorySearchDto dto = new HistorySearchDto();
        dto.setId(id);
        return apUserSearchService.delUserSearch(dto);
    }
}
