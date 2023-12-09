package com.example.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.taskmanager.util.SocketManager;
import com.example.taskmanager.util.TaskClickListener;

import java.util.Arrays;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class TasksFragment extends Fragment {

    private final String[] headers = { "Название", "Описание", "Cрок" };
    private TableView tableView;
    private Context context;
    private int position;
    private int person_id;
    private TaskClickListener listener;
    private Button buttonAddTask;

    public TasksFragment() {
    }

    public TasksFragment(int position, int person_id) {
        this.position = position;
        this.person_id = person_id;
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateView();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        context = view.getContext();
        tableView = view.findViewById(R.id.tableView);
        buttonAddTask = view.findViewById(R.id.buttonOpenAddTaskActivity);
        listener = new TaskClickListener(context);
        tableView.addDataClickListener(listener);
        UpdateView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View v = view.findViewById(R.id.buttonOpenAddTaskActivity);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AddTaskActivity.class);
                intent.putExtra("person_id", person_id);
                startActivity(intent);
            }
        });
    }

    public void UpdateView() {
        String queryFindTasks;
        if (position == 0) {
            buttonAddTask.setVisibility(View.VISIBLE);
            queryFindTasks = "FindNotDoneTasksByPersonId" + "\n" + person_id;
        } else {
            buttonAddTask.setVisibility(View.GONE);
            queryFindTasks = "FindDoneTasksByPersonId" + "\n" + person_id;
        }

        if (!SocketManager.sendParallel(queryFindTasks)) {
            Toast.makeText(context, R.string.send_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!SocketManager.receiveParallel()) {
            Toast.makeText(context, R.string.receive_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        String result = SocketManager.getResult();
        String[] rows = Arrays.stream(result.trim().split("\n"))
                .filter(s -> !s.isEmpty()).toArray(String[]::new);
        String[][] data = new String[rows.length][];
        int[] ids = new int[rows.length];
        for (int i = 0; i < rows.length; i++) {
            String[] rowData = rows[i].split("\t");
            data[i] = new String[] { rowData[1], rowData[2], rowData[3] };
            ids[i] = Integer.parseInt(rowData[0]);
        }

        listener.setIds(ids);
        listener.setPosition(position);

        tableView.setColumnCount(headers.length);

        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, headers);
        simpleTableHeaderAdapter.setTextSize(15);
        tableView.setHeaderAdapter(simpleTableHeaderAdapter);

        TableColumnWeightModel columnModel = new TableColumnWeightModel(headers.length);
        columnModel.setColumnWeight(1, 2);
        tableView.setColumnModel(columnModel);

        SimpleTableDataAdapter simpleTableDataAdapter = new SimpleTableDataAdapter(context, data);
        simpleTableDataAdapter.setTextSize(10);
        tableView.setDataAdapter(simpleTableDataAdapter);
    }
}