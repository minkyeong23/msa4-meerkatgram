# 코드 컨벤션 및 일관성 진단 가이드

이 문서는 Meerkatgram 프로젝트 개발 시 준수해야 하는 코드 스타일 컨벤션과, 현재 소스 코드 상에서 발견된 명명 규칙 불일치 지점(Refactoring 대상)을 정리하여 공유합니다.

---

## 1. 기본 기술 사양 및 규칙

* **언어 버전**: Java 17 (최소 JDK 17 준수)
* **프레임워크**: Spring Boot 3.5.x
* **빌드 도구**: Gradle (Groovy DSL)
* **코딩 표준**: 기본적인 Google Java Style Guide를 지향하며, 아래 프로젝트 전용 컨벤션을 추가 적용합니다.

---

## 2. 주요 코드 작성 규칙

### (1) 데이터 전달 객체 (DTO)는 `record`를 활용
데이터를 주고받는 용도의 DTO 객체는 Java 14 버전부터 정식 도입된 `record` 문법을 적극 사용합니다.
* **이유**: `record`는 정의 시점에 컴파일러가 모든 필드에 대해 `final`을 부여하여 데이터 불변성(Immutability)을 강제하고, Getter 및 `equals()`, `hashCode()`, `toString()`을 자동으로 생성해 주므로 보일러플레이트 코드가 최소화됩니다.

```java
// DTO 선언의 올바른 예 (record 사용)
public record PostIndexReq(
    @Min(value = 1, message = "페이지는 1 이상이어야 합니다.") int page,
    @Min(value = 1, message = "개수는 1 이상이어야 합니다.") int limit
) {}
```

### (2) 의존성 주입 (Dependency Injection) 컨벤션
* 필드 주입 (`@Autowired`) 대신, **생성자 주입**을 원칙으로 삼습니다.
* 생성자 보일러플레이트를 줄이기 위해 Lombok의 `@RequiredArgsConstructor`를 사용하며, 주입 대상 필드는 반드시 `private final` 키워드를 부여합니다.

```java
@RestController
@RequiredArgsConstructor // final이 붙은 필드를 아규먼트로 받는 생성자를 자동 생성
public class PostController {
    private final PostService postService; // private final 필수 적용
    private final JwtProvider jwtProvider;
}
```

### (3) 데이터 모델 (Entity) 클래스 규칙
* 데이터베이스 테이블과 매핑되는 JPA Entity 객체에는 `@Getter`는 적극 열어두되, 데이터의 변경 추적을 모호하게 만드는 `@Setter`는 꼭 필요한 경우를 제외하면 지양해야 합니다.
* 생성 및 수정 시간 추적을 위해 `@EntityListeners(AuditingEntityListener.class)`를 설정하고, `@CreatedDate`, `@LastModifiedDate` 어노테이션이 붙은 필드를 추가합니다.

---

## 3. 🚨 프로젝트 내 구조적 일관성 불일치 분석 및 제안

현재 소스 코드를 면밀히 분석한 결과, 패키지 명칭과 관련하여 **단수형(Singular)**과 **복수형(Plural)**의 혼용 사례가 발견되었습니다. 이는 협업 과정에서 개발자에게 혼란을 주거나 자동 생성 도구의 일관성을 해칠 수 있어, 다음과 같이 통일하는 리팩토링을 제안합니다.

### (1) 컨트롤러 패키지명 불일치
* **현황**:
  * `domain/auth/controller` (단수형 사용)
  * `domain/post/controllers` (복수형 사용)
  * `domain/user/controllers` (복수형 사용)
* **해결책**:
  * 단수형인 `controller` 또는 복수형인 `controllers` 중 하나로 일괄 통일해야 합니다. 업계 표준 및 기존 대형 프레임워크 관례에 따라 **`controllers` (복수형)**로 통일하는 것을 적극 권장합니다.

### (2) 요청/응답 DTO 패키지명 불일치
* **현황**:
  * `domain/auth/requests` / `responses` (복수형 사용)
  * `domain/user/responses` (복수형 사용)
  * `domain/post/requests` / `response` (단수/복수 혼용: requests는 복수형인데 response는 단수형임)
* **해결책**:
  * 모두 **`requests`** 및 **`responses` (복수형)**로 일괄 통일하여 명명 규칙의 일치감을 유지하는 것이 좋습니다.

---

## 4. 트랜잭션 관리 규칙

* 단순 데이터 조회 성능 최적화를 위해 비즈니스 서비스 레이어의 읽기 전용 메서드에는 `@Transactional(readOnly = true)`를 적용하는 것을 권장합니다.
* 데이터의 상태를 변경하는(C, U, D) 메서드는 쓰기 트랜잭션이 보장되도록 `@Transactional`을 붙이고, 예외 발생 시의 롤백 정책을 명확하게 수립해야 합니다.
  ```java
  @Transactional(rollbackFor = Exception.class)
  public void registration(RegistrationReq registrationReq) {
      // 회원가입 비즈니스 로직
  }
  ```
