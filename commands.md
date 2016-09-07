/controlv2.php?
---------------------
cmd=info //shows version, required client version,
cmd=ports //shows list of ports and port names.
cmd=ports&add=23&name=bed //add port 23 to the portlist under the name bed
cmd=ports&rm=23 //removes port 23 from the portlist
cmd=high-24-21-20 // turns on port 24, 21 and 20
cmd=low-24-21-20 // turns off port 24, 21 and 20
cmd=blink-24-21-20 // blinks with port 24, 21 and 20
cmd=sleep-200 //sleeps for 200 seconds, this is useful when sending a tasklist
cmd=effects-stripe-0.2-24-21-20 //turns on port 24 for 0.2 seconds and off again, then port 21...
r=0 // disables redirect to /index.php
cmd=high-23_sleep-30_low-23 //this is a tasklist, it will turn on port 23 for 30 seconds then off
cmd=play //play
cmd=play-"spotify:user:spotify_denmark:playlist:6CLoLp8PXsrTamLWX3OlnY" //play spotify playlist
cmd=pause //pause music
cmd=next //next song
cmd=volume-50 //set volume to 50%
cmd=alarm&alarm=list //shows all alarms
cmd=alarm&alarm=remove-4295 //removes alarm with id 4295
cmd=alarm&alarm=add&year=2015&month=12&day=24&hour=13&minute=13&weekday=2&repeat=year-10&exec=high-24_sleep-200_low-24 //turn on 24 for 200 seconds then off at 13:13 December 24 2015,2025,2035...
cmd=state //show state of all buttons
