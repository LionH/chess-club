#!/bin/bash

set -e -x

cd $(readlink -f $(dirname $0)/../../..)

mvn -Ppostgresql spring-boot:run
