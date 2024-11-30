package com.miso.vinilosapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.database.VinylRoomDatabase
import com.miso.vinilosapp.data.models.Artist
import com.miso.vinilosapp.data.repositories.ArtistRepository
import com.miso.vinilosapp.databinding.FragmentArtistBinding
import com.miso.vinilosapp.ui.adapters.ArtistAdapter
import com.miso.vinilosapp.ui.viewmodels.ArtistViewModel

class ArtistFragment : Fragment() {
    private var _binding: FragmentArtistBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ArtistViewModel
    private var viewModelAdapter: ArtistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = ArtistAdapter()

        val collapsingToolbar = binding.collapsingToolbar

        binding.toolbar.setTitle("")

        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.setSupportActionBar(binding.toolbar)
            if (activity.supportActionBar != null) {
                activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                binding.toolbar.navigationIcon?.setTint(
                    ContextCompat.getColor(requireContext(), R.color.black)
                )
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.artistsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_artists)
        val application = activity.application
        viewModel =
            ViewModelProvider(
                this,
                ArtistViewModel.Factory(
                    application,
                    ArtistRepository(
                        application,
                        VinylRoomDatabase.getDatabase(application).artistDao()
                    )
                )
            )[ArtistViewModel::class.java]
        viewModel.artists.observe(
            viewLifecycleOwner,
            Observer<List<Artist>> {
                it.apply {
                    viewModelAdapter!!.artists = this
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
