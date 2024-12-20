package com.miso.vinilosapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.cache.AlbumsCacheManager
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import com.miso.vinilosapp.databinding.FragmentAlbumBinding
import com.miso.vinilosapp.ui.adapters.AlbumsAdapter
import com.miso.vinilosapp.ui.viewmodels.AlbumViewModel

class AlbumFragment : Fragment() {
    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AlbumViewModel
    private var viewModelAdapter: AlbumsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = AlbumsAdapter { album ->
            val action = AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(album.albumId)
            view.findNavController().navigate(action)
        }

        val collapsingToolbar = binding.collapsingToolbar

        binding.toolbar.setTitle("")

        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.setSupportActionBar(binding.toolbar)
            if (activity.supportActionBar != null) {
                activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
        }

        collapsingToolbar.isTitleEnabled = false

        binding.appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow: Boolean = false
            var scrollRange: Int = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.toolbar.setTitle(binding.title.getText())
                    isShow = true
                } else if (isShow) {
                    binding.toolbar.setTitle("")
                    isShow = false
                }
            }
        })

        binding.fabAddAlbum.setOnClickListener {
            val action = AlbumFragmentDirections.actionAlbumFragmentToAddAlbumFragment()
            view.findNavController().navigate(action)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.albumsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshDataFromRepository()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_albums)
        viewModel =
            ViewModelProvider(
                this,
                AlbumViewModel.Factory(
                    activity.application,
                    AlbumRepository(AlbumsCacheManager(requireActivity()), NetworkServiceAdapter.apiService)
                )
            )[AlbumViewModel::class.java]
        viewModel.albums.observe(
            viewLifecycleOwner,
            Observer<List<Album>> {
                it.apply {
                    viewModelAdapter!!.albums = this
                }
            }
        )
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
}
