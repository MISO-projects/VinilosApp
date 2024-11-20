package com.miso.vinilosapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.cache.AlbumsCacheManager
import com.miso.vinilosapp.data.database.VinylRoomDatabase
import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.data.repositories.ArtistRepository
import com.miso.vinilosapp.data.repositories.CollectorRepository
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import com.miso.vinilosapp.databinding.FragmentHomeBinding
import com.miso.vinilosapp.ui.adapters.HomeAdapter
import com.miso.vinilosapp.ui.viewmodels.AlbumViewModel
import com.miso.vinilosapp.ui.viewmodels.ArtistViewModel
import com.miso.vinilosapp.ui.viewmodels.CollectorViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var artistViewModel: ArtistViewModel
    private lateinit var collectorViewModel: CollectorViewModel
    private var viewModelAdapter: HomeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = HomeAdapter(
            onAlbumTitleClick = {
                val action = HomeFragmentDirections.actionHomeFragmentToAlbumFragment()
                view.findNavController().navigate(action)
            },
            onArtistTitleClick = {
                val action = HomeFragmentDirections.actionHomeFragmentToArtistFragment()
                view.findNavController().navigate(action)
            },
            onCollectorTitleClick = {
                val action = HomeFragmentDirections.actionHomeFragmentToCollectorFragment()
                view.findNavController().navigate(action)
            },
            onAlbumItemClick = { album ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToAlbumDetailFragment(album.albumId)
                view.findNavController().navigate(action)
            },
            onArtistItemClick = { artist ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToArtistDetailFragment(artist.artistId)
                view.findNavController().navigate(action)
            },
            onCollectorItemClick = { collector ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToCollectorDetailFragment(collector.collectorId)
                view.findNavController().navigate(action)
            }
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.homeRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        // Albums
        activity.actionBar?.title = getString(R.string.title_albums)
        val application = activity.application
        albumViewModel = ViewModelProvider(
            this,
            AlbumViewModel.Factory(
                application,
                AlbumRepository(
                    AlbumsCacheManager(requireActivity()),
                    NetworkServiceAdapter.apiService
                )
            )
        ).get(AlbumViewModel::class.java)
        albumViewModel.albums.observe(viewLifecycleOwner) {
            it.apply {
                viewModelAdapter!!.albumItems = this
            }
        }
        albumViewModel.eventNetworkError.observe(
            viewLifecycleOwner
        ) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }

        // Artists
        artistViewModel = ViewModelProvider(
            this,
            ArtistViewModel.Factory(
                application,
                ArtistRepository(
                    application,
                    VinylRoomDatabase.getDatabase(application).artistDao()
                )
            )
        ).get(ArtistViewModel::class.java)
        artistViewModel.artists.observe(viewLifecycleOwner) {
            it.apply {
                viewModelAdapter!!.artistItems = this
            }
        }
        artistViewModel.eventNetworkError.observe(
            viewLifecycleOwner
        ) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }

        // Collectors
        collectorViewModel = ViewModelProvider(
            this,
            CollectorViewModel.Factory(
                application,
                CollectorRepository(
                    application,
                    VinylRoomDatabase.getDatabase(application).collectorDao()
                )
            )
        )[CollectorViewModel::class.java]
        collectorViewModel.collectors.observe(viewLifecycleOwner) {
            it.apply {
                viewModelAdapter!!.collectorItems = this
            }
        }
        collectorViewModel.eventNetworkError.observe(
            viewLifecycleOwner
        ) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if (!albumViewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            albumViewModel.onNetworkErrorShown()
        }
    }
}
