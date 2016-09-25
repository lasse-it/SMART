package dk.lasse_it.smartcontrol;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Musicplayer extends Activity {
    Globals g = Globals.getInstance();
    String songid = "nosongyet";
    TextView songtitle;
    TextView songalbum;
    TextView songartist;
    TextView songlength;
    TextView songcurrenttime;
    SeekBar timebar;
    SeekBar volumebar;
    ImageButton play;
    ImageButton pause;
    String playstate = "stopped";
    ImageView coverart;
    RequestQueue queue;
    String coveruri = "nocover";
    String coverapi = "nocover";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcplayer_player);
        getActionBar().setTitle("Now Playing");
        queue = Volley.newRequestQueue(this);
        songtitle = (TextView) Musicplayer.this.findViewById(R.id.songtitle);
        songalbum = (TextView) findViewById(R.id.songalbum);
        songartist = (TextView) findViewById(R.id.songartist);
        songlength = (TextView) findViewById(R.id.songlength);
        songcurrenttime = (TextView) findViewById(R.id.songcurrenttime);
        timebar = (SeekBar) findViewById(R.id.time);
        volumebar = (SeekBar) findViewById(R.id.volumebar);
        play = (ImageButton) findViewById(R.id.play);
        pause = (ImageButton) findViewById(R.id.pause);
        coverart = (ImageView) findViewById(R.id.imageView);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Volley_request("Setplaystate", "Volley_error", "music_play");
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Volley_request("Setplaystate", "Volley_error", "music_pause");
            }
        });
        timebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int currentseconds = (progress / 1000) % 60;
                    int currentminutes = (progress / (1000 * 60)) % 60;
                    String currentminutesastext = String.valueOf(currentminutes);
                    String currentsecondsastext = String.valueOf(currentseconds);
                    if (currentseconds < 10) {
                        currentsecondsastext = "0" + currentseconds;
                    }
                    String currentlength = currentminutesastext + ":" + currentsecondsastext;
                    songcurrenttime.setText(currentlength);
                    Volley_request("SetTimeUI", "Volley_error", "music_time_set-" + String.valueOf(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        volumebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Volley_request("SetVolumeUI", "Volley_error", "music_volume_set-" + String.valueOf(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        updater();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mcplayer_abqueue, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.queue:
                Intent a = new Intent(getBaseContext(), Musicqueue.class);
                startActivity(a);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updater() {
        final int[] i = {0};
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                i[0]++;
                if (i[0] == 5) {
                    i[0] = 0;
                }

                    Volley_request("GetPlayStateUI", "Volley_error", "music_state");
                    Volley_request("GetVolumeUI", "Volley_error", "music_volume_get");
                    Volley_request("GetCurrentTrackUI", "Volley_error", "music_song");
            }
        }, 0, 1000);
    }

    public void GetCurrentTrackUI(String response, String cmd) {
        try {
            JSONObject json = new JSONObject(response).getJSONObject("result");
            String URI = json.getString("uri").replace("spotify:track:", "");
            if (!coveruri.equals("https://api.spotify.com/v1/tracks/" + URI)) {
                Volley_request("Spotifyapi", "Volley_error", "https://api.spotify.com/v1/tracks/" + URI);
            }
            if (!songid.equals(URI)) {
                songid = URI;
                int lengthinms = json.getInt("length");
                timebar.setMax(lengthinms);
                int seconds = (lengthinms / 1000) % 60;
                int minutes = (lengthinms / (1000 * 60)) % 60;
                String minutesastext = String.valueOf(minutes);
                String secondsastext = String.valueOf(seconds);
                if (seconds < 10) {
                    secondsastext = "0" + seconds;
                }
                String length = minutesastext + ":" + secondsastext;
                songlength.setText(length);
                songtitle.setText(json.getString("name"));
                songalbum.setText(json.getJSONObject("album").getString("name"));
                int numofartists = json.getJSONArray("artists").length();
                String artists = null;
                for (int i = 0; i < numofartists; i++) {
                    if (artists == null) {
                        artists = json.getJSONArray("artists").getJSONObject(i).getString("name");
                    } else {
                        artists = artists + ", " + json.getJSONArray("artists").getJSONObject(i).getString("name");
                    }
                }
                songartist.setText(artists);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Musicplayer.this, "Failed to Get current song", Toast.LENGTH_SHORT).show();
        }
    }

    public void GetPlayStateUI(String response, String cmd) {
        try {
            playstate = new JSONObject(response).getString("result");
            if (playstate.equals("playing")) {
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
            } else if (playstate.equals("paused")) {
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            } else if (playstate.equals("stopped")) {
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                timebar.setProgress(0);
                songcurrenttime.setText("00:00");
                songlength.setText("00:00");
            }
            if (!playstate.equals("stopped")) {
                Volley_request("GetTimeUI", "Volley_error", "music_time_get");
                Volley_request("GetCurrentTrackUI", "Volley_error", "music_song");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Musicplayer.this, "Failed to Get playstate", Toast.LENGTH_SHORT).show();
        }
    }

    public void GetTimeUI(String response, String cmd) {
        if (response == "") {
            Toast.makeText(Musicplayer.this, "Failed to set time", Toast.LENGTH_SHORT).show();
        } else {
            try {
                int time = new JSONObject(response).getInt("result");
                timebar.setProgress(time);
                int currentseconds = (time / 1000) % 60;
                int currentminutes = (time / (1000 * 60)) % 60;
                String currentminutesastext = String.valueOf(currentminutes);
                String currentsecondsastext = String.valueOf(currentseconds);
                if (currentseconds < 10) {
                    currentsecondsastext = "0" + currentseconds;
                }
                String currentlength = currentminutesastext + ":" + currentsecondsastext;
                songcurrenttime.setText(currentlength);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Musicplayer.this, "Failed to set time", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void SetTimeUI(String response, String cmd) {
        if (response == "") {
            Toast.makeText(Musicplayer.this, "Failed to set time", Toast.LENGTH_SHORT).show();
        } else {

        }
    }

    public void Spotifyapi(String response, String cmd) {
        if (response == "") {
            Toast.makeText(Musicplayer.this, "Failed to get cover art", Toast.LENGTH_SHORT).show();
        } else {
            try {
                coverapi = cmd;
                String url = new JSONObject(response).getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                Volley_image_request("SetCoverArt", "Volley_error", url);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Musicplayer.this, "Failed to get cover art", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void SetCoverArt(Bitmap response, String url) {
        if (response == null) {
            Toast.makeText(Musicplayer.this, "Failed to get cover art", Toast.LENGTH_SHORT).show();
        } else {
            coverart.setImageBitmap(response);
            coveruri = coverapi;
        }
    }

    public void Volley_error(VolleyError e, String cmd) {
        Toast.makeText(Musicplayer.this, "An error occurred", Toast.LENGTH_SHORT).show();
        Log.e("Volley_error", "Failed executing following command: " + cmd);
        e.printStackTrace();
    }

    public void Setplaystate(String response, String cmd) {
        String state = "play";
        if (cmd.endsWith("music_pause")) {
            state = "pause";
        }
        if (response == null) {
            Toast.makeText(Musicplayer.this, "Failed to " + state + " music", Toast.LENGTH_SHORT).show();
        } else if (state == "play") {
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
        } else if (playstate.equals("pause")) {
            play.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
        }
    }

    public void GetVolumeUI(String response, String cmd) {
        try {
            int volume = new JSONObject(response).getInt("result");
            volumebar.setProgress(volume);
        } catch (JSONException e) {
            Toast.makeText(Musicplayer.this, "Failed to get volume", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void SetVolumeUI(String response, String cmd) {
        if (response == "") {
            Toast.makeText(Musicplayer.this, "Failed to set volume", Toast.LENGTH_SHORT).show();
        } else {
            int volume = Integer.parseInt(cmd.split("/")[3].split("-")[1]);
            volumebar.setProgress(volume);
        }
    }

    public void Volley_image_request(final String func, final String func_error, final String url) {
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        try {
                            Musicplayer.class.getDeclaredMethod(func, Bitmap.class, String.class).invoke(Musicplayer.this, response, url);
                        } catch (IllegalAccessException e) {
                            Log.e("OnResponse", "Access Denied: " + func);
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            Log.e("OnResponse", "Invocation target exception: " + func);
                            e.printStackTrace();
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            Log.e("OnResponse", "No such method: " + func);
                            e.printStackTrace();
                        }
                    }
                }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Musicplayer.class.getDeclaredMethod(func_error, VolleyError.class, String.class).invoke(Musicplayer.this, error, url);
                } catch (IllegalAccessException e) {
                    Log.e("OnErrorResponse", "Access Denied: " + func_error);
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    Log.e("OnErrorResponse", "Invocation target exception: " + func_error);
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    Log.e("OnErrorResponse", "No such method: " + func_error);
                    e.printStackTrace();
                }
            }
        });
        queue.add(imageRequest);
    }

    public void Volley_request(final String func, final String func_error, String cmd) {
        if (!cmd.startsWith("https://api.spotify.com/v1/tracks/")) {
            cmd = "http://" + g.getAlarmserverip() + "/music.php?" + cmd;
        }
        final String finalCmd = cmd;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, cmd,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Musicplayer.class.getDeclaredMethod(func, String.class, String.class).invoke(Musicplayer.this, response, finalCmd);
                        } catch (IllegalAccessException e) {
                            Log.e("OnResponse", "Access Denied: " + func);
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            Log.e("OnResponse", "Invocation target exception: " + func);
                            e.printStackTrace();
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            Log.e("OnResponse", "No such method: " + func);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Musicplayer.class.getDeclaredMethod(func_error, VolleyError.class, String.class).invoke(Musicplayer.this, error, finalCmd);
                } catch (IllegalAccessException e) {
                    Log.e("OnErrorResponse", "Access Denied: " + func_error);
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    Log.e("OnErrorResponse", "Invocation target exception: " + func_error);
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    Log.e("OnErrorResponse", "No such method: " + func_error);
                    e.printStackTrace();
                }
            }
        });
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }
}
