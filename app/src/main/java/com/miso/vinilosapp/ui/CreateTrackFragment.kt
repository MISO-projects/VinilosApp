package com.miso.vinilosapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.cache.AlbumsCacheManager
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.data.repositories.SongRepository
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import com.miso.vinilosapp.databinding.FragmentCreateTrackBinding
import com.miso.vinilosapp.ui.viewmodels.CreateTrackViewModel

class CreateTrackFragment : Fragment() {
    private var _binding: FragmentCreateTrackBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: CreateTrackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateTrackBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.backBtnTrack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.createTrackButton.setOnClickListener {
            val trackName = binding.trackNameEditText.text.toString()
            val trackDuration = binding.trackDurationEditText.text.toString()

            if (trackName.isEmpty() || trackDuration.isEmpty()) {
                Toast.makeText(
                    context,
                    "Por favor, complete los campos obligatorios.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val albumId =
                    viewModel.albums.value?.get(binding.albumSpinner.selectedItemPosition)?.albumId!!
                viewModel.addSongToAlbum(albumId, trackName, trackDuration)
                binding.loadingIndicator.visibility = View.VISIBLE
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_albums)
        val args: AlbumDetailFragmentArgs by navArgs()

        viewModel =
            ViewModelProvider(
                this,
                CreateTrackViewModel.Factory(
                    activity.application,
                    AlbumRepository(
                        AlbumsCacheManager(requireActivity()),
                        NetworkServiceAdapter.apiService
                    ),
                    SongRepository(activity.application)
                )
            )[CreateTrackViewModel::class.java]
        viewModel.albums.observe(
            viewLifecycleOwner,
            Observer<List<Album>> {
                it.apply {
                    val adapter = ArrayAdapter(
                        requireActivity(),
                        android.R.layout.simple_dropdown_item_1line,
                        this.map { it.name }
                    )
                    binding.albumSpinner.setAdapter(adapter)
                    binding.albumSpinner.setSelection(it.indexOf(it.find { it.albumId == args.albumId }))
                }
            }
        )
        viewModel.newSong.observe(
            viewLifecycleOwner,
            Observer {
                it?.apply {
                    binding.trackNameEditText.text.clear()
                    binding.trackDurationEditText.text.clear()
                }
                binding.loadingIndicator.visibility = View.GONE
                Toast.makeText(requireContext().applicationContext, "Canción agregada exitosamente.", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        )
        viewModel.eventNetworkError.observe(
            viewLifecycleOwner,
            Observer<Boolean> { isNetworkError ->
                if (isNetworkError) onNetworkError()
            }
        )
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}
