# Cookbook Name:: consul
# Recipe:: consul app
# Author:: Jayaram
# This will generate all the need application json, along with application start
#
# Copyright (C) 2016 Intuit
#
# All rights reserved - Do Not Redistribute
#
#Prepare the Consul installation
log 'consul_download_msg' do
  message "Going to Download Consul"
  level :info
end
unless File.exist? "/usr/local/bin/consul"
  remote_file "#{Chef::Config[:file_cache_path]}/consul_#{node[:consul][:version]}.zip" do
    source "https://releases.hashicorp.com/consul/#{node[:consul][:version]}/consul_#{node[:consul][:version]}_linux_amd64.zip"
    notifies :run, "bash[install_consul]", :immediately
  end
end

bash "install_consul" do
  user "root"
  cwd "#{Chef::Config[:file_cache_path]}"
  code <<-EOH
    unzip consul_#{node[:consul][:version]}.zip
    rm consul_#{node[:consul][:version]}.zip
    chmod +x consul
    mv consul /usr/local/bin/
    adduser consul
    mkdir /var/consul
    chown consul:consul /var/consul
    mkdir -p /etc/consul.d/{server,bootstrap}
  EOH
  action :nothing
end


if node['consul']['has_ui']
  log 'consul_ui_download_msg' do
    message "Going to Download Consul Web UI"
    level :info
  end
  unless Dir.exist? "#{node[:consul][:ui_dir]}"
    remote_file "#{Chef::Config[:file_cache_path]}/consul_ui/consul_#{node[:consul][:version]}.zip" do
      source "https://releases.hashicorp.com/consul/#{node[:consul][:version]}/consul_#{node[:consul][:version]}_web_ui.zip"
      notifies :run, "bash[install_consul_ui]", :immediately
    end
  end
end

bash "install_consul_ui" do
  user "root"
  cwd "#{Chef::Config[:file_cache_path]}/consul_ui"
  code <<-EOH
    unzip consul_#{node[:consul][:version]}.zip
    rm consul_#{node[:consul][:version]}.zip
    mkdir -p #{node[:consul][:ui_dir]}
    mv index.html #{node[:consul][:ui_dir]}
    mv static/ #{node[:consul][:ui_dir]}
  EOH
  action :nothing
end

log 'consul_configuration_generation_msg' do
  message "Going to Generate Consul Configs"
  level :info
end

ruby_block 'evaluate consul key' do
  block do
    APPLICATION_JSON_RAW = IO.read('/dev/shm/secrets/application.json').strip
    APPLICATION_JSON=JSON.parse(APPLICATION_JSON_RAW)
    CONSUL_ENC_KEY="#{APPLICATION_JSON['consul']['key']}"
    node.default[:consul][:key] = "#{CONSUL_ENC_KEY}"
  end
end

template "#{node[:consul][:config_dir]}/config.json" do
  source "config.json.erb"
end

template "/etc/systemd/system/consul.service" do
  source "consul.service.erb"
end

log 'consul_start_message' do
  message "Going to start consul"
  level :info
end

service "consul" do
  supports :status => true, :restart => true, :reload => true
  action [:start]
end

if node['consul']['join_ip']
  bash "consul_join_attempt" do
    code <<-EOH
    consul join #{node['consul']['join_ip']}
    EOH
  end
end

