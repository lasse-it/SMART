package dk.lasse_it.smartcontrol;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    Globals g = Globals.getInstance();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour = g.getHour();
        int minute = g.getMinute();
        final Calendar c = Calendar.getInstance();
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hour, int minute) {
        g.setHour(hour);
        g.setMinute(minute);
        View pview = g.getpView();
        Button button = (Button) pview.findViewById(R.id.timeview);
        button.setText(new Alarmview().timeastext(g.getHour()) + ":" + new Alarmview().timeastext(g.getMinute()));
        g.setpView(pview);
        }
    }
