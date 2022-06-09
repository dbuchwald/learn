#!/bin/bash
MACHINENAME=$1

#Create VM
VBoxManage createvm --name ${MACHINENAME} --ostype "Ubuntu_64" --register --basefolder `pwd`
#Set memory and network
VBoxManage modifyvm ${MACHINENAME} --cpus 4
VBoxManage modifyvm ${MACHINENAME} --ioapic on
VBoxManage modifyvm ${MACHINENAME} --memory 4096 --vram 64
VBoxManage modifyvm ${MACHINENAME} --rtcuseutc on
VBoxManage modifyvm ${MACHINENAME} --mouse usbtabled
VBoxManage modifyvm ${MACHINENAME} --graphicscontroller vmsvga
VBoxManage modifyvm ${MACHINENAME} --nic1 nat
VBoxManage modifyvm ${MACHINENAME} --natpf1 SSH,tcp,,2222,,22
VBoxManage modifyvm ${MACHINENAME} --natpf1 HTTP,tcp,,80,,80
VBoxManage modifyvm ${MACHINENAME} --natpf1 HTTPS,tcp,,443,,443
VBoxManage modifyvm ${MACHINENAME} --natpf1 APP,tcp,,8080,,8080
VBoxManage modifyvm ${MACHINENAME} --natpf1 K8S,tcp,,6443,,6443

#Create Disk and connect Debian Iso
VBoxManage createhd --filename `pwd`/${MACHINENAME}/${MACHINENAME}_DISK.vdi --size 40000 --format VDI
VBoxManage storagectl ${MACHINENAME} --name "SATA Controller" --add sata --controller IntelAhci --portcount 1
VBoxManage storageattach ${MACHINENAME} --storagectl "SATA Controller" --port 0 --device 0 --type hdd --medium  `pwd`/${MACHINENAME}/${MACHINENAME}_DISK.vdi
VBoxManage storagectl ${MACHINENAME} --name "IDE Controller" --add ide --controller PIIX4
VBoxManage storageattach ${MACHINENAME} --storagectl "IDE Controller" --port 0 --device 0 --type dvddrive --medium `pwd`/ubuntu.iso
VBoxManage storageattach ${MACHINENAME} --storagectl "IDE Controller" --port 1 --device 0 --type dvddrive --medium `pwd`/seed.iso
VBoxManage modifyvm ${MACHINENAME} --boot1 dvd --boot2 disk --boot3 none --boot4 none

#Enable RDP
#VBoxManage modifyvm $MACHINENAME --vrde on
#VBoxManage modifyvm $MACHINENAME --vrdemulticon on --vrdeport 10001

#Start the VM
#VBoxHeadless --startvm $MACHINENAME
