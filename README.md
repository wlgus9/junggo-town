# Junggo-Town
중고타운은 사용자가 서로 중고물품을 판매하고 구매할 수 있는 API 프로젝트입니다.

---

## 프로젝트 환경
* java : jdk 17
* Spring Boot : 3.4.1
* Build : Gradle
* Database : MySQL 9.2.0
* 기타 라이브러리
  * Spring Web 
  * Spring Data JPA 
  * Spring Validation 
  * JSON Web Token (JJWT)
  * Lombok 
  * Spring Boot Test

---

## 프로젝트 구조도
```
src
├── main
│   ├── java
│   │   └── com
│   │       └── junggotown
│   │           ├── JunggoTownApplication.java
│   │           ├── controller
│   │           ├── domain
│   │           ├── dto
│   │           ├── global
│   │           │   ├── common    -> 공통으로 사용하는 메세지 관리
│   │           │   ├── config
│   │           │   ├── entity    -> 테이블에 공통으로 들어가는 필드 관리
│   │           │   ├── exception -> 커스텀 예외 처리 및 전역 예외 처리 관리
│   │           │   └── jwt 
│   │           ├── repository
│   │           └── service
│   └── resources
│       ├── application.yml
│       ├── static
│       └── templates
└── test
    ├── java
    │   └── com
    │       └── junggotown
    │           ├── JunggoTownApplicationTests.java
    │           ├── TestUtil.java -> MockMvc 요청 메서드 관리
    │           ├── chat
    │           ├── member
    │           ├── payment
    │           └── product
    └── resources
        ├── application-test.yml
        ├── schema.sql  -> 테스트용 DB 스키마
        ├── static
        └── templates
```
---

## 트러블슈팅
* 컨트롤러 계층 단위테스트 중 @WebMvcTest 사용 시 발생한 문제 
  * https://devstudy-record.tistory.com/86
* 스웨거 사용 중 @ControllerAdvice 도입 시 발생한 문제
  * https://devstudy-record.tistory.com/75