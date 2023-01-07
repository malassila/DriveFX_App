#!/bin/bash

# Set up some variables for coloring the output
RED=$'\e[1;31m'
GREEN=$'\e[1;32m'
YELLOW=$'\e[1;33m'
NC=$'\e[0m'

# Print the CPU information
echo "CPU Information"
echo "---------------"
echo ""

# Print the CPU model name
model_name=$(grep "model name" /proc/cpuinfo | head -n 1 | cut -d ":" -f 2)
printf "Model Name: ${GREEN}%s${NC}\n" "$model_name"

# Print the number of CPU cores
num_cores=$(grep -c "model name" /proc/cpuinfo)
printf "Number of Cores: ${GREEN}%d${NC}\n" "$num_cores"

# Print the CPU frequency
cpu_freq=$(grep "cpu MHz" /proc/cpuinfo | head -n 1 | cut -d ":" -f 2)
printf "Frequency: ${GREEN}%s MHz${NC}\n" "$cpu_freq"

# Print the CPU architecture
cpu_arch=$(uname -m)
printf "Architecture: ${GREEN}%s${NC}\n" "$cpu_arch"
echo ""

# Perform some basic testing
echo "Basic Testing"
echo "-------------"
echo ""

# Test the CPU with the stress utility
echo "${YELLOW}Running stress test...${NC}"
if stress --cpu $(nproc) -t 30; then
  echo "${GREEN}CPU test passed${NC}"
else
  echo "${RED}CPU test failed${NC}"
fi
echo ""

# Test the memory with the memtest86+ utility
echo "${YELLOW}Running memtest86+...${NC}"
if memtest-cli; then
  echo "${GREEN}Memory test passed${NC}"
else
  echo "${RED}Memory test failed${NC}"
fi
echo ""
