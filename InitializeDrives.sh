#! /bin/bash

disk=$1
action=$2
sleepTime=$3

RED=$'\e[1;33m'
NC=$'\e[0m'

if [ $action == "ENTRY_CREATE" ]; then
    slot=$(encled $disk | awk '{print $1 $2}' | sed 's/Slot0//')
    echo "Slot:  $slot" > /SMART/${disk}Info.txt
    lsblk -O /dev/$disk -J >> /SMART/${disk}Info.txt
    smartctl -a /dev/$disk > /SMART/$disk.txt
    echo $slot | sed 's:.*/::'
elif [ $action == "ENTRY_DELETE" ]; then
    slot=$(cat /SMART/${disk}Info.txt | head -n 1 | awk '{print $2}')
#    rm -r /SMART/$disk*
    sudo encled $slot off
fi
