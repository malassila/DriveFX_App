#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <linux/hdreg.h>

int main(int argc, char** argv)
{
    HANDLE hDevice = CreateFile("\\\\.\\PhysicalDrive0",
                                GENERIC_READ | GENERIC_WRITE,
                                FILE_SHARE_READ | FILE_SHARE_WRITE,
                                NULL,
                                OPEN_EXISTING,
                                0,
                                NULL);

    if (hDevice == INVALID_HANDLE_VALUE)
    {
        // Error handling
    }

    // Set up the ATA_PASS_THROUGH_EX structure
    ATA_PASS_THROUGH_EX apte;
    memset(&apte, 0, sizeof(apte));
    apte.Length = sizeof(apte);
    apte.AtaFlags = ATA_FLAGS_DATA_IN;
    apte.DataBuffer = buffer;
    apte.DataTransferLength = 512;
    apte.TimeOutValue = 2;

    // Set up the CDB (Command Descriptor Block) for the ATA Secure Erase command
    apte.CurrentTaskFile[0] = 0x60;  // ATA Secure Erase command code
    apte.CurrentTaskFile[3] = 0x01;  // Parameter list length (1 sector)
    apte.CurrentTaskFile[4] = 0x00;  // Feature
    apte.CurrentTaskFile[6] = 0x01;  // Sector count

    // Issue the IOCTL to send the ATA Secure Erase command to the drive
    DWORD bytesReturned;
    if (!DeviceIoControl(hDevice,
                         IOCTL_ATA_PASS_THROUGH,
                         &apte,
                         sizeof(apte),
                         &apte,
                         sizeof(apte),
                         &bytesReturned,
                         NULL))
    {
        // Error handling
    }

    // Check the response from the drive
    if (apte.CurrentTaskFile[2] & 0x01)
    {
        // Error occurred during the ATA Secure Erase command
    }
    else
    {
        // ATA Secure Erase command completed successfully
    }

    return 0;
}