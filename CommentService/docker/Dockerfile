FROM java8
MAINTAINER wuxw <928255095@qq.com>

ADD target/CommentService.jar /root/target/

ADD bin/start_comment.sh /root/


RUN chmod u+x /root/start_comment.sh

CMD ["/root/start_comment.sh","dev"]