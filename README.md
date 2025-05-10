# 🍀 hana_simple_msa
### 나만의 작은 MSA 서비스 만들어보기

<br/>
<br/>


# 🪛 개발 환경
* JAVA, KOTLIN
* SpringBoot3, SpringCloud, JPA, QueryDsl
* SpringSecurity, JWT, OAuth2, RestAPI, RestDocs,
* DB - MySql, PostgreSql, redis
* INFRA - dcoker
<br/>
<br/>


# ⭐ 개발기간
2024. 10. ~ 2024. 12.

<br/>
<br/>

# 📖 시스템 아키텍쳐
<img width="1025" alt="스크린샷 2025-05-11 01 02 11" src="https://github.com/user-attachments/assets/fa36bf3f-5fdd-471e-891d-4fc8a266a992" />

<br/>
<br/>


# 주요기능 및 특징
*  ### SpringCloud를 활용한 MicroServiceArchitecture 서비스 구현
*  ### 회원관리 API, 게시판 API 기능 구현
*  ### 분산 트랜잭션 제어 및 데이터 무결성 문제 해결 시도
    
  <br/>
  <br/>
  
# Trobule Shooting
### MicroService 환경에서의 트랜잭션 및 데이터 무결성 문제 해결 시도
<br/>
<img width="738" alt="스크린샷 2025-05-11 01 07 56" src="https://github.com/user-attachments/assets/11c8cfaa-ac0f-47bf-9aac-6f583120d734" />

MSA 환경에서 비동기 통신의 장점을 유지하면서 예외 처리와 데이터 무결성을 효과적으로 보장하는 방법에 대한 명확한 해답을 찾는 데 어려움을 겪었습니다.<br/>
특히 Saga 패턴 등을 이용해 트랜잭션을 롤백하더라도 이미 사용자에게 전달된 응답을 취소할 수 없다는 점에서 문제 해결의 실마리를 찾기 어려웠습니다.<br/>
고민해본 해결 방안은 아래와 같습니다.<br/>
#### 1. 데이터 조회나 예외 가능성이 높은 요청의 경우 ‘동기요청’으로 처리한다.
동기 통신 증가는 응답 속도 저하 및 API 통신 비용 증가로 이어질 수 있어, 가급적 Microservice 간 통신은 비동기 방식으로 계획했습니다.<br/>
하지만 구현 과정에서 비동기 통신 시 예외 처리에 어려움을 겪으며 비동기 방식의 한계를 인지하게 되었습니다.<br/>
따라서 사용자 응답에 필수적인 요청은 동기 방식으로 처리하고, 제약이 비교적 적은 데이터의 생성/수정(Insert/Update) 작업 등에 비동기 통신을 활용하는 방안을 고려하게 되었습니다.<br/>
<br/>
#### 2. 무결성이 중요한 데이터의 경우 Redis등으로 사전 기록하여 동시성을 제어한다.
쿠폰 발급과 같이 특정 시점에 요청이 집중될 경우, 정보 조회 시점과 갱신 시점 사이에 데이터 불일치가 발생할 수 있을 것으로 예상됩니다.<br/>
MSA 구조에서는 분산 환경으로 인해 DB 레벨의 Locking 적용이 어려울 수 있으므로, Redis와 같은 도구를 활용한 분산 락(Distributed Lock) 또는 유사 메커니즘을 통해 동시 접근을 제어하고, 이를 통해 동시성 문제로 인한 데이터 부정합을 예방하는 방안을 고려하게 되었습니다.<br/>
