package com.pchailam.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class InNoteActivity extends AppCompatActivity {
    EditText editTextTitle, editTextContent;
    TextView textViewTime, textViewDateInNote, countChar;
    int position;
    private MyDatabase myDatabase;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_note);

        editTextTitle = findViewById(R.id.edtTitle);
        editTextContent = findViewById(R.id.edtContent);
        textViewTime = findViewById(R.id.tvTime);
        textViewDateInNote = findViewById(R.id.tvDateInNote);
        countChar = findViewById(R.id.tvCountChar);

        myDatabase = new MyDatabase(InNoteActivity.this);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);

        if (position != -1) {
            String dateTime = intent.getStringExtra("date");

            String[] part = dateTime.split(" ");
            String time = part[0];
            String date = part[1];
            textViewTime.setText(time);
            textViewDateInNote.setText(date);

            editTextTitle.setText(intent.getStringExtra("title"));
            editTextContent.setText(intent.getStringExtra("content"));

            String text = editTextContent.getText().toString();
            int charCount = text.length();
            countChar.setText(String.valueOf(charCount));
        }
        else {
            createNewNote();
        }

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btnMenu = findViewById(R.id.btnMenuInNote);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(InNoteActivity.this, btnMenu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        if (id == R.id.deleteInNote) {
                            myDatabase.deleteNote(NotesFragment.list.get(position).getId());

                            NotesFragment.list.remove(position);

                            Intent intent = new Intent();
                            intent.putExtra("updateData", true);
                            setResult(RESULT_OK, intent);
                            finish();

                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.popup_menu_in_note);
                popupMenu.show();
            }
        });
        editTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int charCount = s.length();
                countChar.setText(String.valueOf(charCount));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ImageButton btnCompleted = findViewById(R.id.btnComplete);
        btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveNote();
            }
        });
    }
    public void SaveNote() {
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();
        String newDateTime = getCurrentDateTime();

        myDatabase = new MyDatabase(InNoteActivity.this);

        if (position != -1) {
            Note newNote= new Note(NotesFragment.list.get(position).getId(),title,content,newDateTime,0);
            myDatabase.editNote(newNote,NotesFragment.list.get(position).getId());
        }
        else {
            Note newNote= new Note(position,title,content,newDateTime,0);
            myDatabase.addNote(newNote);
        }

        if (!title.isEmpty()) {
            NotesFragment.list.get(position).setTitle(title);
            NotesFragment.list.get(position).setContent(content);

            Intent intent = new Intent();
            intent.putExtra("updateData", true);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    public void createNewNote() {
        String dateTime = getCurrentDateTime();
        String time = dateTime.split(" ")[0];
        String date = dateTime.split(" ")[1];

        textViewTime = findViewById(R.id.tvTime);
        textViewTime.setText(time);
        textViewDateInNote = findViewById(R.id.tvDateInNote);
        textViewDateInNote.setText(date);

    }
    public String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        return sdf.format(Calendar.getInstance().getTime());
    }
}