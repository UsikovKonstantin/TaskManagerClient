package com.example.taskmanager.util;

import android.content.Context;
import android.content.Intent;

import com.example.taskmanager.AddTaskActivity;
import com.example.taskmanager.EditTaskActivity;

public class TaskClickListener implements de.codecrafters.tableview.listeners.TableDataClickListener {

    private Context context;
    private int[] ids;
    private int position;

    @Override
    public void onDataClicked(int rowIndex, Object clickedData) {
        String[] data = (String[])clickedData;
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra("id", ids[rowIndex]);
        intent.putExtra("name", data[0]);
        intent.putExtra("description", data[1]);
        intent.putExtra("end_at", data[2]);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    public TaskClickListener(Context context) {
        this.context = context;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
