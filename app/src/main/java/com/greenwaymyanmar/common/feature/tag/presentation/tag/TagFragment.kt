package com.greenwaymyanmar.common.feature.tag.presentation.tag

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.controller.TagController
import com.greenwaymyanmar.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R
import greenway_myanmar.org.databinding.TagFragmentBinding
import greenway_myanmar.org.util.kotlin.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class TagFragment : Fragment(R.layout.tag_fragment) {

    private val binding by viewBinding(TagFragmentBinding::bind)
    private val viewModel by viewModels<TagViewModel>()
    private val controller: TagController by lazy {
        TagController(
            onTabChanged = {
                onTabChanged(it)
            },
            onMoreThreadClicked = {
                onMoreThreadClicked()
            },
            onMorePostClicked = {
                onMorePostClicked()
            }
        )
    }

    private fun onTabChanged(tab: UiTagTab) {
        viewModel.handleEvent(
            TagEvent.OnTabChanged(tab)
        )
    }

    private fun onMoreThreadClicked() {
        viewModel.loadNextPage()
    }

    private fun onMorePostClicked() {
        Toast.makeText(requireContext(), "More Post", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    private fun setupUi() {
        setupToolbar()
        setupList()
    }

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            observeTag()
        }
        observeThreadListing()
    }

    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.tag)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.action_share_tag) {
                //TODO: implement share tag
                Toast.makeText(requireContext(), "//TODO: Share Tag", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }
    }

    private fun setupList() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.list.layoutManager = layoutManager

        binding.list.setController(controller)
        controller.adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    layoutManager.scrollToPosition(0)
                }
            }
        })
    }

    private fun CoroutineScope.observeTag() = launch {
        viewModel.uiState.collect {
            controller.uiState = it
        }
    }

    private fun observeThreadListing() {
        viewModel.threads.observe(viewLifecycleOwner) {
            controller.submitList(it)
            //    controller.setThreadPagedList(it)
        }
        viewModel.networkState.observe(viewLifecycleOwner) {
            Timber.d("NetworkState: ${it.status}")
            controller.setThreadNetworkState(it)
        }
        viewModel.hasMoreThread.observe(viewLifecycleOwner) {
            Timber.d("HasMore: $it")
            controller.setThreadHasMore(it)
        }
    }

}