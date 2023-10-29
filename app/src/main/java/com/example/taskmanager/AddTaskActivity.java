package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanager.util.SocketManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextDate;
    private int person_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intent = getIntent();
        person_id = intent.getIntExtra("id", -1);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
    }


    public void onClickOpenCalendar(View view) {
        View v = findViewById(R.id.editTextDate);

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ((EditText)v).setText(dayOfMonth + "/" + month + "/" + year);
            }
        }, year, month, day);
        dpd.show();

    }

    public void onClickAddTask(View view) {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String dateStr = editTextDate.getText().toString().trim();
        Date date;
        try {
            date = (new SimpleDateFormat("dd/MM/yyyy")).parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        if (!name.isEmpty() && !dateStr.isEmpty()) {
            String sql = "insert into task(person_id, name, description, end_at, done_at) values (" + person_id + ", '" +
                    name + "', '" + description + "', '" + (new SimpleDateFormat("yyyy-MM-dd")).format(date) + "', null);";

            SocketManager.sendParallel(sql);

            SocketManager.receiveParallel();
            String result = SocketManager.getResult();

            if (!result.equals("1")) {
                Toast.makeText(this, "Не удалось добавить задачу", Toast.LENGTH_SHORT).show();
            }
            else {
                finish();
            }
        } else {
            Toast.makeText(this, "Поля 'Название' и 'Дата' должны быть заполнены", Toast.LENGTH_SHORT).show();
        }
    }
}