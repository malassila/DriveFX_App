#! /bin/bash

# Set up some variables for coloring the output
RED=$'\e[1;33m'
NC=$'\e[0m'

# Check if the -h flag was passed as an argument
if [[ $1 == "-h" ]]; then
   echo "Help message for OpenSeaChest installation script"
   echo "-------------------------------------------------------------------------------"
   echo ""
   echo "This script installs and compiles the OpenSeaChest utilities, which provide"
   echo "tools for managing and troubleshooting Seagate hard drives."
   echo ""
   echo "Usage: $0"
   echo ""
   echo "Options:"
   echo "  -h  Display this help message and exit"
   exit 0
else
   # If the -h flag was not passed, check if the script is being run as root
   if [[ $EUID -ne 0 ]]; then
      echo "This script must be run as root"
      exit 1
   fi
fi

# Check if the make command is installed
if [[ $(which make) == "" ]]; then
  echo "${RED}make command is not installed$NC"
  # Install make and check the exit status
  if ! apt install -y make; then
    # If the installation fails, print an error message and exit
    echo "${RED}Error: make installation failed${NC}"
    exit 1
  fi
fi

# Clone the OpenSeaChest repository
cd /root
git clone --recursive --depth=1 https://github.com/Seagate/openSeaChest.git

# Check the exit status of the git command
if [[ $? -ne 0 ]]; then
  # If the clone fails, print an error message and exit
  echo "${RED}Error: git clone failed${NC}"
  exit 1
fi

# Jump into the OpenSeaChest gcc compilation directory and compile the project
cd openSeaChest/Make/gcc && make release

# Check the exit status of the make command
if [[ $? -ne 0 ]]; then
  # If the compilation fails, print an error message and exit
  echo "${RED}Error: compilation failed${NC}"
  exit 1
fi

# Check if the destination directory exists
if [[ ! -d /opt/openSeaChest/bin ]]; then
  # If the directory does not exist, create it
  mkdir -pv /opt/openSeaChest/bin
fi

# Move the compiled binaries to the destination directory
mv openseachest_exes/* /opt/openSeaChest/bin

# Check the exit status of the mv command
if [[ $? -ne 0 ]]; then
  # If the move fails, print an error message and exit
  echo "${RED}Error: failed to move compiled binaries${NC}"
  exit 1
fi

# Remove the code used to compile the project
cd / && rm -rf /root/openSeaChest

# Update the PATH environment variable to include the OpenSeachest Utilities
echo 'export PATH="${PATH}:/opt/openSeaChest/bin"' >> /root/.bashrc

# Check the exit status of the echo command
if [[ $? -ne 0 ]]; then
  # If the update fails, print an error message and exit
  echo "${RED}Error: failed to update PATH environment variable${NC}"
  exit 1
fi

exit 0