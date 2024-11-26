package com.miso.vinilosapp.ui

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.cache.AlbumsCacheManager
import com.miso.vinilosapp.data.database.VinylRoomDatabase
import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.data.repositories.SongRepository
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import com.miso.vinilosapp.databinding.FragmentAlbumDetailBinding
import com.miso.vinilosapp.ui.adapters.SongsAdapter
import com.miso.vinilosapp.ui.viewmodels.AlbumDetailViewModel
import com.miso.vinilosapp.ui.viewmodels.SongViewModel

class AlbumDetailFragment : Fragment() {
    private var _binding: FragmentAlbumDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var songSectionRecyclerView: RecyclerView

    private lateinit var viewModel: AlbumDetailViewModel
    private lateinit var songViewModel: SongViewModel

    private var songViewModelAdapter: SongsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        songViewModelAdapter = SongsAdapter()

        return view
    }

    override fun onResume() {
        super.onResume()
        songViewModel.refreshDataFromRepository()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        songSectionRecyclerView = binding.songItemRv
        songSectionRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        songSectionRecyclerView.adapter = songViewModelAdapter

        val itemDecoration = HorizontalSpaceItemDecoration(32)
        songSectionRecyclerView.addItemDecoration(itemDecoration)

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_comments)
        val args: AlbumDetailFragmentArgs by navArgs()
        Log.d("Args", args.albumId.toString())
        val context = activity.application
        viewModel = ViewModelProvider(
            this,
            AlbumDetailViewModel.Factory(
                context,
                AlbumRepository(
                    AlbumsCacheManager(requireActivity()),
                    NetworkServiceAdapter.apiService
                ),
                args.albumId
            )
        )[AlbumDetailViewModel::class.java]

        songViewModel = ViewModelProvider(
            this,
            SongViewModel.Factory(
                context,
                SongRepository(
                    context,
                    VinylRoomDatabase.getDatabase(context).songDao()
                ),
                args.albumId
            )
        )[SongViewModel::class.java]

        songViewModel.songs.observe(viewLifecycleOwner) {
            it?.let {
                songViewModelAdapter?.songItems = it
            }
        }

        viewModel.album.observe(viewLifecycleOwner) {
            it.apply {
                binding.albumTitle.text = this.name
                binding.albumGenre.text = this.recordLabel + " • " + this.genre
                binding.albumDescription.text = this.description

                songViewModelAdapter?.songItems = this.tracks

                binding.txtCancionesSection.text = getText(R.string.title_songs)
                val drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_add)
                binding.txtCancionesSection.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null
                )
                binding.txtCancionesSection.compoundDrawablePadding = 8
                binding.txtCancionesSection.setOnClickListener {
                    val action =
                        AlbumDetailFragmentDirections
                            .actionAlbumDetailFragmentToCreateTrackFragment(this.albumId)
                    findNavController().navigate(action)
                }

                Glide.with(binding.root.context)
                    .load(this.cover)
                    .placeholder(R.drawable.img_the_band_party)
                    .error(R.drawable.img_the_band_party)
                    .apply(
                        RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .into(binding.albumImage)
            }
        }

        viewModel.eventNetworkError.observe(
            viewLifecycleOwner,
            Observer<Boolean> { isNetworkError ->
                if (isNetworkError) onNetworkError()
            }
        )
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

    fun goBack() {
        findNavController().navigateUp()
    }

    class HorizontalSpaceItemDecoration(private val spaceWidth: Int) :
        RecyclerView.ItemDecoration() {
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
