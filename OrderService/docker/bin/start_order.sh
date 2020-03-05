#!/bin/bash
#### debug model prod
#nohup java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar -Dspring.profiles.active=dev target/OrderService.jar $1 > order.log 2>&1 &

#### normal prod model
#nohup java -jar -Dspring.profiles.active=prod $1 target/OrderService.jar > order.log $1 2>&1 &

#### normal test model
#nohup java -jar -Dspring.profiles.active=test $1 target/OrderService.jar > order.log $1 2>&1 &

#### normal dev model
#nohup java -jar -Dspring.profiles.active=dev $1 target/OrderService.jar > order.log $1 2>&1 &
nohup java -jar target/OrderService.jar -Dspring.profiles.active=$1 $2 > order.log 2>&1 &

tail -100f order.log
