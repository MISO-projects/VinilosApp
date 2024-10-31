package com.miso.vinilosapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miso.vinilosapp.R
import com.miso.vinilosapp.databinding.AlbumItemBinding
import com.miso.vinilosapp.databinding.ItemCardAlbumBinding
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.ui.AlbumFragmentDirections

class AlbumsSectionAdapter(
    private val onItemClick: (Album) -> Unit
) : RecyclerView.Adapter<AlbumsSectionAdapter.AlbumViewHolder>() {
    var albumItems: List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding: ItemCardAlbumBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_card_album,
            parent,
            false
        )

        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albumItems[position])
    }

    override fun getItemCount(): Int = albumItems.size

    inner class AlbumViewHolder( val binding: ItemCardAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.album = album
            Glide.with(binding.root.context)
                .load(album.cover)
                .placeholder(R.drawable.img_the_band_party)
                .error(R.drawable.img_the_band_party)
                .into(binding.albumCover)

            binding.root.setOnClickListener {
                onItemClick(album)
            }
        }
    }
}