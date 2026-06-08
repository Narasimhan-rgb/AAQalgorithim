package com.backend.paper3.serviceimpl;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.backend.paper3.dto.SystemValidationDto;
import com.backend.paper3.repository.AlgorithmRecommendationRepository;
import com.backend.paper3.repository.BenchmarkResultRepository;
import com.backend.paper3.repository.DatasetRepository;
import com.backend.paper3.repository.QuantumAaqMetricsRepository;
import com.backend.paper3.repository.SortingJobRepository;
import com.backend.paper3.service.SystemValidationService;

@Service
public class SystemValidationServiceImpl implements SystemValidationService {

    @Value("${python.service.base-url:http://localhost:8000}")
    private String pythonServiceBaseUrl;

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private SortingJobRepository sortingJobRepository;

    @Autowired
    private BenchmarkResultRepository benchmarkResultRepository;

    @Autowired
    private AlgorithmRecommendationRepository algorithmRecommendationRepository;

    @Autowired
    private QuantumAaqMetricsRepository quantumAaqMetricsRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public SystemValidationDto validateSystem() {

        SystemValidationDto dto = new SystemValidationDto();

        dto.setBackendStatus("RUNNING");

        dto.setPythonServiceBaseUrl(pythonServiceBaseUrl);

        validateDatabase(dto);

        validatePythonService(dto);

        setOverallStatus(dto);

        dto.setGeneratedAt(LocalDateTime.now());

        return dto;
    }

    private void validateDatabase(SystemValidationDto dto) {

        try {

            dto.setTotalDatasets(datasetRepository.count());

            dto.setTotalSortingJobs(sortingJobRepository.count());

            dto.setTotalBenchmarkResults(benchmarkResultRepository.count());

            dto.setTotalRecommendations(algorithmRecommendationRepository.count());

            dto.setTotalQuantumMetricRecords(quantumAaqMetricsRepository.count());

            dto.setDatabaseStatus("CONNECTED");

        } catch (Exception e) {

            dto.setDatabaseStatus("FAILED");

            dto.setTotalDatasets(0L);

            dto.setTotalSortingJobs(0L);

            dto.setTotalBenchmarkResults(0L);

            dto.setTotalRecommendations(0L);

            dto.setTotalQuantumMetricRecords(0L);
        }
    }

    @SuppressWarnings("unchecked")
    private void validatePythonService(SystemValidationDto dto) {

        String healthUrl = pythonServiceBaseUrl + "/health";

        try {

            Map<String, Object> response =
                    restTemplate.getForObject(
                            healthUrl,
                            Map.class
                    );

            dto.setPythonHealthResponse(response);

            dto.setPythonServiceStatus("CONNECTED");

        } catch (ResourceAccessException e) {

            dto.setPythonServiceStatus("NOT_REACHABLE");

            dto.setPythonHealthResponse(null);

        } catch (Exception e) {

            dto.setPythonServiceStatus("FAILED");

            dto.setPythonHealthResponse(null);
        }
    }

    private void setOverallStatus(SystemValidationDto dto) {

        boolean databaseOk =
                "CONNECTED".equalsIgnoreCase(
                        dto.getDatabaseStatus()
                );

        boolean pythonOk =
                "CONNECTED".equalsIgnoreCase(
                        dto.getPythonServiceStatus()
                );

        if (databaseOk && pythonOk) {

            dto.setOverallStatus("READY");

            dto.setMessage(
                    "Backend, database, and Python service are connected successfully."
            );

            return;
        }

        if (databaseOk) {

            dto.setOverallStatus("PARTIAL");

            dto.setMessage(
                    "Backend and database are working, but Python service is not reachable."
            );

            return;
        }

        dto.setOverallStatus("FAILED");

        dto.setMessage(
                "System validation failed. Check database and Python service."
        );
    }
}