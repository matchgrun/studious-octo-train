#!/bin/bash
# -----------------------------------------------------------------------------
# Script:   setup-app-image.sh
# Description:
#   Setup the app image for Docker.
#
# -----------------------------------------------------------------------------

arg_user=${1}
arg_group=${2}
arg_app=${3}

# line_pretty1='---------------------------------------'
# line_pretty1="${line_pretty1}${line_pretty1}"

# Create directories
mkdir -p /applications/bin
mkdir -p /data/config
mkdir -p /data/logs

if [ "${arg_user}" != '' ] && [ "${arg_group}" != '' ]; then
  chown -R ${arg_user}:${arg_group} /applications /data
fi

# Perform installation steps
cp /work/${arg_app}.jar  /applications/bin/${arg_app}.jar
cp /work/start-app.sh /applications/bin/start-app.sh
cp /work/*.yml  /data/config
cp /work/*.xml  /data/config

echo ''
echo "Done."
echo ''

exit 0

# -----------------------------------------------------------------------------
# End of Script.
# -----------------------------------------------------------------------------


