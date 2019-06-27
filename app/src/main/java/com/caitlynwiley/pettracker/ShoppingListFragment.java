package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingListFragment extends Fragment {

    List<String> mList;
    EditText mNewItemEditText;
    ImageButton mAddButton;
    View mFragView;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.shopping_list_fragment, container, false);
        mNewItemEditText = mFragView.findViewById(R.id.new_list_item);
        mAddButton = mFragView.findViewById(R.id.add_list_item);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = mNewItemEditText.getText().toString();
                mList.add(newItem);
                mNewItemEditText.setText("");
                mAdapter.notifyDataSetChanged();
            }
        });

        mRecyclerView = mFragView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setClickable(true);
        mManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper ith = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                mManager.moveView(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // honestly idk???
                mManager.removeView(viewHolder.itemView);
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

        //ith.attachToRecyclerView(mRecyclerView);
        return mFragView;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> /*implements ItemTouchHelperAdapter*/ {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CheckedTextView item = (CheckedTextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shopping_list_item, parent, false);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckedTextView ctv = (CheckedTextView) v;
                    if (ctv.isChecked()) {
                        ctv.setChecked(false);
                        String text = ctv.getText().toString();
                        mList.remove(text);
                        mList.add(text);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ctv.setChecked(true);
                    }
                }
            });
            return new MyViewHolder(item);
        }

        @Override
        public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {
            holder.textView.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
/*
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onItemDismiss(int position) {
            mList.remove(position);
            mAdapter.notifyItemRemoved(position);
        }*/

        class MyViewHolder extends RecyclerView.ViewHolder {

            CheckedTextView textView;

            MyViewHolder(View itemView) {
                super(itemView);
                textView = (CheckedTextView) itemView;
            }
        }
    }
}
