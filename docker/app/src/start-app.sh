#!/bin/sh
########################################################## {tag.top} ###
## IBM Confidential
## OCO Source Materials
## *** siam-msv-core ***
##
## (C) Copyright IBM Corporation 2021  
##
## The source code for this program is not published or otherwise
## divested of its trade secrets, irrespective of what has been
## deposited with the U.S. Copyright Office.
########################################################## {tag.end} ###

# ---------------------------------------------------------------------------
#
# Script:  start-app.sh
# Author:  Match Grun
# Purpose: Starter script for Spring Boot application.
# Arguments:
#   None.
#
# Returns:
#
# ---------------------------------------------------------------------------

dir_script="$( cd "$( dirname "$0" )" && pwd )"
cd ${dir_script}

app_name=siam-msv-core
app_user='appadm'
app_config='custom.yml'
jdk_version='11.0.10'

app_jar="${app_name}.jar"

dir_app=${dir_script}
dir_config=/data/config

# Load startup options
path_start_opts="./start-opts.sh"
if [ -f ${path_start_opts} ]; then
  source ${path_start_opts}
fi

if [ "${jdk_version}" == '' ]; then
  echo "ERROR: Java version not specified."
  exit 1
fi

JAVA_HOME=/usr/local/jdk/jdk-${jdk_version}
export PATH=${PATH}:${JAVA_HOME}/bin

deco_line1='-----------------------------------------'
deco_line1="${deco_line1}${deco_line1}"

echo "${deco_pretty1}"
echo "dir_script [${dir_script}]"
echo "dir_app    [${dir_app}]"
echo "dir_config [${dir_config}]"
echo "app_user   [${app_user}]"
echo "${deco_pretty1}"

# Setup java options
java_opts=''

# Setup command line
cmd_run="${JAVA_HOME}/bin/java ${java_opts} -jar ${dir_app}/${app_jar}"
cmd_run="${cmd_run} --spring.config.location=${dir_config}/${app_config}"
echo "cmd_run [${cmd_run}]"

${cmd_run}

# Run process in background
# su ${app_user} -c "nohup ${cmd_run} > /dev/null 2>&1 &"

# This is the PID of the process
# pgrep -f "java.*${app_name}"

# ----------------------------------------------------------------------------
# END OF SCRIPT.
# ----------------------------------------------------------------------------


