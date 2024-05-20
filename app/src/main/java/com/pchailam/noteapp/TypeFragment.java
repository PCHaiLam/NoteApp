package com.pchailam.noteapp;

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
import android.widget.Toast;

import java.util.ArrayList;

public class TypeFragment extends Fragment {
     ArrayList<Type> types;
     ArrayList<Note> notes;
    TypeAdapter adapter;
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
        showTypeListDialogFromDatabase();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_type);
        adapter = new TypeAdapter(getContext(), types);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        ImageButton btnAddType = view.findViewById(R.id.btnAddType);
        btnAddType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTypeDialog();
            }
        });
        return view;
    }

    private void showTypeListDialogFromDatabase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Danh sách loại đã đọc");

        // Tạo một StringBuilder để xây dựng nội dung của dialog
        StringBuilder message = new StringBuilder();
        for (Type type : types) {
            message.append(type.getId() + ": ").append(type.getType()).append("\n"); // Thêm tên loại vào StringBuilder
        }

        // Thiết lập nội dung của dialog bằng nội dung từ StringBuilder
        builder.setMessage(message.toString());

        // Tạo nút "Đóng" để đóng dialog khi người dùng nhấn
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Đóng dialog
            }
        });

        // Hiển thị dialog
        builder.show();
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