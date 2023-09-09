package ru.example.androidapp.presentation.booking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import ru.example.androidapp.R

class RecycleAdapter(var context: Context, val list: MutableList<ParentData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == Constants.PARENT) {
            val rowView: View =
                LayoutInflater.from(parent.context).inflate(R.layout.parent_row, parent, false)
            GroupViewHolder(rowView)
        } else {
            val rowView: View =
                LayoutInflater.from(parent.context).inflate(R.layout.child_row, parent, false)
            ChildViewHolder(rowView)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataList = list[position]
        if (dataList.type == Constants.PARENT) {
            holder as GroupViewHolder
            holder.apply {
                parentTV?.text = dataList.parentTitle
                downIV?.setOnClickListener {
                    expandOrCollapseParentItem(dataList, position, holder)
                }
            }
        } else {
            holder as ChildViewHolder

            holder.apply {
                val singleService = dataList.subList.first()
                childTil?.hint = singleService.childTitle
            }
        }
    }

    private fun expandOrCollapseParentItem(singleBoarding: ParentData, position: Int, holder: GroupViewHolder) {
        if (singleBoarding.isExpanded) {
            collapseParentRow(position)
            holder.downIV?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_down))
        } else {
            expandParentRow(position)
            holder.downIV?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_up))
        }
    }

    private fun expandParentRow(position: Int) {
        val currentBoardingRow = list[position]
        val services = currentBoardingRow.subList
        currentBoardingRow.isExpanded = true
        var nextPosition = position
        if (currentBoardingRow.type == Constants.PARENT) {
            services.forEach { service ->
                val parentModel = ParentData()
                parentModel.type = Constants.CHILD
                val subList: ArrayList<ChildData> = ArrayList()
                subList.add(service)
                parentModel.subList = subList
                list.add(++nextPosition, parentModel)
            }
            notifyDataSetChanged()
        }
    }

    private fun collapseParentRow(position: Int) {
        val currentBoardingRow = list[position]
        val services = currentBoardingRow.subList
        list[position].isExpanded = false
        if (list[position].type == Constants.PARENT) {
            services.forEach { _ ->
                list.removeAt(position + 1)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int = list[position].type

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class GroupViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val parentTV = row.findViewById(R.id.parentTitle) as TextView?
        val downIV = row.findViewById(R.id.parentIcon) as ImageView?
    }

    class ChildViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val childTil = row.findViewById(R.id.childTitle) as TextInputLayout?
    }
}
