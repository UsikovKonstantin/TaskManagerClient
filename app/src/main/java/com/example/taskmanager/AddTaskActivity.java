package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.taskmanager.util.SocketManager;
import java.util.Calendar;

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
        person_id = intent.getIntExtra("person_id", -1);

        editTextName = findViewById(R.id.editTextAddTaskName);
        editTextDescription = findViewById(R.id.editTextAddTaskDescription);
        editTextDate = findViewById(R.id.editTextAddTaskEndDate);
    }

    public void onClickOpenCalendar(View view) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(view.getContext(), (viewDp, yearDp, monthDp, dayDp) -> {
            String text = yearDp + "-" + monthDp + "-" + dayDp;
            editTextDate.setText(text);
        }, year, month, day);
        dpd.show();
    }

    public void onClickAddTask(View view) {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();

        if (!name.isEmpty() && !date.isEmpty()) {
            String sql = "insert into task(person_id, name, description, end_at, done_at) values (" +
                    person_id + ", '" + name + "', '" + description + "', '" + date + "', null)";

            if (!SocketManager.sendParallel(sql)) {
                Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!SocketManager.receiveParallel()) {
                Toast.makeText(this, R.string.receive_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            String result = SocketManager.getResult();
            if (!result.equals("1")) {
                Toast.makeText(this, R.string.add_task_failed, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, R.string.task_added, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, R.string.empty_fields_add_error, Toast.LENGTH_SHORT).show();
        }
    }
}