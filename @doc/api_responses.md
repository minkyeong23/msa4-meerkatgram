# API Response 명세서

본 문서는 `msa4-meerkatgram` 프로젝트 내 각 API의 Response 종류를 정리한 문서입니다.
모든 API 응답은 공통 응답 구조인 `GlobalRes<T>` 객체에 래핑되어 일관된 구조로 반환됩니다.

---

## 📌 [공통 Response 구조 (GlobalRes)]
```json
{
  "code": "응답 코드 (성공: '00', 실패: 'E01' ~ 'E99')",
  "message": "응답 메시지",
  "data": "응답 데이터 (성공 시 API별 반환 객체, 실패 시 에러 상세 메시지 혹은 상세 맵)"
}
```

---

## 🗂️ 1. Auth API (인증 및 인가 담당 API)

### 🔹 [API] POST `/api/login` (로그인 처리)
* **[HttpStatus] 200 OK**
  * **[에러/성공코드] `00` (로그인 완료)**
    * **Response Data (`AuthRes`)**:
      * `user` (`UserWithPostCountRes`):
        * `user` (`UserRes`): 유저 기본 정보 (id, email, nick, role, profile, createdAt)
        * `countPosts`: 유저가 작성한 총 게시글 수
      * `accessToken`: JWT 액세스 토큰
* **[HttpStatus] 400 Bad Request**
  * **[에러/성공코드] `E21` (요청 파라미터 이상)**
    * **발생 원인**: 이메일이나 비밀번호 형식 검증(`@Valid`) 실패 또는 타입 오류 (`MethodArgumentNotValidException`, `MethodArgumentTypeMismatchException`).
* **[HttpStatus] 401 Unauthorized**
  * **[에러/성공코드] `E01` (로그인 에러)**
    * **발생 원인**: 일치하는 이메일 회원 정보가 없거나 비밀번호가 다를 때 (`NotRegisteredException`).
* **[HttpStatus] 500 Internal Server Error**
  * **[에러/성공코드] `E80` (DB 에러)**
    * **발생 원인**: 로그인 데이터베이스 조회 중 SQL 에러 발생 (`SQLException`).
  * **[에러/성공코드] `E99` (시스템 에러)**
    * **발생 원인**: 기타 예측하지 못한 런타임 예외 발생 (`Exception`).

### 🔹 [API] POST `/api/reissue-token` (토큰 재발급)
* **[HttpStatus] 200 OK**
  * **[에러/성공코드] `00` (토큰 재발급 완료)**
    * **Response Data (`AuthRes`)**: 갱신된 Access Token 및 갱신된 Refresh Token Cookie.
* **[HttpStatus] 401 Unauthorized**
  * **[에러/성공코드] `E04` (토큰 이상)**
    * **발생 원인**: Refresh Token이 없거나, 유효하지 않은 사용자의 토큰이거나, DB에 저장된 토큰 정보와 일치하지 않는 경우 (`InvalidTokenException`).
  * **[에러/성공코드] `E01` (로그인 에러)**
    * **발생 원인**: 토큰의 사용자 식별 ID에 해당하는 회원이 존재하지 않는 경우 (`NotRegisteredException`).
* **[HttpStatus] 500 Internal Server Error**
  * **[에러/성공코드] `E80` / `E99` (DB 및 시스템 에러)**

### 🔹 [API] POST `/api/logout` (로그아웃 처리)
* **[HttpStatus] 200 OK**
  * **[에러/성공코드] `00` (로그아웃 완료)**
    * **Response Data**: `null` (성공 메시지만 전달하며 쿠키의 Refresh Token 만료 처리)
* **[HttpStatus] 401 Unauthorized**
  * **[에러/성공코드] `E02` (UNAUTHENTICATED_ERROR)**
    * **발생 원인**: 유효한 Access Token이 제공되지 않아 비로그인 상태로 판단될 때 (`AuthenticationException`).
  * **[에러/성공코드] `E04` (토큰 이상)**
    * **발생 원인**: 로그아웃 대상 회원의 토큰이 유효하지 않은 경우 (`InvalidTokenException`).
* **[HttpStatus] 403 Forbidden**
  * **[에러/성공코드] `E03` (UNAUTHORIZED_ERROR)**
    * **발생 원인**: 요청한 작업에 대해 회원의 권한이 부족할 때 (`AccessDeniedException`).
* **[HttpStatus] 500 Internal Server Error**
  * **[에러/성공코드] `E80` / `E99` (DB 및 시스템 에러)**

---

## 🗂️ 2. File API (파일 업로드 관련 API)

### 🔹 [API] POST `/api/files/profiles` (프로필 사진 파일 업로드)
* **[HttpStatus] 200 OK**
  * **[에러/성공코드] `00` (파일 저장 성공)**
    * **Response Data (`FileRes`)**:
      * `fileUri`: 저장된 프로필 이미지 파일의 서버 URL 경로
* **[HttpStatus] 400 Bad Request**
  * **[에러/성공코드] `E21` (요청 파라미터 이상)**
    * **발생 원인**: MultipartFile 객체 바인딩 문제 및 유효성 검증 오류.
