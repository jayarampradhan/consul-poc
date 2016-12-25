default['consul']['version']              = '0.7.0'
default['consul']['is_bootstrap']         = false
default['consul']['is_server']            = true
default['consul']['bootstrap_expect']     = 3
default['consul']['leave_on_terminate']   = true
default['consul']['datacenter']           = 'pod1'
default['consul']['data_directory']       = '/var/consul'
default['consul']['retry_join_ips']       = []
default['consul']['log_level']            = 'INFO'
default['consul']['enable_syslog']        = true
default['consul']['has_ui']               = false
default['consul']['ui_dir']               = '/home/consul/www'
default['consul']['client_addr']          = '0.0.0.0'
default['consul']['services']             = {"consul"=>nil}


default['git2consul']['conf_version']     = "1.0"


