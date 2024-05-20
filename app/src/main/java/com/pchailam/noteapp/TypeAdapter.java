package com.pchailam.noteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeViewHolder> {
    Context context;
    static ArrayList<Type> types;

    public TypeAdapter(Context context, ArrayList<Type> types) {
        this.context = context;
        this.types = types;
    }
    @NonNull
    @Override
    public TypeAdapter.TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.type_layout, parent, false);
        return new TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeAdapter.TypeViewHolder holder, int position) {
        Type type = types.get(position);
        holder.tvTypeName.setText(type.getType());
        holder.tvCountNote.setText(String.valueOf(countNoteInType(type.getId())));
    }

    @Override
    public int getItemCount() {
        return types.size();
    }
    static final class TypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTypeName;
        TextView tvCountNote;

        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTypeName = itemView.findViewById(R.id.nameType);
            tvCountNote = itemView.findViewById(R.id.countNoteInType);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Type type = types.get(clickedPosition);
            Toast.makeText(v.getContext(), "Clicked: " + type.getType(), Toast.LENGTH_SHORT).show();
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
