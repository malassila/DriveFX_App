#! /bin/bash
disk=$1
action=$2
sleepTime=$3

RED=$'\e[1;33m'
NC=$'\e[0m'

sudo sea_erase -d /dev/$disk \
	--ataSecureErase normal \
	--confirm this-will-erase-data > /SMART/${disk}Wipe.txt

