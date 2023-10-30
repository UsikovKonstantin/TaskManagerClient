package com.example.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.taskmanager.util.SocketManager;
import com.example.taskmanager.util.TaskClickListener;
import java.util.ArrayList;
import java.util.Arrays;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class TasksFragment extends Fragment {

    private TableLayout tableLayout;
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
        UpdateTableView();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        context = view.getContext();
        tableView = view.findViewById(R.id.tableView);
        buttonAddTask = view.findViewById(R.id.buttonAddTask);
        listener = new TaskClickListener(context);
        tableView.addDataClickListener(listener);
        UpdateTableView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View v = view.findViewById(R.id.buttonAddTask);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AddTaskActivity.class);
                intent.putExtra("id", person_id);
                startActivity(intent);
            }
        });
    }

    public void UpdateTableView() {
        String sql;
        if (position == 0) {
            buttonAddTask.setVisibility(View.VISIBLE);
            sql = "select id, name, description, end_at from task where person_id = " + person_id + " and done_at is null";
        } else {
            buttonAddTask.setVisibility(View.GONE);
            sql = "select id, name, description, end_at from task where person_id = " + person_id + " and done_at is not null";
        }
        SocketManager.sendParallel(sql);
        SocketManager.receiveParallel();
        String result = SocketManager.getResult();

        String[] rows = result.split("\n");
        String[][] data = new String[rows.length][];
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < rows.length; i++) {
            data[i] = Arrays.stream(rows[i].split("\t")).skip(1).toArray(String[]::new);
            if (!rows[i].split("\t")[0].equals(" "))
                ids.add(Integer.parseInt(rows[i].split("\t")[0]));
        }

        int[] arr = new int[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            arr[i] = ids.get(i);
        }
        listener.setIds(arr);
        listener.setPosition(position);


        String[] headers = {"Название", "Описание", "Cрок"};
        tableView.setColumnCount(headers.length);


        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, headers);
        simpleTableHeaderAdapter.setTextSize(15);

        tableView.setHeaderAdapter(simpleTableHeaderAdapter);

        SimpleTableDataAdapter simpleTableDataAdapter = new SimpleTableDataAdapter(context, data);
        simpleTableDataAdapter.setTextSize(10);

        TableColumnWeightModel columnModel = new TableColumnWeightModel(headers.length);
        columnModel.setColumnWeight(1, 2);
        tableView.setColumnModel(columnModel);

        tableView.setDataAdapter(simpleTableDataAdapter);
    }
}