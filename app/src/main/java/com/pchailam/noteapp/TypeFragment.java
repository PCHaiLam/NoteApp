package com.pchailam.noteapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TypeFragment extends Fragment {
     static ArrayList<Type> types;
     ArrayList<Note> notes;
    @SuppressLint("StaticFieldLeak")
    static TypeAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    static NoteAdapter noteAdapter;
    MyDatabase myDatabase;
    ActivityResultLauncher<Intent> activityResultLauncher;
    public TypeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type, container, false);

        myDatabase = new MyDatabase(getActivity());
        notes = myDatabase.readData();
        types = myDatabase.readTypeData();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_type);
        adapter = new TypeAdapter(getContext(), types, new TypeAdapter.OnTypeClickListener() {
            @Override
            public void onTypeClick(int typeId) {
                TextView tvTypeName = view.findViewById(R.id.typeName);
                tvTypeName.setText(types.get(typeId-1).getType());
                updateNotesForType(typeId);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        RecyclerView recyclerView1 = view.findViewById(R.id.recyclerNoteInType);
        noteAdapter = new NoteAdapter(getContext(), new ArrayList<>());
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView1.setAdapter(noteAdapter);

        ImageButton btnAddType = view.findViewById(R.id.btnAddType);
        btnAddType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTypeDialog();
            }
        });
        return view;
    }

    private void updateNotesForType(int typeId) {
        ArrayList<Note> notes1 = new ArrayList<>();
        for (Note note : notes) {
            if (note.getId_type() == typeId) {
                notes1.add(note);
            }
        }
        noteAdapter.updateData(notes1);
    }

    private void showAddTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thêm sổ tay");

        final EditText input = new EditText(getActivity());
        input.setHint("Nhập tên của sổ tay");
        builder.setView(input);

        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String typeName = input.getText().toString();
                if (!typeName.isEmpty()) {
                    myDatabase.addType(typeName);
                    Type newType = new Type(types.size()+1, typeName);
                    adapter.addType(newType);
                } else {
                    Toast.makeText(getActivity(), "Tên loại không được để trống", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}