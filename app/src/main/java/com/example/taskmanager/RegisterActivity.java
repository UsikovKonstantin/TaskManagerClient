package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextLoginUsername;
    private EditText editTextLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextLoginUsername = findViewById(R.id.editTextLoginUsername);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
    }

    public void onClickRegister(View view) {
        String username = editTextLoginUsername.getText().toString().trim();
        String password = editTextLoginPassword.getText().toString().trim();
        if (!username.isEmpty() && !password.isEmpty()) {
            String sql = "select * from person where username = '" + username + "'";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SocketManager.send(sql);
                }
            }).start();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    SocketManager.result = SocketManager.receive();
                }
            });
            thread.start();
            synchronized (SocketManager.lock) {
                try {
                    SocketManager.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String result = SocketManager.result;
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
        }
    }
}