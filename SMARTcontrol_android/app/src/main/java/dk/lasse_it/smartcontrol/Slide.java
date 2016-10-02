package dk.lasse_it.smartcontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Slide extends Fragment {
    Globals g = Globals.getInstance();
    int slidenum = g.getSlidenum();
    JSONObject lights = g.getLights();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slide, container, false);
    }
    public void onStart() {
        super.onStart();
        Button btn1 = (Button) getView().findViewById(R.id.btn1);
        Button btn2 = (Button) getView().findViewById(R.id.btn2);
        Button btn3 = (Button) getView().findViewById(R.id.btn3);
        TextView title = (TextView) getView().findViewById(R.id.title);
        btn1.setOnClickListener(clickbtn1);
        btn2.setOnClickListener(clickbtn2);
        btn3.setOnClickListener(clickbtn3);

        try {
            btn1.setText(lights.getJSONArray("lightcat").getJSONObject(slidenum).getJSONArray("btn").getJSONObject(0).getString("title"));
            btn2.setText(lights.getJSONArray("lightcat").getJSONObject(slidenum).getJSONArray("btn").getJSONObject(1).getString("title"));
            btn3.setText(lights.getJSONArray("lightcat").getJSONObject(slidenum).getJSONArray("btn").getJSONObject(2).getString("title"));
            title.setText(lights.getJSONArray("lightcat").getJSONObject(slidenum).getString("title"));
            shrinkTextToFit(900, btn1, 100, 15);
            shrinkTextToFit(900, btn2, 100, 15);
            shrinkTextToFit(900, btn3, 100, 15);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void shrinkTextToFit(float availableWidth, Button button,
                                float startingTextSize, float minimumTextSize) {

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

    View.OnClickListener clickbtn1 = new View.OnClickListener() {
        public void onClick(View v) {
            String server = null;
            try {
                server = lights.getJSONArray("lightcat").getJSONObject(slidenum).getJSONArray("btn").getJSONObject(0).getString("serverip");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String cmd = null;
            try {
                cmd = lights.getJSONArray("lightcat").getJSONObject(slidenum).getJSONArray("btn").getJSONObject(0).getString("cmd");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (server != null && cmd != null) {
                request("http://" + server + "/controlv2.php?r&cmd=" + cmd);
            } else {
                Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_LONG).show();
            }
        }
    };

    View.OnClickListener clickbtn2 = new View.OnClickListener() {
        public void onClick(View v) {
            String server = null;
            try {
                server = lights.getJSONArray("lightcat").getJSONObject(slidenum).getJSONArray("btn").getJSONObject(1).getString("serverip");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String cmd = null;
            try {
                cmd = lights.getJSONArray("lightcat").getJSONObject(slidenum).getJSONArray("btn").getJSONObject(1).getString("cmd");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (server != null && cmd != null) {
                request("http://" + server + "/controlv2.php?r&cmd=" + cmd);
            } else {
                Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_LONG).show();
            }
        }
    };

    View.OnClickListener clickbtn3 = new View.OnClickListener() {
        public void onClick(View v) {
            String server = null;
            try {
                server = lights.getJSONArray("lightcat").getJSONObject(slidenum).getJSONArray("btn").getJSONObject(2).getString("serverip");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String cmd = null;
            try {
                cmd = lights.getJSONArray("lightcat").getJSONObject(slidenum).getJSONArray("btn").getJSONObject(2).getString("cmd");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (server != null && cmd != null) {
                request("http://" + server + "/controlv2.php?r&cmd=" + cmd);
            } else {
                Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_LONG).show();
            }
        }
    };

    public String request (String url) {
        final String[] output = {null};
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != "") {
                            output[0] = response;
                        } else {
                            Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
        return output[0];
    }

}