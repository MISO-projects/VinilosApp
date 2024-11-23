package com.miso.vinilosapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.models.Collector
import com.miso.vinilosapp.databinding.ItemCardCollectorBinding

class CollectorSectionAdapter(
    private val onItemClick: (Collector) -> Unit
) :
    RecyclerView.Adapter<CollectorSectionAdapter.CollectorViewHolder>() {
    var collectorItems: List<Collector> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val binding: ItemCardCollectorBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_card_collector,
            parent,
            false
        )

        return CollectorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.bind(collectorItems[position])
    }

    override fun getItemCount(): Int = collectorItems.size

    inner class CollectorViewHolder(val binding: ItemCardCollectorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(collector: Collector) {
            Log.d("CollectorSectionAdapter", "bind: $collector")
            binding.collector = collector

            binding.root.setOnClickListener {
                onItemClick(collector)
            }
        }
    }
}
