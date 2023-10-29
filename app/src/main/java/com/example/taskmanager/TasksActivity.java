package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.taskmanager.util.SampleFragmentPagerAdapter;
import com.example.taskmanager.util.SocketManager;
import com.google.android.material.tabs.TabLayout;

public class TasksActivity extends AppCompatActivity {

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



        // Получаем ViewPager и устанавливаем в него адаптер
        ViewPager viewPager = findViewById(R.id.ViewPager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), TasksActivity.this, id));

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = findViewById(R.id.TabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}