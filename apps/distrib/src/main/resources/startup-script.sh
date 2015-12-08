#!/bin/bash

script=$(readlink -f "$0")
script_path=$(dirname "$script")
app_path=$(dirname ${script_path})

APP="${app_path}/lib/@fileName@"
PIDFile="${app_path}/run/application.pid"

check_if_pid_file_exists(){
    if [ ! -f $PIDFile ];  then
        echo "PID file not found: $PIDFile"
        exit 1
    fi
}

check_if_process_is_running() {
 if ps -p $(print_process) > /dev/null
 then
     return 0
 else
     return 1
 fi
}

print_process() {
    echo $(<"$PIDFile")
}


#Add needed jvm options
JAVA_OPTS="${JAVA_OPTS} "

#Spring configuration files without extentions
CONFIG_NAMES="application,server"

SPRING_OPTS="--spring.config.location="${app_path}"/conf/ --logging.config="${app_path}"/conf/logback.xml --spring.config.name="${CONFIG_NAMES}""

#Get the shutdownflag file from yaml configuration file
#todo remove this in the future.
SHUT_DOWN_FILE=$(cat ${app_path}/conf/* 2>/dev/null | grep -A1 haltFlag | grep fileName | awk '{print $2}')

NOW=$(date +"%d-%m-%Y")
if [ -z "${JAVA_HOME}" ]; then
  JAVA_CMD="java"
else
  JAVA_CMD="${JAVA_HOME}/bin/java"
fi

# test if java command exists
if [ ! `which ${JAVA_CMD}` ];then
  echo "Can't find java command. Please set correctly your $JAVA_HOME or add java path to you $PATH variable"
  exit 1;
fi

#todo test the supported jvm vendor sun, ibm, openjdk !!

#todo test the correct version of jvm


# create run directory
if [ ! -d "${app_path}/run/" ]; then
  mkdir "${app_path}/run/"
fi

# create temp directory
if [ ! -d "${app_path}/temp/" ]; then
  mkdir "${app_path}/temp/"
fi

# create temp directory
if [ ! -d "${app_path}/logs/" ]; then
  mkdir "${app_path}/logs/"
fi

case "$1" in
  status)
    check_if_pid_file_exists
    if check_if_process_is_running
    then
      echo "@appName@ [" $(print_process) " ] is running"
    else
      echo "@appName@ is not running: $(print_process)"
    fi
    ;;
  stop)
    check_if_pid_file_exists
    if ! check_if_process_is_running
    then
      echo "@appName@ $(print_process) already stopped"
      exit 0
    fi
    kill -TERM $(print_process)
    echo -ne "Waiting for @appName@ to stop"
    NOT_KILLED=1
    for i in {1..20}; do
      if check_if_process_is_running
      then
        echo -ne "."
        sleep 1
      else
        NOT_KILLED=0
      fi
    done
    echo
    if [ $NOT_KILLED = 1 ]
    then
      echo "Cannot kill @appName@ $(print_process)"
      exit 1
    fi
    echo "@appName@ stopped"
    ;;
  start)
    if [ -f $PIDFile ] && check_if_process_is_running
    then
      echo "Process $(print_process) already running"
      exit 1
    fi
    nohup ${JAVA_CMD} \
          ${JPDA_OPTS} \
          ${JAVA_OPTS} \
          -DAPP_PATH=${app_path} \
          -jar $APP \
          ${SPRING_OPTS} &>> ${app_path}/logs/console-${NOW}.log &
    echo "$!" > ${PIDFile}
    echo "@appName@ started"
    ;;
  restart)
    $0 stop
    if [ $? = 1 ]
    then
      exit 1
    fi
    $0 start
    ;;
  debug)
    JPDA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,suspend=n,server=y"
    $0 start
    ;;
  *)
    echo "Usage: $0 {start|stop|restart|status|debug}"
    exit 1
esac

exit 0