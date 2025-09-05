# Spring Boot API Documentation

## 개요

이 문서는 Spring Boot 기반의 게시판 API에 대한 문서입니다.

## 🔧 빌드 및 실행

### 문서 생성

```bash
# REST Docs 문서 생성
./gradlew clean test asciidoctor copyRestDocs

# JAR 빌드 (문서 포함)
./gradlew bootJar
```

### 접근 방법

- **애플리케이션 실행**: `./gradlew bootRun`
- **API 문서**: `http://localhost:8080/docs/api-documentation.html`

## Base URL

```
http://localhost:8080
```

## 인증

JWT 토큰을 사용한 인증이 필요합니다.

- 로그인 후 받은 JWT 토큰을 `Authorization` 헤더에 포함해야 합니다.
- 형식: `Bearer {JWT_TOKEN}`

---

## 1. 인증 API

### 1.1 회원가입

**POST** `/join`

새로운 사용자를 등록합니다.

#### Request Body

```json
{
  "username": "testUser",
  "password": "1234",
  "email": "test@metacoding.com",
  "roles": "USER"
}
```

#### Response (200 OK)

```json
{
  "id": 3,
  "username": "testUser",
  "email": "test@metacoding.com",
  "roles": "USER"
}
```

#### Response (400 Bad Request)

```json
{
  "status": 400,
  "msg": "email:이메일 형식이 올바르지 않습니다",
  "body": null
}
```

### 1.2 로그인

**POST** `/login`

사용자 로그인을 수행합니다.

#### Request Body

```json
{
  "username": "ssar",
  "password": "1234"
}
```

#### Response (200 OK)

```
Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Response (401 Unauthorized)

```json
{
  "status": 401,
  "msg": "비밀번호가 일치하지 않습니다",
  "body": null
}
```

### 1.3 사용자명 중복 체크

**GET** `/check-username`

사용자명의 중복 여부를 확인합니다.

#### Query Parameters

- `username` (string): 확인할 사용자명

#### Response (200 OK)

```json
{
  "available": true
}
```

---

## 2. 사용자 API

### 2.1 회원 정보 수정

**PUT** `/api/users`

사용자 정보를 수정합니다.

#### Headers

- `Authorization`: Bearer {JWT_TOKEN}

#### Request Body

```json
{
  "email": "update@metacoding.com",
  "password": "12345"
}
```

#### Response (200 OK)

```json
{
  "id": 1,
  "username": "ssar",
  "email": "update@metacoding.com",
  "roles": "USER"
}
```

#### Response (401 Unauthorized)

```json
{
  "status": 401,
  "message": "로그인 후 이용해주세요"
}
```

### 2.2 회원 정보 조회

**GET** `/api/users/{userId}`

특정 사용자의 정보를 조회합니다.

#### Headers

- `Authorization`: Bearer {JWT_TOKEN}

#### Path Parameters

- `userId` (integer): 사용자 ID

#### Response (200 OK)

```json
{
  "id": 1,
  "username": "ssar",
  "email": "ssar@metacoding.com",
  "roles": "USER"
}
```

#### Response (403 Forbidden)

```json
{
  "status": 403,
  "msg": "접근할 수 없는 유저입니다"
}
```

---

## 3. 게시글 API

### 3.1 게시글 목록 조회

**GET** `/api/boards`

모든 게시글 목록을 조회합니다.

#### Headers

- `Authorization`: Bearer {JWT_TOKEN}

#### Response (200 OK)

```json
[
  {
    "id": 1,
    "title": "title 1",
    "content": "Spring Study 1"
  },
  {
    "id": 2,
    "title": "title 2",
    "content": "Spring Study 2"
  }
]
```

### 3.2 게시글 작성

**POST** `/api/boards`

새로운 게시글을 작성합니다.

#### Headers

- `Authorization`: Bearer {JWT_TOKEN}

#### Request Body

```json
{
  "title": "test title",
  "content": "test content"
}
```

#### Response (200 OK)

```json
{
  "id": 6,
  "title": "test title",
  "content": "test content"
}
```

### 3.3 게시글 상세 조회

**GET** `/api/boards/{boardId}`

특정 게시글의 상세 정보를 조회합니다.

#### Headers

- `Authorization`: Bearer {JWT_TOKEN}

#### Path Parameters

- `boardId` (integer): 게시글 ID

#### Response (200 OK)

```json
{
  "isBoardOwner": true,
  "boardId": 1,
  "title": "title 1",
  "content": "Spring Study 1",
  "userId": 1,
  "username": "ssar",
  "replies": []
}
```

### 3.4 게시글 수정

**PUT** `/api/boards/{boardId}`

게시글을 수정합니다.

#### Headers

- `Authorization`: Bearer {JWT_TOKEN}

#### Path Parameters

- `boardId` (integer): 게시글 ID

#### Request Body

```json
{
  "title": "update title",
  "content": "update content"
}
```

#### Response (200 OK)

```json
{
  "id": 1,
  "title": "update title",
  "content": "update content"
}
```

### 3.5 게시글 삭제

**DELETE** `/api/boards/{boardId}`

게시글을 삭제합니다.

#### Headers

- `Authorization`: Bearer {JWT_TOKEN}

#### Path Parameters

- `boardId` (integer): 게시글 ID

#### Response (200 OK)

```
게시글 삭제 완료
```

---

## 4. 댓글 API

### 4.1 댓글 작성

**POST** `/api/replies`

새로운 댓글을 작성합니다.

#### Headers

- `Authorization`: Bearer {JWT_TOKEN}

#### Request Body

```json
{
  "comment": "test comment",
  "boardId": 1
}
```

#### Response (200 OK)

```json
{
  "id": 1,
  "comment": "test comment",
  "userId": 1,
  "username": "ssar",
  "boardId": 1
}
```

### 4.2 댓글 삭제

**DELETE** `/api/replies/{replyId}`

댓글을 삭제합니다.

#### Headers

- `Authorization`: Bearer {JWT_TOKEN}

#### Path Parameters

- `replyId` (integer): 댓글 ID

#### Response (200 OK)

```
댓글 삭제 완료
```

---

## 5. 관리자 API

### 5.1 관리자 게시글 삭제

**DELETE** `/api/admin/boards/{boardId}`

관리자가 게시글을 삭제합니다.

#### Headers

- `Authorization`: Bearer {JWT_TOKEN} (ADMIN 권한 필요)

#### Path Parameters

- `boardId` (integer): 게시글 ID

#### Response (200 OK)

```
게시글 삭제 완료
```

#### Response (403 Forbidden)

```json
{
  "status": 403,
  "msg": "접근 권한이 없습니다"
}
```

---

## 에러 코드

| 코드 | 설명                              |
| ---- | --------------------------------- |
| 400  | Bad Request - 잘못된 요청         |
| 401  | Unauthorized - 인증 실패          |
| 403  | Forbidden - 권한 없음             |
| 404  | Not Found - 리소스 없음           |
| 500  | Internal Server Error - 서버 오류 |

---

## 사용 예시

### 1. 회원가입 후 로그인

```bash
# 1. 회원가입
curl -X POST http://localhost:8080/join \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testUser",
    "password": "1234",
    "email": "test@metacoding.com",
    "roles": "USER"
  }'

# 2. 로그인
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testUser",
    "password": "1234"
  }'
```

### 2. 게시글 작성

```bash
curl -X POST http://localhost:8080/api/boards \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "새 게시글",
    "content": "게시글 내용입니다."
  }'
```

---

_이 문서는 자동으로 생성되었습니다. API 변경사항이 있을 경우 문서도 함께 업데이트됩니다._
