Follow these instructions in order :)

All activities reside in Ubuntu VM

In Terminal1:
    (0) cd ~/Desktop
    (1) ./setup-cuckoo.sh

Using Ubuntu GUI:
    (2) Start VirtualBox and import WindowsXPSP3.ova (located on your Ubuntu Desktop)
        (*) File->Import Appliances
        (*) Follow the directions

In Terminal1:
    (3) VBoxHeadless --startvm "WindowsXPSP3"

In Terminal2:
    (4) cd ~/Desktop
    (5) rdesktop 192.168.56.1:3389 

In rdesktop:
    (6) Ensure Windows fully starts up and that a Command Prompt has started stating "Starting agent on 0.0.0.0:8000 ..."
    (7) Minimize Command Prompt window

In Terminal3:
    (8) cd ~/Desktop
    (9) VBoxManage snapshot "WindowsXPSP3" take "XP1" --pause
    (10) VBoxManage controlvm "WindowsXPSP3" poweroff
        (*) rDesktop should be shown as disconnected in Terminal2 (don't panic)
    (11) VBoxManage snapshot "WindowsXPSP3" restore "XP1"
    (12) ./setup-firewall.sh

Now you can begin the assigned problems (PhaseI, PhaseII, PhaseIII, and PhaseIV) as documented in "Malware-Project.pdf"