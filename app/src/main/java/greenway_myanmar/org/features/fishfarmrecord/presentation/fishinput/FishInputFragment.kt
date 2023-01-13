package greenway_myanmar.org.features.fishfarmrecord.presentation.fishinput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.FfrFishInputFragmentBinding
import greenway_myanmar.org.features.fishfarmrecord.presentation.fishpicker.FishPickerFragment
import greenway_myanmar.org.features.fishfarmrecord.presentation.model.UiFish
import greenway_myanmar.org.util.extensions.bindText
import greenway_myanmar.org.util.extensions.getParcelableExtraCompat
import greenway_myanmar.org.util.extensions.setError
import greenway_myanmar.org.util.kotlin.autoCleared
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FishInputFragment : Fragment() {

    private val viewModel: FishInputViewModel by viewModels()

    private var binding: FfrFishInputFragmentBinding by autoCleared()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observePickFishStatus()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataBinding: FfrFishInputFragmentBinding = FfrFishInputFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragmentResultListener()
        setupUi()
        observeViewModel()
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(FishPickerFragment.REQUEST_KEY_FISH) { _, bundle ->
            val fish = bundle.getParcelableExtraCompat<UiFish?>(FishPickerFragment.KEY_FISH)
            if (fish != null) {
                viewModel.handleEvent(FishInputUiEvent.OnFishChanged(fish))
            }
        }
    }

    private fun observePickFishStatus() {
        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry!!
        val savedStateHandle = currentBackStackEntry.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(FishPickerFragment.KEY_PICK_FISH_SUCCESS)
            .observe(currentBackStackEntry) { success ->
                if (!success && viewModel.selectedFish == null) {
                    navController.popBackStack()
                }
            }
    }

    private fun setupUi() {
        setupFishInputUi()
        setupSpeciesInputUi()
        setupCancelButton()
        setupSubmitButton()
    }

    private fun setupSpeciesInputUi() {
        binding.speciesTextInputEditText.doAfterTextChanged {
            viewModel.handleEvent(FishInputUiEvent.OnSpeciesChanged(it?.toString().orEmpty()))
        }
    }

    private fun setupFishInputUi() {
        binding.fishInputEditText.setOnClickListener {
            viewModel.handleEvent(FishInputUiEvent.ResetFishValidationError)
            openFishPicker()
        }
    }

    private fun setupCancelButton() {
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            viewModel.handleEvent(FishInputUiEvent.OnSubmit)
        }
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeFish()
            observeFishError()
            observeSpecies()
            observeSubmitted()
            observeNavigationEvents()
        }
    }

    private fun CoroutineScope.observeFish() = launch {
        viewModel.uiState.map { it.fish }
            .distinctUntilChanged()
            .collect { fish ->
                if (fish != null) {
                    bindFish(fish)
                } else {
                    openFishPicker()
                }
            }
    }

    private fun CoroutineScope.observeSpecies() = launch {
        viewModel.uiState.map { it.species }
            .distinctUntilChanged()
            .collect {
                binding.speciesTextInputEditText.bindText(it)
            }
    }

    private fun CoroutineScope.observeNavigationEvents() = launch {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is FishInputNavigationEvent.NavigateBackWithResult -> {
                    navigateBackWithResult(event.fish)
                }
            }
        }
    }

    private fun CoroutineScope.observeFishError() = launch {
        viewModel.uiState.map { it.fishValidationError }
            .distinctUntilChanged()
            .collect {
                binding.fishInputLayout.setError(it)
            }
    }

    private fun CoroutineScope.observeSubmitted() = launch {
        viewModel.uiState.map { it.submitted }
            .distinctUntilChanged()
            .collect {

            }
    }

    private fun bindFish(fish: UiFish) {
        binding.fishInputEditText.setText(fish.name)
        bindFishImage(fish.imageUrl)
    }

    private fun bindFishImage(imageUrl: String) {
        Glide.with(requireContext())
            .load(imageUrl)
            .animate(R.anim.image_fade_in)
            .fallback(R.drawable.image_placeholder_circle)
            .error(R.drawable.image_placeholder_circle)
            .placeholder(R.drawable.image_placeholder_circle)
            .into(binding.fishImage)
    }

    private fun openFishPicker() {
        findNavController().navigate(
            FishInputFragmentDirections.actionFishInputFragmentToFishPickerFragment()
        )
    }

    private fun navigateBackWithResult(fish: UiFish) {
        setFragmentResult(
            REQUEST_KEY_FISH, bundleOf(
                KEY_FISH to fish
            )
        )
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY_FISH = "request_key.FishInput.FISH"
        const val KEY_FISH = "key.FISH"
    }
}
