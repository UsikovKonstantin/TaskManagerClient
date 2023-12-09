package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.taskmanager.util.HashUtil;
import com.example.taskmanager.util.SocketManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterUsername;
    private EditText editTextRegisterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextRegisterUsername = findViewById(R.id.editTextRegisterUsername);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
    }

    public void onClickRegister(View view) {
        String username = editTextRegisterUsername.getText().toString().trim();
        String password = HashUtil.hashPassword(editTextRegisterPassword.getText().toString().trim());

        if (!username.isEmpty() && !password.isEmpty()) {
            String queryFindPerson = "FindPersonByUsername" + "\n" + username;
            System.out.println(queryFindPerson);

            if (!SocketManager.sendParallel(queryFindPerson)) {
                Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!SocketManager.receiveParallel()) {
                Toast.makeText(this, R.string.receive_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            String user = SocketManager.getResult();
            System.out.println(user);

            if (!user.equals(" ")) {
                Toast.makeText(this, R.string.user_already_exists, Toast.LENGTH_SHORT).show();
            }
            else {
                String queryAddPerson = "AddPerson" + "\n" + username + "\n" + password;

                if (!SocketManager.sendParallel(queryAddPerson)) {
                    Toast.makeText(this, R.string.send_failed, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!SocketManager.receiveParallel()) {
                    Toast.makeText(this, R.string.receive_failed, Toast.LENGTH_SHORT).show();
                    return;
                }

                String result = SocketManager.getResult();
                if (result.equals("1")) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, R.string.add_user_failed, Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            Toast.makeText(this, R.string.empty_fields_error, Toast.LENGTH_SHORT).show();
        }
    }
}