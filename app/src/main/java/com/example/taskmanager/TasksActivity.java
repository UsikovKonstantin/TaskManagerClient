package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import com.example.taskmanager.util.SampleFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class TasksActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        Intent intent = getIntent();
        int person_id = intent.getIntExtra("person_id", -1);

        ViewPager viewPager = findViewById(R.id.ViewPagerFragment);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), TasksActivity.this, person_id));

        TabLayout tabLayout = findViewById(R.id.TabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}