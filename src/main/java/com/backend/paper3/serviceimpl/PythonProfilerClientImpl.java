package com.backend.paper3.serviceimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.backend.paper3.dto.PythonProfileRequestDto;
import com.backend.paper3.dto.PythonProfileResponseDto;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.service.PythonProfilerClient;

@Service
public class PythonProfilerClientImpl
        implements PythonProfilerClient {

    @Value("${python.service.base-url:http://localhost:8000}")
    private String pythonServiceBaseUrl;

    private final RestTemplate restTemplate =
            new RestTemplate();

    @Override
    public PythonProfileResponseDto profileDataset(
            PythonProfileRequestDto request
    ) {

        String url =
                pythonServiceBaseUrl + "/profile/dataset";

        try {

            return restTemplate.postForObject(
                    url,
                    request,
                    PythonProfileResponseDto.class
            );

        } catch (HttpStatusCodeException e) {

            throw new ApiException(
                    "Python profiling failed : "
                            + e.getResponseBodyAsString()
            );

        } catch (ResourceAccessException e) {

            throw new ApiException(
                    "Python service is not reachable. Start Python service on port 8000."
            );

        } catch (Exception e) {

            throw new ApiException(
                    "Unexpected error while calling Python profiler : "
                            + e.getMessage()
            );
        }
    }
}