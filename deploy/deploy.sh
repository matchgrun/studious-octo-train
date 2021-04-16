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
# ------------------------------------------------------------------------------
#
# Script:   deploy.sh
# Author:   Match Grun
# Purpose:  SIAM deployment script.
#
# Arguments:
#   $1  Environment. Mandatory.
#         [ dev | int | qa | pre | at | prod ...]
#
#   $2  Stack color. Mandatory.
#         [ blue | green ].
#
#   $3  Live/Test indicator. Mandatory.`
#         [ live | test ].
#
#   $4  Version number. Mandatory.`
#         eg, 0.0.5
#
# Returns:
#   0   Success.
#   1   Failure.
#   9   Configuration error.
#
# ------------------------------------------------------------------------------

DRY_RUN='echo'
DRY_RUN=''

# use the 'deploy_environment' environment variable to know the environment
# if it is not set then use the first script argument
# if it is not set then default to the 'dev' environment

arg_env=${1}
arg_stack_color=${2}
arg_mode_live=${3}
arg_app_version=${4}
arg_5_url=${5}
arg_env=${arg_env,,}
arg_stack_color=${arg_stack_color,,}
arg_mode_live=${arg_mode_live,,}

if [ "${arg_env}" == '' ]; then
  echo "Environment not specified."
  echo "  EXITING NOW."
  exit 1
fi

if [ "${arg_stack_color}" == '' ]; then
  echo "Stack Color not specified."
  echo "  EXITING NOW."
  exit 1
fi

if [ "${arg_mode_live}" == '' ]; then
  echo "Mode (Live/Test) not specified."
  echo "  EXITING NOW."
  exit 1
fi

if [ "${arg_app_version}" == '' ]; then
  echo "Application version not specified."
  echo "  EXITING NOW."
  exit 1
fi

# Setup current directory
dir_script="$( cd "$( dirname "$0" )" && pwd )"
cd ${dir_script}

export DEPLOY_ENVIRONMENT=${arg_env}

# Setup default build opts
stage_jdk='no'
stage_install='no'

# Setup enviroment
source './config/deploy-cfg.sh'

# Include all functions
source './lib/deploy-funcs.sh'

# Setup directories
deploySetupDirs

# Handle overides
file_overrides="./config/overrides-${arg_env}.sh"
if [ -f ${file_overrides} ]; then
  source ${file_overrides}
fi

echo ""
echo "Configuration:"
echo "---------------------------------------------------------------------------------"
dumpMapVars
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo ""

# Setup build options
file_build_opts="./build-opts-${arg_env}.sh"
if [ -f ${file_build_opts} ]; then
  source "${file_build_opts}"
else
  file_build_opts="./build-opts.sh"
  if [ -f ${file_build_opts} ]; then
    source "${file_build_opts}"
  fi
fi

# Create config for ansible
createYmlFile  "${MAP_VARS['dir_build']}/siam-deploy-vars.yml"

# Display configuration
echo "Build Opts:"
echo "  stage_jdk         [${stage_jdk}]"
echo "  stage_install     [${stage_install}]"
echo "  arg_5_url         [${arg_5_url}]" 
echo "DEPLOY VARS         [${MAP_VARS['dir_build']}/siam-deploy-vars.yml]"
echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
echo ""

retVal=0

# Avoid Ansible/SSH prompt for host key verification 
export ANSIBLE_HOST_KEY_CHECKING=false

if [ "${stage_jdk}" == 'yes' ]; then
  # Run Ansible playbook with specified environment to setup base install
  echo ""
  echo "Installing..."
  deployAnsibleSite 'site-jdk.yml' '' 'Install JDK' ''
  retVal=${?}
  if [ "${retVal}" != '0' ]; then
    exit ${retVal}
  fi
fi

if [ "${stage_install}" == 'yes' ]; then
  # Run Ansible playbook with specified environment to setup base install
  echo ""
  echo "Installing..."
  deployAnsibleSite 'site-base.yml' '' 'Install Application' ''
  retVal=${?}
  if [ "${retVal}" != '0' ]; then
    exit ${retVal}
  fi
fi

exit ${retVal}

# ------------------------------------------------------------------------------
# END OF SCRIPT.
# ------------------------------------------------------------------------------
#


