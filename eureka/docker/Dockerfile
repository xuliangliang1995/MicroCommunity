FROM java:8
MAINTAINER wuxw <wuxw7@asiainfo.com>

ADD bin/start_eureka.sh /root/
ADD ../target/eureka.jar /root/target/eureka.jar

RUN chmod u+x /root/start_eureka.sh

CMD ["/root/start_eureka.sh"]