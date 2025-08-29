package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        
        String genUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        String body = "{\"name\":\"Danish Shah\",\"regNo\":\"22BCI0194\",\"email\":\"danish.piyushbhai2022@vitstudent.ac.in\"}";

        HttpHeaders genHeaders = new HttpHeaders();
        genHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> genEntity = new HttpEntity<>(body, genHeaders);

        ResponseEntity<String> genResp = restTemplate.postForEntity(genUrl, genEntity, String.class);
        JsonNode json = mapper.readTree(genResp.getBody());
        String webhookUrl = json.get("webhook").asText();
        String token = json.get("accessToken").asText();

        System.out.println("Webhook: " + webhookUrl);
        System.out.println("AccessToken: " + token);

        String sql =
            "SELECT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME, " +
            "COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT " +
            "FROM EMPLOYEE e " +
            "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
            "LEFT JOIN EMPLOYEE e2 ON e.DEPARTMENT = e2.DEPARTMENT AND e2.DOB > e.DOB " +
            "GROUP BY e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME " +
            "ORDER BY e.EMP_ID DESC";

        String testUrl = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
        HttpHeaders testHeaders = new HttpHeaders();
        testHeaders.setContentType(MediaType.APPLICATION_JSON);
        testHeaders.set("Authorization", token); 

        String finalBody = "{\"finalQuery\":\"" + sql.replace("\"", "\\\"") + "\"}";
        HttpEntity<String> testEntity = new HttpEntity<>(finalBody, testHeaders);

        ResponseEntity<String> testResp = restTemplate.postForEntity(testUrl, testEntity, String.class);
        System.out.println("Response: " + testResp.getBody());
    }
}

