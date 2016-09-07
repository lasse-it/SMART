package dk.lasse_it.smartcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class PlaylistPickerFragment extends DialogFragment {
    Globals g = Globals.getInstance();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int playnum = 10;
        JSONArray json = null;
        try {
            json = g.getPlaylists().getJSONArray("result");
            playnum = json.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] list = new String[playnum];
        try {
            list[0] = "No music";
            for (int i = 1; i < playnum; i = i + 1) {
                if (json != null) {
                    list[i] = json.getJSONObject(i).getString("name");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please select playlist")
                .setItems(list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int position) {
                        String playlisttitle = null;
                        String url = null;
                        try {
                            JSONArray json = g.getPlaylists().getJSONArray("result");
                            if (position > 0) {
                                url = json.getJSONObject(position).getString("uri");
                                playlisttitle = json.getJSONObject(position).getString("name");
                            } else {
                                url = "";
                                playlisttitle = "No Music";
                            }
                            if (g.getCurrent().equals("alarmtab")) {
                                g.setDefplaylisturi(url);
                                g.setDefplaylistname(playlisttitle);
                                String[] list = g.getList();
                                list[1] = "Default Playlist: " + g.getDefplaylistname();
                                g.setList(list);
                                g.getAdapter().notifyDataSetChanged();
                                setString("playlistname", playlisttitle);
                                setString("playlisturi", url);
                            } else {
                                g.setPlaylisturi(url);
                                g.setPlaylistname(playlisttitle);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (g.getCurrent().equals("alarmtab")) {

                        } else {
                            View pview = g.getpView();
                            Button button = (Button) pview.findViewById(R.id.playlistview);
                            button.setText(playlisttitle);
                            shrinkTextToFit(800, button, 27, 15);
                            g.setpView(pview);
                        }
                    }
                });
        return builder.create();
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
    public void setString(String key,String value){
        SharedPreferences sharedPref=getActivity().getSharedPreferences("dk.lasse_it.smartcontrol", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putString(key,value);
        editor.apply();
    }
}
