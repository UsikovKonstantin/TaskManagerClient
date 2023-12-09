package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.taskmanager.util.SocketManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    private int task_id;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextDate;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTextName = findViewById(R.id.editTextEditTaskName);
        editTextDescription = findViewById(R.id.editTextEditTaskDescription);
        editTextDate = findViewById(R.id.editTextEditTaskDate);
        Button buttonCompleteTask = findViewById(R.id.buttonCompleteTask);

        Intent intent = getIntent();
        task_id = intent.getIntExtra("task_id", -1);
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String end_at = intent.getStringExtra("end_at");
        position = intent.getIntExtra("position", -1);

        editTextName.setText(name);
        editTextDescription.setText(description);
        editTextDate.setText(end_at);
        buttonCompleteTask.setText(position == 0 ? "Выполнить задачу" : "Снять выполнение");
    }

    public void onClickOpenCalendar(View view) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(view.getContext(), (viewDp, yearDp, monthDp, dayDp) -> {
            String text = yearDp + "-" + (monthDp + 1) + "-" + dayDp;
            editTextDate.setText(text);
        }, year, month, day);
        dpd.show();
    }

    public void onClickEditTask(View view) {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String end_at = editTextDate.getText().toString().trim();

        if (!name.isEmpty() && !end_at.isEmpty()) {
            String queryUpdateTask = "UpdateTask" + "\n" + task_id + "\n" + name + "\n" + description + "\n" + end_at;

            if (!SocketManager.isConnected()) {
                if (!SocketManager.connectParallel("82.179.140.18", 45125)) {
                    Toast.makeText(this, R.string.connect_failed, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (!SocketManager.sendParallel(queryUpdateTask)) {
                Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!SocketManager.receiveParallel()) {
                Toast.makeText(this, R.string.receive_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            String result = SocketManager.getResult();
            if (!result.equals("1")) {
                Toast.makeText(this, R.string.edit_task_failed, Toast.LENGTH_SHORT).show();
            }
            else {
                finish();
            }
        } else {
            Toast.makeText(this, R.string.empty_fields_add_error, Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickCompleteTask(View view) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = currentDate.format(formatter);

        String queryUpdateTaskDoneAt;
        if (position == 0) {
            queryUpdateTaskDoneAt = "UpdateTaskDoneAt" + "\n" + task_id + "\n" + date;
        } else {
            queryUpdateTaskDoneAt = "UpdateTaskNotDone" + "\n" + task_id;
        }

        if (!SocketManager.isConnected()) {
            if (!SocketManager.connectParallel("82.179.140.18", 45125)) {
                Toast.makeText(this, R.string.connect_failed, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!SocketManager.sendParallel(queryUpdateTaskDoneAt)) {
            Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!SocketManager.receiveParallel()) {
            Toast.makeText(this, R.string.receive_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        String result = SocketManager.getResult();
        if (!result.equals("1")) {
            Toast.makeText(this, R.string.edit_task_failed, Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

    public void onClickDeleteTask(View view) {
        String queryDelete = "DeleteTask" + "\n" + task_id;

        if (!SocketManager.isConnected()) {
            if (!SocketManager.connectParallel("82.179.140.18", 45125)) {
                Toast.makeText(this, R.string.connect_failed, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!SocketManager.sendParallel(queryDelete)) {
            Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!SocketManager.receiveParallel()) {
            Toast.makeText(this, R.string.receive_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        String result = SocketManager.getResult();
        if (!result.equals("1")) {
            Toast.makeText(this, R.string.delete_task_failed, Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}