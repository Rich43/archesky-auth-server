#!/bin/bash
yum update -y
yum install -y which unzip zip
curl -s "https://get.sdkman.io" | bash
source "/root/.sdkman/bin/sdkman-init.sh"
sdk install gradle
gradle wrapper
