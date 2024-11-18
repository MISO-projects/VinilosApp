package com.miso.vinilosapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.cache.AlbumsCacheManager
import com.miso.vinilosapp.data.database.VinylRoomDatabase
import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.data.repositories.ArtistRepository
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import com.miso.vinilosapp.databinding.FragmentArtistDetailBinding
import com.miso.vinilosapp.ui.adapters.AlbumsAdapter
import com.miso.vinilosapp.ui.viewmodels.AlbumViewModel
import com.miso.vinilosapp.ui.viewmodels.ArtistDetailViewModel

class ArtistDetailFragment : Fragment() {
    private var _binding: FragmentArtistDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var albumRecyclerView: RecyclerView
    private lateinit var artistViewModel: ArtistDetailViewModel
    private lateinit var albumsViewModel: AlbumViewModel
    private var albumsViewModelAdapter: AlbumsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        albumsViewModelAdapter = AlbumsAdapter { album ->
            val action = ArtistDetailFragmentDirections.actionArtistDetailFragmentToAlbumDetailFragment(album.albumId)
            view.findNavController().navigate(action)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        albumRecyclerView = binding.albumsRv
        albumRecyclerView.layoutManager = LinearLayoutManager(context)
        albumRecyclerView.adapter = albumsViewModelAdapter

        binding.backBtnArtist.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_artist_detail)
        val args: ArtistDetailFragmentArgs by navArgs()
        val application = activity.application
        artistViewModel = ViewModelProvider(
            this,
            ArtistDetailViewModel.Factory(
                application,
                ArtistRepository(
                    application,
                    VinylRoomDatabase.getDatabase(application).artistDao()
                ),
                args.artistId
            )
        )[ArtistDetailViewModel::class.java]

        albumsViewModel = ViewModelProvider(
            this,
            AlbumViewModel.Factory(application, AlbumRepository(AlbumsCacheManager(requireActivity()), NetworkServiceAdapter.apiService))
        )[AlbumViewModel::class.java]

        artistViewModel.artist.observe(viewLifecycleOwner) {
            it.apply {
                binding.artistName.setText(this.name)
                binding.artistDescription.setText(this.description)
                val formattedDate = this.birthDate.split("T")[0]
                binding.artistBirthdate.setText("Músico(a) • $formattedDate")

                albumsViewModelAdapter?.albums = this.albums

                if (this.albums.isNotEmpty()) {
                    binding.artistAlbumsTitle.setText(R.string.collapsing_toolbar_album_title)
                    binding.artistAlbumsSubtitle.setText(R.string.subtitle_artist_detail_albums)
                }

                Glide.with(binding.root.context).load(this.image)
                    .placeholder(R.drawable.img_the_band_party).error(R.drawable.img_the_band_party)
                    .into(binding.artistImage)
            }
        }

        artistViewModel.eventNetworkError.observe(
            viewLifecycleOwner,
            { isNetworkError ->
                if (isNetworkError) onNetworkError()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if (!artistViewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            artistViewModel.onNetworkErrorShown()
        }
    }
}
