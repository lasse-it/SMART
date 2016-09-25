package dk.lasse_it.smartcontrol;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main extends Activity {
    Globals g = Globals.getInstance();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        ////////set vars from sharedprefs////////
        JSONArray lightcat = new JSONArray();
        JSONObject lights = new JSONObject();
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

                    btnsub.put("serverip", "10.0.1.106");
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
            lights.put("lightcat", lightcat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            g.setLights(new JSONObject(getString("lights", String.valueOf(lights))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        g.setServerip(getString("serverip", "10.0.1.106"));
        g.setPlaylisturi("spotify:user:spotify:playlist:1GQLlzxBxKTb6tJsD4RxHI");
        g.setSoundgpio(getInt("soundgpio", 23));
        g.setAlarmserverip(getString("alarmserverip", "10.0.1.106"));
        g.setDefalarmcmd(getString("alarmcmd", "high-17-22-27"));
        g.setDefplaylistname(getString("playlistname", "electroNOW by spotify"));
        g.setDefplaylisturi(getString("playlisturi","spotify:user:spotify:playlist:1GQLlzxBxKTb6tJsD4RxHI"));
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
        Intent c = new Intent(getBaseContext(), Musicplayer_web.class);
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
}