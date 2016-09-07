package dk.lasse_it.smartcontrol;

import android.view.View;
import android.widget.ArrayAdapter;

import org.json.JSONObject;

public class Globals {
    private static Globals instance;
    public Globals(){}

    //minute
    private int minute;
    public void setMinute(int d){
        this.minute=d;
    }
    public int getMinute(){
        return this.minute;
    }

    //hour
    private int hour;
    public void setHour(int d){
        this.hour=d;
    }
    public int getHour(){
        return this.hour;
    }

    //day
    private int day;
    public void setDay(int d){
        this.day=d;
    }
    public int getDay(){
        return this.day;
    }

    //month
    private int month;
    public void setMonth(int d){
        this.month=d;
    }
    public int getMonth(){
        return this.month;
    }

    //year
    private int year;
    public void setYear(int d){
        this.year=d;
    }
    public int getYear(){
        return this.year;
    }

    //month as text
    private String monthastext;
    public void setMonthastext(String d){
        this.monthastext=d;
    }
    public String getMonthastext(){
        return this.monthastext;
    }

    //hour as text
    private String hourastext;
    public void setHourastext(String d){
        this.hourastext=d;
    }
    public String getHourastext(){
        return this.hourastext;
    }

    //minute as text
    private String minuteastext;
    public void setMinuteastext(String d){
        this.minuteastext=d;
    }
    public String getMinuteastext(){
        return this.minuteastext;
    }

    //playlisturi
    private String playlisturi;
    public void setPlaylisturi(String d){
        this.playlisturi=d;
    }
    public String getPlaylisturi(){
        return this.playlisturi;
    }

    //playlistname
    private String playlistname;
    public void setPlaylistname(String d){
        this.playlistname=d;
    }
    public String getPlaylistname(){
        return this.playlistname;
    }

    //playlists
    private JSONObject playlists;
    public void setPlaylists(JSONObject d) {
        this.playlists=d;
    }
    public JSONObject getPlaylists() {
        return this.playlists;
    }

    private JSONObject alarms;
    public void setAlarms(JSONObject d) {
        this.alarms=d;
    }
    public JSONObject getAlarms() {
        return this.alarms;
    }

    //alarm server ip
    private String alarmserverip;
    public void setAlarmserverip(String d){
        this.alarmserverip=d;
    }
    public String getAlarmserverip(){
        return this.alarmserverip;
    }

    //alarm cmd
    private String alarmcmd;
    public void setAlarmcmd(String d){
        this.alarmcmd=d;
    }
    public String getAlarmcmd(){
        return this.alarmcmd;
    }

    //alarm to add
    private String EditAlarm;
    public void setEditAlarm(String d) { this.EditAlarm=d; }

    public String getEditAlarm() {
        return this.EditAlarm;
    }

    //alarmid
    private String alarmid;
    public void setAlarmid(String d){
        this.alarmid=d;
    }
    public String getAlarmid(){
        return this.alarmid;
    }

    //GPIO port where speakers is connected
    private int soundgpio;
    public void setSoundgpio(int d){
        this.soundgpio=d;
    }
    public int getSoundgpio(){
        return this.soundgpio;
    }

    //Current slide number
    private int slidenum;
    public void setSlidenum(int d){
        this.slidenum=d;
    }
    public int getSlidenum(){
        return this.slidenum;
    }

    //Lighttab info
    private JSONObject lights;
    public void setLights(JSONObject d){
        this.lights=d;
    }
    public JSONObject getLights(){
        return this.lights;
    }

    //serverip
    private String serverip;
    public void setServerip(String d){
        this.serverip=d;
    }
    public String getServerip(){
        return this.serverip;
    }

    //get current void
    private String current;
    public void setCurrent(String d){
        this.current=d;
    }
    public String getCurrent(){
        return this.current;
    }

    public View promptView;
    public View getpView() {
        return this.promptView;
    }
    public void setpView(View d) {
        this.promptView=d;
    }

    //Is light checked
    private int lightchecked;
    public void setLightchecked(int d){
        this.lightchecked=d;
    }
    public int getLightchecked(){
        return this.lightchecked;
    }

    //Is sound checked
    private int soundchecked;
    public void setSoundchecked(int d){
        this.soundchecked=d;
    }
    public int getSoundchecked(){
        return this.soundchecked;
    }

    //alarm cmd
    private String defalarmcmd;
    public void setDefalarmcmd(String d){
        this.defalarmcmd=d;
    }
    public String getDefalarmcmd(){
        return this.defalarmcmd;
    }

    //default playlist uri
    private String defplaylisturi;
    public void setDefplaylisturi(String d){
        this.defplaylisturi=d;
    }
    public String getDefplaylisturi(){
        return this.defplaylisturi;
    }

    //default playlist name
    private String defplaylistname;
    public void setDefplaylistname(String d){
        this.defplaylistname=d;
    }
    public String getDefplaylistname(){
        return this.defplaylistname;
    }

    //Is deflight checked
    private int deflightchecked;
    public void setDeflightchecked(int d){
        this.deflightchecked=d;
    }
    public int getDeflightchecked(){
        return this.deflightchecked;
    }

    //Is defsound checked
    private int defsoundchecked;
    public void setDefsoundchecked(int d){
        this.defsoundchecked=d;
    }
    public int getDefsoundchecked(){
        return this.defsoundchecked;
    }

    private String[] list;
    public void setList(String[] d){
        this.list=d;
    }
    public String[] getList(){
        return this.list;
    }

    private ArrayAdapter<String> adapter;
    public void setAdapter(ArrayAdapter d){
        this.adapter=d;
    }
    public ArrayAdapter getAdapter(){
        return this.adapter;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}

