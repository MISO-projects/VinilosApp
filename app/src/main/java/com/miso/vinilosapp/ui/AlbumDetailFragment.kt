package com.miso.vinilosapp.ui

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import android.widget.Toast

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miso.vinilosapp.R

import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.data.repositories.SongRepository

import com.miso.vinilosapp.databinding.FragmentAlbumDetailBinding
import com.miso.vinilosapp.ui.adapters.SongsAdapter
import com.miso.vinilosapp.viewmodels.AlbumDetailViewModel
import com.miso.vinilosapp.viewmodels.SongViewModel


class AlbumDetailFragment : Fragment() {
    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var songSectionRecyclerView: RecyclerView

    private lateinit var viewModel: AlbumDetailViewModel
    private lateinit var songViewModel: SongViewModel

    private var songViewModelAdapter: SongsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        songViewModelAdapter = SongsAdapter()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        songSectionRecyclerView = binding.songItemRv
        songSectionRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        songSectionRecyclerView.adapter = songViewModelAdapter

        val itemDecoration = HorizontalSpaceItemDecoration(32)
        songSectionRecyclerView.addItemDecoration(itemDecoration)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_comments)
        val args: AlbumDetailFragmentArgs by navArgs()
        Log.d("Args", args.albumId.toString())
        viewModel = ViewModelProvider(
            this, AlbumDetailViewModel.Factory(
                activity.application,
                AlbumRepository(),
                args.albumId
            )
        )[AlbumDetailViewModel::class.java]

        songViewModel = ViewModelProvider(
            this, SongViewModel.Factory(
                activity.application,
                SongRepository(),
                args.albumId
            )
        )[SongViewModel::class.java]

        viewModel.album.observe(viewLifecycleOwner) {
            it.apply {
                binding.albumTitle.setText(this.name)
                binding.albumGenre.setText(this.recordLabel + " • " + this.genre)
                binding.albumDescription.setText(this.description)

                songViewModelAdapter?.songItems = this.tracks

                Glide.with(binding.root.context)
                    .load(this.cover)
                    .placeholder(R.drawable.img_the_band_party)
                    .error(R.drawable.img_the_band_party)
                    .into(binding.albumImage)
            }
        }

        viewModel.eventNetworkError.observe(
            viewLifecycleOwner,
            Observer<Boolean> { isNetworkError ->
                if (isNetworkError) onNetworkError()
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    class HorizontalSpaceItemDecoration(private val spaceWidth: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = spaceWidth
        }
    }
}