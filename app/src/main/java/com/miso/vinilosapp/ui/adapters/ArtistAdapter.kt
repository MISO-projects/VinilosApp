package com.miso.vinilosapp.ui.adapters

import java.text.SimpleDateFormat
import java.util.Locale

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.models.Artist
import com.miso.vinilosapp.databinding.ArtistItemBinding

class ArtistAdapter : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    var artists: List<Artist> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val withDataBinding: ArtistItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ArtistViewHolder.LAYOUT,
            parent,
            false
        )
        return ArtistViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artists[position]

        val artistWithFormattedDate = artist.copy(
            birthDate = formatBirthDate(artist.birthDate)
        )

        holder.viewDataBinding.also {
            it.artist = artistWithFormattedDate
        }
        Glide.with(holder.viewDataBinding.root.context)
            .load(artist.image)
            .placeholder(R.drawable.img_the_band_party)
            .error(R.drawable.img_the_band_party)
            .into(holder.viewDataBinding.imageViewArtist)

        holder.viewDataBinding.root.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    class ArtistViewHolder(val viewDataBinding: ArtistItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.artist_item
        }
    }

    private fun formatBirthDate(date: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate = inputFormat.parse(date)
            parsedDate?.let { outputFormat.format(it) } ?: date
        } catch (e: Exception) {
            date
        }
    }
}
