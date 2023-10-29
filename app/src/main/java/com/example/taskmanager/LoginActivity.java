package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanager.util.Hasher;
import com.example.taskmanager.util.SocketManager;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginUsername;
    private EditText editTextLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextLoginUsername = findViewById(R.id.editTextLoginUsername);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
    }

    public void onClickLogin(View view) {
        String username = editTextLoginUsername.getText().toString().trim();
        String password = Hasher.hashPassword(editTextLoginPassword.getText().toString().trim());
        if (!username.isEmpty() && !password.isEmpty()) {
            String sql = "select * from person where username = '" + username + "'";

            SocketManager.sendParallel(sql);

            SocketManager.receiveParallel();
            String result = SocketManager.getResult();

            if (result.equals(" ")) {
                Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
            }
            else {
                String[] arr = result.split("\t");

                if (arr[1].equals(username) && arr[2].equals(password)) {
                    Intent intent = new Intent(this, TasksActivity.class);
                    intent.putExtra("id", Integer.parseInt(arr[0]));
                    intent.putExtra("username", arr[1]);
                    intent.putExtra("password", arr[2]);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
        }
    }
}