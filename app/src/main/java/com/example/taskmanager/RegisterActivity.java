package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanager.util.Hasher;
import com.example.taskmanager.util.SocketManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterUsername;
    private EditText editTextRegisterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextRegisterUsername = findViewById(R.id.editTextDescription);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
    }

    public void onClickRegister(View view) {
        String username = editTextRegisterUsername.getText().toString().trim();
        String password = Hasher.hashPassword(editTextRegisterPassword.getText().toString().trim());
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