package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.taskmanager.util.HashUtil;
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
        String password = HashUtil.hashPassword(editTextLoginPassword.getText().toString().trim());

        if (!username.isEmpty() && !password.isEmpty()) {
            String queryFindPerson = "FindPersonByUsername" + "\n" + username;

            if (!SocketManager.sendParallel(queryFindPerson)) {
                Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!SocketManager.receiveParallel()) {
                Toast.makeText(this, R.string.receive_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            String user = SocketManager.getResult();

            if (user.equals(" ")) {
                Toast.makeText(this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
            }
            else {
                String[] userData = user.split("\t");

                if (userData[1].equals(username) && userData[2].equals(password)) {
                    Intent intent = new Intent(this, TasksActivity.class);
                    intent.putExtra("person_id", Integer.parseInt(userData[0]));
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, R.string.incorrect_password, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, R.string.empty_fields_error, Toast.LENGTH_SHORT).show();
        }
    }
}