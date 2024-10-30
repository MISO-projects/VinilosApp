package com.miso.vinilosapp.ui.adapters

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miso.vinilosapp.R
import com.miso.vinilosapp.databinding.AlbumItemBinding
import com.miso.vinilosapp.databinding.ItemAlbumSectionBinding
import com.miso.vinilosapp.databinding.ItemGreetingBinding
import com.miso.vinilosapp.models.Album
import com.miso.vinilosapp.ui.AlbumFragmentDirections

class HomeAdapter(
    private val onTitleClick: () -> Unit,
    private val onAlbumItemClick: (Album) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var albumItems: List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    companion object {
        const val VIEW_TYPE_GREETING = 0
        const val VIEW_TYPE_ALBUMS = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GREETING -> {
                val view =
                    ItemGreetingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GreetingViewHolder(view)
            }

            else -> {
                val view = ItemAlbumSectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AlbumSectionViewHolder(view, onTitleClick, onAlbumItemClick)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_GREETING
            else -> VIEW_TYPE_ALBUMS
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GreetingViewHolder -> holder.bind()
            is AlbumSectionViewHolder -> holder.bind(albumItems)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    class GreetingViewHolder(private val binding: ItemGreetingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            val greeting = "Hola, "
            val name = "Miso"

            val spannable = SpannableString(greeting + name)

            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                greeting.length, greeting.length + name.length,
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
}