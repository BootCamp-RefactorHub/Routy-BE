//package com.c4.routy.domain.chatbot.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/chatbot")
//public class ChatBotController {
//
//    @Value("${openai.secret-key}")
//    private String apiKey;
//
//    @PostMapping
//    public Map<String, String> chat(@RequestBody Map<String, String> request) {
//        String userMessage = request.get("message");
//
//        String aiResponse = callOpenAI(userMessage);
//
//        Map<String, String> result = new HashMap<>();
//        result.put("reply", aiResponse);
//        return result;
//    }
//
//    private String callOpenAI(String prompt) {
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            String url = "https://api.openai.com/v1/chat/completions";
//
//            String body = """
//                {
//                  "model": "gpt-3.5-turbo",
//                  "messages": [{"role": "user", "content": "%s"}]
//                }
//                """.formatted(prompt);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.setBearerAuth(apiKey);
//
//            HttpEntity<String> entity = new HttpEntity<>(body, headers);
//
//            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
//
//            Map choices = (Map) ((List) response.getBody().get("choices")).get(0);
//            Map message = (Map) choices.get("message");
//            return (String) message.get("content");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "AI 응답 중 오류가 발생했어요 😢";
//        }
//    }
//}
