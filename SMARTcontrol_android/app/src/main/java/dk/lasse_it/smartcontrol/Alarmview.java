package dk.lasse_it.smartcontrol;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OptionalDataException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Alarmview extends FragmentActivity {
    Globals g = Globals.getInstance();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        LayoutInflater layoutInflater = LayoutInflater.from(Alarmview.this);
        g.setpView(layoutInflater.inflate(R.layout.alarmedit, null));
        final TextView title = (TextView) findViewById(R.id.listviewtitle);
        final TextView subtitle = (TextView) findViewById(R.id.listviewsubtitle);
        title.setText("ALARMs");
        subtitle.setText("Choose an ALARM");
        Volley_request("getplaylists","getplaylists_error", "http://" + g.getAlarmserverip() + "/music.php?music_playlists_get");
        Volley_request("getalarms", "getalarms_error", "http://" + g.getAlarmserverip() + "/alarm/list.php");
    }

    public void getplaylists(String response, String url) {
        if (response != "") {
            try {
                g.setPlaylists(new JSONObject(response));
            } catch (JSONException e) {
                Toast.makeText(Alarmview.this,"Unable to load playlists", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(Alarmview.this,"Unable to load playlists", Toast.LENGTH_SHORT).show();
            Log.e("Exception", "Unable to load playlists, response are empty");
        }
    }
    public void getplaylists_error(VolleyError e, String url) {
        e.printStackTrace();
        Toast.makeText(Alarmview.this,"Unable to load playlists", Toast.LENGTH_SHORT).show();
    }

    public void addalarm(String response, String url) {
        String callvoid = "getalarms_send";
        if (response == "") {
            Log.e("Exception", "Failed to add alarm, response are empty");
            Toast.makeText(Alarmview.this, "Unable to add alarm", Toast.LENGTH_SHORT).show();
            callvoid = "getalarms";
        }
        Volley_request("getalarms", "getalarms_error", "http://" + g.getAlarmserverip() + "/alarm/list.php");
    }

    public void addalarm_error(VolleyError e, String url) {
        e.printStackTrace();
        Toast.makeText(Alarmview.this,"Unable to add alarm", Toast.LENGTH_SHORT).show();
    }

    public void editalarm_add(String response, String url) {
        String callvoid = "getalarms_send";
        if (response == "") {
            Log.e("Exception", "Failed to edit alarm, response are empty");
            Toast.makeText(Alarmview.this, "Unable to edit alarm", Toast.LENGTH_SHORT).show();
            callvoid = "getalarms";
        }
        Volley_request(callvoid, "getalarms_error", "http://" + g.getAlarmserverip() + "/alarm/list.php");
    }

    public void editalarm_add_error(VolleyError e, String url) {
        e.printStackTrace();
        Toast.makeText(Alarmview.this,"Unable to edit alarm", Toast.LENGTH_SHORT).show();
    }

    public void editalarm(String response, String url) {
        if (response == "") {
            Log.e("Exception", "Failed to edit alarm, response are empty");
            Toast.makeText(Alarmview.this, "Unable to edit alarm", Toast.LENGTH_SHORT).show();
            Volley_request("getalarms", "getalarms_error", "http://" + g.getAlarmserverip() + "/alarm/list.php");
        } else {
            Volley_request("editalarm_add", "editalarm_add_error", "http://" + g.getAlarmserverip() + "/alarm/add.php?r&" + g.getEditAlarm());
        }
    }

    public void editalarm_error(VolleyError e, String url) {
        e.printStackTrace();
        Toast.makeText(Alarmview.this,"Unable to edit alarm", Toast.LENGTH_SHORT).show();
    }

    public void deletealarm(String response, String url) {
        String callvoid = "getalarms_send";
        if (response == "") {
            Log.e("Exception", "Failed to delete alarm, response are empty");
            Toast.makeText(Alarmview.this, "Unable to delete alarm", Toast.LENGTH_SHORT).show();
            callvoid = "getalarms";
        }
        Volley_request(callvoid, "getalarms_error", "http://" + g.getAlarmserverip() + "/alarm/list.php");
    }

    public void deletealarm_error(VolleyError e, String url) {
        e.printStackTrace();
        Toast.makeText(Alarmview.this,"Unable to delete alarm", Toast.LENGTH_SHORT).show();
    }

    public void getalarms_send(String response, String url) {
        Mgetalarms(response, url, 1);
    }

    public void getalarms(String response, String url) {
        Mgetalarms(response,url,0);
    }

    public void getalarms_error(VolleyError e, String url) {
        e.printStackTrace();
        Toast.makeText(Alarmview.this,"Unable to load alarms", Toast.LENGTH_SHORT).show();
    }

    public void Mgetalarms(String response, String url, int send) {
        if (response != "") {
            try {
                Refresh(new JSONObject(response));
                if (send == 1) {
                    Toast.makeText(Alarmview.this, "Submitted!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Alarmview.this,"Unable to load alarms", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("Exception", "Unable to load alarms, response are empty");
            Toast.makeText(Alarmview.this,"Unable to load alarms", Toast.LENGTH_SHORT).show();
        }
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    public void Refresh (final JSONObject json) {
        final ListView listview = (ListView) findViewById(R.id.listview);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        final ArrayList<String> list = new ArrayList<String>();
        g.setAlarms(json);
        JSONArray jsonarray = null;
        int alarmnum = 10;
        try {
            jsonarray = json.getJSONArray("alarms");
            alarmnum = jsonarray.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < alarmnum; i = i + 1) {
                if (jsonarray != null) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    list.add(timeastext(Integer.parseInt(obj.getString("hour"))) + ":" + timeastext(Integer.parseInt(obj.getString("minute"))) + " " + obj.getString("day") + ". " + monthastext(Integer.parseInt(obj.getString("month"))) + " " + obj.getString("year"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                try {
                    JSONObject playlists = g.getPlaylists();
                    JSONArray alarms = json.getJSONArray("alarms");
                    JSONObject obj = alarms.getJSONObject(position);
                    g.setHour(Integer.parseInt(obj.getString("hour")));
                    g.setMinute(Integer.parseInt(obj.getString("minute")));
                    g.setYear(Integer.parseInt(obj.getString("year")));
                    g.setMonth(Integer.parseInt(obj.getString("month")));
                    g.setDay(Integer.parseInt(obj.getString("day")));
                    g.setMonthastext(timeastext(Integer.parseInt(obj.getString("month"))));
                    g.setHourastext(timeastext(Integer.parseInt(obj.getString("hour"))));
                    g.setMinuteastext(timeastext(Integer.parseInt(obj.getString("minute"))));
                    g.setPlaylisturi(obj.getString("spotify"));
                    g.setAlarmcmd(obj.getString("cmd"));
                    for (int i = 0; i < playlists.getJSONArray("result").length(); i++) {
                        if (playlists.getJSONArray("result").getJSONObject(i).getString("uri").equals(g.getPlaylisturi())) {
                            g.setPlaylistname(playlists.getJSONArray("result").getJSONObject(i).getString("name"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showInputDialog("edit");
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) +1;
                int day = c.get(Calendar.DAY_OF_MONTH);
                JSONObject playlists = g.getPlaylists();
                g.setHour(hour);
                g.setMinute(minute);
                g.setYear(year);
                g.setMonth(month);
                g.setDay(day);
                g.setMonthastext(monthastext(month));
                g.setHourastext(timeastext(hour));
                g.setMinuteastext(timeastext(minute));
                g.setPlaylisturi(g.getDefplaylisturi());
                g.setAlarmcmd(g.getDefalarmcmd());
                try {
                    for (int i = 0; i < playlists.getJSONArray("result").length(); i++) {
                        if (playlists.getJSONArray("result").getJSONObject(i).getString("uri").equals(g.getPlaylisturi())) {
                            g.setPlaylistname(playlists.getJSONArray("result").getJSONObject(i).getString("name"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showInputDialog("new");
            }
        });
    }

    protected void showInputDialog(final String version) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Alarmview.this);
        View newview = g.getpView();
        final Button timeview = (Button) newview.findViewById(R.id.timeview);
        final Button dateview = (Button) newview.findViewById(R.id.dateview);
        final Button playlistview = (Button) newview.findViewById(R.id.playlistview);
        final Button commandview = (Button) newview.findViewById(R.id.commandview);
            timeview.setText(timeastext(g.getHour()) + ":" + timeastext(g.getMinute()));
        dateview.setText(g.getDay() + ". " + monthastext(g.getMonth()) + " " + g.getYear());
            commandview.setText(g.getAlarmcmd());
            shrinkTextToFit(800, commandview, 27, 15);
            playlistview.setText(g.getPlaylistname());
            shrinkTextToFit(800, playlistview, 27, 15);
            g.setpView(newview);
            alertDialogBuilder.setView(newview);
            timeview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getFragmentManager(), "timepicker");
                }
            });

            dateview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datepicker");
                }
            });

            commandview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater = LayoutInflater.from(Alarmview.this);
                    View promptView = layoutInflater.inflate(R.layout.edittextdialog, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Alarmview.this);
                    alertDialogBuilder.setView(promptView);

                    final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
                    final TextView textview = (TextView) promptView.findViewById(R.id.textView);
                    editText.setText(g.getAlarmcmd());
                    textview.setText("Change command to execute");
                    alertDialogBuilder.setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String alarmcmd = String.valueOf(editText.getText());
                                    g.setAlarmcmd(alarmcmd);
                                    View pview = g.getpView();
                                    Button button = (Button) pview.findViewById(R.id.commandview);
                                    button.setText(alarmcmd);
                                    shrinkTextToFit(800, button, 27, 15);
                                    g.setpView(pview);
                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                }
            });

            playlistview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    g.setCurrent("alarmview");
                    android.app.DialogFragment newFragment = new PlaylistPickerFragment();
                    newFragment.show(getFragmentManager(), "playlistpicker");
                }
            });
        final Integer hour = g.getHour();
        final Integer minute = g.getMinute();
        final Integer year = g.getYear();
        final Integer month = g.getMonth();
        final Integer day = g.getDay();
        final String playlisturi = g.getPlaylisturi();
        final String alarmcmd = g.getAlarmcmd();
        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String args = "year=" + g.getYear() + "&month=" + g.getMonth() + "&day=" + g.getDay() + "&hour=" + g.getHour() + "&minute=" + g.getMinute() + "&cmd=" + g.getAlarmcmd() + "&spotify=" + g.getPlaylisturi();
                        if (version.equals("edit")) {
                            Volley_request("editalarm", "editalarm_error", "http://" + g.getAlarmserverip() + "/alarm/delete.php?r&year=" +year + "&month=" + month + "&day=" + day + "&hour=" + hour + "&minute=" + minute);
                            g.setEditAlarm(args);
                        } else {
                            Volley_request("addalarm", "addalarm_error", "http://" + g.getAlarmserverip() + "/alarm/add.php?r&" + args);
                        }
                        LayoutInflater layoutInflater = LayoutInflater.from(Alarmview.this);
                        g.setpView(layoutInflater.inflate(R.layout.alarmedit, null));
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        LayoutInflater layoutInflater = LayoutInflater.from(Alarmview.this);
                        g.setpView(layoutInflater.inflate(R.layout.alarmedit, null));
                    }
                });
            if (version.equals("edit")) {
                alertDialogBuilder.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Volley_request("deletealarm", "deletealarm_error", "http://" + g.getAlarmserverip() + "/alarm/delete.php?r&year=" + +year + "&month=" + month + "&day=" + day + "&hour=" + hour + "&minute=" + minute);
                        LayoutInflater layoutInflater = LayoutInflater.from(Alarmview.this);
                        g.setpView(layoutInflater.inflate(R.layout.alarmedit, null));
                    }
                });
            }
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    public static void shrinkTextToFit(float availableWidth, TextView textView, float startingTextSize, float minimumTextSize) {
        CharSequence text = textView.getText();
        float textSize = startingTextSize;
        textView.setTextSize(startingTextSize);
        while (text != (TextUtils.ellipsize(text, textView.getPaint(), availableWidth, TextUtils.TruncateAt.END))) {
            textSize -= 1;
            if (textSize < minimumTextSize) {
                break;
            } else {
                textView.setTextSize(textSize);
            }
        }
    }

    public String monthastext(int month){
        String monthastext = String.valueOf(month);
        if (month == 1) {
            monthastext = "January";
        } else if (month == 2) {
            monthastext = "February";
        } else if (month == 3) {
            monthastext = "March";
        } else if (month == 4) {
            monthastext = "April";
        } else if (month == 5) {
            monthastext = "May";
        } else if (month == 6) {
            monthastext = "June";
        } else if (month == 7) {
            monthastext = "July";
        } else if (month == 8) {
            monthastext = "August";
        } else if (month == 9) {
            monthastext = "September";
        } else if (month == 10) {
            monthastext = "October";
        } else if (month == 11) {
            monthastext = "November";
        } else if (month == 12) {
            monthastext = "December";
        }
        return monthastext;
    }

    public String timeastext(int time) {
        String timeastext = String.valueOf(time);
        for (int i = 0; i < 10; i++) {
            if (time == i) {
                timeastext = "0" + time;
            }
        }
        return timeastext;
    }

    public void Volley_request(final String func, final String func_error, final String url) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Alarmview.class.getDeclaredMethod(func, String.class, String.class).invoke(Alarmview.this, response, url);
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
                    Alarmview.class.getDeclaredMethod(func_error, VolleyError.class, String.class).invoke(Alarmview.this, error, url);
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
        queue.add(stringRequest);
    }
}