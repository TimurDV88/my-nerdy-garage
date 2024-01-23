FROM amazoncorretto:17

RUN yum -y install wget

RUN mkdir -p /root/.postgresql && \
    wget "https://storage.yandexcloud.net/cloud-certs/CA.pem" \
         --output-document /root/.postgresql/root.crt && \
    chmod 0600 /root/.postgresql/root.crt

COPY target/*.jar main-service.jar
ENTRYPOINT ["java","-jar","/main-service.jar"]