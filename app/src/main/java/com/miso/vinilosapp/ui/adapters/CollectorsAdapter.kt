package com.miso.vinilosapp.ui.adapters;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView;

import com.miso.vinilosapp.R;
import com.miso.vinilosapp.data.models.Collector;
import com.miso.vinilosapp.databinding.ItemCollectorBinding;

class CollectorsAdapter : RecyclerView.Adapter<CollectorsAdapter.CollectorViewHolder>() {

    var collectors: List<Collector> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val withDataBinding: ItemCollectorBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CollectorViewHolder.LAYOUT,
            parent,
            false
        )
        return CollectorViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.collector = collectors[position]
        }
    }

    override fun getItemCount(): Int {
        return collectors.size
    }


    class CollectorViewHolder(val viewDataBinding: ItemCollectorBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_collector
        }
    }
}