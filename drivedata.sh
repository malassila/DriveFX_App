#!/bin/bash

device=$1
shift
# Check if the file "/tmp/$device"_smart.txt" exists
if [[ ! -f /tmp/$device"_smart.txt" ]]; then
  # create the file and change the permissions
    touch /tmp/$device"_smart.txt"
    chmod 777 /tmp/$device"_smart.txt"
else
  # Overwrite the file with the output of the smartctl command
  smartctl -a $device > /tmp/$device"_smart.txt"
fi

# Check if the file "/tmp/$device"_data.txt" exists
if [[ ! -f /tmp/$device"_data.txt" ]]; then
  # create the file and change the permissions
    touch /tmp/$device"_data.txt"
    chmod 777 /tmp/$device"_data.txt"
else
  # Overwrite the file and run the udevadm and lsblk commands
#   echo "udevadm info output"
  udevadm info $device >> /tmp/$device"_data.txt"
  echo ""
  echo 
  lsblk -JdO $device >> /tmp/$device"_data.txt"
fi


# Process the command-line arguments
while [[ $# -gt 0 ]]; do
  case "$1" in
    -h|--help)
      # Display the help message
      echo "Usage: $0 [options]"
      echo "Options:"
      echo "  -h, --help      Display this help message"
      echo "  -smart1         Display the raw read error rate"
      echo "  -smart3         Display the spin-up time"
      echo "  -smart4         Display the start/stop count"
      echo "  -smart5         Display the reallocated sector count"
      echo "  -smart7         Display the seek error rate"
      echo "  -smart9         Display the power-on hours"
      echo "  -smart10        Display the spin retry count"
      echo "  -smart12        Display the power cycle count"
      echo "  -model          Display the model number"
      echo "  -serial         Display the serial number"
      echo "  -rotation       Display the rotation rate"
      echo "  -size           Display the size"
      echo "  -sector         Display the sector size"
      echo "  -result         Display the SMART overall-health result"
      exit 0
      ;;
    -smart1)
      # Get the raw read error rate an awk out only the raw value
    #   get_raw_read()
      shift
      ;;
    -smart3)
      # Get the spin-up time
      value=$(cat /tmp/$device"_smart.txt" | grep '^ 3 ' | awk '{print $10}')
      echo "$value"
      shift
      ;;
    -smart4)
      # Get the start/stop count
      value=$(cat /tmp/$device"_smart.txt" | grep '^ 4 ' | awk '{print $10}')
      echo "$value"
      shift
      ;;
    -smart5)
      # Get the reallocated sector count
      echo "SMART 5"
      value=$(cat /tmp/$device"_smart.txt" | grep '^ 5 ' | awk '{print $10}')
      echo "$value"
      shift
      ;;
    -smart7)
      # Get the seek error rate
      value=$(cat /tmp/$device"_smart.txt" | grep '^ 7 ' | awk '{print $10}')
      echo "$value"
      shift
      ;;
    -smart9)
      # Get the power-on hours
      value=$(cat /tmp/$device"_smart.txt" | grep '^ 9 ' | awk '{print $10}')
      echo "$value"
      shift
      ;;
    -smart10)
        # Get the spin retry count
        value=$(cat /tmp/$device"_smart.txt" | grep '^ 10 ' | awk '{print $10}')
        echo "$value"
        shift
        ;;
    -smart12)
        # Get the power cycle count
        value=$(cat /tmp/$device"_smart.txt" | grep '^ 12 ' | awk '{print $10}')
        echo "$value"
        shift
        ;;
    -model)
        # Get the model number
        value=$(grep 'Device Model:' /tmp/$device"_smart.txt")
        echo "$value"
        shift
        ;;
    -serial)
        # Get the serial number
        value=$(grep 'Serial Number:' /tmp/$device"_smart.txt")
        echo "$value"
        shift
        ;;
    -rotation)
        # Get the rotation rate
        value=$(grep 'Rotation Rate:' /tmp/$device"_smart.txt")
        echo "Rotation rate: $value"
        shift
        ;;
    -size)
        # Get the size
        value=$(grep 'User Capacity:' /tmp/$device"_smart.txt")
        echo "Size: $value"
        shift
        ;;
    -sector)
        # Get the sector size
        value=$(grep 'Logical block size:' /tmp/$device"_smart.txt")
        echo "Sector size: $value"
        shift
        ;;
    -result)
        # Get the SMART overall-health result
        value=$(grep 'SMART overall-health self-assessment test result:' /tmp/$device"_smart.txt")
        echo "SMART overall-health result: $value"
        shift
        ;;
    *)
        # Unknown option
        echo "Unknown option: $1"
        exit 1
        ;;
    esac
done

