package com.pchailam.noteapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

public class NotesFragment extends Fragment implements NoteAdapter.OnItemClickListener {
    static ArrayList<Note> list;
    private NoteAdapter adapter;
    private RecyclerView recyclerView;
    TextView textViewCount;
    private MyDatabase myDatabase;
    ActivityResultLauncher<Intent> activityResultLauncher;

    public NotesFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getBooleanExtra("updateData", false)) {
                    list.clear();
                    list.addAll(myDatabase.readData());
                    adapter.notifyDataSetChanged();
                    textViewCount.setText(String.valueOf(list.size()));
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        list = new ArrayList<>();
        myDatabase = new MyDatabase(getActivity());
        list = myDatabase.readData();

        textViewCount = view.findViewById(R.id.countNote);
        textViewCount.setText(String.valueOf(list.size()));

        recyclerView = view.findViewById(R.id.recyclerNote);

        adapter = new NoteAdapter(getActivity(), list);

        LinearLayoutManager listLayoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(listLayoutManager);

        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton btnAddNewNode;
        btnAddNewNode = view.findViewById(R.id.btnAddNote);
        btnAddNewNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewNote();
            }
        });

        ImageButton btnMenu = view.findViewById(R.id.btnMenuNote);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), btnMenu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.grid_view) {
                            recyclerView.setLayoutManager(gridLayoutManager);
                            return true;
                        } else if (id == R.id.list_view) {
                            recyclerView.setLayoutManager(listLayoutManager);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.popup_menu_app);
                popupMenu.show();
            }
        });
        return view;
    }
    @Override
    public void onItemClick(int position) {
        Note clickedItem = list.get(position);

        Intent intent = new Intent(getActivity(), InNoteActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("title", clickedItem.getTitle());
        intent.putExtra("content", clickedItem.getContent());
        intent.putExtra("date", clickedItem.getDate());
        intent.putExtra("type", clickedItem.getId_type());

        activityResultLauncher.launch(intent);
    }
    public void createNewNote() {
        Intent intent = new Intent(getActivity(), InNoteActivity.class);
        startActivityForResult(intent,8000);
    }
}