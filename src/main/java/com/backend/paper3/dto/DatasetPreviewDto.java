package com.backend.paper3.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatasetPreviewDto {

    private Long datasetId;

    private String datasetUniqueId;

    private String datasetName;

    private String fileType;

    private List<String> columns;

    private List<List<String>> rows;
}