package com.heima.model.wemedia.dtos;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WmNewsDto {
    
    private Integer id;
     /**
     * title
     */
    private String title;
     /**
     * Channel id
     */
    private Integer channelId;
     /**
     * labels
     */
    private String labels;
     /**
     * publish time
     */
    private Date publishTime;
     /**
     * content
     */
    private String content;
     /**
     * cover type  0 no images 1 single image 3 multi imag -1 auto
     */
    private Short type;
     /**
     * submit time
     */
    private Date submitedTime; 
     /**
     * status:submited 1  draft 0
     */
    private Short status;
     
     /**
     * Cover image list, multiple pictures are separated by commas
     */
    private List<String> images;

    /**
     * up or down：
     *      0：Take down articles
     *      1：Show article
     */
    private Short enable;
}