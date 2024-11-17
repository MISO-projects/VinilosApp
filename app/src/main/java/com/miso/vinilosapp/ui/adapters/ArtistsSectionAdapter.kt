package com.miso.vinilosapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.models.Artist
import com.miso.vinilosapp.databinding.ItemCardArtistBinding

class ArtistsSectionAdapter(
    private val onItemClick: (Artist) -> Unit
) : RecyclerView.Adapter<ArtistsSectionAdapter.ArtistViewHolder>() {
    var artistItems: List<Artist> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding: ItemCardArtistBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_card_artist,
            parent,
            false
        )

        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(artistItems[position])
    }

    override fun getItemCount(): Int = artistItems.size

    inner class ArtistViewHolder(val binding: ItemCardArtistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: Artist) {
            binding.artist = artist
            Glide.with(binding.root.context)
                .load(artist.image)
                .placeholder(R.drawable.img_singer)
                .error(R.drawable.img_singer)
                .into(binding.artistImage)

            binding.root.setOnClickListener {
                onItemClick(artist)
            }
        }
    }
}
