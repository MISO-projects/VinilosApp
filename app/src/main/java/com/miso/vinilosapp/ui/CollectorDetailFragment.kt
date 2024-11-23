package com.miso.vinilosapp.ui

import android.os.Bundle
import android.util.Log
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
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.database.VinylRoomDatabase
import com.miso.vinilosapp.data.repositories.CollectorRepository
import com.miso.vinilosapp.databinding.FragmentCollectorDetailBinding
import com.miso.vinilosapp.ui.adapters.AlbumsAdapter
import com.miso.vinilosapp.ui.viewmodels.CollectorDetailViewModel

class CollectorDetailFragment : Fragment() {
    private var _binding: FragmentCollectorDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var albumSectionRecyclerView: RecyclerView
    private lateinit var viewModel: CollectorDetailViewModel
    private var albumViewModelAdapter: AlbumsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        albumViewModelAdapter = AlbumsAdapter { album ->
            val action = AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(album.albumId)
            view.findNavController().navigate(action)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        albumSectionRecyclerView = binding.albumsCollectorRv
        albumSectionRecyclerView.layoutManager = LinearLayoutManager(context)
        albumSectionRecyclerView.adapter = albumViewModelAdapter

        binding.backBtnCollector.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        activity.actionBar?.title = getString(R.string.collapsing_toolbar_collector_title)

        val args: CollectorDetailFragmentArgs by navArgs()
        val application = activity.application
        Log.d("test123", args.collectorId.toString())
        viewModel = ViewModelProvider(
            this,
            CollectorDetailViewModel.Factory(
                application,
                CollectorRepository(application,  VinylRoomDatabase.getDatabase(application).collectorDao()),
                args.collectorId
            )
        )[CollectorDetailViewModel::class.java]

        viewModel.collector.observe(viewLifecycleOwner) {
            it.apply {
                binding.collectorName.setText(this.name)
                binding.collectorInfo.setText(this.telephone + " • " + this.email)

                albumViewModelAdapter?.albums = this.collectorAlbums

                if (this.collectorAlbums.isNotEmpty()) {
                    binding.collectorAlbumsTitle.setText("Álbumes")
                    binding.collectorAlbumsSubtitle.setText("Navega los albumes coleccionados")
                }
            }
        }

        viewModel.eventNetworkError.observe(
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
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}
