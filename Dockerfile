# baseimage -> 컨테이너의 내 애플리케이션이 돌아갈 수 있는 환경을 제공해주는 이미지
FROM eclipse-temurin:17-jdk

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

ENV JAVA_OPTS="-Xms256m -Xmx512m"

#COPY {Dockerfile을 기준으로 container에 넣고자 하는 내용의 경로} {container 내에 복사할 경로}
COPY build/libs/cerdeuk-server-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]
