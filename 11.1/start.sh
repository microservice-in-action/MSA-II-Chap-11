#!/bin/sh

set -e
set -x

# check whether an env exists
check_env_exist() {
    envname=$1
    envvalue=$2
    if [ -z "$envvalue" ]; then
        /bin/echo "env $envname not exist"
        exit 1
    fi
}

check_ccenv_exist() {
    envname=$1
    envvalue=$2
    if [ -z "$envvalue" ]; then
        /bin/echo "WARNING: env $envname not exist"
    fi
}

check_metricenv_exist() {
    envname=$1
    envvalue=$2
    if [ -z "$envvalue" ]; then
        /bin/echo "WARNING: env $envname not exist"
    fi
}


#////////////////////////////////////////////////////#
#          go SDK                                   #
#///////////////////////////////////////////////////#
#check_env_exist "CSE_SERVICE_CENTER" $CSE_SERVICE_CENTER
check_env_exist "CSE_SERVICE_CENTER" $CSE_SERVICE_CENTER
check_ccenv_exist "CSE_CONFIG_CENTER_ADDR" $CSE_CONFIG_CENTER_ADDR
check_metricenv_exist "CSE_MONITOR_SERVER_ADDR" $CSE_MONITOR_SERVER_ADDR

#name=app/user
#echo $(env)

#export CSE_REGISTRY_ADDR=$CSE_SERVICE_CENTER
export CSE_REGISTRY_ADDR=$CSE_SERVICE_CENTER

advertise_addr=$(ifconfig eth0 | grep -E 'inet\W' | grep -o -E [0-9]+.[0-9]+.[0-9]+.[0-9]+ | head -n 1)

#replace ip addr
sed -i s/"advertiseAddress:\s\{1,\}[0-9]\{1,3\}.[0-9]\{1,3\}.[0-9]\{1,3\}.[0-9]\{1,3\}"/"advertiseAddress: $advertise_addr"/g conf/chassis.yaml

cp bin/user .

./user --config-dir ./conf

while true; do
    sleep 60
done

