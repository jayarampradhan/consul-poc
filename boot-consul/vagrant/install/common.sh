#!/usr/bin/env bash
echo "Installing dependencies..."
yum -y update
yum -y install unzip wget
yum -y install unzip curl

wget --directory-prefix="/home/vagrant/dev/tools/" https://packages.chef.io/stable/el/7/chef-12.8.1-1.el7.x86_64.rpm
wget --directory-prefix="/home/vagrant/dev/tools/" https://packages.chef.io/stable/el/7/chefdk-0.15.15-1.el7.x86_64.rpm
cd /home/vagrant/dev/tools/
rpm -Uvh chef-12.8.1-1.el7.x86_64.rpm
rpm -Uvh chefdk-0.15.15-1.el7.x86_64.rpm

yum -y install git
yum -y install java-1.8.0-openjdk-devel