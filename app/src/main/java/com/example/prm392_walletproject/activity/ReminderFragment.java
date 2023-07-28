package com.example.prm392_walletproject.activity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.prm392_walletproject.R;
import com.example.prm392_walletproject.database.AppDatabase;
import com.example.prm392_walletproject.entity.Reminder;

import java.util.Calendar;
import java.util.Date;

public class ReminderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        createNotificationChannel();
        TimePicker alarmTimePicker = (TimePicker) view.findViewById(R.id.timePicker);
        Button button = view.findViewById(R.id.btn_save_reminder);
        Switch simpleSwitch = (Switch) view.findViewById(R.id.simpleSwitch);

        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "wallet.db").allowMainThreadQueries().build();


        Reminder oldReminder = db.reminderDao().getReminder();
        if (oldReminder!=null) {
            simpleSwitch.setChecked(oldReminder.getStatus());
            alarmTimePicker.setHour(oldReminder.getHour());
            alarmTimePicker.setMinute(oldReminder.getMinute());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_MUTABLE);

                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());

                if (calendar.getTime().compareTo(new Date()) < 0)
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (!simpleSwitch.isChecked()) {
                    alarmManager.cancel(pendingIntent);
                } else if (alarmManager != null) {
                    Toast.makeText(getActivity(), "Reminder Set!", Toast.LENGTH_SHORT).show();
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                }
                db.reminderDao().insert(new Reminder(1, alarmTimePicker.getHour(), alarmTimePicker.getMinute(), simpleSwitch.isChecked()));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new SettingFragment()).commit();
            }
        });
        return view;
    }

    public void createNotificationChannel() {
        String name = "ReminderChannel";
        String description = "Channel for reminder notification";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("notification", name, importance);
        channel.setDescription(description);


        NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


}