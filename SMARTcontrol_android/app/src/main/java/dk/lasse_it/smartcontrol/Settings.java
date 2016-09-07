package dk.lasse_it.smartcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Settings extends Activity {
    Globals g = Globals.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        g.setCurrent("settings");
        getplaylists();
        final ListView listview = (ListView) findViewById(R.id.listview);
        final TextView title = (TextView) findViewById(R.id.listviewtitle);
        final TextView subtitle = (TextView) findViewById(R.id.listviewsubtitle);
        final ArrayList<String> list = new ArrayList<String>();
        title.setText("Settings");
        subtitle.setText("");
        list.add("Server address: " + g.getServerip());
        list.add("Customize LIGHT");
        list.add("Customize ALARM");
        list.add("Help");
        final StableArrayAdapter2 adapter = new StableArrayAdapter2(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                if (position == 0) {
                    showInputDialog("serverip");
                }
                if (position == 1) {
                    lighttab();
                }
                if (position == 2) {
                    alarmtab();
                }
                if (position == 3) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/lasse-it/SMART/wiki"));
                    startActivity(browserIntent);
                }
            }

        });
    }
    private class StableArrayAdapter2 extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter2(Context context, int textViewResourceId,
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
    private class StableArrayAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final String[] values;

        public StableArrayAdapter(Context context, String[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.listview_with_delete, parent, false);
            Button slidebtn = (Button) v.findViewById(R.id.listview_edit);
            ImageButton btn = (ImageButton) v.findViewById(R.id.listview_delete);
            slidebtn.setText(values[position]);
            slidebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeslide(position+1);
                }
            });
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONArray lightcat = new JSONArray();
                        JSONObject lights = g.getLights();
                        int cindex = 0;
                        for (int s = 0; s < lights.getJSONArray("lightcat").length(); s++) {
                            if (s != position) {
                                lightcat.put(cindex, lights.getJSONArray("lightcat").getJSONObject(s));
                                cindex++;
                            }
                        }
                        lights.put("lightcat", lightcat);
                        setString("lights", String.valueOf(lights));
                        g.setLights(lights);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    lighttab();
                }
            });
            return v;
        }

    }
    protected void showInputDialog(final String version) {

        LayoutInflater layoutInflater = LayoutInflater.from(Settings.this);
        View promptView = layoutInflater.inflate(R.layout.edittextdialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Settings.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        final TextView title = (TextView) promptView.findViewById(R.id.textView);
        final NumberPicker numpicker = (NumberPicker) promptView.findViewById(R.id.numberPicker);
        final EditText editText2 = (EditText) promptView.findViewById(R.id.edittext2);
        final TextView title2 = (TextView) promptView.findViewById(R.id.textView2);
        final EditText editText3 = (EditText) promptView.findViewById(R.id.edittext3);
        final TextView title3 = (TextView) promptView.findViewById(R.id.textView3);
        JSONObject lights = g.getLights();
        int position = g.getSlidenum();
        int btn = 0;

        if (version.equals("serverip")) {
            editText.setText(g.getServerip());

        } else if (version.equals("alarmcmd")) {
            editText.setText(g.getAlarmcmd());
            editText.setHint("high/blink/low-GPIO-GPIO-GPIO");
            title.setText("Command to execute when \"turn on LIGHT\" is checked");
        } else if (version.equals("alarmserverip")) {
            editText.setText(g.getAlarmserverip());
            editText.setHint("10.0.1.106");
            title.setText("Change ALARM server IP address");

        } else if (version.equals("soundgpio")) {
            editText.setText(String.valueOf(g.getSoundgpio()));
            editText.setHint("GPIO");
            title.setText("The GPIO port, the speakers are connected to");

        } else if (version.equals("btn1") || version.equals("btn2") || version.equals("btn3")) {
            if (version.equals("btn1")) {
                btn = 1;
            } else if (version.equals("btn2")) {
                btn = 2;

            } else if (version.equals("btn3")) {
                btn = 3;
            }
            title.setText("Change text of Button " + btn);
            editText.setHint("ON/BLINK/OFF");
            title2.setText("Change function of Button " + btn);
            title2.setVisibility(View.VISIBLE);
            editText2.setVisibility(View.VISIBLE);
            editText2.setHint("high/blink/low-GPIO-GPIO-GPIO-GPIO");
            editText3.setVisibility(View.VISIBLE);
            title3.setVisibility(View.VISIBLE);
            title3.setText("Change server IP");
            try {
                editText.setText(lights.getJSONArray("lightcat").getJSONObject(position - 1).getJSONArray("btn").getJSONObject(btn - 1).getString("title"));
                editText2.setText(lights.getJSONArray("lightcat").getJSONObject(position - 1).getJSONArray("btn").getJSONObject(btn - 1).getString("cmd"));
                editText3.setText(lights.getJSONArray("lightcat").getJSONObject(position - 1).getJSONArray("btn").getJSONObject(btn - 1).getString("serverip"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (version.equals("title")) {
            title.setText("Change title");
            editText.setHint("DESK LIGHT");
            try {
                editText.setText(lights.getJSONArray("lightcat").getJSONObject(position - 1).getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String serverip = String.valueOf(editText.getText());
                        JSONObject lights = g.getLights();
                        int btn = 0;
                        int position = g.getSlidenum();
                        String title = String.valueOf(editText.getText());
                        String cmd = String.valueOf(editText2.getText());
                        String alarmserverip = String.valueOf(editText.getText());

                        if (version.equals("alarmserverip")) {
                            g.setAlarmserverip(alarmserverip);
                            setString("alarmserverip", alarmserverip);

                        } else if (version.equals("serverip")) {
                            g.setServerip(serverip);
                            g.setAlarmserverip(serverip);
                            setString("serverip", serverip);
                            setString("alarmserverip", serverip);
                            try {
                                for (int i = 0; i < lights.getJSONArray("lightcat").length(); i++) {//i = slide 0-29
                                    for (int a = 0; a < 3; a++) { //a = button 0,1,2
                                        try {
                                            lights.getJSONArray("lightcat").getJSONObject(i).getJSONArray("btn").getJSONObject(a).put("serverip", serverip);
                                        } catch (JSONException ignored) {
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            g.setLights(lights);
                            setString("lights", String.valueOf(lights));

                            startActivity(getIntent());
                            finish();

                        } else if (version.equals("soundgpio")) {
                            g.setSoundgpio(Integer.parseInt(String.valueOf(editText.getText())));
                            setInt("soundgpio", Integer.parseInt(String.valueOf(editText.getText())));
                            alarmtab();

                        } else if (version.equals("alarmcmd")) {
                            g.setAlarmcmd(String.valueOf(editText.getText()));
                            setString("alarmcmd", String.valueOf(editText.getText()));
                            alarmtab();
                        } else if (version.equals("btn1") || version.equals("btn2") || version.equals("btn3")) {
                            if (version.equals("btn1")) {
                                btn = 1;

                            } else if (version.equals("btn2")) {
                                btn = 2;

                            } else if (version.equals("btn3")) {
                                btn = 3;

                            }
                            serverip = String.valueOf(editText3.getText());
                            try {
                                lights.getJSONArray("lightcat").getJSONObject(position - 1).getJSONArray("btn").getJSONObject(btn - 1).put("title", title);
                                lights.getJSONArray("lightcat").getJSONObject(position - 1).getJSONArray("btn").getJSONObject(btn - 1).put("cmd", cmd);
                                lights.getJSONArray("lightcat").getJSONObject(position - 1).getJSONArray("btn").getJSONObject(btn - 1).put("serverip", serverip);
                                setString("lights", String.valueOf(lights));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            changeslide(position);

                        } else if (version.equals("title")) {
                            try {
                                lights.getJSONArray("lightcat").getJSONObject(position - 1).put("title", title);
                                setString("lights", String.valueOf(lights));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            changeslide(position);

                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    public void lighttab() {
        setContentView(R.layout.listview);
        g.setCurrent("lighttab");
        final ListView listview = (ListView) findViewById(R.id.listview);
        final TextView title = (TextView) findViewById(R.id.listviewtitle);
        final TextView subtitle = (TextView) findViewById(R.id.listviewsubtitle);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject lights = g.getLights();
                    JSONArray lightcat = lights.getJSONArray("lightcat");
                    JSONObject lightcatsub = new JSONObject("{\"title\":\"NEW LIGHT\",\"btn\":[{\"title\":\"ON\",\"cmd\":\"high-27\",\"serverip\":\""+g.getServerip()+"\"},{\"title\":\"BLINK\",\"cmd\":\"blink-27\",\"serverip\":\""+g.getServerip()+"\"},{\"title\":\"OFF\",\"cmd\":\"low-27\",\"serverip\":\""+g.getServerip()+"\"}]}");
                    lightcat.put(lights.getJSONArray("lightcat").length(), lightcatsub);
                    lights.put("lightcat", lightcat);
                    setString("lights", String.valueOf(lights));
                    g.setLights(lights);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lighttab();
            }
        });
        JSONArray json = null;
        try {
            json = g.getLights().getJSONArray("lightcat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] list = new String[json.length()];
        title.setText("LIGHT\'s");
        subtitle.setText("");
        for (int i = 0; i < json.length(); i++) {
            try {
                String jsontitle = json.getJSONObject(i).getString("title");
                list[i] = jsontitle;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this, list);
        listview.setAdapter(adapter);
    }

    public void alarmtab() {
        setContentView(R.layout.listview);
        g.setCurrent("alarmtab");
        final ListView listview = (ListView) findViewById(R.id.listview);
        final TextView title = (TextView) findViewById(R.id.listviewtitle);
        final TextView subtitle = (TextView) findViewById(R.id.listviewsubtitle);
        final String[] list = new String[4];
        title.setText("ALARM");
        subtitle.setText("");
        list[0] = "Default Command: " + g.getDefalarmcmd();
        list[1] = "Default Playlist: " + g.getDefplaylistname();
        list[2] = "Sound GPIO port: " + g.getSoundgpio();
        list[3] = "Server IP address: " + g.getAlarmserverip();
        g.setList(list);
        g.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, g.getList()));
        listview.setAdapter(g.getAdapter());
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                if (position == 0) {
                    showInputDialog("alarmcmd");
                } else if (position == 1) {
                    android.app.DialogFragment newFragment = new PlaylistPickerFragment();
                    newFragment.show(getFragmentManager(), "playlistpicker");
                } else if (position == 2) {
                    showInputDialog("soundgpio");
                } else if (position == 3) {
                    showInputDialog("alarmserverip");
                }
            }

        });
    }

    private void changeslide(int position) {
        setContentView(R.layout.slide);
        g.setCurrent("changeslide");
        JSONObject lights = g.getLights();
        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);
        Button btn3 = (Button) findViewById(R.id.btn3);
        TextView title = (TextView) findViewById(R.id.title);
        try {
            btn1.setText(lights.getJSONArray("lightcat").getJSONObject(position -1).getJSONArray("btn").getJSONObject(0).getString("title"));
            btn2.setText(lights.getJSONArray("lightcat").getJSONObject(position - 1).getJSONArray("btn").getJSONObject(1).getString("title"));
            btn3.setText(lights.getJSONArray("lightcat").getJSONObject(position - 1).getJSONArray("btn").getJSONObject(2).getString("title"));
            title.setText(lights.getJSONArray("lightcat").getJSONObject(position - 1).getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        shrinkTextToFit(900, btn1, 100, 15);
        shrinkTextToFit(900, btn2, 100, 15);
        shrinkTextToFit(900, btn3, 100, 15);
        btn1.setOnClickListener(onclickbtn1);
        btn2.setOnClickListener(onclickbtn2);
        btn3.setOnClickListener(onclickbtn3);
        title.setOnClickListener(onclicktitle);
        g.setSlidenum(position);
    }

    public static void shrinkTextToFit(float availableWidth, Button button, float startingTextSize, float minimumTextSize) {

        CharSequence text = button.getText();
        float textSize = startingTextSize;
        button.setTextSize(startingTextSize);
        while (text != (TextUtils.ellipsize(text, button.getPaint(),
                availableWidth, TextUtils.TruncateAt.END))) {
            textSize -= 1;
            if (textSize < minimumTextSize) {
                break;
            } else {
                button.setTextSize(textSize);
            }
        }
    }

    View.OnClickListener onclickbtn1 = new View.OnClickListener() {
        public void onClick(View v) {
            showInputDialog("btn1");
        }
    };

    View.OnClickListener onclickbtn2 = new View.OnClickListener() {
        public void onClick(View v) {
            showInputDialog("btn2");
        }
    };

    View.OnClickListener onclickbtn3 = new View.OnClickListener() {
        public void onClick(View v) {
            showInputDialog("btn3");
        }
    };

    View.OnClickListener onclicktitle = new View.OnClickListener() {
        public void onClick(View v) {
            showInputDialog("title");
        }
    };

    public void setString(String key,String value){
        SharedPreferences sharedPref=getSharedPreferences("dk.lasse_it.smartcontrol",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public void setInt(String key,Integer value){
        SharedPreferences sharedPref=getSharedPreferences("dk.lasse_it.smartcontrol",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        String current = g.getCurrent();
        if (current == "settings") {
            finish();
        }
        if (current == "lighttab") {
            Intent b = new Intent(getBaseContext(), Settings.class);
            startActivity(b);
            finish();
        }
        if (current == "changeslide") {
            lighttab();
        }
        if (current == "alarmtab") {
            Intent b = new Intent(getBaseContext(), Settings.class);
            startActivity(b);
            finish();
        }
    }
    public void getplaylists() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://" + g.getAlarmserverip() + "/music.php?music_playlists_get",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != "") {
                            try {
                                g.setPlaylists(new JSONObject(response));
                            } catch (JSONException e) {
                                Toast.makeText(Settings.this,"Unable to load playlists", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Settings.this,"Unable to load playlists", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Settings.this,"Unable to load playlists", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }
}
