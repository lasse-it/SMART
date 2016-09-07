package dk.lasse_it.smartcontrol;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public class Musiccom extends Activity {
    Globals g = Globals.getInstance();

    public int GetVolume() {
        try {
            return new JSONObject(musicreq("music_volume_get")).getInt("result");
        } catch (JSONException e) {
            return 500;
        }
    }



    public String musicreq(String cmd) {
        final String[] out = {null};
        final RequestQueue queue = Volley.newRequestQueue(this);
        while (out[0] == null) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://" + g.getAlarmserverip() + "/music.php?" + cmd,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != "") {
                                out[0] = response;
                            } else {
                                out[0] = "error";
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    out[0] = "error";
                }
            });
            queue.add(stringRequest);
        }
        return out[0];
    }
}
