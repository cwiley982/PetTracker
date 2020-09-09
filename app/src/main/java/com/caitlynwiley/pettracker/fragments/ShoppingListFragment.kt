package com.caitlynwiley.pettracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caitlynwiley.pettracker.R
import com.caitlynwiley.pettracker.fragments.ShoppingListFragment.MyAdapter.MyViewHolder
import java.util.*

class ShoppingListFragment : Fragment() {
    var mList: MutableList<String>? = null
    lateinit var mNewItemEditText: EditText
    lateinit var mAddButton: ImageButton
    private lateinit var mFragView: View
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<*>
    lateinit var mManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mFragView = inflater.inflate(R.layout.shopping_list_fragment, container, false)
        mNewItemEditText = mFragView.findViewById(R.id.new_list_item)
        mAddButton = mFragView.findViewById(R.id.add_list_item)
        mAddButton.setOnClickListener(View.OnClickListener {
            val newItem = mNewItemEditText.getText().toString()
            mList!!.add(newItem)
            mNewItemEditText.setText("")
            mAdapter!!.notifyDataSetChanged()
        })
        mRecyclerView = mFragView.findViewById(R.id.recyclerView)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.isClickable = true
        mManager = LinearLayoutManager(context)
        mRecyclerView.layoutManager = mManager
        mAdapter = MyAdapter()
        mRecyclerView.adapter = mAdapter
        val ith = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                mManager.moveView(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // honestly idk???
                mManager.removeView(viewHolder.itemView)
                mAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        })

        //ith.attachToRecyclerView(mRecyclerView);
        return mFragView
    }

    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>() /*implements ItemTouchHelperAdapter*/ {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val item = LayoutInflater.from(parent.context)
                    .inflate(R.layout.shopping_list_item, parent, false) as CheckedTextView
            item.setOnClickListener { v ->
                val ctv = v as CheckedTextView
                if (ctv.isChecked) {
                    ctv.isChecked = false
                    val text = ctv.text.toString()
                    mList!!.remove(text)
                    mList!!.add(text)
                    mAdapter!!.notifyDataSetChanged()
                } else {
                    ctv.isChecked = true
                }
            }
            return MyViewHolder(item)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textView.text = mList!![position]
        }

        override fun getItemCount(): Int {
            return mList!!.size
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
        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textView: CheckedTextView

            init {
                textView = itemView as CheckedTextView
            }
        }
    }
}