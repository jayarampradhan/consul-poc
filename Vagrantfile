# -*- mode: ruby -*-
# vi: set ft=ruby :
$script = <<SCRIPT
echo Installing dependencies...
yum -y update
yum -y install unzip wget
sudo yum install -y unzip curl
echo Fetching Consul...
cd /tmp/
wget https://releases.hashicorp.com/consul/0.7.0/consul_0.7.0_linux_amd64.zip
wget https://releases.hashicorp.com/consul/0.7.0/consul_0.7.0_web_ui.zip
unzip consul_0.7.0_web_ui.zip
unzip consul_0.7.0_linux_amd64.zip
echo Installing Consul...
rm *.zip
sudo chmod +x consul
mv consul /usr/bin/

echo Preparing UI...
mkdir -p /home/consul/www
mv index.html /home/consul/www/
mv static/ /home/consul/www/

adduser consul
mkdir /var/consul
chown consul:consul /var/consul

mkdir -p /etc/consul.d/{server,bootstrap}

cat <<EOF > /etc/consul.d/server/config.json 
{
    "advertise_addr": "192.168.50.5",
    "bootstrap": false,
    "server": true,
    "datacenter": "pod1",
    "data_dir": "/var/consul",
    "encrypt": "uGZiPgGs7JLhYTEOk2QgOw==",
    "log_level": "INFO",
    "enable_syslog": true,
    "ui_dir": "/home/consul/www"
}
EOF

#Create Service file and config
cat <<EOF > /etc/sysconfig/consul
CONFIG_DIR="/etc/consul.d/server"
EXTRA_COMMAND="-rejoin -bootstrap-expect 3"
SERVER="-server"
EOF

cat <<EOF > /etc/systemd/system/consul.service
[Unit]
Description=consul agent
Requires=network-online.target
After=network-online.target

[Service]
EnvironmentFile=-/etc/sysconfig/consul
Environment=GOMAXPROCS=2
Restart=on-failure
ExecStart=/usr/bin/consul agent $SERVER -config-dir=$CONFIG_DIR $EXTRA_COMMAND
ExecReload=/bin/kill -HUP $MAINPID
KillSignal=SIGTERM

[Install]
WantedBy=multi-user.target
EOF

yum install bind-utils

SCRIPT

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "bento/centos-7.2"

  config.vm.provision "shell", inline: $script

  config.vm.define "s1" do |s1|
      s1.vm.hostname = "s1"
      s1.vm.network "private_network", ip: "192.168.50.5"
      s1.vm.network :forwarded_port, guest: 8500, host: 8503
      s1.vm.hostname = "test.uimirror.consul.server1"
  end

  config.vm.define "s2" do |s2|
      s2.vm.hostname = "s2"
      s2.vm.network "private_network", ip: "192.168.50.6"
      s2.vm.network :forwarded_port, guest: 8500, host: 8502
      s2.vm.hostname = "test.uimirror.consul.server2"
  end
  config.vm.define "s3" do |s3|
      s3.vm.hostname = "s3"
      s3.vm.network "private_network", ip: "192.168.50.7"
      s3.vm.network :forwarded_port, guest: 8500, host: 8501
      s3.vm.hostname = "test.uimirror.consul.server3"
  end
  config.vm.define "a1" do |a1|
      a1.vm.hostname = "a1"
      a1.vm.network "private_network", ip: "192.168.50.8"
      a1.vm.network :forwarded_port, guest: 8500, host: 8500
      a1.vm.network :forwarded_port, guest: 80, host: 8505
  end
  config.vm.define "a2" do |a2|
      a2.vm.hostname = "a2"
      a2.vm.network "private_network", ip: "192.168.50.9"
      a2.vm.network :forwarded_port, guest: 8500, host: 8504
      a2.vm.network :forwarded_port, guest: 80, host: 8506
  end
  config.vm.define "a3" do |a3|
      a3.vm.hostname = "a3"
      a3.vm.network "private_network", ip: "192.168.50.11"
      a3.vm.network :forwarded_port, guest: 8500, host: 8505
      a3.vm.network :forwarded_port, guest: 80, host: 8507
      a3.vm.network :forwarded_port, guest: 8181, host: 8508
      a3.vm.synced_folder ".", "/dev/workspace"
      a3.vm.hostname = "test.uimirror.consul.client"
  end
end
