#!/usr/bin/python3
import RPi.GPIO as GPIO
import os, signal, subprocess
from threading import Thread
from time import sleep
import urllib.request
import json

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
def highlowf(port, state):
    if state == 1:
        GPIO.setup(port, GPIO.OUT)
        GPIO.output(port, True)
        
    if state == 0:
        GPIO.setup(port, GPIO.OUT)
        GPIO.output(port, False)
        GPIO.cleanup(port)
        
def getports(cmdinput):
    pins = set()
    cmdsedit = []
    cmds = cmdinput.split('_')
    for cmd in cmds:
        ports = cmd.split('-')
        if ports[0] == 'repeat':
            del ports[0]
        cmd = "-".join(str(i) for i in ports)
        if ports[0] != 'sleep':
            cmdsedit.append(cmd)
        for cmd in cmdsedit:
            ports = cmd.split('-')
            for port in ports:
                if port != 'repeat' and port != 'high' and port != 'low' and port != 'blink':
                    pins.add(int(port))
    return pins

def addinstance(cmd):
    pid = os.getpid()
    pins = set([pid]);
    pins.update(getports(cmd))
    pinsstr = "-".join(str(i) for i in sorted(pins,reverse=True))
    with open("/opt/SMARTserver/instance.list", "a") as file:
        file.write(pinsstr + '\n')
        
def killinstance(cmd):
    for port in getports(cmd):
        delline = ""
        lines = []
        with open('/opt/SMARTserver/instance.list', 'r') as f:
            for line in f:
                lines.append(line)
            for line in lines:
                linelist = line.split("-")
                for entry in linelist:
                    if port == int(entry):
                        print("killing PID "+linelist[0])
                        subprocess.call(["kill", linelist[0]])
                        delline = line
        with open('/opt/SMARTserver/instance.list','w') as f:
            for line in lines:
                if line != delline:
                    f.write(line)
            f.close
            
def instance(cmd):
    subprocess.call( ["python3", "/opt/SMARTserver/instance.py", cmd] )

def runcmd(cmd, kill=1):
    cmdsend = ""
    if cmd[:5] == "state":
            portarr = cmd.split("-")
            for port in portarr:
                if port != "state":
                    portstate = GPIO.gpio_function(int(port))
                    portstatef = "3"
                    if portstate == 1:
                        portstatef = "0"
                    elif portstate == 0:
                        portstatef = "1"
                    else:
                        portstatef = "3"
                        
                    cmdsend = cmdsend + port + "-" + portstatef + "-"
            cmdsend = cmdsend[:-1]
    elif '_' in cmd:
        if kill == 1:
            killinstance(cmd)
        Thread(target = instance, args = (cmd,)).start()
    elif cmd[:4] == "high":
        if kill == 1:
            killinstance(cmd)
        for port in cmd.split('-'):
            if port != 'high':
                highlowf(int(port), 1)
    elif cmd[:3] == "low":
        if kill == 1:
            killinstance(cmd)
        for port in cmd.split('-'):
            if port != 'low':
                highlowf(int(port), 0)
    elif cmd.startswith("sleep"):
        sleep(float(cmd.split('-')[1]))
    elif cmd[:5] == "blink":
        ports = "-".join(str(i) for i in getports(cmd))
        runcmd("repeat-high-"+ports+"_sleep-0.1_low-"+ports+"_sleep-0.1")
    return cmdsend

def check_login(username, password):
    valid = False
    with open('/opt/SMARTserver/login.list', 'r') as f:
        f.seek(0)
        fc = f.read(1)
        if not fc:
            valid = True
            print("Authentication is disabled, because no logins have been added")
        else:
            f.seek(0)
            for line in f:
                l = line.split('<->')
                if username == l[0]:
                    if password == l[1]:
                        valid = True
        f.close()
    if valid == True:
        print("login successful for user: "+username)
    else:
        print("login failed for user: "+username)
    return valid

def add_login(username, password):
    del_login(username,1)
    f = open('/opt/SMARTserver/login.list','a')
    f.write(username+"<->"+password)
    print("Added user: "+username)
    f.close()
    
def del_login(username,silent=0):
    delline = ""
    lines = []
    with open('/opt/SMARTserver/login.list', 'r') as f:
        for line in f:
            lineuser = line.split("<->")[0]
            if lineuser != username:
                    lines.append(line)
            else:
                if silent == 0:
                    print("Deleted user: "+lineuser)
        f.close()
    with open('/opt/SMARTserver/login.list','w') as f:
            for line in lines:
                if line != delline:
                    f.write(line)
            f.close

