package com.m7.forecasto.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<B : ViewDataBinding, D>(
    @LayoutRes private val itemRes: Int,
    private var data: List<D>
) : RecyclerView.Adapter<BaseAdapter<B, D>.ViewHolder>() {

    abstract fun bindItem(itemView: B, itemData: D, pos: Int)

    fun updateData(newData: List<D>){
        data = newData
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                itemRes,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(private val iView: B) : RecyclerView.ViewHolder(iView.root) {
        fun bind(itemData: D) {
            bindItem(iView, itemData, adapterPosition)
        }
    }
}