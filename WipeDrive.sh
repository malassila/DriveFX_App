#!/bin/bash

##############################################################################################################################################################
# Author: Matt L
# Initial Release: 2023 Q1
# Purpose: This script performs a secure erase on a specified hard drive using
#          the openSeaChest_Erase command. It also handles various error cases
#          and allows for retrying the erase with a different method if the
#          initial method fails.
# This bash script is designed to securely wipe a specified drive on a system. 
# It does this by using the openSeaChest_Erase command, which has several options for different methods of wiping a drive.
# The script takes two arguments: the device handle of the drive to be wiped, and the serial number of the drive.
# These arguments are passed to the main function when the script is run.
#
# The main function has a loop that runs the wipe_drive function and then the check_wipe_output function.
#
# The output of the check_wipe_output function is stored in a variable and checked for three possible values: "SUCCESS", "FAILURE", or "RESTART".
# 		- If the output is "SUCCESS", the loop is broken and the script completes.
# 		- If the output is "FAILURE", the loop calls the next_wipe_option function, which determines the next wipe method to try according to the wipe_preference_order array. 
# 		This new wipe method is then passed to the wipe_drive function and the loop continues.
# 		- If the output is "RESTART", the loop simply continues and re-runs the wipe_drive function with the same wipe method.
#
# The wipe_drive function takes two arguments: the device handle of the drive to be wiped, and the wipe method to be used.
# It then runs the appropriate function using these arguments.
#
# The check_wipe_output function takes one argument: the file path of the output of the wipe_drive function.
# It checks the contents of this file for specific strings that indicate the success or failure of the wipe, and returns "SUCCESS", "FAILURE", or "RESTART" accordingly.
#
# The show_help function displays usage instructions for the script.
#
# The next_wipe_option function is called if the output of the check_wipe_output function is "FAILURE".
# It determines the next wipe method to try according to the wipe_preference_order array and what the drive supports.
# -------------------------------------------------------------------------------------------------------------------------------------------------------------
# The wipe_preference_order array is an ordered list of the different wipe methods that the script will try, in order of preference.
# 			This is located at the end of this script after the main function.
# -------------------------------------------------------------------------------------------------------------------------------------------------------------
# The check_wipe_output_path function checks if the /drive/wipe directory exists, and creates it if necessary.
# This directory is where the output files of the wipe_drive function are stored.
#
#          The script takes one required argument: the device handle of the
#          drive to be wiped (e.g. "sdb").
#
#          The script also accepts the following optional arguments:
#           -h, --help: Display the help message and exit
#
# Updated: 2023-01-06
#          Added support for retrying the wipe with a different method if the
#          initial method fails.
#          Improved error handling and added more detailed error messages.
##############################################################################################################################################################
# Required Utilities:
#		- openSeaChest
#			This is a command line utility that is part of the openSeaChest library created by Seagate
#			
#			openSeaChest_Erase for Linux and Windows - Seagate drive utilities
#-------------------------------------------------------------------------------------------------------------------------------------------------------------
# To download the openSeaChest library, it can be cloned the following link:
# https://github.com/Seagate/openSeaChest
#
#
###### BEFORE DOING ANYTHING FURTHER ENSURE THAT THE "MAKE" UTILITY IS INSTALLED ######
#
#
# These are the steps required to install the openSeaChest library on a Linux system:
#	cd /root
# 	git clone --recursive --branch v22.07 https://github.com/Seagate/openSeaChest.git
#	cd openSeaChest/Make/gcc && make release
# 	mkdir -pv /opt/openSeaChest/bin && mv openseachest_exes/* /opt/openSeaChest/bin
#	cd / && rm -rf /root/openSeaChest
#	cat <<<'export PATH="${PATH}:/opt/openSeaChest/bin"' >> /root/.bashrc
#	exit
# 	
##############################################################################################################################################################

