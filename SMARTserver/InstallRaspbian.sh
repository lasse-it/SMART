#!/bin/bash
echo "WARNING!!"
echo "If you use Mopidy, please note that libspotify is in beta!"
echo "If the script crashes you can just re run it."
echo "If you need help, please feel free to contact me"
echo "Press enter to continue, or Ctrl-C to abort installation"

read

dpkg-reconfigure tzdata
echo 'Please enter a new password for the user "root":'
passwd root

echo -n "Please specify a new hostname:"
read hostname
echo $hostname > /etc/hostname
easy_install pip
pip install RPi.GPIO
pip3 install RPi.GPIO
sudo modprobe ipv6
echo ipv6 >> /etc/modules
apt-get update

echo "Installing SMARTserver"
wget https://github.com/lasse-it/SMART/archive/master.zip
unzip master.zip
rm master.zip
cd SMART-master/SMARTserver
cp -r opt/SMARTserver/ /opt/
chmod -R 775 /opt/SMARTserver
cp etc/init.d/smartserver /etc/init.d/
chmod a+rx /etc/init.d/smartserver
update-rc.d smartserver defaults
cd ../..
rm -r SMART-master
touch /etc/cron.d/smart
chmod 775 /etc/cron.d/smart
echo "Web Interface at http://"$(hostname -i)"/" >> endmessage
echo "To enable https copy a combined cert to /opt/SMARTserver/ssl.pem" >> endmessage
echo "You can modify index.html in /opt/SMARTserver/html to your liking" >> endmessage

function music {
read answer
if [ "$answer" = "yes" ]; then
echo "Installing Mopidy music server"

wget -q -O - https://apt.mopidy.com/mopidy.gpg | sudo apt-key add -
echo "deb http://apt.mopidy.com/ stable main contrib non-free" >> /etc/apt/sources.list
echo "deb-src http://apt.mopidy.com/ stable main contrib non-free" >> /etc/apt/sources.list
apt-get update
apt-get -y install mopidy mopidy-alsamixer python-setuptools python-dev libffi-dev libssl-dev libspotify-dev

pip2.7 install mopidy-mopify
pip2.7 install mopidy-spotify
pip2.7 install Mopidy-Spotify-Tunigo
echo "pcm.!default {
        type hw
        card 1
}

ctl.!default {
        type hw
        card 1
}" > /var/lib/mopidy/.asoundrc
echo "------------------------------------------------------"
echo "| Please enter your Spotify Premium credentials,     |"
echo "| if you created your Spotify account with facebook, |"
echo "| then goto spotify.com/account/set-device-password/ |"
echo "| to get your credentials for Spotify                |"
echo "------------------------------------------------------"
echo -n "Username: "
read username
echo -n "Password: "
read password

echo '[spotify]
enabled = true
username = '"$username"'
password = '"$password"'

[mpd]
hostname = ::

[logging]
color = true
console_format = %(levelname)-8s %(message)s
debug_format = %(levelname)-8s %(asctime)s [%(process)d:%(threadName)s] %(name)s\n  %(message)s
debug_file = mopidy.log
config_file =

[audio]
mixer = software
mixer_volume = 100
output = autoaudiosink

[local]
enabled = false

[http]
enabled = true
hostname = ::
port = 6680
static_dir =
zeroconf = Mopidy HTTP server on $hostname' > /etc/mopidy/mopidy.conf
chmod 777 /var/log/mopidy /etc/mopidy/mopidy.conf /var/lib/mopidy/.asoundrc
update-rc.d mopidy defaults
echo "Spotify Interface at http://"$(hostname -i)":6680/" >> endmessage
echo "0 0 * * * /usr/sbin/service mopidy restart
@reboot /usr/sbin/service mopidy restart" > /etc/cron.d/mopidy
elif [ "$answer" = "no" ]; then
echo 1 > /dev/null
else
echo "please answer yes or no"
music
fi
}
echo "Do you want to install the Mopidy music server?"
echo "yes/no"
music



function smartscreen {
read answer
if [ "$answer" = "yes" ]; then
echo "Installing SMARTscreen"
elif [ "$answer" = "no" ]; then
echo 1 > /dev/null
else
echo "please answer yes or no"
smartscreen
fi
}
#echo "Do you want to install the SMARTscreen?"
#echo "yes/no"
#smartscreen

echo "Installation finished, press any key to reboot" >> endmessage
cat endmessage
rm endmessage
read
reboot