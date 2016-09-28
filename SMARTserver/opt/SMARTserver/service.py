#!/usr/bin/python3

import http.server
import ssl
import html
import RPi.GPIO as GPIO
import os.path
from functions import *
import urllib.request
import sys

class Logger(object):
    def __init__(self):
        self.terminal = sys.stdout
        self.log = open("/var/log/smartserver.log", "a")

    def write(self, message):
        self.terminal.write(message)
        self.log.write(message)
        
sys.stdout = Logger()
sys.stderr = Logger()

class HTTPRequestHandler(http.server.BaseHTTPRequestHandler):
  
  def do_GET(self):
    rootdir = '/opt/SMARTserver/html'
    try:
        args = self.path.split('?')
        if len(args) > 1:
            args = args[1].split('&')
        r=1
        user=""
        password=""
        dayofweek = '*'
        for arg in args:
            if arg.startswith("user="):
                user = arg.split('=')[1]
            elif arg.startswith("pass="):
                password = arg.split('=')[1]
        if check_login(user,password):
            for arg in args:
                print ("Got argument: "+arg)
                if len(arg.split('-')) > 2:
                    c = arg.split('-')[2]
                    b = arg.split('-')[1]
                elif len(arg.split('-')) > 1:
                    b = arg.split('-')[1]
                
                if arg.startswith("cmd="):
                    cmd = arg.split('=')[1]
                elif arg.startswith("r"):
                    r=0
                elif arg.startswith("year="):
                    year = arg.split('=')[1]
                elif arg.startswith("month="):
                    month = arg.split('=')[1]
                elif arg.startswith("day="):
                    day = arg.split('=')[1]
                elif arg.startswith("dayofweek="):
                    if len(arg) > 1:
                        dayofweek = arg.split('=')[1]
                elif arg.startswith("hour="):
                    hour = arg.split('=')[1]
                elif arg.startswith("minute="):
                    minute = arg.split('=')[1]
                elif arg.startswith("spotify="):
                    spotify = arg.split('=')[1]
                elif arg.startswith("music_pause"):
                    o = music_pause()
                elif arg.startswith("music_add"):
                    o = music_add(b)
                elif arg.startswith("music_clear"):
                    o = music_clear()
                elif arg.startswith("music_next"):
                    o = music_next()
                elif arg.startswith("music_previous"):
                    o = music_previous()
                elif arg.startswith("music_volume_set"):
                    o = music_volume_set(b)
                elif arg.startswith("music_volume_get"):
                    o = music_volume_get()
                elif arg.startswith("music_state"):
                    o = music_state()
                elif arg.startswith("music_song"):
                    o = music_song()
                elif arg.startswith("music_time_get"):
                    o = music_time_get()
                elif arg.startswith("music_time_set"):
                    o = music_time_set(b)
                elif arg.startswith("music_playlists_get"):
                    o = music_playlists_get()
                elif arg.startswith("music_playlist_get"):
                    o = music_playlist_get(b)
                elif arg.startswith("music_play"):
                    o = music_play()
                elif arg.startswith("music_random_set"):
                    o = music_random_set(b)
                elif arg.startswith("music_random_get"):
                    o = music_random_get()
                elif arg.startswith("music_queue_get"):
                    o = music_queue_get()
                elif arg.startswith("auth_set_login") or arg.startswith("auth_add_login"):
                    o = add_login(b,c).encode('ascii')
                elif arg.startswith("auth_del_login"):
                    o = del_login(b).encode('ascii')
                elif arg.startswith("auth_check_login"):
                    o = "success".encode('ascii')
                elif arg.startswith("gpio="):
                    gpio = arg.split('=')[1]
                elif arg.startswith("name="):
                    name = arg.split('=')[1]
                elif arg.startswith("type="):
                    type = arg.split('=')[1]
                elif arg.startswith("location="):
                    location = arg.split('=')[1]
            if self.path is '/' or self.path.startswith('/index'):
                f = open(rootdir + '/index.html')
                fout = f.read()
                host = self.headers["Host"].split(":")[0];
                fout = fout.replace("$hostname", host)
                self.send_response(200)
                self.send_header('Content-type','text-html')
                self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                self.send_header("Pragma", "no-cache")
                self.send_header("Expires", "0")
                self.end_headers()
                self.wfile.write(fout.encode('ascii'))
                f.close()
            elif self.path.endswith('.html'):
                f = open(rootdir + self.path)
                fout = f.read()
                self.send_response(200)
                self.send_header('Content-type','text-html')
                self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                self.send_header("Pragma", "no-cache")
                self.send_header("Expires", "0")
                self.end_headers()
                self.wfile.write(fout.encode('ascii'))
                f.close()
            elif self.path.startswith("/controlv2.php"):
                if r == 1:
                    self.send_response(301)
                    self.send_header('Location', 'index.html')
                    self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                    self.send_header("Pragma", "no-cache")
                    self.send_header("Expires", "0")
                    self.end_headers()
                else:
                    self.send_response(200)
                    self.send_header('Content-type','text-html')
                    self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                    self.send_header("Pragma", "no-cache")
                    self.send_header("Expires", "0")
                    self.end_headers()
                f = runcmd(cmd)
                if f == "" and r == 0:
                    f = "200"
                self.wfile.write(f.encode('ascii'))
            elif self.path.startswith("/alarm/add.php"):
                alarm_add(year,month,day,hour,minute,cmd,spotify,dayofweek)
                if r == 1:
                    self.send_response(301)
                    self.send_header('Location', '/alarm/index.html')
                    self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                    self.send_header("Pragma", "no-cache")
                    self.send_header("Expires", "0")
                    self.end_headers()
                else:
                    self.send_response(200)
                    self.send_header('Content-type','text-html')
                    self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                    self.send_header("Pragma", "no-cache")
                    self.send_header("Expires", "0")
                    self.end_headers()
                f = ""
                if f == "" and r == 0:
                    f = "200"
                self.wfile.write(f.encode('ascii'))
            elif self.path.startswith("/alarm/delete.php"):
                alarm_del(year,month,day,hour,minute,dayofweek)
                if r == 1:
                    self.send_response(301)
                    self.send_header('Location', '/alarm/index.html')
                    self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                    self.send_header("Pragma", "no-cache")
                    self.send_header("Expires", "0")
                    self.end_headers()
                else:
                    self.send_response(200)
                    self.send_header('Content-type','text-html')
                    self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                    self.send_header("Pragma", "no-cache")
                    self.send_header("Expires", "0")
                    self.end_headers()
                f = ""
                if f == "" and r == 0:
                    f = "200"
                self.wfile.write(f.encode('ascii'))
            elif self.path.startswith("/alarm/list.php"):
                self.send_response(200)
                self.send_header('Content-type','text-html')
                self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                self.send_header("Pragma", "no-cache")
                self.send_header("Expires", "0")
                self.end_headers()
                self.wfile.write(alarm_list_json().encode('ascii'))
            elif self.path.startswith("/music.php"):
                music_consume()
                self.send_response(200)
                self.send_header('Content-type','text-html')
                self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                self.send_header("Pragma", "no-cache")
                self.send_header("Expires", "0")
                self.end_headers()
                self.wfile.write(o)
            elif self.path.startswith("/auth.php"):
                self.send_response(200)
                self.send_header('Content-type','text-html')
                self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                self.send_header("Pragma", "no-cache")
                self.send_header("Expires", "0")
                self.end_headers()
                self.wfile.write(o)
            elif self.path.startswith("/ports/list.php"):
                self.send_response(200)
                self.send_header('Content-type','text-html')
                self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                self.send_header("Pragma", "no-cache")
                self.send_header("Expires", "0")
                self.end_headers()
                ports = list_ports()
                if ports == "":
                    ports = "empty"
                self.wfile.write(ports.encode('ascii'))
            elif self.path.startswith("/ports/add.php"):
                self.send_response(200)
                self.send_header('Content-type','text-html')
                self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                self.send_header("Pragma", "no-cache")
                self.send_header("Expires", "0")
                self.end_headers()
                self.wfile.write(add_port(gpio,name,type,location).encode('ascii'))
            elif self.path.startswith("/ports/delete.php"):
                self.send_response(200)
                self.send_header('Content-type','text-html')
                self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
                self.send_header("Pragma", "no-cache")
                self.send_header("Expires", "0")
                self.end_headers()
                self.wfile.write(del_port(gpio).encode('ascii'))
            else:
                self.send_error(404, 'file not found')
                self.end_headers()
        else:
            self.send_error(403, 'Access denied')
            self.end_headers()
        return
    except IOError:
        self.send_error(404, 'file not found')

