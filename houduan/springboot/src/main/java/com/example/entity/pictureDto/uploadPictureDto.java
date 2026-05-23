package com.example.entity.pictureDto;

import lombok.Data;

@Data
public class uploadPictureDto {
    private String newUrl;
    private String oldUrl;
    private Integer pictureId;
}
