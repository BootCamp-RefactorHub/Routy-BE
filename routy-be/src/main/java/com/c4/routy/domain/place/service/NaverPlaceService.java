package com.c4.routy.domain.place.service;

import com.c4.routy.domain.place.dto.NaverLocalItem;
import com.c4.routy.domain.place.dto.NaverLocalSearchResponse;
import com.c4.routy.domain.place.dto.NaverPlaceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NaverPlaceService {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.test.mode:false}")
    private boolean testMode;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 테마별 장소 추천 검색
     */
    public List<NaverPlaceDTO> searchByTheme(String theme, String region) {
        if (testMode || "default".equals(clientId)) {
            log.warn("⚠️ 테스트 모드 - 목 데이터 반환");
            return getMockData(theme, region);
        }

        String keyword = buildSearchKeyword(theme, region);
        log.info("🌐 실제 네이버 API 호출 시작: {}", keyword);
        return searchPlaces(keyword);
    }

    /**
     * 검색 키워드 생성
     */
    private String buildSearchKeyword(String theme, String region) {
        String themeKeyword = switch(theme.toLowerCase()) {
            case "restaurant", "맛집" -> "맛집";
            case "cafe", "카페" -> "커피"; // 🔥 카페 대신 커피
            case "tourist", "관광지" -> "관광";
            default -> "";
        };
        return region + " " + themeKeyword;
    }

    /**
     * 네이버 로컬 검색 API 호출
     */
    public List<NaverPlaceDTO> searchPlaces(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = String.format(
                    "https://openapi.naver.com/v1/search/local.json?query=%s&display=15",
                    encodedQuery
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            log.info("📡 네이버 API 요청");
            log.info("  - URL: {}", url);
            log.info("  - Client ID: {}***", clientId.substring(0, Math.min(5, clientId.length())));

            ResponseEntity<NaverLocalSearchResponse> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, NaverLocalSearchResponse.class);

            if (response.getBody() != null && response.getBody().getItems() != null) {
                int count = response.getBody().getItems().size();
                log.info("✅ 네이버 API 응답 성공: {}건", count);

                // 🔥 결과가 0건이면 목 데이터 반환
                if (count == 0) {
                    log.warn("⚠️ 검색 결과 0건 - 목 데이터로 대체");
                    return getMockDataByQuery(query);
                }

                if (count > 0) {
                    log.info("📍 첫 번째 장소: {}",
                            removeHtmlTags(response.getBody().getItems().get(0).getTitle()));
                }

                return convertToPlaceDTOs(response.getBody().getItems());
            } else {
                log.warn("⚠️ 네이버 API 응답이 비어있음 - 목 데이터로 대체");
                return getMockDataByQuery(query);
            }

        } catch (Exception e) {
            log.error("❌ 네이버 API 호출 실패: {}", e.getMessage());
            log.warn("🔄 목 데이터로 대체");
            return getMockDataByQuery(query);
        }
    }

    /**
     * 네이버 응답을 NaverPlaceDTO로 변환
     */
    private List<NaverPlaceDTO> convertToPlaceDTOs(List<NaverLocalItem> items) {
        return items.stream()
                .map(item -> NaverPlaceDTO.builder()
                        .placeName(removeHtmlTags(item.getTitle()))
                        .category(item.getCategory())
                        .addressName(item.getAddress())
                        .roadAddress(item.getRoadAddress())
                        .phone(item.getTelephone())
                        .latitude(convertCoordinate(item.getMapy()))
                        .longitude(convertCoordinate(item.getMapx()))
                        .naverLink(item.getLink())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * HTML 태그 제거
     */
    private String removeHtmlTags(String text) {
        if (text == null) return "";
        return text.replaceAll("<[^>]*>", "");
    }

    /**
     * 네이버 좌표를 위경도로 변환
     */
    private Double convertCoordinate(String coord) {
        try {
            return Double.parseDouble(coord) / 10000000.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * 쿼리 기반 목 데이터
     */
    private List<NaverPlaceDTO> getMockDataByQuery(String query) {
        if (query.contains("카페") || query.contains("커피")) {
            return getMockData("cafe", extractRegion(query));
        } else if (query.contains("맛집") || query.contains("음식")) {
            return getMockData("restaurant", extractRegion(query));
        } else if (query.contains("관광") || query.contains("여행")) {
            return getMockData("tourist", extractRegion(query));
        }
        return getMockData("cafe", "대구"); // 기본값
    }

    /**
     * 쿼리에서 지역명 추출
     */
    private String extractRegion(String query) {
        if (query.contains("대구")) return "대구";
        if (query.contains("서울")) return "서울";
        if (query.contains("부산")) return "부산";
        return "대구"; // 기본값
    }

    /**
     * 테스트용 목 데이터
     */
    private List<NaverPlaceDTO> getMockData(String theme, String region) {
        List<NaverPlaceDTO> mockData = new ArrayList<>();

        if ("cafe".equalsIgnoreCase(theme) || "카페".equals(theme)) {
            mockData.add(createMockPlace(
                    "보드게임카페 레드버튼 통성로점", "카페,디저트>카페",
                    region + " 중구 통성로 25", "053-123-4567", 35.8686, 128.5944
            ));
            mockData.add(createMockPlace(
                    "녹슬 대구동성로점", "카페,디저트>카페",
                    region + " 중구 동성로2가 22", "053-234-5678", 35.8690, 128.5950
            ));
            mockData.add(createMockPlace(
                    "별툰 파리지엥 동성로점", "카페,디저트>카페",
                    region + " 중구 동성로3가 26", "053-345-6789", 35.8695, 128.5955
            ));
            mockData.add(createMockPlace(
                    "레드버튼 동성로2호점", "카페,디저트>카페",
                    region + " 중구 동성로길 62", "053-456-7890", 35.8700, 128.5960
            ));
            mockData.add(createMockPlace(
                    "맨선5", "카페,디저트>카페",
                    region + " 중구 종합상가7길 28", "053-567-8901", 35.8705, 128.5965
            ));
        } else if ("restaurant".equalsIgnoreCase(theme) || "맛집".equals(theme)) {
            mockData.add(createMockPlace(
                    "동아식당", "한식>한정식",
                    region + " 중구 국채보상로 125-4", "053-111-2222", 35.8686, 128.5944
            ));
            mockData.add(createMockPlace(
                    "중앙떡볶이", "한식>분식",
                    region + " 중구 동성로2가 81", "053-222-3333", 35.8690, 128.5950
            ));
            mockData.add(createMockPlace(
                    "낙영껍데기 본점", "한식>고기",
                    region + " 중구 동성로3가 9-17", "053-333-4444", 35.8695, 128.5955
            ));
        } else if ("tourist".equalsIgnoreCase(theme) || "관광지".equals(theme)) {
            mockData.add(createMockPlace(
                    "대구 근대역사관", "관광지>역사유적",
                    region + " 중구 경상감영길 지하 89", "053-661-2000", 35.8686, 128.5944
            ));
            mockData.add(createMockPlace(
                    "동성로", "관광지>문화시설",
                    region + " 중구 동성로 122", "053-661-3081", 35.8690, 128.5950
            ));
            mockData.add(createMockPlace(
                    "수성못", "관광지>공원",
                    region + " 수성구 두산동 산 180", "053-803-7770", 35.8695, 128.5955
            ));
        }

        log.info("✅ 목 데이터 생성: {}건", mockData.size());
        return mockData;
    }

    /**
     * 목 데이터 생성 헬퍼
     */
    private NaverPlaceDTO createMockPlace(String name, String category,
                                          String address, String phone,
                                          double lat, double lng) {
        return NaverPlaceDTO.builder()
                .placeName(name)
                .category(category)
                .addressName(address)
                .roadAddress(address)
                .phone(phone)
                .latitude(lat)
                .longitude(lng)
                .naverLink("https://map.naver.com/")
                .build();
    }
}