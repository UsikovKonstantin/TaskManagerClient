package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanager.util.SocketManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    private int id;
    private String name;
    private String description;
    private String end_at;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextDate;
    private Button buttonCompleteTask;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        end_at = intent.getStringExtra("end_at");
        position = intent.getIntExtra("position", -1);

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        buttonCompleteTask = findViewById(R.id.buttonCompleteTask);

        editTextName.setText(name);
        editTextDescription.setText(description);
        editTextDate.setText(end_at);

        if (position == 0) {
            buttonCompleteTask.setText("Выполнить задачу");
        } else {
            buttonCompleteTask.setText("Снять выполнение");
        }

        System.out.println(id);
    }

    public void onClickOpenCalendar(View view) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                editTextDate.setText(year + "-" + month + "-" + dayOfMonth);
            }
        }, year, month, day);
        dpd.show();
    }

    public void onClickEditTask(View view) {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String end_at = editTextDate.getText().toString().trim();

        if (!name.isEmpty() && !end_at.isEmpty()) {
            String sql = "update task set name = '" + name + "', description = '" + description + "', end_at = '" + end_at + "' where id = " + id;

            SocketManager.sendParallel(sql);

            SocketManager.receiveParallel();
            String result = SocketManager.getResult();

            if (!result.equals("1")) {
                Toast.makeText(this, "Не удалось изменить задачу", Toast.LENGTH_SHORT).show();
            }
            else {
                finish();
            }
        } else {
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickCompleteTask(View view) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        String sql;
        if (position == 0) {
            sql = "update task set done_at = '" + formattedDate + "' where id = " + id;
        } else {
            sql = "update task set done_at = null where id = " + id;
        }

        SocketManager.sendParallel(sql);

        SocketManager.receiveParallel();
        String result = SocketManager.getResult();

        if (!result.equals("1")) {
            Toast.makeText(this, "Не удалось изменить задачу", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

    public void onClickDeleteTask(View view) {
        String sql = "delete from task where id = " + id;

        SocketManager.sendParallel(sql);

        SocketManager.receiveParallel();
        String result = SocketManager.getResult();

        if (!result.equals("1")) {
            Toast.makeText(this, "Не удалось удалить задачу", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}