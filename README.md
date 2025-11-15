# **🧭 Routy — 국내 여행 일정 관리 플랫폼**

<center>
<img width="900" height="700" alt="image" src="https://github.com/user-attachments/assets/f97687df-e0a0-4c4e-8dd5-8f989fdcb6b7" />
</center>

<br><br>

## 👨‍💻 DEVELOPERS

<table>
  <tr>
    <td align="center">
      <img width="100" height="1024" alt="동근" src="https://github.com/user-attachments/assets/8f7ce39c-afdc-477d-92b5-a80c3327ff6b" />
    </td>
    <td align="center">
      <img width="100" height="1024" alt="승민" src="https://github.com/user-attachments/assets/55d9f5b1-0d88-4569-9db3-debce16bd524" />
    </td>
    <td align="center">
      <img width="100" height="1024" alt="민철" src="https://github.com/user-attachments/assets/ac93b96e-f3de-4f75-a30d-35a3b2d870ca" />
    </td>
    <td align="center">
      <img width="100" height="1024" alt="승건" src="https://github.com/user-attachments/assets/491f0361-1eff-4fbf-bbf7-b31e105de293" />
    </td>
    <td align="center">
      <img width="100" height="1024" alt="지윤" src="https://github.com/user-attachments/assets/2e12ff4f-9cc8-4029-b0c1-1dc014789392" />
    </td>
    <td align="center">
      <img width="100" height="100" alt="혜원" src="https://github.com/user-attachments/assets/b7de8188-0686-44e3-89c0-9a120db21866" />
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/dddd0ng"><b>곽동근</b></a>
    </td>
    <td align="center">
      <a href="https://github.com/indy0322"><b>김승민</b></a>
    </td>
    <td align="center">
      <a href="https://github.com/bynmch"><b>변민철</b></a>
    </td>
    <td align="center">
      <a href="https://github.com/Seung-Geon"><b>이승건</b></a>
    </td>
    <td align="center">
      <a href="https://github.com/Easy-going12"><b>이지윤</b></a>
    </td>
    <td align="center">
      <a href="https://github.com/haenin"><b>최혜원</b></a>
    </td>
  </tr>
</table>

<br>

## 📜 목차

#### [💡 기술 스택](#-기술-스택)  <br>
#### [📢 프로젝트 소개](#-프로젝트-소개)  <br>
#### [🐾 Catchy FE 테스트 결과 보고서](#-catchy-fe-테스트-결과-보고서)  <br>
#### [🔡 요구사항 명세서](#-요구사항-명세서)  <br>
#### [📟 REST API 명세서](#-rest-api-명세서)  <br>
#### [🗃️ DB 모델링](#-db-모델링)  <br>
#### [📈 플로우 차트](#-플로우-차트)  <br>
#### [🪄 Figma](#-figma)  <br>
#### [🛜 프로젝트 아키텍처](#-프로젝트-아키텍처)  <br>
#### [🚩 젠킨스 파이프라인 파일 스크립트 코드](#-젠킨스-파이프라인-파일-스크립트-코드)  <br>
#### [📱 CI/CD 테스트](#-cicd-테스트)  <br>
#### [🍪 개인 회고록](#-개인-회고록)  <br>

<br>




### **💡 서비스 개요** 

**사용자가 여행지를 계획할 때** 

**도착지점 지도 기반 장소 추천과 일정 구성 기능을 통해 더 쉽고 빠르게 나만의 여행 일정을 완성할 수 있도록 돕는 플랫폼입니다.** 
**사용자는 여행 지역, 기간, 테마(맛집, 카페, 관광지)를 선택하고 선택을 기반으로 맛집 · 카페 · 관광지를 조합하여 일정으로 구성할 수 있습니다.** 

--- 

### 🚀 서비스 목표

- 복잡한 일정 짜기 과정을 단순화
- **지도 기반 시각화로 직관적인 여행 일정 구성 경험 제공**
- 사용자가 만든 일정을 **공유 · 열람할 수 있는 개방형 일정 플랫폼 구현**

--- ## ✨ 주요 기능

### 🚩 **핵심 기능** 

- 지도 기반 장소 탐색 및 일정 구성
- 내가 구성한 일정 **수정 / 삭제** 기능 제공
- 다른 사람의 일정을 **북마크(스크랩)** 하여 내 일정에 추가 또는 삭제 가능
- 추천받은 장소의 **상세 정보 조회** 가능

--- 

### 🧭 기능 흐름 
1. 사용자가 **여행 갈 지역** 선택
2. **여행 기간(며칠 동안)** 선택
3. **여행 테마** 선택 (맛집 / 카페 / 관광지)
4. 추천된 장소 중 **맛집·카페·관광지**를 조합해 **나만의 일정 구성**
