#!/usr/bin/python3
import datetime, sys
from functions import *
from time import sleep

class Logger(object):
    def __init__(self):
        self.terminal = sys.stdout
        self.log = open("/var/log/smartserver.log", "a")

    def write(self, message):
        self.terminal.write(message)
        self.log.write(message)
        
sys.stdout = Logger()

year = sys.argv[1]
cmd = sys.argv[2]
time = datetime.datetime.now()
spotify = ""
if len(sys.argv) == 4:
    spotify = sys.argv[3]

if year == str(time.year):
    runcmd(cmd)
    if spotify != "":
        music_consume()
        sleep(0.5)
        music_random_set("true")
        sleep(0.5)
        music_clear()
        sleep(0.5)
        music_add(spotify)
        sleep(0.5)
        music_play()
        sleep(0.5)
        music_next()