def music_request(json):
    jsonb = json.encode('utf8')
    return urllib.request.urlopen(urllib.request.Request('http://localhost:6680/mopidy/rpc', data=jsonb, headers={'Content-Type': 'application/json'})).read()

def music_play():
    json = '{"method": "core.playback.play", "jsonrpc": "2.0", "id": 1}'
    return music_request(json)

def music_pause():
    json = '{"method": "core.playback.pause", "jsonrpc": "2.0", "id": 1}'
    return music_request(json)

def music_add(song):
    json = '{"method": "core.tracklist.add", "jsonrpc": "2.0", "params": {"uri": "'+song+'"}, "id": 1}'
    return music_request(json)

def music_clear():
    json = '{"method": "core.tracklist.clear", "jsonrpc": "2.0", "id": 1}'
    return music_request(json)

def music_next():
    json = '{"method": "core.playback.next", "jsonrpc": "2.0", "id": 1}'
    return music_request(json)
    
def music_previous():
    json = '{"method": "core.playback.previous", "jsonrpc": "2.0", "id": 1}'
    return music_request(json)

def music_volume_set(volume):
    json = '{"method": "core.mixer.set_volume", "jsonrpc": "2.0", "params": {"volume": '+volume+'}, "id": 1}'
    return music_request(json)
    
def music_volume_get():
    json = '{"method": "core.mixer.get_volume", "jsonrpc": "2.0", "id": 1}'
    return music_request(json)

def music_state():
    json = '{"method": "core.playback.get_state", "jsonrpc": "2.0", "id": 1}'
    return music_request(json)

def music_song():
    json = '{"method": "core.playback.get_current_track", "jsonrpc": "2.0", "id": 1}'
    return music_request(json)
    
def music_time_get():
    json = '{"method": "core.playback.get_time_position", "jsonrpc": "2.0", "id": 1}'
    return music_request(json)

def music_time_set(time):
    json = '{"method": "core.playback.seek", "jsonrpc": "2.0", "params": {"time_position": '+time+'}, "id": 1}'
    return music_request(json)

def music_playlists_get():
    json = '{"method": "core.playlists.as_list", "jsonrpc": "2.0", "id": 1}'
    return music_request(json)
    
def music_playlist_get(playlist):
    json = '{"method": "core.playlists.get_items", "jsonrpc": "2.0", "params": {"uri": "'+playlist+'"}, "id": 1}'
    return music_request(json)

def music_consume(): #retest
    json = '{"method": "core.tracklist.set_consume", "jsonrpc": "2.0", "params": {"value": true}, "id": 1}'
    return music_request(json)

def music_random_set(random):
    json = '{"method": "core.tracklist.set_random", "jsonrpc": "2.0", "params": {"value": '+random+'}, "id": 1}'
    return music_request(json)
    
def music_random_get():
    json = '{"method": "core.tracklist.get_random", "jsonrpc": "2.0", "id": 1}'
    return music_request(json)

def music_queue_get():
    json = '{"method": "core.tracklist.get_tracks", "jsonrpc": "2.0", "id": 1}'
    return music_request(json)

def alarm_add(year,month,day,hour,minute,cmd,spotify):
    new_alarm = minute+' '+hour+' '+day+' '+month+' * root /opt/SMARTserver/alarm.py '+year+' '+cmd+' "'+spotify+'"'
    f = open('/etc/cron.d/smart','a')
    f.write(new_alarm + '\n')

def alarm_list():
    alarms = []
    for line in open('/etc/cron.d/smart'):
        alarms.append(line)    
    return alarms

def alarm_list_json():
    alarms = alarm_list()
    alarms_list = []
    for alarm in alarms:
        alarmlist = alarm.split(' ')
        minute = alarmlist[0]
        hour = alarmlist[1]
        day = alarmlist[2]
        month = alarmlist[3]
        year = alarmlist[7]
        cmd = alarmlist[8]
        spotify = ""
        if len(alarmlist) > 9:
            spotify = alarmlist[9]
        alarm_json = '{"spotify":'+spotify+', "cmd":"'+cmd+'",  "year":"'+year+'", "month":"'+month+'", "day":"'+day+'", "hour":"'+hour+'", "minute":"'+minute+'"}'
        alarms_list.append(alarm_json)
    return "{\"alarms\": ["+", ".join(alarms_list)+"]}"
        
def alarm_del(year,month,day,hour,minute):
    del_alarm = minute+' '+hour+' '+day+' '+month+' * root /opt/SMARTserver/alarm.py '+year
    alarms = alarm_list()
    f = open('/etc/cron.d/smart', 'w')
    for line in alarms:
        if not line.startswith(del_alarm):
            f.write(line)
            
            
            
    
    
    
