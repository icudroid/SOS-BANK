#!/bin/sh

script=$(readlink -f "$0")

script_path=$(dirname "$script")

artifacts_repository="10.199.54.45"

remote_user="starter-publisher"

# artifact to publish separted with space
all_artefacts="backoffice backoffice-statics"


if [ $# -lt 1 ]; then
    echo "No artefact specified. We publish all artefacts"
    artifacts_to_publish="${all_artefacts}"
elif [ "$1" = "-h" ]; then
    echo "Usage : publish.sh [artifact1 [artifact2 [...]]]"
    exit 0
else
    #Artifacts to send
    artifacts_to_publish=`printf '%s\|' "$@";`
    artifacts_to_publish=${artifacts_to_publish%|}  #delete last character
    artifacts_to_publish=${artifacts_to_publish%\\} #delete last character
fi

echo "Reading Project version ...."

project_version=`${script_path}/gradlew --daemon -q  projectVersion`

artifacts_to_publish=`echo ${artifacts_to_publish} | awk -v version=${project_version} '{for(i=1;i<=NF-1;i++){printf $i"-"version"\\\|"} printf $NF"-"version }'`

#Calculate the destination directory
if [ ! -z `expr "${project_version}" : '\(.*SNAPSHOT$\)'`  ]; then
    remote_dir="snapshots"
else
    remote_dir="releases"

fi

#Searching artifacts to publish
artifacts_list=`find -type f \
           | grep -e '\/build\/libs' -e '\/build\/distributions' \
           | grep -v '\/libs\/lib'  \
           | grep -e '\.war\|\.tar\.gz\|\.jar\|\.zip' \
           | grep -v -e 'original$' \
           | grep -e "${artifacts_to_publish}" `

# test if we have files to send.
[  -z "${artifacts_list}" ] && echo "No artifacts found. you need to build before publish ;)" && exit 1

# test if we can execute ssh commande on the remote server without password in BachMode

ssh -o "BatchMode yes" ${remote_user}@${artifacts_repository} pwd > /dev/null  2>&1

if [ $? -ne 0 ]; then
    echo "Connexion test to the remote artifact repository failed. Please ensure that you have copied your public key to the remote server"
    exit 1
fi

for artifact in ${artifacts_list}
do
    echo "${artifact##*/*/}"
    #scp ${artifact}  ${remote_user}@${artifacts_repository}:~/${project_version}/${artifact##*/*/}
    rsync -ave ssh ${artifact} ${remote_user}@${artifacts_repository}:~/${remote_dir}/${project_version}/
    [ ! -d /tmp/${project_version} ] && mkdir /tmp/${project_version}
    sha1sum ${artifact} > /tmp/${project_version}/${artifact##*/*/}.sha1
    rsync -ave ssh /tmp/${project_version}/${artifact##*/*/}.sha1 ${remote_user}@${artifacts_repository}:~/${remote_dir}/${project_version}/
done

# we clean temp data
rm -rf /tmp/${project_version}






