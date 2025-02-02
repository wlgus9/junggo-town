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

> Token
> > 클라이언트에서 인증 정보를 보관하는 방법

### `Bearer token`
Bearer = 소유자

OAuth 2.0 프레임워크에서 사용하는 토큰 인증 방식 (= OAuth 프레임워크에서 액세스 토큰으로 사용하는 토큰의 유형)

> OAuth 프레임워크
> > 제 3자의 클라이언트에게 보호된 리소스를 제한적으로 접근하게 해주는 프레임워크
> >
> > ex) 소셜 로그인 (구글, 카카오, 네이버, ...)

HTTP 인증 프로토콜 중에 하나로 클라이언트가 서버의 자원에 접근하고자 할 때 헤더에 Bearer token을 포함하고 요청하여 인증을 받는다.

일반적으로 OAuth나 JWT와 같은 프레임워크에서 사용된다.

### `JWT (Json Web Token)`
Json 포맷을 이용하여 사용자에 대한 속성을 저장하는 Web Token

Header, Payload, Signature로 구성되며 각 부분은 Base64로 인코딩되어 표현된다.

각 부분은 .을 구분자로 사용

* Header : token type, Signature 해싱 알고리즘
  * ex) 다음과 같은 알고리즘과 토큰 유형 정보가 담긴 JSON 객체를 `Base64Url`로 인코딩하고 JWT의 첫 번째 부분을 형성
```
{
  "alg": "HS256",
  "typ": "JWT"
}
```
* Payload: 데이터 (토큰에서 사용할 정보의 조각들인 클레임(Claim)이 담겨 있다.)
```
{
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true
}
```
* Signature: 토큰을 인코딩하거나 유효성 검증을 할 때 사용하는 고유한 암호화 코드
  * 생성 과정
      1. 위에서 만든 헤더(Header)와 페이로드(Payload)의 값을 각각 `Base64Url`로 인코딩
      2. 인코딩한 값을 비밀 키를 이용해 헤더(Header)에서 정의한 알고리즘으로 해싱
      3. 이 값을 다시 `Base64Url`로 인코딩하여 생성

### `JWT` 종류
* Access Token
  * 보호된 정보들에 접근할 수 있는 권한 부여에 사용
  * 보통 만료 기간을 짧게 주고 refresh token을 이용해 재사용한다. 
* refresh token 
  * Access Token refresh


---

## 스프링 시큐리티




