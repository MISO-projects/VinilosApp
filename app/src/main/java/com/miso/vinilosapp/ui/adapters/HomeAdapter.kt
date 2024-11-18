package com.miso.vinilosapp.ui.adapters

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.Artist
import com.miso.vinilosapp.data.models.Collector
import com.miso.vinilosapp.databinding.ItemAlbumSectionBinding
import com.miso.vinilosapp.databinding.ItemArtistSectionBinding
import com.miso.vinilosapp.databinding.ItemCollectorSectionBinding
import com.miso.vinilosapp.databinding.ItemGreetingBinding

class HomeAdapter(
    private val onAlbumTitleClick: () -> Unit,
    private val onArtistTitleClick: () -> Unit,
    private val onCollectorTitleClick: () -> Unit,
    private val onAlbumItemClick: (Album) -> Unit,
    private val onArtistItemClick: (Artist) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var albumItems: List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var artistItems: List<Artist> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var collectorItems: List<Collector> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    companion object {
        const val VIEW_TYPE_GREETING = 0
        const val VIEW_TYPE_ARTISTS = 1
        const val VIEW_TYPE_ALBUMS = 2
        const val VIEW_TYPE_COLLECTORS = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GREETING -> {
                val view =
                    ItemGreetingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GreetingViewHolder(view)
            }

            VIEW_TYPE_ARTISTS -> {
                val view = ItemArtistSectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ArtistSectionViewHolder(view, onArtistTitleClick, onArtistItemClick)
            }

            VIEW_TYPE_COLLECTORS -> {
                val view = ItemCollectorSectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CollectorSectionViewHolder(view, onCollectorTitleClick)
            }

            else -> {
                val view = ItemAlbumSectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AlbumSectionViewHolder(view, onAlbumTitleClick, onAlbumItemClick)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_GREETING
            1 -> VIEW_TYPE_ARTISTS
            2 -> VIEW_TYPE_ALBUMS
            else -> VIEW_TYPE_COLLECTORS
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GreetingViewHolder -> holder.bind()
            is AlbumSectionViewHolder -> holder.bind(albumItems)
            is ArtistSectionViewHolder -> holder.bind(artistItems)
            is CollectorSectionViewHolder -> holder.bind(collectorItems)
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    class GreetingViewHolder(private val binding: ItemGreetingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            val greeting = "Hola, "
            val name = "Miso"

            val spannable = SpannableString(greeting + name)

            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                greeting.length,
                greeting.length + name.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.greeting.text = spannable
        }
    }

    class AlbumSectionViewHolder(
        private val binding: ItemAlbumSectionBinding,
        private val onTitleClick: () -> Unit,
        private val onAlbumItemClick: (Album) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(items: List<Album>) {
            val drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_arrow)
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            binding.txtAlbumSection.setCompoundDrawables(null, null, drawable, null)
            binding.txtAlbumSection.compoundDrawablePadding = 8
            binding.recyclerViewAlbumSection.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = AlbumsSectionAdapter(onAlbumItemClick).apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    albumItems = items
                }
            }

            binding.txtAlbumSection.setOnClickListener {
                onTitleClick()
            }
        }
    }

    class ArtistSectionViewHolder(
        private val binding: ItemArtistSectionBinding,
        private val onTitleClick: () -> Unit,
        private val onArtistItemClick: (Artist) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(items: List<Artist>) {
            val drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_arrow)
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            binding.txtArtistSection.setCompoundDrawables(null, null, drawable, null)
            binding.txtArtistSection.compoundDrawablePadding = 8
            binding.recyclerViewArtistSection.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ArtistsSectionAdapter(onArtistItemClick).apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    artistItems = items
                }
            }

            binding.txtArtistSection.setOnClickListener {
                onTitleClick()
            }
        }
    }

    class CollectorSectionViewHolder(
        private val binding: ItemCollectorSectionBinding,
        private val onTitleClick: () -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(items: List<Collector>) {
            val drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_arrow)
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            binding.txtCollectorSection.setCompoundDrawables(null, null, drawable, null)
            binding.txtCollectorSection.compoundDrawablePadding = 8
            binding.recyclerViewCollectorSection.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = CollectorSectionAdapter().apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    collectorItems = items
                }
            }

            binding.txtCollectorSection.setOnClickListener {
                onTitleClick()
            }
        }
    }
}