secure_erase() {
  # --------------------------------------------------------------------------
  # This script uses the openSeaChest library created by Seagate
  # openSeaChest_Erase for Linux and Windows - Seagate drive utilities
  # Copyright (c) 2014-2020 Seagate Technology LLC and/or its Affiliates
  # --------------------------------------------------------------------------
  # A full manual can be found at the following link:
  # https://github.com/Seagate/openSeaChest/blob/develop/docs/openSeaChest_Erase.212.txt
  # --------------------------------------------------------------------------
  # This is how the command in this script is structured:
  # --------------------------------------------------------------------------
  #	        -d, --device deviceHandle
  #              Use this option with most commands to specify the device
  #              handle on which to perform an operation. Example: /dev/sd?
  # --------------------------------------------------------------------------
  #         --ataSecureErase [normal | enhanced]            (SATA only)
  #              Use "normal" to start a standard ATA security erase
  #              or "enhanced" to start an enhanced ATA security erase.
  #
  #              ATA Security Erase takes a very long time to complete at
  #              approximately three (3) hours per Tera-byte (HDD).
  #
  #              * normal writes binary zeros (0) or ones (1) to all user
  #              data areas.
  #
  #              * enhanced will fill all user data areas and reallocated
  #              user data with a vendor specific pattern. Some Seagate
  #              Instant Secure Erase will perform a cryptographic
  #              erase instead of an overwrite.
  # --------------------------------------------------------------------------
  #        --confirm this-will-erase-data
  # 			 SeaGate requires this flag to start the wipe
  # 			 Confirm that the user wants to erase the data on the drive
  # --------------------------------------------------------------------------
  #        tee "$1"
  # 			 This command pipes the output of the openSeaChest_Erase command to a file named after the drives serial number
  #				 The tee command copies standard input to standard output and also to any files given as arguments. 
  #				 This is useful when you want not only to send some data down a pipe, but also to save a copy.
  # 			 If a file being written to does not already exist, it is created. If a file being written to already exists,
  # 			 the data it previously contained is overwritten unless the -a option is used.
  # --------------------------------------------------------------------------
  # 
  drive="$1"
  output_path="$2"			 

  openSeaChest_Erase -d /dev/"$drive" --ataSecureErase normal --confirm this-will-erase-data | tee $output_path

  handle_exit_code $?

}

# enhanced_secure_erase() {
#   openSeaChest_Erase -d /dev/"$drive" --ataSecureErase enhanced --confirm this-will-erase-data | tee "$1"

#   handle_exit_code $?
# }

# secure_write_same_erase() {
# # TODO: WRITE THIS FUNCTION
# }

# secure_overwrite_erase() {
# # TODO: WRITE THIS FUNCTION
# }

# format_unit_erase() {
# # TODO: WRITE THIS FUNCTION
# }

# sanitize_block_erase() {
# # TODO: WRITE THIS FUNCTION
# }

# sanitize_overwrite_erase() {
# # TODO: WRITE THIS FUNCTION
# }

quickest_erase() {
	openSeaChest_Erase -d /dev/$1 --performQuickestErase --confirm this-will-erase-data | tee "$2_wipe.txt"

	handle_exit_code $?
}


handle_exit_code() {
	  # Handle the exit code
  case $1 in
    0)
      # Wipe successful! no need to do anything
      ;;
    1)
      echo "Error in command line options" >&2
      exit 1
      ;;
    2)
      echo "Invalid device handle or missing device handle" >&2
      exit 2
      ;;
    3)
      echo "Operation failure" >&2
      exit 3
      ;;
    4)
      echo "Operation not supported" >&2
      exit 4
      ;;
    5)
      echo "Operation aborted" >&2
      exit 5
      ;;
    6)
      echo "File path not found" >&2
      exit 6
      ;;
    7)
      echo "Cannot open file" >&2
      exit 7
      ;;
    8)
      echo "File already exists" >&2
      exit 8
      ;;
    9)
      echo "Need elevated privileges" >&2
      exit 9
      ;;
    *)
      echo "Unknown error" >&2
      exit 10
      ;;
  esac
}



