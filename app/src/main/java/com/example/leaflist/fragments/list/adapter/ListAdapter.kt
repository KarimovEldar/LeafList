package com.example.leaflist.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.leaflist.R
import com.example.leaflist.data.model.Priority
import com.example.leaflist.data.model.ToDoData
import com.example.leaflist.databinding.RowLayoutBinding
import com.example.leaflist.fragments.list.ListFragmentDirections
import com.example.leaflist.utils.ToDoDiffUtil

class ListAdapter: RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    var toDoList = emptyList<ToDoData>()

    class ListViewHolder(val binding: RowLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            RowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = toDoList[position]
        holder.binding.titleTextView.text = currentItem.title
        holder.binding.descriptionTextView.text = currentItem.description
        holder.binding.rowBackground.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
        when(currentItem.priority){
            Priority.High ->
                holder.binding.priorityIndicator.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.red
                    )
                )
            Priority.Medium ->
                holder.binding.priorityIndicator.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.blue
                    )
                )
            Priority.Low ->
                holder.binding.priorityIndicator.setBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.orange
                    )
                )
        }
    }

    override fun getItemCount(): Int {
        return toDoList.size
    }

    fun setData(newList : List<ToDoData>){
        val recipesDiffUtil = ToDoDiffUtil(toDoList,newList)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        toDoList = newList
        diffUtilResult.dispatchUpdatesTo(this)
    }

}