class HTTPRedirector(http.server.BaseHTTPRequestHandler):
  
  def do_GET(self):
    self.send_response(301)
    self.send_header('Location', "https://"+self.headers["Host"].split(":")[0]+self.path)
    self.send_header("Cache-Control", "no-cache, no-store, must-revalidate")
    self.send_header("Pragma", "no-cache")
    self.send_header("Expires", "0")
    self.end_headers()
    rmessage = "Redirecting you to: https://"+self.headers["Host"].split(":")[0]+self.path
    self.wfile.write(rmessage.encode('ascii'))

def httpredirect():
    httpd = http.server.HTTPServer(('0.0.0.0', 80), HTTPRedirector)
    httpd.serve_forever()
    
def run():
    httpd = http.server.HTTPServer(('0.0.0.0', 443), HTTPRequestHandler)
    if os.path.isfile('/opt/SMARTserver/ssl.pem'):
        Thread(target = httpredirect).start()
        httpd.socket = ssl.wrap_socket (httpd.socket, certfile='/opt/SMARTserver/ssl.pem', server_side=True)
    else:
        httpd = http.server.HTTPServer(('0.0.0.0', 80), HTTPRequestHandler)
    print('http server is running...')
    httpd.serve_forever()
  
if __name__ == '__main__':
  run()