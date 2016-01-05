#!/bin/bash

set -e -x

cd $(readlink -f $(dirname $0)/../../..)

export PATH=$PATH:/usr/games

mvn -Ppostgresql spring-boot:run
