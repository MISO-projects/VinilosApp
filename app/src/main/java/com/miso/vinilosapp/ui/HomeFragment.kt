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
import com.miso.vinilosapp.databinding.FragmentHomeBinding
import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.ui.adapters.HomeAdapter
import com.miso.vinilosapp.viewmodels.AlbumViewModel


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AlbumViewModel
    private var viewModelAdapter: HomeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = HomeAdapter(
            onTitleClick = {
                val action = HomeFragmentDirections.actionHomeFragmentToAlbumFragment()
                view.findNavController().navigate(action)
            },
            onAlbumItemClick = { album ->
                val action = HomeFragmentDirections.actionHomeFragmentToAlbumDetailFragment(album.albumId)
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
        activity.actionBar?.title = getString(R.string.title_albums)
        viewModel = ViewModelProvider(
            this,
            AlbumViewModel.Factory(activity.application, AlbumRepository())
        ).get(AlbumViewModel::class.java)
        viewModel.albums.observe(viewLifecycleOwner) {
            it.apply {
                viewModelAdapter!!.albumItems = this
            }
        }
        viewModel.eventNetworkError.observe(
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
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}