#!/bin/bash

script=$(readlink -f "$0")
script_path=$(dirname "$script")
artifacts_repository="10.199.54.45"
all_artefacts="backoffice"
artifacts_server="10.199.54.45"
base_artifacts_path="starter-artifacts"
target_dir="installs"
lastest_link="latest"
base_target_path=${HOME}

if [ -t 0 ]; then
  NORM=`tput sgr0`
  BOLD=`tput bold`
  REV=`tput smso`
fi

doHelp(){
  echo -e \\n"Help documentation for starter artifact fetch script"\\n
  echo -e "${BOLD}Basic usage:${script} [options]"\\n
  echo "The following options are recognized."
  echo "${BOLD}-a${NORM}  --Sets the artifact name to be fetched. If this option is not given we try to find"
  echo "    artifact name by using env var $ARTIFACT"
  echo "${BOLD}-A${NORM}  --List available artifacts"
  echo "${BOLD}-d${NORM}  --Set destination directory. Default value is $HOME of the current user"
  echo "${BOLD}-v${NORM}  --Sets the artifact version to be fetched"
  echo "${BOLD}-l${NORM}  --List available artifacts versions in the remote repository. This option acceptes 2 values"
  echo "    ${BOLD}releases${NORM} and ${BOLD}snapshots${NORM}"
  echo "${BOLD}-p${NORM}  --Print actions only. This is dry run mode"
  echo "${BOLD}-F${NORM}  --Force artifact deploy. This will remove the old artifact destination folder."
  echo -e "${BOLD}-h${NORM}  --Displays this help message. No further functions are performed."\\n
  exit 1
}

getArtifactExtention(){
    echo ".zip"
}

checkArtifactName(){
 if  echo $all_artefacts |  grep -F -q -w "$1" ; then
        artifact_to_fetch="$1"
    else
        echo "Artifact not known [ $1 ]"
        echo "${BOLD}Available artifcats${NORM}: ${all_artefacts}"
        exit 1
    fi
}

getPublishedSnaphotVersions(){
 curl -s http://10.199.54.45/starter-artifacts/$1/ \
    | xml2 2>/dev/null \
    | grep '/html/body/hr/pre/a=0' \
    | cut -d '=' -f2 \
    | sed -e's/\///g' \
    | sort -t'.' -k1,1n -k2,2n -k3,3n
}

while getopts :a:v:d:l:FpAh option; do
case $option in
    a)
      artifact_to_fetch=$OPTARG
      ;;
    A)
      echo "${all_artefacts}"
      exit 0
      ;;
    d)
      destination_directory=$OPTARG
      ;;
    v)
      artifact_version=$OPTARG
      ;;
    F)
      do_force=true
      ;;
    p)
      do_dryRun=true
      ;;
    l)
      if  echo "releases snapshots" |  grep -F -q -w "$OPTARG" ; then
            getPublishedSnaphotVersions $OPTARG
            exit 0
      else
            echo -e \\n"Option ${BOLD}-l${NORM}  does not accept not ${BOLD}$OPTARG${NORM} value. Accepted values are ${BOLD}releases snapshots${NORM} "
            exit -1
      fi
      ;;
    h)
        doHelp
      ;;
    :)  echo "Argument required for ${BOLD}-$OPTARG${NORM} option"
        exit 1
      ;;
    \?)
      echo -e \\n"Option -${BOLD}$OPTARG${NORM} not allowed."
      doHelp
      exit 2
      ;;
  esac
done

#check arguments
if [ -z "${artifact_to_fetch}" ];then
    if [ -z "${ARTIFACT}" ]; then
        echo "${BOLD}ARTIFACT${NORM}  variable is missing. Please  set variable ARTIFACT or use the command line option ${BOLD}-a${NORM}"
        exit 1
    else
        checkArtifactName ${ARTIFACT}
    fi
else
  checkArtifactName ${artifact_to_fetch}
fi

#check the destination directory option
if [ ! -z "${destination_directory}" ]; then
    target_path=${base_target_path}/${destination_directory}
else
    target_path=${HOME}
fi

if [ -z "${do_dryRun}" ]; then
    do_dryRun= false
fi

echo "Dry run mode: "${do_dryRun}

# common config
common_config_dir=${target_path}/conf

if [ ! -d "${target_path}/${target_dir}" ]; then
    mkdir -p "${target_path}/${target_dir}"
fi

#Calculate the remote dir
if [ ! -z `expr "${artifact_version}" : '\(.*SNAPSHOT$\)'`  ]; then
    remote_dir="snapshots"
else
    remote_dir="releases"
fi

#Calculate artifact name
artifact_name=${artifact_to_fetch}-${artifact_version}$(getArtifactExtention)

#Calculate artifact fetch url
base_url="http://"${artifacts_server}"/"${base_artifacts_path}"/"${remote_dir}"/"${artifact_version}
#calculate fetch url
fetch_url=${base_url}"/"${artifact_name}
#calculate checksum_fetch url
checksum_fetch_url=${fetch_url}".sha1"

# Check if the artifact exists in local file system
if [ -d "${target_path}/${target_dir}/${artifact_version}" ]; then
    if [ ${do_force} ]; then
        echo "Forcing artifact deploy .."
        if [ ! ${do_dryRun} ]; then
            rm -rf "${target_path}/${target_dir}/${artifact_version}"
        fi
    else
        echo "Artifact already deployed. Please check  artifact version or remove already present artifact"
        exit 1
    fi
fi

echo "Fetching artifact checksum  file [ ${artifact_name} ]"
echo "checksum_url: ${checksum_fetch_url}"

if [ ! ${do_dryRun} ]; then
    wget -q ${checksum_fetch_url} -O /tmp/${artifact_name}".sha1"
    exitCode=$?
    [ ${exitCode} -ne 0  ] && echo "Fetching artifact checksum file error." && exit ${exitCode}
fi


echo "Fetching artifact file [ ${artifact_name} ]"
echo "artifact_url: ${fetch_url}"

if [ ! ${do_dryRun} ]; then
    # fetch artifcat
    wget -q ${fetch_url} -O /tmp/${artifact_name}
    exitCode=$?
    [ ${exitCode} -ne 0  ] && echo "Fetching artifact error." && exit ${exitCode}
fi

#todo check the checksum

if [ ! ${do_dryRun} ]; then
    # unzip artifact to target directory
    unzip /tmp/${artifact_name} -d ${target_path}/${target_dir}

    # remove zip file
    rm -f /tmp/${artifact_name}

    # if the common conf directory doesn't exists we create it ( we think that is the first installation !!)
    if [ ! -d ${common_config_dir} ]; then
        cp -r ${target_path}/${target_dir}/${artifact_version}/conf ${common_config_dir}
    fi

    #renaming the config directory
    mv ${target_path}/${target_dir}/${artifact_version}/conf   ${target_path}/${target_dir}/${artifact_version}/conf.new


    #create a symbolic link to the common config directory in the deployed artifact directory
    ln -s ${common_config_dir} ${target_path}/${target_dir}/${artifact_version}/conf

    #delete latest link
    [ -L "$HOME/${lastest_link}" ] && rm -f ${target_path}/${lastest_link}

    # Create a symlink to la latest fetched artifact
    ln -nfs ${target_path}/${target_dir}/${artifact_version} ${target_path}/${lastest_link}

    mv /tmp/${artifact_name}".sha1" ${target_path}/${target_dir}/${artifact_version}/lib/packet.sha1

    echo "Artifact is deployed successfully"
fi
exit 0
