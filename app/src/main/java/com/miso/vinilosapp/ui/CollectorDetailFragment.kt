package com.miso.vinilosapp.ui

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.database.VinylRoomDatabase
import com.miso.vinilosapp.data.repositories.SongRepository
import com.miso.vinilosapp.data.repositories.CollectorRepository
import com.miso.vinilosapp.databinding.FragmentCollectorDetailBinding
import com.miso.vinilosapp.ui.adapters.AlbumsAdapter
import com.miso.vinilosapp.ui.viewmodels.CollectorDetailViewModel
import com.miso.vinilosapp.ui.viewmodels.SongViewModel

class CollectorDetailFragment : Fragment() {
    private var _binding: FragmentCollectorDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var albumSectionRecyclerView: RecyclerView

    private lateinit var viewModel: CollectorDetailViewModel
    private lateinit var albumViewModel: SongViewModel

    private var albumViewModelAdapter: AlbumsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        albumViewModelAdapter = AlbumsAdapter { album ->
            val action = AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(album.albumId)
            view.findNavController().navigate(action)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        albumSectionRecyclerView = binding.albumsRv
        albumSectionRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        albumSectionRecyclerView.adapter = albumViewModelAdapter

        val itemDecoration = HorizontalSpaceItemDecoration(32)
        albumSectionRecyclerView.addItemDecoration(itemDecoration)

        binding.backBtnCollector.setOnClickListener {
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
            CollectorDetailViewModel.Factory(
                context,
                CollectorRepository(context,  VinylRoomDatabase.getDatabase(context).collectorDao()),
                args.albumId
            )
        )[CollectorDetailViewModel::class.java]

        albumViewModel = ViewModelProvider(
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
