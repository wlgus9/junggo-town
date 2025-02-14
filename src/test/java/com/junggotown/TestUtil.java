package com.junggotown;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // GET 요청 공통 메서드
    public static JsonNode performGetRequestAndGetResponse(MockMvc mockMvc, String url, String userId, String token, HttpStatus expectedStatus) throws Exception {
        return performRequestAndGetResponse(mockMvc, get(url), userId, token, null, expectedStatus);
    }

    // POST 요청 공통 메서드
    public static JsonNode performPostRequestAndGetResponse(MockMvc mockMvc, String url, String userId, String token, Object requestBody, HttpStatus expectedStatus) throws Exception {
        return performRequestAndGetResponse(mockMvc, post(url), userId, token, requestBody, expectedStatus);
    }

    // PATCH 요청 공통 메서드
    public static JsonNode performPatchRequestAndGetResponse(MockMvc mockMvc, String url, String userId, String token, Object requestBody, HttpStatus expectedStatus) throws Exception {
        return performRequestAndGetResponse(mockMvc, patch(url), userId, token, requestBody, expectedStatus);
    }

    // DELETE 요청 공통 메서드
    public static JsonNode performDeleteRequestAndGetResponse(MockMvc mockMvc, String url, String userId, String token, HttpStatus expectedStatus) throws Exception {
        return performRequestAndGetResponse(mockMvc, delete(url), userId, token, null, expectedStatus);
    }

    // Missing Token 요청 메서드
    public static JsonNode performMissingTokenRequestAndGetResponse(MockMvc mockMvc, String url, String userId, Object requestBody, HttpStatus expectedStatus) throws Exception {
        return performRequestAndGetResponse(mockMvc, post(url), userId, null, requestBody, expectedStatus);
    }

    // 공통 요청 처리 메서드 (GET, PATCH, DELETE)
    private static JsonNode performRequestAndGetResponse(MockMvc mockMvc,
                                                         MockHttpServletRequestBuilder requestBuilder,
                                                         String userId,
                                                         String token,
                                                         Object requestBody,
                                                         HttpStatus expectedStatus) throws Exception {
        if (requestBody != null) {
            requestBuilder.contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
                    .header("userId", userId);
        }

        if(token != null) {
            requestBuilder
                    .header("Authorization", "Bearer " + token);
        }

        ResultActions resultActions = mockMvc.perform(requestBuilder)
                .andExpect(status().is(expectedStatus.value()))
                .andDo(print());

        MvcResult result = resultActions.andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }
}
