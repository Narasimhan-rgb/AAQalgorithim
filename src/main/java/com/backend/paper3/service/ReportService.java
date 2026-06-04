package com.backend.paper3.service;

import com.backend.paper3.dto.ReportSummaryDto;

public interface ReportService {

    ReportSummaryDto getDatasetReport(
            Long datasetId
    );
}