* **[HttpStatus] 500 Internal Server Error**
  * **[에러/성공코드] `E40` (파일 업로드 실패)**
    * **발생 원인**: 파일 크기가 0이거나 파일이 비어있는 경우, 허용되지 않는 확장자이거나 디렉토리 생성 또는 파일 저장 IO 쓰기 단계 오류 (`FileManagedException`).
  * **[에러/성공코드] `E99` (시스템 에러)**

### 🔹 [API] POST `/api/files/posts` (피드 게시글 이미지 파일 업로드)
* **[HttpStatus] 200 OK**
  * **[에러/성공코드] `00` (파일 저장 성공)**
    * **Response Data (`FileRes`)**:
      * `fileUri`: 저장된 피드 이미지 파일의 서버 URL 경로
* **[HttpStatus] 400 Bad Request**
  * **[에러/성공코드] `E21` (요청 파라미터 이상)**
* **[HttpStatus] 500 Internal Server Error**
  * **[에러/성공코드] `E40` (파일 업로드 실패)**
    * **발생 원인**: 파일 쓰기 및 업로드 과정 처리 오류 (`FileManagedException`).
  * **[에러/성공코드] `E99` (시스템 에러)**

---

## 🗂️ 3. Post API (게시글 관련 API)

### 🔹 [API] GET `/api/posts` (게시글 목록 페이징 조회)
* **[HttpStatus] 200 OK**
  * **[에러/성공코드] `00` (정상처리)**
    * **Response Data (`PostIndexRes`)**:
      * `total`: 전체 게시글 수
      * `lastPage`: 마지막 페이지 여부
      * `posts` (`List<PostWithUserRes>`): 게시글 리스트
* **[HttpStatus] 400 Bad Request**
  * **[에러/성공코드] `E21` (요청 파라미터 이상)**
    * **발생 원인**: 페이징 인자(`page`, `limit`) 바인딩 오류 및 타입 미스매치 (`MethodArgumentTypeMismatchException` 등).

### 🔹 [API] GET `/api/posts/{id}` (게시글 상세 조회)
* **[HttpStatus] 200 OK**
  * **[에러/성공코드] `00` (게시글 상세 정상 처리)**
    * **Response Data (`PostWithUserRes`)**:
      * `id`: 게시글 식별 ID
      * `content`: 본문 텍스트
      * `image`: 업로드 이미지 URL
      * `createdAt` / `updatedAt` / `deletedAt`: 생성/수정/삭제 시각
      * `user` (`UserRes`): 작성자 정보
* **[HttpStatus] 400 Bad Request**
  * **[에러/성공코드] `E21` (요청 파라미터 이상)**
    * **발생 원인**: 패스 배리어블 `id`가 1 미만(예: `@Min(1)`)이거나 유효하지 않은 숫자 타입일 때.
* **[HttpStatus] 404 Not Found**
  * **[에러/성공코드] `E10` (DELETED_RECORD_ERROR)**
    * **발생 원인**: 요청한 ID에 해당하는 게시글 정보가 DB에 존재하지 않거나 이미 삭제 처리된 상태인 경우 (`DeletedRecordException`).
* **[HttpStatus] 500 Internal Server Error**
  * **[에러/성공코드] `E80` / `E99` (DB 및 시스템 에러)**

### 🔹 [API] POST `/api/posts` (게시글 작성)
* **[HttpStatus] 201 Created**
  * **[에러/성공코드] `00` (게시글 작성 성공)**
    * **Response Data (`PostCreateRes`)**:
      * `id`: 새로 등록된 게시글 ID
* **[HttpStatus] 400 Bad Request**
  * **[에러/성공코드] `E21` (요청 파라미터 이상)**
    * **발생 원인**: 본문 텍스트가 누락되었거나 비정상적인 값으로 전달되어 유효성 검사(`@Valid`)에 걸린 경우 (`MethodArgumentNotValidException`).
* **[HttpStatus] 401 Unauthorized**
  * **[에러/성공코드] `E04` (토큰 이상)**
    * **발생 원인**: 요청 헤더에 Access Token이 누락되었거나 유효하지 않은 경우 (`InvalidTokenException`).
  * **[에러/성공코드] `E02` (UNAUTHENTICATED_ERROR)**
    * **발생 원인**: 인증 정보 미보유 또는 비인증 상태.
* **[HttpStatus] 403 Forbidden**
  * **[에러/성공코드] `E03` (UNAUTHORIZED_ERROR)**
    * **발생 원인**: 회원의 작성 권한 부족 (`AccessDeniedException`).
* **[HttpStatus] 500 Internal Server Error**
  * **[에러/성공코드] `E80` / `E99` (DB 및 시스템 에러)**


### 프롬프트
현재 프로젝트에서 각 API 별로 Response 종류를 정리해줘.
- @doc/*: 파일 작성 위치
- 대분류: API, 중분류 HttpStatus, 소분류: 에러코드(예: E01, E02 등등)
- 코드를 분석해서 확인후에 정리 할 것