# Check if the /drive/wipe directory exists, and create it if necessary
check_wipe_output_path() {
	if [ ! -d "/drive" ]; then
		# If it does not exist, create it
		mkdir -m 777 /drive 2> /dev/null
	fi
	if [ ! -f "/drive/wipe" ]; then
		# If it does not exist, create it
		mkdir -m 777 /drive/wipe 2> /dev/null
	fi
}



# Check if the /drive/wipe/serial_wipe.txt file exists, and create it if necessary
check_wipe_output_file() {
	if [ ! -f "/drive/wipe/"$1"_wipe.txt" ]; then
		# If it does not exist, create it
		touch /drive/wipe/"$1"_wipe.txt
		chmod 777 /drive/wipe/"$1"_wipe.txt
	fi
}



# Check the output file of the openSeaChest_Erase command located in the /drive/wipe directory
# This function is used to determine if the wipe was successful or not
# openSeachest_Erase will return a 0 exit code even if the wipe was not successful
# The output of the command will be evaluated to determine what to do next
check_wipe_output() {
	output_path=$1
	exit_message_path=$2
  if grep -q "ATA security erase has completed successfully" "$output_path"; then
  # Wipe completed successfully
	if tail -n 5 $output_path | grep 'hour\|minute\|second'; then
		echo "SUCCESS" | tee $exit_message_path
	else
		"SOMETHING WENT WRONG"
	fi	
  elif grep -q "ATA security not supported" "$output_path"; then
  # Command did not wipe the drive more investigating might be necessary

    echo "FAILURE" | tee $exit_message_path
  elif grep -q "WARNING!!! The drive is in a security state where clearing the password is not possible" "$output_path"; then
  # Command displayed a warning that the drive stopped wiping after it had started and needs restarting
    echo "RESTART"
  else
    cat $1;
  fi
}



# Check if the help argument was passed
check_argument() {
	if [ "$1" = "-h" ] || [ "$1" = "--help" ] || [ -z "$1" ]; then
		show_help
		exit 0
	elif [[ ! "$1" == *"sd"* ]]; then
		# if the first argument does not contain a drive name or a help flag
		echo "Error: Invalid device handle. Please specify a valid device handle (e.g. /dev/sdX)." >&2
		echo "" >&2
		show_help
		exit 2
	# Check if the drive being passed as an argument is the boot drive
	elif [ "$1" = "sda" ]; then
		# Exit the script with an error message
		echo "Error: Cannot wipe the boot drive" >&2
		exit 1
	fi
}


# Print the help instructions for the script
show_help() {
  echo "Usage: $0 <device_handle> <serial_number>"
  echo "Example: wipedrive sdc 1234567890"
  echo ""
  echo "Erases the specified drive and saves the output to a file in the /drive/wipe directory."
}



# This function determines the next wipe option to try
# based on the available options and the preferred order
next_wipe_option() {
  # Get the list of available wipe options
  # using the openSeaChest_Erase command and filtering the output
  # to only include options 1, 2, and 3
  # and remove the leading whitespace and option number
  available_options=$(openSeaChest_Erase -d /dev/"$1" --showEraseSupport | grep '^ 1 \|^ 2 \|^ 3' | awk '{$1=""; print $0}' | cut -c 2-)

  # Iterate through the wipe_prefence_order array
  for option in "${wipe_prefence_order[@]}"; do
    # Check if the current option is available
    if [[ "$available_options" == *"$option"* ]]; then
      # If it is available, return the option
      erase_preference="$option"
    fi
  done

	wipe_drive "$1" "$erase_preference"

}



