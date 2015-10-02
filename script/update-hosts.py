#!/bin/python
from __future__ import print_function
import subprocess
import sys
import os
import json
from util import appendline

if __name__ == "__main__":
	path = os.path.dirname(os.path.realpath(__file__))
	config = json.load(open(path+'/cluster-config.json'));
	for node in config['nodes']:
		appendline('/etc/hosts', node['ip']+'\t'+node['host'])

	appendline('/etc/hosts', '192.168.1.4\t'+'zoo1')