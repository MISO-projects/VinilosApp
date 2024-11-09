package com.miso.vinilosapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.models.Song
import com.miso.vinilosapp.databinding.SongItemBinding

class SongsAdapter() : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    var songItems: List<Song> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding: SongItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            SongViewHolder.LAYOUT,
            parent,
            false
        )
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.song = songItems[position]
        }
    }

    override fun getItemCount(): Int {
        return songItems.size
    }

    class SongViewHolder(val viewDataBinding: SongItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.song_item
        }
    }
}
