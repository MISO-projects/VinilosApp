package com.miso.vinilosapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.databinding.ItemAlbumDetailBinding

class AlbumDetailAdapter : RecyclerView.Adapter<AlbumDetailAdapter.AlbumDetailViewHolder>() {

    var album: Album? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumDetailViewHolder {
        val binding: ItemAlbumDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_album_detail,
            parent,
            false
        )
        return AlbumDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumDetailViewHolder, position: Int) {
        album?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class AlbumDetailViewHolder(val binding: ItemAlbumDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.album = album
            Glide.with(binding.root.context)
                .load(album.cover)
                .placeholder(R.drawable.img_the_band_party)
                .error(R.drawable.img_the_band_party)
                .into(binding.albumImage)
        }
    }


}