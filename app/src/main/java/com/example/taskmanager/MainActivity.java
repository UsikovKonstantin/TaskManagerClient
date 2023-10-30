package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.taskmanager.util.SocketManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!SocketManager.connectParallel("82.179.140.18", 45125)) {
            Toast.makeText(this, R.string.connect_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!SocketManager.disconnectParallel()) {
            Toast.makeText(this, R.string.disconnect_failed, Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickOpenRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onClickOpenLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}