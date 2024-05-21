package com.pchailam.noteapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ListNoteViewHolder> {
    @SuppressLint("StaticFieldLeak")
    static Context context;
    static ArrayList<Note> data;
    static ArrayList<Type> types;
    private static OnItemClickListener mListener;
    static int position;

    public NoteAdapter(Context context, ArrayList<Note> data) {
        NoteAdapter.context = context;
        NoteAdapter.data = data;
    }

    @NonNull
    @Override
    public NoteAdapter.ListNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.note_layout, parent, false);
        return new ListNoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ListNoteViewHolder holder, int position) {
        Note card =data.get(position);
        holder.textViewTitle.setText(card.getTitle());
        holder.textViewContent.setText(card.getContent());
        holder.textViewDate.setText(card.getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    static final class ListNoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView textViewTitle, textViewContent, textViewDate;
        ImageButton btnMenu;

        @SuppressLint("WrongViewCast")
        public ListNoteViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tvTitle);
            textViewContent = itemView.findViewById(R.id.tvContent);
            textViewDate = itemView.findViewById(R.id.tvDate);
            btnMenu = itemView.findViewById(R.id.btnMenu);

            itemView.setOnClickListener(this);
            btnMenu.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnMenu) {
                showPopupMenu();
            } else {
                //click vào card note
                if (mListener != null) {
                    position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                }
            }
        }
        private void showPopupMenu() {
            // https://www.geeksforgeeks.org/popup-menu-in-android-with-example/?ref=header_search
            PopupMenu popupMenu = new PopupMenu(context, btnMenu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.popup_menu_on_note);
            popupMenu.show();
        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.typing) {
                showTypeDialog();
                return true;
            }
            return false;
        }
        private void showTypeDialog() {
            position = getLayoutPosition();
            MyDatabase myDatabase = new MyDatabase(context);
            types = myDatabase.readTypeData();

            String[] typeNames = new String[types.size()];
            int[] IDs = new int[1];

            for (int i = 0; i < types.size(); i++) {
                typeNames[i] = types.get(i).getType();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Chọn loại")
                    .setSingleChoiceItems(typeNames, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IDs[0] = types.get(which).getId();
                        }
                    })
                    .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Note note = data.get(position);
                            myDatabase.updateNoteType(note.getId(),IDs[0]);

                            TypeFragment.adapter.notifyDataSetChanged();

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }

    }
}