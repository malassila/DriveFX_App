#! /bin/bash

RED=$'\e[1;33m'
NC=$'\e[0m'

if [[ $1 == "-h" ]]; then
   echo "Help message"
   echo "------------"
   echo ""
   echo ""
   echo ""
   exit 1
fi

if [[ $EUID -ne 0 ]]; then
   echo "This script must be run as root"
   exit 1
fi

if [[ $(which make) == "" ]]; then
  echo "${RED}make command is not installed$NC"
  apt install -y make
fi

#You will need to compile OpenSeaChest Utilities, as root:

cd /root
git clone --recursive https://github.com/Seagate/openSeaChest.git

#Jump into OpenSeaChest gcc compilation method and Compile the project:
cd openSeaChest/Make/gcc && make release

#Wait for compilation to Finish, and check if there are erros at the end.
#If there are no errors, then copy binaries to final destination and remove code stuff used to compile:
mkdir -pv /opt/openSeaChest/bin && mv openseachest_exes/* /opt/openSeaChest/bin
cd / && rm -rf /root/openSeaChest

#Update your root PATH environment var to know about OpenSeachest Utilities:
cat <<<'export PATH="${PATH}:/opt/openSeaChest/bin"' >> /root/.bashrc

exit
