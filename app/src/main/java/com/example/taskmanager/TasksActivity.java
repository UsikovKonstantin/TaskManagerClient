package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class TasksActivity extends AppCompatActivity {

    private TextView textViewTasks;
    private int id;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        textViewTasks = findViewById(R.id.textViewTasks);

        String sql = "select * from task where person_id = " + id;
        SocketManager.sendParallel(sql);
        SocketManager.receiveParallel();
        String result = SocketManager.getResult();

        String[] lines = result.split("\n");
        String text = "";
        for (String line : lines) {
            String[] words = line.split("\t");
            for (String word : words) {
                text += word + " ";
            }
            text += "\n";
        }

        textViewTasks.setText(text);
    }
}