# This method takes three parameters driveHandle and wipe method
wipe_drive() {
	drive_handle=$1
	wipe_method=$2
	output_path=$3

	# determine the wipe_method
	if [ "$wipe_method" = "ATA Security Erase" ]; then
		secure_erase  "$drive_handle" "$output_path"
	elif [ "$wipe_method" = "ATA Enhanced Security Erase" ]; then
		enhanced_secure_erase "$drive_handle" "$output_path"
	elif [ "$wipe_method" = "Write Same Erase" ]; then
		secure_write_same_erase "$drive_handle" "$output_path"
	elif [ "$wipe_method" = "Overwrite Erase" ]; then
		secure_overwrite_erase "$drive_handle" "$output_path"
	elif [ "$wipe_method" = "Format Unit" ]; then
		format_unit_erase "$drive_handle" "$output_path"
	elif [ "$wipe_method" = "Sanitize Block Erase" ]; then
		sanitize_block_erase "$drive_handle" "$output_path"
	elif [ "$wipe_method" = "Sanitize Overwrite Erase" ]; then
		sanitize_overwrite_erase "$drive_handle" "$output_path"
	fi
}




main() {
  # Entry point the the script
  #
  # First check the first argument to see if it is a help flag or a valid device handle
  check_argument "$1"
  #
  # The first argument should be the device handle of the drive to wipe.
  # The second argument should be the serial number of the drive to wipe.
  # The output_path is the stored output of the erase command.
  # The exit_message_path is the path of the file that the bash script will write to when it exits.
  # The exit_message_path is the file that Java will be listening to.
  # Initialize the wipe_output, and wipe_method variables
  # The wipe_method variable will get the first value from the array wipe_prefence_order
  drive=$1
  serial=$2
  output_path="/drive/wipe/"$serial"_wipe.txt"
  exit_message_path="/dev/disk/by-path/"$drive"_wipe.txt"
  wipe_output=""
  wipe_method="${wipe_prefence_order[0]}"

  


  # Check if the /drive/wipe directory exists, and create it if necessary
  check_wipe_output_path
  check_wipe_output_file "$serial"



 
# -----------------------------------------------------------------------------------------------------
# This is an example output of a successful erase
# 		ACTION: Nothing the erase operation was successful
# -----------------------------------------------------------------------------------------------------
#
# 	ATA security erase has completed successfully
# 	Time to erase was  4 hours 15 minutes 3 seconds
#
# -----------------------------------------------------------------------------------------------------
# -----------------------------------------------------------------------------------------------------
# This is an example output of a failed erase (the drive does not support ATA security erase)
# 		ACTION: More investigation is required to determine what to do next
# -----------------------------------------------------------------------------------------------------
#
#	ATA security erase
# 	ATA security not supported
#
# -----------------------------------------------------------------------------------------------------
# -----------------------------------------------------------------------------------------------------
# This is an example output of a failed erase (the drive did not complete the last wipe)
# 		ACTION: The drive needs to be restarted and the erase command needs to be run again
# -----------------------------------------------------------------------------------------------------
# ATA Security erase failed to complete after
#
#        WARNING!!! The drive is in a security state where clearing the password is not possible!
#        Please power cycle the drive and try clearing the password upon powerup.
#        Erase password that was used was:  "SeaChest" (User)
# -----------------------------------------------------------------------------------------------------
# 
# Loop until the output is "SUCCESS" or "FAILURE" or counter is reached 
while [ "$output" != "SUCCESS" ] && [ $loop_count -le 3 ]; do
  # Wipe the drive
  
  wipe_drive "$drive" "$wipe_method" "$output_path"

  # Check the output of the wipe command
  output=$(check_wipe_output "$output_path" "$exit_message_path")
  echo "$output"
  # Handle the different output values
  if [ "$output" = "FAILURE" ]; then
    # Command did not wipe the drive more investigating might be necessary
	# Check the supported erase options and execute the erase command with the highest preference
	next_wipe_option "$drive"
    echo "Wipe command failed" >&2
    exit 1
  elif [ "$output" = "RESTART" ]; then
    # The wipe command was interrupted in a previous attempt, loop again to wipe
    continue
  fi
done

}

# Initialize the erase commands array in order of preference
wipe_prefence_order=(
  "ATA Security Erase"
  "ATA Enhanced Security Erase"
  "Write Same Erase"
  "Overwrite Erase"
  "Format Unit"
  "Sanitize Block Erase"
  "Sanitize Overwrite Erase"
)

# Call main function with all the command line arguments
main "$@"
