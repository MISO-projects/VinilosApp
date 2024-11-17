package com.miso.vinilosapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.models.Artist
import com.miso.vinilosapp.databinding.ArtistItemBinding
import com.miso.vinilosapp.ui.ArtistFragmentDirections
import java.text.SimpleDateFormat
import java.util.Locale

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
            .placeholder(R.drawable.img_singer)
            .error(R.drawable.img_singer)
            .into(holder.viewDataBinding.imageViewArtist)

        holder.viewDataBinding.root.setOnClickListener {
            val action = ArtistFragmentDirections.actionArtistFragmentToArtistDetailFragment(artists[position].artistId)
            holder.viewDataBinding.root.findNavController().navigate(action)
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
