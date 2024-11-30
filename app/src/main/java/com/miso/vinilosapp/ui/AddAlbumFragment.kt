package com.miso.vinilosapp.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.miso.vinilosapp.R
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
            if (validateInputs()) {
                addAlbum()
            }
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

        binding.newAlbumName.editText?.let {
            addValidationListeners(it) { validateAlbumName() }
        }

        binding.newAlbumCover.editText?.let {
            addValidationListeners(it) { validateAlbumCover() }
        }

        binding.newAlbumReleaseDate.editText?.let {
            addValidationListeners(it) { validateAlbumReleaseDate() }
        }

        binding.newAlbumGenre.editText?.let {
            addValidationListeners(it) { validateAlbumGenre() }
        }

        binding.newAlbumLabel.editText?.let {
            addValidationListeners(it) { validateAlbumLabel() }
        }

        binding.newAlbumDescription.editText?.let {
            addValidationListeners(it) { validateAlbumDescription() }
        }
    }

    private fun addValidationListeners(
        textInputEditText: EditText,
        validate: () -> Boolean
    ) {
        textInputEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validate()
        }

        textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                (textInputEditText.parent.parent as? TextInputLayout)?.error = null
            }
        })
    }

    private fun validateAlbumName(): Boolean {
        return if (binding.newAlbumName.editText?.text.isNullOrEmpty()) {
            binding.newAlbumName.error = getString(R.string.album_name_cannot_be_empty)
            false
        } else {
            binding.newAlbumName.error = null
            true
        }
    }

    private fun validateAlbumCover(): Boolean {
        val coverText = binding.newAlbumCover.editText?.text.toString()

        return if (!Patterns.WEB_URL.matcher(coverText).matches()) {
            binding.newAlbumCover.error = getString(R.string.album_cover_must_be_a_valid_url)
            false
        } else {
            binding.newAlbumCover.error = null
            true
        }
    }

    private fun validateAlbumReleaseDate(): Boolean {
        return if (binding.newAlbumReleaseDate.editText?.text.isNullOrEmpty()) {
            binding.newAlbumReleaseDate.error = getString(R.string.release_date_cannot_be_empty)
            false
        } else {
            binding.newAlbumReleaseDate.error = null
            true
        }
    }

    private fun validateAlbumGenre(): Boolean {
        return if (binding.newAlbumGenre.editText?.text.isNullOrEmpty()) {
            binding.newAlbumGenre.error = getString(R.string.genre_cannot_be_empty)
            false
        } else {
            binding.newAlbumGenre.error = null
            true
        }
    }

    private fun validateAlbumLabel(): Boolean {
        return if (binding.newAlbumLabel.editText?.text.isNullOrEmpty()) {
            binding.newAlbumLabel.error = getString(R.string.label_cannot_be_empty)
            false
        } else {
            binding.newAlbumLabel.error = null
            true
        }
    }

    private fun validateAlbumDescription(): Boolean {
        return if (binding.newAlbumDescription.editText?.text.isNullOrEmpty()) {
            binding.newAlbumDescription.error = getString(R.string.description_cannot_be_empty)
            false
        } else {
            binding.newAlbumDescription.error = null
            true
        }
    }

    private fun validateInputs(): Boolean {
        val isNameValid = validateAlbumName()
        val isCoverValid = validateAlbumCover()
        val isReleaseDateValid = validateAlbumReleaseDate()
        val isGenreValid = validateAlbumGenre()
        val isLabelValid = validateAlbumLabel()
        val isDescriptionValid = validateAlbumDescription()

        return isNameValid && isCoverValid && isReleaseDateValid && isGenreValid && isLabelValid && isDescriptionValid
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
