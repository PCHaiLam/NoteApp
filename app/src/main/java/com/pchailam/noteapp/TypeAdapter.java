package com.pchailam.noteapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeViewHolder> {
    Context context;
    static ArrayList<Type> types;
    OnTypeClickListener onTypeClickListener;
    static int isSelected = -1;
    public TypeAdapter(Context context, ArrayList<Type> types, OnTypeClickListener onTypeClickListener) {
        this.context = context;
        TypeAdapter.types = types;
        this.onTypeClickListener = onTypeClickListener;
    }
    public interface OnTypeClickListener {
        void onTypeClick(int typeId);
    }
    @NonNull
    @Override
    public TypeAdapter.TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.type_layout, parent, false);
        return new TypeViewHolder(view, onTypeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeAdapter.TypeViewHolder holder, int position) {
        Type type = types.get(position);
        holder.tvTypeName.setText(type.getType());
        holder.tvCountNote.setText(String.valueOf(countNoteInType(type.getId())));

        if(isSelected == position) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.selected_background));
        }
        else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return types.size();
    }
    final class TypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTypeName;
        TextView tvCountNote;
        CardView cardView;
        OnTypeClickListener onTypeClickListener;

        public TypeViewHolder(@NonNull View itemView, OnTypeClickListener onTypeClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTypeName = itemView.findViewById(R.id.nameType);
            tvCountNote = itemView.findViewById(R.id.countNoteInType);
            cardView = itemView.findViewById(R.id.cardViewType);
            this.onTypeClickListener = onTypeClickListener;
        }
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            if (position != RecyclerView.NO_POSITION && onTypeClickListener != null) {
                onTypeClickListener.onTypeClick(types.get(position).getId());

                isSelected = position;
                notifyDataSetChanged();
            }
        }
    }
    private int countNoteInType(int id_type) {
        int count = 0;
        for (Note note : NotesFragment.list) {
            if (id_type == note.getId_type()) {
                count++;
            }
        }
        return count;
    }
    public void addType(Type type) {
        types.add(type);
        notifyItemInserted(types.size() - 1);
    }

}
