#!/bin/bash
# -----------------------------------------------------------------------------
# Script:   docker-build.sh
# Description:
#   Build artifacts for docker.
#
# -----------------------------------------------------------------------------

# Configuration
declare -A map_build_cfg

# -----------------------------------------------------------------------------
# Functions.
# -----------------------------------------------------------------------------

# Dump configuration.
# Args:
#   ${1} Work download directory.
#
function dump_config {
  local dir_config=${1}
  local item=''
  local key=''
  local list_keys=()
  local list_sorted=()

  # Build list of keys
  for key in ${!map_build_cfg[@]}; do
    if [[ ${key} =~ deco_line* ]]; then
      continue
    fi
    list_keys+=(${key})
  done

  # Order keys
  list_sorted=( $(for key in ${list_keys[@]}; do echo ${key}; done | sort ))

  # Dump
  for key in ${list_sorted[@]}; do
    printf "%-20s [%s]\n" ${key} ${map_build_cfg[${key}]}
  done
  return 0 
}

function setup_config {
  local dir_app_base=${1}
  local deco_line1='-------------------------------------------'
  local deco_line2='==========================================='
  local deco_line3='~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~'

  map_build_cfg['deco_line1']=${deco_line1}${deco_line1}
  map_build_cfg['deco_line2']=${deco_line2}${deco_line2}
  map_build_cfg['deco_line3']=${deco_line3}${deco_line3}

  map_build_cfg['dir_app_base']=${dir_app_base}
  map_build_cfg['dir_app_home']=${dir_app_base}/app
  map_build_cfg['dir_app_deploy']=${dir_app_base}/deploy
  map_build_cfg['dir_app_docker']=${dir_app_base}/docker
  map_build_cfg['dir_app_build']=${dir_app_base}/docker/app/build

  map_build_cfg['app_name']='siam-msv-core'
 
  map_build_cfg['version_app']='1.0.0'
 
  # Empty build directory
  rm -rf ${map_build_cfg['dir_app_build']}

  # Create build directory
  mkdir -p ${map_build_cfg['dir_app_build']}
}

function build_app {
  local dir_curr=$(pwd)
  local retVal=0

  echo "Building application..."
  cd ${map_build_cfg['dir_app_home']}

  mvn -N io.takari:maven:wrapper
  retVal=${?}

  ./mvnw package -D skipTests  -Dbuild.version=${map_build_cfg['version_app']}
  retVal=${?}
  echo "${map_build_cfg['deco_line2']}"
  echo "retVal  [${retVal}]"

  cd ${dir_curr}
  return ${retVal}
}

function setup_docker_artifacts {
  local name_art=${map_build_cfg['app_name']}-${map_build_cfg['version_app']}.jar
  local name_target=${map_build_cfg['app_name']}.jar

  cp ${map_build_cfg['dir_app_home']}/target/${name_art} \
    ${map_build_cfg['dir_app_build']}/${name_target}

  cp ${map_build_cfg['dir_app_docker']}/app/src/*.sh \
    ${map_build_cfg['dir_app_build']}

  cp ${map_build_cfg['dir_app_docker']}/app/src/*.yml \
    ${map_build_cfg['dir_app_build']}

  cp ${map_build_cfg['dir_app_docker']}/app/src/*.xml \
    ${map_build_cfg['dir_app_build']}

}

function build_docker_image {
  local dir_curr=$(pwd)
  local name_image=${map_build_cfg['app_name']}

  cd ${map_build_cfg['dir_app_docker']}/app
  pwd
  docker build --tag=${name_image} .

  retVal=${?}
  echo "${map_build_cfg['deco_line2']}"
  echo "retVal  [${retVal}]"

  cd ${dir_curr}
}

# -----------------------------------------------------------------------------
# End of Functions.
# -----------------------------------------------------------------------------

# Setup current directory
dir_script="$( cd "$( dirname "$0" )" && pwd )"
cd ${dir_script}
cd ..
dir_home=$(pwd)

setup_config ${dir_home}

echo "${map_build_cfg['deco_line1']}"
echo "script_name          [${0##*/}]"
echo "dir_script           [${dir_script}]"
echo "dir_home             [${dir_home}]"
echo "${map_build_cfg['deco_line1']}"
dump_config
echo "${map_build_cfg['deco_line1']}"
echo ""

# Build application
# build_app

# Install into build directory for Docker
setup_docker_artifacts

# Build Docker image
build_docker_image

# -----------------------------------------------------------------------------
# End of Script.
# -----------------------------------------------------------------------------


