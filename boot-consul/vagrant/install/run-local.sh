#!/usr/bin/env bash

cd /dev/workspace/chef/consul

berks vendor /dev/workspace/target/chef/vendor/cookbooks

mkdir -p /tmp/chef/conf/

cat <<EOF > /tmp/chef/conf/client.rb
cookbook_path ["/dev/workspace/target/chef/vendor/cookbooks"]
role_path '/dev/workspace/target/chef/vendor/cookbooks/consul/roles'
environment_path '/dev/workspace/target/chef/vendor/cookbooks/consul/environments'
environment 'dev'
local_mode 'true'
node_path '/dev/workspace/target/chef/vendor/cookbooks/consul/nodes'
node_name 'dev-node-consul'
log_level :info
log_location STDOUT
EOF

chef-client -z -c /tmp/chef/conf/client.rb -j /tmp/instance_meta.json