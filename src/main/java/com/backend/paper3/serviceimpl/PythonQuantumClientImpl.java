package com.backend.paper3.serviceimpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.backend.paper3.dto.PythonQuantumRequestDto;
import com.backend.paper3.exception.ApiException;
import com.backend.paper3.service.PythonQuantumClient;

@Service
public class PythonQuantumClientImpl implements PythonQuantumClient {

    @Value("${python.service.base-url:http://localhost:8000}")
    private String pythonServiceBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, Object> simulateAmplitude(PythonQuantumRequestDto request) {

        String url = pythonServiceBaseUrl + "/quantum/amplitude-simulate";

        return callPythonQuantumApi(url, request);
    }

    @Override
    public Map<String, Object> simulateInterference(PythonQuantumRequestDto request) {

        String url = pythonServiceBaseUrl + "/quantum/interference-simulate";

        return callPythonQuantumApi(url, request);
    }

    @Override
    public Map<String, Object> generateQasm(PythonQuantumRequestDto request) {

        String url = pythonServiceBaseUrl + "/quantum/generate-qasm";

        return callPythonQuantumApi(url, request);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> callPythonQuantumApi(
            String url,
            PythonQuantumRequestDto request
    ) {

        try {

            Map<String, Object> response =
                    restTemplate.postForObject(
                            url,
                            request,
                            Map.class
                    );

            if (response == null) {
                throw new ApiException("Python quantum response is empty");
            }

            return response;

        } catch (HttpStatusCodeException e) {

            throw new ApiException(
                    "Python quantum API failed : " + e.getResponseBodyAsString()
            );

        } catch (ResourceAccessException e) {

            throw new ApiException(
                    "Python service is not reachable. Start Python service on port 8000."
            );

        } catch (ApiException e) {

            throw e;

        } catch (Exception e) {

            throw new ApiException(
                    "Unexpected error while calling Python quantum API : " + e.getMessage()
            );
        }
    }
}