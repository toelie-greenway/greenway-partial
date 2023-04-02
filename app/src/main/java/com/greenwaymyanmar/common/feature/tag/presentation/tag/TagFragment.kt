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
            },
            onMoreProductClicked = {
                onMoreProductClicked()
            }
        )
    }

    private fun onTabChanged(tab: UiTagTab) {
        viewModel.handleEvent(
            TagEvent.OnTabChanged(tab)
        )
    }

    private fun onMoreThreadClicked() {
        viewModel.loadThreadNextPage()
    }

    private fun onMorePostClicked() {
        viewModel.loadPostNextPage()
    }

    private fun onMoreProductClicked() {
        viewModel.loadProductNextPage()
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
        observePostListing()
        observeProductListing()
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
        binding.toolbarTitleTextView.setOnClickListener {
            binding.list.smoothScrollToPosition(0)
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
            controller.setThreadPagedList(it)
        }
        viewModel.threadNetworkState.observe(viewLifecycleOwner) {
            Timber.d("NetworkState Thread: ${it.status}")
            controller.setThreadNetworkState(it)
        }
        viewModel.hasMoreThread.observe(viewLifecycleOwner) {
            Timber.d("HasMore Thread: $it")
            controller.setHasMoreThread(it)
        }
    }

    private fun observePostListing() {
        viewModel.posts.observe(viewLifecycleOwner) {
            controller.setPostPagedList(it)
        }
        viewModel.postNetworkState.observe(viewLifecycleOwner) {
            Timber.d("NetworkState Post: ${it.status}")
            controller.setPostNetworkState(it)
        }
        viewModel.hasMorePost.observe(viewLifecycleOwner) {
            Timber.d("HasMore Post: $it")
            controller.setHasMorePost(it)
        }
    }

    private fun observeProductListing() {
        viewModel.products.observe(viewLifecycleOwner) {
            controller.setProductPagedList(it)
        }
        viewModel.productNetworkState.observe(viewLifecycleOwner) {
            Timber.d("NetworkState Product: ${it.status}")
            controller.setProductNetworkState(it)
        }
        viewModel.hasMoreProduct.observe(viewLifecycleOwner) {
            Timber.d("HasMore Product: $it")
            controller.setHasMoreProduct(it)
        }
    }

}