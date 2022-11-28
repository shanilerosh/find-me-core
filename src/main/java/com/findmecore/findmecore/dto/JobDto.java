package com.findmecore.findmecore.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ShanilErosh
 */
@Data
@Builder
public class JobDto implements Serializable {

    private Long jobId;
    private String title;
    private String type;
    private String content;
    private String createdOn;
    private Date expiryDate;

    private String employerName;
    private String employerPic;


}
