package com.miso.vinilosapp.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.miso.vinilosapp.data.cache.AlbumsCacheManager
import com.miso.vinilosapp.data.models.AlbumRequest
import com.miso.vinilosapp.data.repositories.AlbumRepository
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import com.miso.vinilosapp.databinding.FragmentAddAlbumBinding
import com.miso.vinilosapp.ui.viewmodels.AddAlbumViewModel
import java.util.Calendar

class AddAlbumFragment : Fragment() {
    private var _binding: FragmentAddAlbumBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AddAlbumViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAlbumBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backBtnAddAlbum.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addAlbumBtn.setOnClickListener {
            addAlbum()
        }

        binding.newAlbumReleaseDate.editText?.setOnClickListener {
            showDatePickerDialog()
        }

        val genres = listOf("Classical", "Salsa", "Rock", "Folk")
        val genresAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, genres)
        (binding.newAlbumGenre.editText as? AutoCompleteTextView)?.setAdapter(genresAdapter)

        val recordLabels = listOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records")
        val recordLabelsAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                recordLabels
            )
        (binding.newAlbumLabel.editText as? AutoCompleteTextView)?.setAdapter(recordLabelsAdapter)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.newAlbumReleaseDate.editText?.setText(selectedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun addAlbum() {
        val newAlbum = AlbumRequest(
            name = binding.newAlbumName.editText?.text.toString(),
            cover = binding.newAlbumCover.editText?.text.toString(),
            releaseDate = binding.newAlbumReleaseDate.editText?.text.toString(),
            description = binding.newAlbumDescription.editText?.text.toString(),
            genre = binding.newAlbumGenre.editText?.text.toString(),
            recordLabel = binding.newAlbumLabel.editText?.text.toString()
        )

        viewModel.addAlbum(newAlbum)
        binding.newAlbumLoadingIndicator.visibility = View.VISIBLE
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        val context = activity.application

        viewModel = ViewModelProvider(
            this,
            AddAlbumViewModel.Factory(
                context,
                AlbumRepository(
                    AlbumsCacheManager(requireActivity()),
                    NetworkServiceAdapter.apiService
                )
            )
        )[AddAlbumViewModel::class.java]

        viewModel.eventNetworkError.observe(viewLifecycleOwner, { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        viewModel.albumCreated.observe(viewLifecycleOwner, { isCreated ->
            if (isCreated) {
                binding.newAlbumLoadingIndicator.visibility = View.GONE
                Toast.makeText(activity, "Album agregado", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
                viewModel.onAlbumCreatedHandled()
            }
        })
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
