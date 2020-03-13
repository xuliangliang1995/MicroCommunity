FROM java8
MAINTAINER wuxw <928255095@qq.com>

ADD target/CodeService.jar /root/target/

ADD bin/start_code.sh /root/


RUN chmod u+x /root/start_code.sh

CMD ["/root/start_code.sh","dev"]