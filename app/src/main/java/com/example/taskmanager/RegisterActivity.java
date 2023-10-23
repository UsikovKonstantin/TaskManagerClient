package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

            SocketManager.sendParallel(sql);

            SocketManager.receiveParallel();
            String result = SocketManager.getResult();

            if (!result.equals(" ")) {
                Toast.makeText(this, "Пользователь с таким именем уже существует", Toast.LENGTH_SHORT).show();
            }
            else {
                sql = "insert into person (username, password) values ('" + username + "', '" + password + "')";

                SocketManager.sendParallel(sql);

                SocketManager.receiveParallel();
                result = SocketManager.getResult();

                if (result.equals("1")) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        } else {
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
        }
    }
}