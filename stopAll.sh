#!/usr/bin/env bash
docker-compose -f ./eureka/docker/docker-compose.yml       down;
docker-compose -f ./Api/docker/docker-compose.yml        down;
docker-compose -f ./OrderService/docker/docker-compose.yml      down;
docker-compose -f ./ShopService/docker/docker-compose.yml       down;
docker-compose -f ./StoreService/docker/docker-compose.yml       down;
docker-compose -f ./UserService/docker/docker-compose.yml       down;
docker-compose -f ./WebService/docker/docker-compose.yml       down;
docker-compose -f ./FeeService/docker/docker-compose.yml       down;
docker-compose -f ./CommunityService/docker/docker-compose.yml       down;
docker-compose -f ./CommonService/docker/docker-compose.yml       down;
docker-compose -f ./AppFrontService/docker/docker-compose.yml     down;
docker-compose -f ./MallService/docker/docker-compose.yml     down;