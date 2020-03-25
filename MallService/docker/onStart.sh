#!/bin/bash

cp -r ../bin .

cp  -r ../target .

docker build -t java110/mall .

docker run -ti --name mall_test -p8000:8000 -idt java110/mall:latest

docker logs -f mall_test