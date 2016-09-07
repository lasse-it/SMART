package dk.lasse_it.smartcontrol;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    Globals g = Globals.getInstance();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = g.getYear();
        int month = g.getMonth() -1;
        int day = g.getDay();
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    public void onDateSet(DatePicker view, int year, int month, int day) {
        month = month +1;
        g.setDay(day);
        g.setMonth(month);
        g.setYear(year);
        View pview = g.getpView();
        Button button = (Button) pview.findViewById(R.id.dateview);
        button.setText(g.getDay()+". "+new Alarmview().monthastext(g.getMonth())+" "+g.getYear());
        g.setpView(pview);
    }
}
