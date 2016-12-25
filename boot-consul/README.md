--For Git2Consul
yum install npm
npm install -g git2consul
sudo yum -y install git

cat <<EOF > /tmp/git2consul.json
{
  "version": "1.0",
  "repos" : [{
    "name" : "config",
    "source_root": "config",
    "ignore_file_extension" : true,
    "expand_keys": true,
    "url" : "https://github.com/jayarampradhan/consul_kv_store.git",
    "branches" : ["master"],
    "hooks": [{
      "type" : "polling",
      "interval" : "1"
    }]
  }]
}
EOF


git2consul --config-file /tmp/git2consul.json
