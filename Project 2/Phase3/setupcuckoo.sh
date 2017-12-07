#!/bin/bash

##############################
# Just run ./setup-cuckoo.sh #
##############################

# Do all of this on the Desktop
cd $HOME/Desktop

# Install necessary software
sudo apt-get -y install rdesktop
sudo apt-get -y install python-pip
sudo apt-get -y install git

# Install necessary packages
sudo apt-get -y install python python-sqlalchemy python-bson python-dpkt python-jinja2 python-magic python-pymongo python-gridfs python-libvirt python-bottle python-pefile bridge-utils python-pyrex

# Install python libraries
pip install sqlalchemy bson jinja2 pymongo bottle pefile cybox maec django chardet nose --user

# Install tcpdump with proper permissions
sudo apt-get -y install tcpdump
sudo setcap cap_net_raw,cap_net_admin=eip /usr/sbin/tcpdump
getcap /usr/sbin/tcpdump

# Install specific versions of Pydeep and Yara
wget http://sourceforge.net/projects/ssdeep/files/ssdeep-2.12/ssdeep-2.12.tar.gz
tar xvzf ssdeep-2.12.tar.gz
cd ssdeep-2.12/
./configure && make && sudo make install
cd $HOME/Desktop
git clone https://github.com/kbandla/pydeep
cd pydeep
python setup.py build
sudo python setup.py install
cd $HOME/Desktop

sudo apt-get -y install libpcre3 libpcre3-dev
sudo apt-get -y install subversion
wget https://yara-project.googlecode.com/files/yara-1.7.tar.gz
tar xvzf yara-1.7.tar.gz
cd yara-1.7/
./configure --enable-cuckoo --enable-magic && make && sudo make install
sudo sh -c "echo '/usr/local/lib' >> /etc/ld.so.conf"
sudo ldconfig
cd $HOME/Desktop

wget https://yara-project.googlecode.com/files/yara-python-1.7.tar.gz
tar xvzf yara-python-1.7.tar.gz
cd yara-python-1.7/
python setup.py build
sudo python setup.py install
cd $HOME/Desktop

# Install Distorm and Volatility
wget https://distorm.googlecode.com/files/distorm3.zip
unzip distorm3.zip
cd distorm3/
python setup.py build
sudo python setup.py install
cd $HOME/Desktop

wget https://volatility.googlecode.com/files/volatility-2.3.1.tar.gz
tar xvzf volatility-2.3.1.tar.gz
cd volatility-2.3.1/
python setup.py build
sudo python setup.py install
cd $HOME/Desktop

# Install Linux headers
sudo apt-get -y install build-essential linux-headers-`uname -r` libvpx1

# Download and install specific version of VirtualBox
sudo apt-get -y install libsdl1.2debian
wget http://download.virtualbox.org/virtualbox/4.3.38/virtualbox-4.3_4.3.38-106717~Debian~wheezy_amd64.deb
sudo dpkg -i virtualbox-4.3_4.3.38-106717~Debian~wheezy_amd64.deb

# Download and install extension
wget http://download.virtualbox.org/virtualbox/4.3.38/Oracle_VM_VirtualBox_Extension_Pack-4.3.38-106717.vbox-extpack
sudo VBoxManage extpack install Oracle_VM_VirtualBox_Extension_Pack-4.3.38-106717.vbox-extpack

# Add user permissions
sudo usermod -a -G vboxusers `whoami`
sudo usermod -a -G libvirtd `whoami`

# Build network interface
VBoxManage hostonlyif create
sudo ip link set vboxnet0 up
sudo ip addr add 192.168.56.1/24 dev vboxnet0

# Create Windows VM
#   VBoxManage createvm --name "WindowsXPSP3" --register
#   VBoxManage modifyvm "WindowsXPSP3" --memory 256 --acpi off --boot1 dvd
#   VBoxManage modifyvm "WindowsXPSP3" --nic1 hostonly --hostonlyadapter1 vboxnet0
#   VBoxManage modifyvm "WindowsXPSP3" --ostype WindowsXP
#   VBoxManage createhd --filename windowsxpsp3.vdi --size 10000
#   VBoxManage storagectl "WindowsXPSP3" --name "IDE Controller" --add ide
#   VBoxManage storageattach "WindowsXPSP3" --storagectl "IDE Controller" --port 0 --device 0 --type hdd --medium windowsxpsp3.vdi
#   VBoxManage storageattach "WindowsXPSP3" --storagectl "IDE Controller" --port 1 --device 0 --type dvddrive --medium winxpsp3.iso
#   VBoxManage modifyvm "WindowsXPSP3" --vrde on

# Set up so that Windows can talk to Internet
sudo iptables -A FORWARD -o eth0 -i vboxnet0 -s 192.168.56.0/24 -m conntrack --ctstate NEW -j ACCEPT
sudo iptables -A FORWARD -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT
sudo iptables -A POSTROUTING -t nat -j MASQUERADE

# Forward IP connection
sudo sysctl -w net.ipv4.ip_forward=1
sudo sysctl -p
