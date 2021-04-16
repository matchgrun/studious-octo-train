#!/bin/sh
########################################################## {tag.top} ###
## IBM Confidential
## OCO Source Materials
## *** siam-deploy ***
##
## (C) Copyright IBM Corporation 2021
##
## The source code for this program is not published or otherwise
## divested of its trade secrets, irrespective of what has been
## deposited with the U.S. Copyright Office.
########################################################## {tag.end} ###
# ------------------------------------------------------------------------------
#
# Script:   deploy-funcs.sh
# Author:   Match Grun
# Purpose:  Library functions.
#
# ------------------------------------------------------------------------------

# Create YML file from associative array
function createYmlFile() {
  local file_name=${1}
  local key=''
  local value=''
  local list_keys
  local list_sorted

  # Build list of keys
  for key in ${!MAP_VARS[@]}; do
    list_keys+=(${key})
  done

  # Order keys
  list_sorted=( $(for key in ${list_keys[@]}; do echo ${key}; done | sort ))

  printf "" > ${file_name}
  for key in ${list_sorted[@]}; do
    value="${MAP_VARS[${key}]}"
    printf "${key}: \"${value}\"\n"  >> ${file_name}
  done
  printf "\n" >> ${file_name}
}

#
# Dump map variables
#
function dumpMapVars() {
  local key
  local keylc
  local value
  local list_keys=()
  local list_sorted

  # Build list of keys
  for key in ${!MAP_VARS[@]}; do
    if [[ ${key} =~ .*password.* ]]; then
      continue
    fi
    if [[ ${key} =~ .*passwd.* ]]; then
      continue
    fi
    if [[ ${key} =~ .*pwd.* ]]; then
      continue
    fi
    if [[ ${key} =~ deco_line* ]]; then
      continue
    fi
   list_keys+=(${key})
  done

  # Order keys
  list_sorted=( $(for key in ${list_keys[@]}; do echo ${key}; done | sort ))

  # Dump
  for key in ${list_sorted[@]}; do
    value="${MAP_VARS[${key}]}"

    # Suppress password display
    keylc=${key,,}
    if [[ ${keylc} =~ .*pwd.* ]]; then
      value='********'
    fi
    if [[ ${keylc} =~ .*passwd.* ]]; then
      value='********'
    fi
    if [[ ${keylc} =~ .*password.* ]]; then
      value='********'
    fi
    printf "%-27s [%s]\n" ${key} "${value}"
  done
  return 0
}

#
# Setup directories.
#
# Arguments:
#  None.
#
function deploySetupDirs() {
  local dir_build=${MAP_VARS['dir_build']}
  local dir_tmp=${MAP_VARS['dir_tmp']}

  if [ ! -d ${dir_build} ]; then
    mkdir -p  ${dir_build}
  fi

  if [ -d ${dir_tmp} ]; then
    rm -f ${dir_tmp}/*
  else
    mkdir -p  ${dir_tmp}
  fi
  return 0
}

#
# Deploy Ansible site.
#
# Arguments:
#   $1  Site YML file.
#   $2  Stage name.
#   $3  Message.
#   $4  Additional arguments (TBD).
#
# Returns:
#   0         Success.
#   non-Zero  Failure.
#
function deployAnsibleSite() {
  local siam_site_yml=${1}
  local siam_stage=${2}
  local msg_stage=${3}
  local arg_extras=${4}
  local arg_stage=""
  local retVal=0
  local target_env=${MAP_VARS['deploy_environment']}
  local dir_build=${MAP_VARS['dir_build']}
  local file_yml="@${dir_build}/siam-deploy-vars.yml"
  local dir_pipeline=${MAP_VARS['dir_pipeline']}
  local filename_inv=${MAP_VARS['file_inventory']}
  local path_inventory="${dir_pipeline}/${target_env}/${filename_inv}"
  local user_connect=${MAP_VARS['user_connect']}
  local identity_file=${MAP_VARS['ssh_identity_file']}
  local dry_run=''

  if [ "${siam_stage}" != "" ]; then
    arg_stage="-e siam_openam_stage='${siam_stage}'"
  fi

  echo ""
  echo "${msg_stage}"
  echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
  echo ""
  ${dry_run} ansible-playbook -i ${path_inventory} -u ${user_connect} \
    ${SIAM_ANSIBLE_OPTS} \
    --private-key ${identity_file} \
    -e ${file_yml} \
    ${arg_stage} \
    ${siam_site_yml}

  retVal=${?}

  # Display status
  echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
  echo "${msg_stage} return value [${retVal}]"
  echo "================================================================================="
  if [ "${retVal}" != "0" ]; then
    echo "FAILED to install ${msg_stage} - Exiting."
  fi
  return ${retVal}
}


# ------------------------------------------------------------------------------
# END OF FUNCTIONS.
# ------------------------------------------------------------------------------
#


