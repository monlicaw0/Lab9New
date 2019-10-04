package com.myweb.lab9new

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.edit_delete_item_layout.view.*

class EditStudentsAdapter(val items : List<Student>, val context: Context) :
    RecyclerView.Adapter<EditStudentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view_item = LayoutInflater.from(parent.context).inflate(R.layout.edit_delete_item_layout, parent, false)
        return ViewHolder(view_item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvID?.text =  items[position].std_id
        holder.tvName?.text =  items[position].std_name
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each student to
        val tvID = view.tvID
        val tvName = view.tvName
    }
}