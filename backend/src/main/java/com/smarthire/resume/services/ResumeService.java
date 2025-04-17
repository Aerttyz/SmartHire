package com.smarthire.resume.services;

import com.smarthire.resume.exceptions.EmptyPathException;
import com.smarthire.resume.exceptions.FlaskConnectionException;
import com.smarthire.resume.exceptions.InvalidPathException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ResumeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskUrl = "http://localhost:5000/extract_entities";

    public Map<String, Object> analyzeFolder(String path) {
        if(path == null || path.isEmpty()) {
            throw new EmptyPathException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = Map.of("path", path);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, entity, Map.class);
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            throw new InvalidPathException();
        } catch (ResourceAccessException ex) {
            throw new FlaskConnectionException();
        }
    }
}

