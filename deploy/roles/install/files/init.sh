#!/bin/bash
#
# Startup script for Spring Boot application
#
# chkconfig: - 96 16
# description: Spring Boot project

# Source function library.
[ -f "/etc/rc.d/init.d/functions" ] && . /etc/rc.d/init.d/functions

app_name=siam-msv-core
LOCK="/var/lock/subsys/${app_name}"

function get_app_pid() {
  pgrep -f "java.*${app_name}"
}

function app_start() {
  local app_start_script="/applications/bin/${app_name}/start-app.sh"
  local app_pid=$(get_app_pid)
  local retVal=1
  local counter=30

  echo $"Starting ${app_name}: "
  while [ ${counter} -gt 0 ] ; do
    app_pid=$(get_app_pid)
    if [ "${app_pid}" == '' ]; then
      # Start application
      exec ${app_start_script}
    fi

    if [ ${counter} == 0 ]; then
      echo $"ERROR: Unable to start application: [${app_name}]"
      exit 1
    fi

    echo $"...sleeping"
    sleep 1
    let counter-=1
  done

  app_pid=$(get_app_pid)
  if [ "${app_pid}" != '' ]; then
    touch "${LOCK}"
    retVal=0
  fi

  [ ${retVal} = 0 ] && echo $"Started" || echo $"Failed"

  return ${retVal}
}

function app_stop() {
  local app_pid=$(get_app_pid)
  local retVal=0
  local cnt

  echo $"Stopping ${app_name}: "

  # Kill application
  [ -n "${app_pid}" ] && kill ${app_pid}
  retVal=${?}

  # Test whether stopped
  cnt=10
  while [ ${retVal} = 0 -a ${cnt} -gt 0 ] &&
    { get_app_pid > /dev/null ; } ; do
    sleep 1
    ((cnt--))
  done

  [ ${retVal} = 0 ] && rm -f "${LOCK}"
  [ ${retVal} = 0 ] && echo $"Ok" || echo $"Failed"
  return ${retVal}
}

function app_status() {
  local app_pid=$(get_app_pid)

  if [ -n "${app_pid}" ]; then
    echo "${app_name} (pid ${app_pid}) is running..."
    return 0
  fi

  if [ -f "${LOCK}" ]; then
    echo "${app_name} dead but subsys locked"
    return 2
  fi
  echo "${app_name} is stopped"
  return 3
}

# See how we were called.
case "${1}" in
  'start')
    app_start
    retVal=${?}
    ;;
  'stop')
    app_stop
    retVal=${?}
    ;;
  'status')
    app_status
    retVal=${?}
    ;;
  'restart')
    app_stop
    app_start
    retVal=${?}
    ;;
  *)
    echo $"Usage: ${0} {start|stop|restart|status}"
    exit 1
esac

exit ${retVal}


