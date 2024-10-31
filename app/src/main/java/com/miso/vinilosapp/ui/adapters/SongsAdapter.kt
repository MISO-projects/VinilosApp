package com.miso.vinilosapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.miso.vinilosapp.R
import com.miso.vinilosapp.databinding.SongItemBinding
import com.miso.vinilosapp.data.models.Song

class SongsAdapter : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    var songs: List<Song> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val withDataBinding: SongItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            SongViewHolder.LAYOUT,
            parent,
            false
        )
        return SongViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.song = songs[position]
        }
//        holder.viewDataBinding.root.setOnClickListener {
//            val action =
//                AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(songs[position].songId)
//            holder.viewDataBinding.root.findNavController().navigate(action)
//        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }


    class SongViewHolder(val viewDataBinding: SongItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.song_item
        }
    }


}