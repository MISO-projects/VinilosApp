package com.miso.vinilosapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
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
        val withDataBinding: ItemAlbumDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AlbumDetailViewHolder.LAYOUT,
            parent,
            false
        )
        return AlbumDetailViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AlbumDetailViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.album = album
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    class AlbumDetailViewHolder(val viewDataBinding: ItemAlbumDetailBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.item_album_detail
        }
    }


}