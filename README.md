# Copang Backend 프로젝트

### Dev Server
https://alconn.co

### Api Document

https://alconn.co/docs

## Project Structure

Tech Stacks

- Spring boot
- Spring Security
- Jpa & QueryDsl
- Jwt
- Junit & MockMvc & Restdocs (TEST)
- AWS EC2, S3
- Jenkins (CI/CD)
- Docker
- Nginx (ReverseProxy)
- Mariadb 
- React
- Redux
- Redux-Thunk

## API server

Spring boot 로 구성된 서버


## 인증과 인가 구조

Spring Security + JWT 를 사용하여 Statless 한 인증방식을 구성했습니다.

Logout 기능은 in memory 토큰 블랙리스트를 만들어 접근을 차단했습니다.

인증이 필요한 접근은  @Secured annotation 통해 제한 하였습니다.

인증 과정을 최대한 비즈니스 로직들과 분리하기 위해서 

유저의 식별자 로써 인가를 진행해야하는 서비스들은 

Custom annotaion 을 통해 직접 토큰을 파싱하는 구조가 아닌 필요한 인자를 주입하는 방식으로 설계했습니다

## JPA & QueryDsl

비즈니스 로직에 적합한 도메인간 연관관계 설정으로 

꼭 필요한 Repository 만 생성했습니다.

많은 조인문이 반복되거나 조건이 걸린 조회기능은 QueryDsl을 통해 구현하였습니다.

Domain entity 와 DTO 를 분리하여 외부로 공개할 정보와 내부에서 관리할 정보들에 대해 차이를 두었고

순환 참조문제를 원천적으로 만들지 않을수 있도록 했습니다.

## TEST

Junit 5 를 통한 시나리오들에 대한 테스트들을 진행하였습니다

Controller테스트는 Mockbean 을 활용해 슬라이스 테스트를 진행하였고

Service 테스트는 Integration 로써 데이터와 비즈니스 로직을 함께 테스트하였습니다.

또한 MockMvc와 Restdocs 를 활용해 문서 자동화를 구축해 테스트 케이스들의 변동사항을 실시간으로 동기화 하였습니다.

결과적으로 100개 이상의 test case들을 작성했습니다.

## CI/CD

백엔드와 프론트를 나누어 작업하는 과정에서 실시간으로 동기화 되는 개발 서버가 필요했습니다.

하지만 로컬 에서 진행하는 잦은 배포는 동기화도 힘들고 무엇보다 반복적인 시간소모가 많았기때문에

Jenkins pipeline 을 통한 지속적인 통합과 배포를 실천하였습니다

별도로 AWS EC2 인스턴스를 CI 서버로써 운영하였고 

Github 액션을 활용해 master에 merge하기전에 테스트를 진행하였습니다.

결과적으로 100회이상의 자동 테스트와 배포를 실천할수 있었습니다.

## Infrastructure

프로젝트 infrastructure 구조는 다음과 같습니다

- API 서버
- Static upload 서버
- FrontEnd 서버
- 판매자용 Front end server
- Image 용 S3
- Ci 서버

프로젝트의 여건상 CI서버만 분리 할수 있었고

다른 서버들은 Docker 콘테이너 위에서 실행환경을 구축했습니다.

Docker 컨테이너 들은 직접 이미지를 빌드해서 Docker-compose 를 통해 관리하였고

Frontend 서버는 nginx를 통해 static 리소스들을 serve 하였습니다.

API 서버와 static 서버는 spring boot 를 통해 구성하고 내장 Tomcat과 undertow 를 사용하였습니다.

서버들이 실행되고 있는 EC2에서 Nginx 를 통해 리버스 프록시를 구축하였고 

서버들을 Docker network 에 연결하는 방식으로 nginx 와 연결했습니다.


## Logging

많은 테스트들을 진행하고 테스트 서버에 코드를 동기화 시켰지만 그래도 처리되지 않은 Exception 들이나 예상하지 못한 

시나리오에 대한 디버깅을 실행해야 했습니다.

그렇기 때문에 AOP를 이용한 Exception 핸들러로써 미리 정의한 예외 상황들에 처리를 logging하고 에러 메세지를 리턴하였고

그리고 Controller 파라메터들에 대한 로그를 남기기 위해 로그 유닛을 만들어 사용했습니다.

## Name Server & Https 인증

Cloudflare 네임서버를 이용해 도메인을 운영하였으며

Cloudflare 인증서를 이용하여 Https를 구축했습니다.