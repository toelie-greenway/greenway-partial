package greenway_myanmar.org.common.presentation.adapter

import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.*
import greenway_myanmar.org.vo.NetworkState

abstract class LoadStatePagedListAdapter<T, VH : RecyclerView.ViewHolder> protected constructor(
    diffCallback: DiffUtil.ItemCallback<T>
) :
    PagedListAdapter<T, VH>(diffCallback) {

    private var _networkState: NetworkState? = null
    private val adapterCallback by lazy { AdapterListUpdateCallback(this) }

    private val differ: AsyncPagedListDiffer<T> = AsyncPagedListDiffer(
        object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                adapterCallback.onInserted(position + 1, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                adapterCallback.onRemoved(position + 1, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                adapterCallback.onMoved(fromPosition + 1, toPosition + 1)
            }

            override fun onChanged(position: Int, count: Int, payload: Any?) {
                adapterCallback.onChanged(position + 1, count, payload)
            }
        },
        AsyncDifferConfig.Builder(
            diffCallback
        ).build()
    )


    override fun getItemCount(): Int {
        return differ.itemCount + if (hasExtraRow()) 1 else 0
    }

    protected fun hasExtraRow(): Boolean {
        return _networkState != null && _networkState !== NetworkState.LOADED
    }

    fun isEmpty(): Boolean {
        return itemCount <= if (hasExtraRow()) 1 else 0
    }

    override fun submitList(pagedList: PagedList<T>?) {
        differ.submitList(pagedList)
    }

    override fun submitList(pagedList: PagedList<T>?, commitCallback: Runnable?) {
        differ.submitList(pagedList, commitCallback)
    }

    override fun getItem(position: Int): T? {
        return differ.getItem(position)
    }

    override fun getCurrentList(): PagedList<T>? {
        return differ.currentList
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = _networkState
        val hadExtraRow = hasExtraRow()
        _networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(differ.itemCount)
            } else {
                notifyItemInserted(differ.itemCount)
            }
        } else if (hasExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(differ.itemCount - 1)
        }
    }

    protected fun getNetworkState() = _networkState
}

