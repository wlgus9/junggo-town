# junggo-town

# 중고거래 프로젝트

---

## 프로젝트 셋팅
* java : 17
* Spring Boot : 3.4.1
* Build : Gradle
* Database : MySQL 9.2.0

---

## API 권한 관리
유저마다 Bearer Token을 부여하는 방식 적용

### `Bearer token`
Bearer = 소유자

토큰을 전송하는 방식이다.

HTTP 인증 프로토콜 중에 하나로 클라이언트가 서버의 자원에 접근하고자 할 때 헤더에 Bearer token을 포함하고 요청하여 인증을 받는다.

일반적으로 OAuth나 JWT와 같은 프레임워크에서 사용된다.

### `JWT`
특정한 형식의 토큰을 정의하는 표준

`Json Web Token` : Json 포맷을 이용하여 사용자에 대한 속성을 저장하는 Web Token

Header, Payload, Signature로 구성되며 각 부분은 Base64로 인코딩되어 표현된다.

각 부분은 .을 구분자로 사용

* Header : token type, Signature 해싱 알고리즘
* Payload: 데이터
* Signature: 토큰 인코딩, 유효성 검증. 헤더와 페이로드를 해싱, 인코딩




