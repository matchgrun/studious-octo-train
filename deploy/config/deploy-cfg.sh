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
# Script:   deploy-cfg.sh
# Author:   Match Grun
# Purpose:  Set's up global configuration variables for deployment in
#           Associative array MAP_VARS.
#
# Arguments:
#   None.
#
# Updates:
#
# Returns:
#   None.
#
# ------------------------------------------------------------------------------

# Global configuration
declare -A MAP_VARS

# Decorators
MAP_VARS['deco_line1']=${siam_deco_line1}
MAP_VARS['deco_line2']=${siam_deco_line2}
MAP_VARS['deco_line3']=${siam_deco_line3}

# Determine OS Type (Cygwin or Linux)
MAP_VARS['is_linux']='no'
MAP_VARS['is_cygwin']='no'
MAP_VARS['siam_svc_os_type']=''
os_name=$(uname)
if [ "${os_name^^}" == 'LINUX' ]; then
  MAP_VARS['is_linux']='yes'
  MAP_VARS['siam_svc_os_type']='rh7'
else
  os_name=${os_name%_*}
  if [ "${os_name^^}" == 'CYGWIN' ]; then
    MAP_VARS['is_cygwin']='yes'
  fi
fi
MAP_VARS['os_name']="${os_name}"

MAP_VARS['siam_app_name']='siam-msv-core'
MAP_VARS['siam_app_version']=${arg_app_version}
MAP_VARS['siam_app_art_path']='com/ibm/wh/siam'

# Setup enviroment.
MAP_VARS['deploy_environment']=${DEPLOY_ENVIRONMENT}
MAP_VARS['stack_color']=${arg_stack_color}
MAP_VARS['siam_mode_live_test']=${arg_mode_live}
MAP_VARS['siam_timestamp']=$(date +%Y%m%d_%H%M00)
MAP_VARS['ssh_identity_file']="${SSH_KEY}"

MAP_VARS['dir_name_curr']=${dir_script}
if [ "${MAP_VARS['is_linux']}" == 'yes' ]; then
  MAP_VARS['dir_config']='./config'
  MAP_VARS['dir_build']='./build'
  MAP_VARS['dir_tmp']='./tmp'
  MAP_VARS['dir_pipeline']='./pipeline'
else
  MAP_VARS['dir_config']='config'
  MAP_VARS['dir_build']="build"
  MAP_VARS['dir_tmp']='tmp'
  MAP_VARS['dir_pipeline']='pipeline'
fi

MAP_VARS['dir_common_apps']='/applications'
MAP_VARS['dir_common_data']='/data'
MAP_VARS['dir_common_shared']='/data/shared'
MAP_VARS['dir_siam_temp']='/applications/tmp'

MAP_VARS['file_inventory']='inventory.txt'

MAP_VARS['dir_keystore']='/data/shared/keystore'
MAP_VARS['dir_local_keystore']='/data/local/keystore'
MAP_VARS['common_download_dir']='/tmp'
MAP_VARS['url_artifactory']='http://artifactory.truven.com:8070'
MAP_VARS['art_repo_release']='libs-release-local'
MAP_VARS['art_repo_local_lic']='libs-ibm-lic-local'

MAP_VARS['jdk_vendor']='ibm'
MAP_VARS['jdk_artifact_path']='com/oracle/jdk'
MAP_VARS['jdk_major_version']='11'
MAP_VARS['jdk_minor_version']='0'
MAP_VARS['jdk_update_number']='10'
MAP_VARS['jdk_prefix']='jdk'
MAP_VARS['jdk_suffix']='-linux_x64.tar.gz'

# NFS Parameters
MAP_VARS['nfs_http_port']='8080'
MAP_VARS['nfs_http_protocol']='http'
MAP_VARS['nfs_dir_root']='files'
MAP_VARS['nfs_remote_root']='/data/remote-root'

MAP_VARS['siam_common_user']='appadm'
MAP_VARS['siam_common_group']='appops'
MAP_VARS['siam_become_user']='appadm'
MAP_VARS['siam_owner']='appadm:appops'
MAP_VARS['sudo_chain']='sudo -u appadm sudo -u root'

# JVM Parameters
#MAP_VARS['siam_jvm_ms']='2048m'
#MAP_VARS['siam_jvm_mx']='2048m'
#MAP_VARS['siam_jvm_gc_files']='10'
#MAP_VARS['siam_jvm_gc_size']='50M'

# Setup Dataprobe Solution Center step-up endpoint
MAP_VARS['siam_local_user']=$(whoami)
MAP_VARS['siam_local_group']=$(groups)

# Setup production-like servers
MAP_VARS['user_deploy_opts']='no'
MAP_VARS['user_deployer']='appadm'
MAP_VARS['user_connect']='confman'

# Check for legacy JDK numbering
str_ver=${MAP_VARS['jdk_major_version']}
jdk_ver=''
jdk_pre_sep=''
if [ ${str_ver} -lt 9 ]; then
  jdk_ver="1.${MAP_VARS['jdk_major_version']}"
  jdk_ver="${jdk_ver}.${MAP_VARS['jdk_minor_version']}"
  jdk_ver="${jdk_ver}_${MAP_VARS['jdk_update_number']}"
  MAP_VARS['jdk_suffix']='-linux-x64.tar.gz'
else
  jdk_pre_sep='-'
  jdk_ver="${MAP_VARS['jdk_major_version']}"
  jdk_ver="${jdk_ver}.${MAP_VARS['jdk_minor_version']}"
  jdk_ver="${jdk_ver}.${MAP_VARS['jdk_update_number']}"
fi
MAP_VARS['jdk_prefix_sep']=${jdk_pre_sep}
MAP_VARS['jdk_version_full']=${jdk_ver}

MAP_VARS['jdk_name_base']="${MAP_VARS['jdk_prefix']}${jdk_pre_sep}${jdk_ver}"

# App startup
MAP_VARS['app_config_file']='custom.yml'

# ------------------------------------------------------------------------------
# End of Configuration.
# ------------------------------------------------------------------------------
#


