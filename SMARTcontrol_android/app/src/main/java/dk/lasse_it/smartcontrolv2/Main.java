package dk.lasse_it.smartcontrolv2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dk.lasse_it.smartcontrol.Alarmview;
import dk.lasse_it.smartcontrol.Light;
import dk.lasse_it.smartcontrol.Musicplayer;
import dk.lasse_it.smartcontrol.R;
import dk.lasse_it.smartcontrol.Settings;

public class Main extends Activity {
    int error = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        /*
        Error codes:
        Main = 1x
        First time setup lightsjson = 11
        First time setup servers = 12

        Shared prefs:
        lights = info about controls in the LIGHT part
        servers = all servers in a json array
        */
        ////////set vars from sharedprefs////////
        JSONArray lightcat = new JSONArray();
        JSONObject lightsjson = new JSONObject();
        try {
            for (int i = 0; i < 5; i++) {//i = slide 0-4
                JSONArray btn = new JSONArray();
                for (int a = 0; a < 3; a++) { //a = button 0,1,2
                    JSONObject btnsub = new JSONObject();
                    switch (a) {
                        case 0: //btn1
                            switch (i) {
                                case 0:
                                    btnsub.put("title", "ON");
                                    btnsub.put("cmd", "high-17-22-27");
                                    break;
                                case 1:
                                    btnsub.put("title", "ON");
                                    btnsub.put("cmd", "high-27");
                                    break;
                                case 2:
                                    btnsub.put("title", "ON");
                                    btnsub.put("cmd", "high-17");
                                    break;
                                case 3:
                                    btnsub.put("title", "ON");
                                    btnsub.put("cmd", "high-22");
                                    break;
                                case 4:
                                    btnsub.put("title", "STRIPE");
                                    btnsub.put("cmd", "effects-stripe-0.2-17-22-27");
                                    break;
                            }
                            break;

                        case 1: //btn2
                            switch (i) {
                                case 0:
                                    btnsub.put("title", "BLINK");
                                    btnsub.put("cmd", "blink-17-22-27");
                                    break;
                                case 1:
                                    btnsub.put("title", "BLINK");
                                    btnsub.put("cmd", "blink-27");
                                    break;
                                case 2:
                                    btnsub.put("title", "BLINK");
                                    btnsub.put("cmd", "blink-17");
                                    break;
                                case 3:
                                    btnsub.put("title", "BLINK");
                                    btnsub.put("cmd", "blink-22");
                                    break;
                                case 4:
                                    btnsub.put("title", "SOUND ON");
                                    btnsub.put("cmd", "high-23");
                                    break;
                            }
                            break;

                        case 2: //btn3
                            switch (i) {
                                case 0:
                                    btnsub.put("title", "OFF");
                                    btnsub.put("cmd", "low-17-22-27");
                                    break;
                                case 1:
                                    btnsub.put("title", "OFF");
                                    btnsub.put("cmd", "low-27");
                                    break;
                                case 2:
                                    btnsub.put("title", "OFF");
                                    btnsub.put("cmd", "low-17");
                                    break;
                                case 3:
                                    btnsub.put("title", "OFF");
                                    btnsub.put("cmd", "low-22");
                                    break;
                                case 4:
                                    btnsub.put("title", "SOUND OFF");
                                    btnsub.put("cmd", "low-23");
                                    break;
                            }
                            break;
                    }

                    btnsub.put("serverip", 0);
                    btn.put(a, btnsub);
                }
                JSONObject lightcatsub = new JSONObject();
                switch (i) {
                    case 0:
                        lightcatsub.put("title", "ALL LIGHT's");
                        break;
                    case 1:
                        lightcatsub.put("title", "BED LIGHT");
                        break;
                    case 2:
                        lightcatsub.put("title", "CENTER LIGHT");
                        break;
                    case 3:
                        lightcatsub.put("title", "PC LIGHT");
                        break;
                    case 4:
                        lightcatsub.put("title", "OTHER");
                        break;
                }
                lightcatsub.put("btn",btn);
                lightcat.put(i,lightcatsub);
            }
            lightsjson.put("lightcat", lightcat);
        } catch (JSONException e) {
            e.printStackTrace();
            error = 11;
        }
        String lights = getString("lights", String.valueOf(lightsjson));
        if (lights.equals(String.valueOf(lightsjson))) {
            setString("lights", lights);
        }
        JSONArray serversjson = new JSONArray();
        JSONObject serversub = new JSONObject();
        JSONObject serverports = new JSONObject();
        try {
            serversub.put("url", "10.0.1.106"); //ip address to of gateway or server
            serversub.put("ssl",0); //should i use https 0/1
            serversub.put("gateway",0); //is this a gateway 0/1
            serversub.put("gwpassword",""); //password for the gateway
            serversub.put("gwurl",""); //url the gateway should contact
            serverports.put("17", "BED LIGHT");
            serverports.put("22", "PC LIGHT");
            serverports.put("23", "SPEAKERS");
            serverports.put("27", "CENTER LIGHT");
            serversub.put("ports",serverports); //all ports with port BCM_GPIO number and name
            serversjson.put(serversub);
            serversub = new JSONObject();
            serverports = new JSONObject();
            serversub.put("url", "10.0.1.107"); //ip address to of gateway or server
            serversub.put("ssl",0); //should i use https 0/1
            serversub.put("gateway",0); //is this a gateway 0/1
            serversub.put("gwpassword",""); //password for the gateway
            serversub.put("gwurl",""); //url the gateway should contact
            serverports.put("23","FRONT GARDEN");
            serverports.put("24", "BACK GARDEN");
            serversub.put("ports", serverports); //all ports with port BCM_GPIO number and name
            serversjson.put(serversub);
        } catch (JSONException e) {
            e.printStackTrace();
            error = 12;
        }
        String servers = getString("servers", String.valueOf(serversjson));
        if (servers.equals(String.valueOf(serversjson))) {
            setString("servers", servers);
        }
        //g.setDefalarmcmd(getString("alarmcmd", "high-17-22-27"));
        //g.setDefplaylistname(getString("playlistname", "electroNOW by spotify"));
        //g.setDefplaylisturi(getString("playlisturi","spotify:user:spotify:playlist:1GQLlzxBxKTb6tJsD4RxHI"));
    }


    public void first1(View view) {
        Intent a = new Intent(getBaseContext(), Light.class);
        startActivity(a);
    }

    public void first2(View view) {
        Intent b = new Intent(getBaseContext(), Alarmview.class);
        startActivity(b);
    }
    public void first3(View view) {
        Intent c = new Intent(getBaseContext(), Musicplayer.class);
        startActivity(c);
    }
    public void first4(View view) {
        Intent d = new Intent(getBaseContext(), Settings.class);
        startActivity(d);
    }
    public String getString (String key, String defvalue) {
        SharedPreferences sharedPref = getSharedPreferences("dk.lasse_it.smartcontrol", MODE_PRIVATE);
        return sharedPref.getString(key, defvalue);
    }
    public int getInt (String key, int defvalue) {
        SharedPreferences sharedPref = getSharedPreferences("dk.lasse_it.smartcontrol", MODE_PRIVATE);
        return sharedPref.getInt(key, defvalue);
    }
    public void setString(String key,String value){
        SharedPreferences sharedPref=getSharedPreferences("dk.lasse_it.smartcontrol",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putString(key,value);
        editor.apply();
    }
}