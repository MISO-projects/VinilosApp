package com.miso.vinilosapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


import com.miso.vinilosapp.R
import com.miso.vinilosapp.databinding.ItemCardAlbumBinding
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.Song

class AlbumDetailAdapter(
    private val album: Album,
    private val songs: List<Song>,
    private val onSongItemClick: (Song) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding: ItemCardAlbumBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_card_album,
            parent,
            false
        )
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AlbumViewHolder).bind(album)
    }

    override fun getItemCount(): Int = 1

    inner class AlbumViewHolder(
       private val binding: ItemCardAlbumBinding,
       private val onSongItemClick: (Song) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            Glide.with(binding.root.context)
                .load(album.cover)
                .placeholder(R.drawable.img_the_band_party)
                .error(R.drawable.img_the_band_party)
                .into(binding.albumCover)
        }
    }
}
