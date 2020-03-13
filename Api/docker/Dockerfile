FROM java8
MAINTAINER wuxw <928255095@qq.com>

ADD bin/start_api.sh /root/


RUN chmod u+x /root/start_api.sh

CMD ["/root/start_api.sh","dev -Dcache"]