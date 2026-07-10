# Meerkatgram 프로젝트 분석 문서 (Index)

이 디렉토리는 **Meerkatgram** 백엔드 애플리케이션의 아키텍처, 코드 컨벤션, 레이어 구성 및 비즈니스 흐름을 분석한 공식 문서 보관소입니다. 비전공자부터 신입 개발자까지 프로젝트를 한눈에 파악할 수 있도록 각 주제별로 상세히 나누어 기록하였습니다.


프롬프트  
### > 현재 프로젝트를 아래 사항들을 고려해서 분석해줘. 
필요에 따라 파일을 분리해서 작성해줘 
- @doc/analysis 에 작성할 것
- 필요에 따라 파일을 분리해서 작성할 것
- 프로젝트 아키텍쳐 정리  관련
- 코드 컨벤션 정리 필요
- 레이어 구성 정리 필요 관련
- 비전공자가 알 수 있도록 전반적인 흐름과 관련된 내용
---

## 📂 문서 목차 안내

아래의 각 링크를 클릭하여 해당하는 분석 문서를 바로 확인할 수 있습니다.

1. **[시스템 아키텍처 및 레이어 구성 (architecture.md)](./architecture.md)**
   - 전체 시스템의 구조 및 패키지 레이아웃
   - Controller - Service - Repository 로 이어지는 3계층 아키텍처와 역할
   - JPA, QueryDSL, Soft Delete 등 핵심 데이터베이스 기술 분석

2. **[코드 컨벤션 가이드 (code_convention.md)](./code_convention.md)**
   - Java 17 및 Spring Boot 3 최신 문법(Record 등) 활용 컨벤션
   - 네이밍 규칙 및 Lombok 어노테이션 사용 패턴
   - **(중요)** 프로젝트 내부의 일관성 불일치 문제(네이밍 편차) 진단 및 개선 방향

3. **[비전공자를 위한 서비스 동작 흐름 (business_flow.md)](./business_flow.md)**
   - 웹(Frontend)과 서버(Backend)의 대화 방식(HTTP API)
   - 그림처럼 이해하는 로그인/인증 유지 흐름 (Access & Refresh Token)
   - 데이터베이스에서 안전하고 빠르게 글 목록을 꺼내오는 원리

---

## 🛠️ 핵심 분석 요약

* **기반 프레임워크**: Java 17 + Spring Boot 3.5.15-SNAPSHOT
* **구조적 특징**: 도메인 협력형 계층형 아키텍처 (Domain-Driven Layered Architecture)
* **보안 메커니즘**: Spring Security + JWT 기반 무상태(Stateless) 토큰 인증
* **데이터 관리**: JPA(기본 CRUD) + QueryDSL(복잡한 조인 및 동적 쿼리 성능 최적화) + Soft Delete(논리 삭제)
