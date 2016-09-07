#!/usr/bin/python3
import sys
from time import sleep
from functions import *

class Logger(object):
    def __init__(self):
        self.terminal = sys.stdout
        self.log = open("/var/log/smartserver.log", "a")

    def write(self, message):
        self.terminal.write(message)
        self.log.write(message)
        
sys.stdout = Logger()

sys.stdout = Logger()
cmd = sys.argv[1]
cmdlist = cmd.split('_')
addinstance(cmd)
def runcmds(cmdlist):
    for cmd in cmdlist:
        cmdsplit = cmd.split('-')
        if cmdsplit[0] == "repeat":
            del cmdsplit[0]
            cmd = '-'.join(cmdsplit)
        runcmd(cmd,kill=0)

if cmdlist[0].split('-')[0] == 'repeat':
    while True:
        runcmds(cmdlist)
else:
    runcmds(cmdlist)