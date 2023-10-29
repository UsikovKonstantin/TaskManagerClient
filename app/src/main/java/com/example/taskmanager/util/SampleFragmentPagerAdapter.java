package com.example.taskmanager.util;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.taskmanager.TasksFragment;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Текущие задачи", "Завершенные задачи" };
    private Context context;
    private int person_id;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context, int person_id) {
        super(fm);
        this.context = context;
        this.person_id = person_id;
    }

    @Override public int getCount() {
        return PAGE_COUNT;
    }

    @Override public Fragment getItem(int position) {
        return new TasksFragment(position, person_id);
    }

    @Override public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
