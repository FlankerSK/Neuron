FROM openjdk:17
VOLUME /tmp
ENV TZ=Asia/Beijing
ADD target/neuron-0.0.1-SNAPSHOT.jar /neuron.jar
ENTRYPOINT ["java","-jar","/neuron.jar"]