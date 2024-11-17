package com.miso.vinilosapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.miso.vinilosapp.R
import com.miso.vinilosapp.data.database.VinylRoomDatabase
import com.miso.vinilosapp.data.repositories.CollectorRepository
import com.miso.vinilosapp.databinding.FragmentCollectorBinding
import com.miso.vinilosapp.ui.adapters.CollectorsAdapter
import com.miso.vinilosapp.ui.viewmodels.CollectorViewModel

class CollectorFragment : Fragment() {
    private var _binding: FragmentCollectorBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorViewModel
    private var viewModelAdapter: CollectorsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = CollectorsAdapter()

        val collapsingToolbar = binding.collectorCollapsingToolbar

        binding.collectorToolbar.setTitle("")

        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.setSupportActionBar(binding.collectorToolbar)
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
                    binding.collectorToolbar.setTitle(binding.collectorCollapsingToolbarTitle.getText())
                    isShow = true
                } else if (isShow) {
                    binding.collectorToolbar.setTitle("")
                    isShow = false
                }
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.collectorsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_collectors)
        val application = activity.application
        viewModel = ViewModelProvider(
            this,
            CollectorViewModel.Factory(
                application,
                CollectorRepository(
                    application,
                    VinylRoomDatabase.getDatabase(application).collectorDao()
                )
            )
        )[CollectorViewModel::class.java]
        viewModel.collectors.observe(viewLifecycleOwner) {
            it.apply {
                viewModelAdapter!!.collectors = this
            }
        }
        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
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
