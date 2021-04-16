#!/bin/bash
# -----------------------------------------------------------------------------
# Script:   setup-base-image.sh
# Description:
#   Setup the base image for Docker.
#
# -----------------------------------------------------------------------------

arg_user=${1}
arg_group=${2}

line_pretty1='---------------------------------------'
line_pretty1="${line_pretty1}${line_pretty1}"

# Dump configuration.
# Args:
#   ${1} Work download directory.
#
function dump_config {
  local dir_config=${1}
  local item=''
  local key=''
  for item in $(ls ${dir_config}/*.conf); do
    if [ -f ${item} ]; then
      echo "config file  [${item##*/}]"
      echo '---'
      unset map_sx_config
      source ${item}
      for key in ${!map_sx_config[@]}; do
        printf "%-12s [%s]\n" ${key} ${map_sx_config[${key}]}
      done
      echo ''
   fi
  done
}

# Download software package and install.
# Args:
#   ${1} Work download directory.
#
function download_file {
  local dir_dl=${1}
  local dir_install=${map_sx_config['path_parent']}
  local dl_filename=${map_sx_config['file_name']}
  local flag_unpack=${map_sx_config['unpack']}
  local file_target=${map_sx_config['target']}
  local symlink_name=${map_sx_config['symlink']}
  local symlink_target=${map_sx_config['target']}
  local opt_chmod=${map_sx_config['chmod']}
  local dl_url=''
  local file_ext=''
  local dir_curr=$(pwd)

  dl_url=${map_sx_config['dl_protocol']}://${map_sx_config['dl_site']}
  dl_url=${dl_url}/${map_sx_config['dl_path']}
  file_ext=${dl_filename##*.}

  echo "Software package: [${map_sx_config['name']}]"
  echo '  Downloading...'
  echo "    URL [${dl_url}]"
  wget -q -O ${dir_dl}/${dl_filename} ${dl_url}

  echo '  Installing...'
  mkdir -p ${dir_install}
  cd ${dir_install}

  if [ "${flag_unpack}" != 'no' ]; then 
    if [ "${file_ext}" == 'zip' ]; then
      unzip -q ${dir_dl}/${dl_filename}
    else
      tar -xzf ${dir_dl}/${dl_filename}
    fi
  else
    cp ${dir_dl}/${dl_filename} ${file_target}

    if [ "${opt_chmod}" != '' ]; then
      chmod ${opt_chmod} ${file_target}
    fi
  fi

  if [ "${symlink_name}" != '' ]; then
    ln -sn ${symlink_target} ${symlink_name}
  fi

  cd ${dir_curr}
}


# Download software package and install
# Args:
#   ${1} Configuration directory.
#   ${2} Work download directory.
#
function sw_download {
  local dir_config=${1}
  local dir_dl=${2}
  local item=''
  local name_pkg=''

  for item in $(ls ${dir_config}/*.conf); do
    if [ -f ${item} ]; then
      unset map_sx_config
      source ${item}
      name_pkg=${map_sx_config['name']}
      download_file ${dir_dl}
      if [ "${name_pkg}" == 'vCloud vmTools' ]; then
        config_vcloud_tools
      fi
      echo ''
   fi
  done
}

# Configure environment for user.
# Args:
#   ${1} Configuration output file.
#
function config_environ {
  local path_config=${1}
  local dir_install=${map_sx_config['path_parent']}
  local dir_target=${map_sx_config['dir_target']}
  local flag_default=${map_sx_config['default']}
  local path_env=${dir_install}/${dir_target}
  local name_env=${map_sx_config['env_name']}

  if [ "${flag_default}" == 'yes' ]; then
    # Build environment
    echo "Configuring [${map_sx_config['name']}]"
    echo "if [ -d '${path_env}' ]; then" >> ${path_config}
    echo "  export ${name_env}=${path_env}" >> ${path_config}
    echo "fi" >> ${path_config}
    echo '' >> ${path_config}
    map_sx_config['fragment_path']="\${${name_env}}/bin"
  fi
}

# Configure environment for user.
# Args:
#   ${1} Configuration directory.
#   ${2} Configuration output file.
#
function build_environ {
  local dir_config=${1}
  local path_config=${2}
  local item=''
  local fragment_path=''
  local frag_path=''

  echo '# .bashrc' > ${path_config} 
  echo '' >> ${path_config}
  echo '# User specific aliases and functions' >> ${path_config}
  echo '' >> ${path_config}
  echo "alias rm='rm -i'" >> ${path_config}
  echo "alias cp='cp -i'" >> ${path_config}
  echo "alias mv='mv -i'" >> ${path_config}
  echo "alias ll='ls -l'" >> ${path_config}
  echo "alias la='ls -al'" >> ${path_config}
  echo '' >> ${path_config}

  echo '# Source global definitions' >> ${path_config}
  echo 'if [ -f /etc/bashrc ]; then' >> ${path_config}
  echo '  . /etc/bashrc' >> ${path_config}
  echo 'fi' >> ${path_config}
  echo '' >> ${path_config}
 
  echo '# Setup environment' >> ${path_config}
  for item in $(ls ${dir_config}/*.conf); do
    if [ -f ${item} ]; then
      unset map_sx_config
      source ${item}
      config_environ ${path_config}

      frag_path=${map_sx_config['fragment_path']}
      if [ "${frag_path}" != '' ]; then
        fragment_path="${fragment_path}:${frag_path}"
      fi
   fi
  done

  # Build PATH statement
  if [ "${fragment_path}" != '' ]; then
    echo "PATH=\${PATH}${fragment_path}" >> ${path_config}
  fi
  echo '' >> ${path_config}
}

# Create directories
mkdir -p /applications
mkdir -p /data

if [ "${arg_user}" != '' ] && [ "${arg_group}" != '' ]; then
  chown ${arg_user}:${arg_group} /applications /data
fi

# Setup file names and versions
dir_config='./conf'
dir_dl='/work/dl'
path_envfile='/work/bashrc'

# Create down directory
mkdir -p ${dir_dl}

# Perform installation steps
# dump_config ${dir_config}
sw_download ${dir_config} ${dir_dl}
build_environ ${dir_config} ${path_envfile}

# Perform some clean-up
rm -rf /work/dl
rm -rf /work/conf

echo ''
echo "Note: File '/work/bashrc' contains a sample that can be copied to your environment."
echo ''

exit 0

# -----------------------------------------------------------------------------
# End of Script.
# -----------------------------------------------------------------------------


