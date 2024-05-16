package com.pchailam.noteapp;

import static android.app.Activity.RESULT_OK;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NotesFragment extends Fragment implements ListNoteAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static ArrayList<Note> list;
    private ListNoteAdapter adapter;
    private RecyclerView recyclerView;
    TextView textViewCount;
    private MyDatabase myDatabase;
    ActivityResultLauncher<Intent> activityResultLauncher;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NotesFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        adapter = new ListNoteAdapter(getActivity(), list);

        LinearLayoutManager listLayoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(listLayoutManager);

        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

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
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 8000 && resultCode == RESULT_OK) {
            if (intent.getBooleanExtra("updateData", false)) {
                adapter.notifyDataSetChanged();

                String sumNote = String.valueOf(list.size());
                textViewCount.setText(sumNote);
            }
        }
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
}