# Spring Boot API Documentation

## 📋 개요

이 문서는 Spring Boot 기반의 게시판 API에 대한 문서입니다.

## 🚀 실행 방법

```bash
# 애플리케이션 실행
./gradlew bootRun

# API 문서 생성 (REST Docs)
./gradlew clean test asciidoctor copyRestDocs

# JAR 빌드 (문서 포함)
./gradlew bootJar
```

## 📁 문서 구조

- `api-documentation.html` - HTML 형태의 API 문서
- `api-documentation.md` - Markdown 형태의 API 문서
- `README.md` - 이 파일

## 🌐 API 접근

- **Base URL**: `http://localhost:8080`
- **API 문서**: `http://localhost:8080/docs/api-documentation.html`

## 🔧 빌드 설정

이 프로젝트는 `build.gradle`에서 다음과 같이 설정되어 있습니다:

```gradle
// REST Docs 설정
ext {
    set('snippetsDir', file("build/generated-snippets"))
}

tasks.named('test') {
    outputs.dir snippetsDir
    useJUnitPlatform()
}

tasks.named('asciidoctor') {
    inputs.dir snippetsDir
    dependsOn test
}

// 문서 복사 작업
tasks.register('copyRestDocs', Copy) {
    dependsOn tasks.named('asciidoctor')
    from "${asciidoctor.outputDir}"
    into "src/main/resources/static/docs"
}

// JAR 빌드 시 문서 포함
tasks.named('bootJar') {
    dependsOn tasks.named('copyRestDocs')
}
```

## 📝 사용법

1. 애플리케이션을 실행합니다
2. 브라우저에서 `http://localhost:8080/docs/api-documentation.html`에 접속합니다
3. API 문서를 확인하고 테스트할 수 있습니다

## 🔄 문서 업데이트

API 변경사항이 있을 경우:

1. 테스트 코드를 수정합니다
2. `./gradlew clean test asciidoctor copyRestDocs`를 실행합니다
3. 문서가 자동으로 업데이트됩니다
