
#FROM은 Docker 이미지의 기반이 되는 베이스 이미지를 지정합니다.
#LABEL은 이미지에 메타데이터를 추가할 때 사용됩니다.

#FROM ubuntu:latest
#LABEL authors="대선"
#
#ENTRYPOINT ["top", "-b"]

# Gradle 8.6과 JDK 17을 포함한 경량 Alpine 베이스 이미지를 사용합니다. , 현재 해당 프로젝트에서 사용하고 있는 gradle이 8.6버전이므로 이렇게 설정
# 컨테이너 안에서 작업 디렉토리를 **/build**로 설정합니다.
FROM gradle:8.6-jdk17-alpine as builder
WORKDIR /build

# Gradle 캐시 활용을 위한 디렉토리 복사
COPY --chown=gradle:gradle .gradle /home/gradle/.gradle

# 그래들 파일이 변경되었을 때만 새롭게 의존패키지 다운로드 받게함.
COPY build.gradle settings.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

# 로컬 디렉토리의 모든 파일을 컨테이너 내의 /build 디렉토리로 복사하기
COPY . /build
# -x test : 테스트 코드 없이, parallel : gradle의 병렬 빌드기능을 사용하여 가능한 작업들을 동시에 처리하여 빌드 속도 증가
# 이 방법을 통해서 gradle을 build하는 명령어
RUN gradle build -x test --parallel

# openjdk:17.0-slim**은 JDK 17이 포함된 최소한의 리눅스 환경을 제공하는 경량 이미지이고 최종 실행 이미지를 지정하는 것
FROM openjdk:17.0-slim
# 컨테이너에서 작업 디렉토리를 /app으로 설정
WORKDIR /app

#최종 이미지의 루트 디렉토리에 app.jar라는 이름으로 JAR 파일을 복사
COPY --from=builder /build/build/libs/*-SNAPSHOT.jar ./app.jar
# N번 포트로 외부요청받기 설정
EXPOSE 8080

#컨테이너 내에서 nobody라는 사용자로 애플리케이션을 실행
USER nobody
#컨테이너가 실행될 때 실행할 기본 명령어를 설정하는 Dockerfile의 지시어
ENTRYPOINT [                                                \
    "java",                                                 \
    "-jar",                                                 \
    "-Djava.security.egd=file:/dev/./urandom",              \
    "-Dsun.net.inetaddr.ttl=0",                             \
    "app.jar"                                               \
]
#-Djava.security.egd=file:/dev/./urandom 옵션은 Java의 난수 생성기를 더 빠르게 동작시키기 위한 설정
#Dsun.net.inetaddr.ttl=0 옵션은 DNS 캐싱 시간을 0으로 설정하여, 각 DNS 조회가 매번 네트워크를 통해 새로운 결과를 얻도록 만듬