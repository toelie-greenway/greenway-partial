package com.greenwaymyanmar.common.feature.tag.presentation.tag.epoxy.controller

import android.annotation.SuppressLint
import android.os.Handler
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyViewHolder
import timber.log.Timber
import kotlin.reflect.KClass


/**
 * An [EpoxyController] that can work with a [PagedList].
 *
 * Internally, it caches the model for each item in the [PagedList]. You should override
 * [buildItemModel] method to build the model for the given item. Since [PagedList] might include
 * `null` items if placeholders are enabled, this method needs to handle `null` values in the list.
 *
 * By default, the model for each item is added  to the model list. To change this behavior (to
 * filter items or inject extra items), you can override [addModels] function and manually add built
 * models.
 *
 * @param T The type of the items in the [PagedList].
 */
abstract class Typed3PagedListEpoxyController<F, FM : EpoxyModel<*>, S, SM : EpoxyModel<*>, T, TM : EpoxyModel<*>>(
    /**
     * The handler to use for building models. By default this uses the main thread, but you can use
     * [EpoxyAsyncUtil.getAsyncBackgroundHandler] to do model building in the background.
     *
     * The notify thread of your PagedList (from setNotifyExecutor in the PagedList Builder) must be
     * the same as this thread. Otherwise Epoxy will crash.
     */
    modelBuildingHandler: Handler = EpoxyController.defaultModelBuildingHandler,
    /**
     * The handler to use when calculating the diff between built model lists.
     * By default this uses the main thread, but you can use
     * [EpoxyAsyncUtil.getAsyncBackgroundHandler] to do diffing in the background.
     */
    diffingHandler: Handler = EpoxyController.defaultDiffingHandler,
    /**
     * [PagedListEpoxyController] uses an [DiffUtil.ItemCallback] to detect changes between
     * [PagedList]s. By default, it relies on simple object equality but you can provide a custom
     * one if you don't use all fields in the object in your models.
     */
    firstItemDiffCallback: DiffUtil.ItemCallback<F> = DEFAULT_ITEM_DIFF_CALLBACK as DiffUtil.ItemCallback<F>,
    secondItemDiffCallback: DiffUtil.ItemCallback<S> = DEFAULT_ITEM_DIFF_CALLBACK as DiffUtil.ItemCallback<S>,
    thirdItemDiffCallback: DiffUtil.ItemCallback<T> = DEFAULT_ITEM_DIFF_CALLBACK as DiffUtil.ItemCallback<T>
) : EpoxyController(modelBuildingHandler, diffingHandler) {

    // this is where we keep the already built models
    private val firstModelCache = PagedListModelCache(
        modelBuilder = { pos, item ->
            buildFirstItemModel(pos, item)
        },
        rebuildCallback = {
            requestModelBuild()
        },
        itemDiffCallback = firstItemDiffCallback,
        modelBuildingHandler = modelBuildingHandler
    )
    private val secondModelCache = PagedListModelCache(
        modelBuilder = { pos, item ->
            buildSecondItemModel(pos, item)
        },
        rebuildCallback = {
            requestModelBuild()
        },
        itemDiffCallback = secondItemDiffCallback,
        modelBuildingHandler = modelBuildingHandler
    )
    private val thirdModelCache = PagedListModelCache(
        modelBuilder = { pos, item ->
            buildThirdItemModel(pos, item)
        },
        rebuildCallback = {
            requestModelBuild()
        },
        itemDiffCallback = thirdItemDiffCallback,
        modelBuildingHandler = modelBuildingHandler
    )

    @Suppress("UNCHECKED_CAST")
    final override fun buildModels() {
        addModels(
            firstModelCache.getModels() as List<FM>,
            secondModelCache.getModels() as List<SM>,
            thirdModelCache.getModels() as List<TM>,
        )
    }

    /**
     * This function adds all built models to the adapter. You can override this method to add extra
     * items into the model list or remove some.
     */
    open fun addModels(firstModels: List<FM>, secondModels: List<SM>, thirdModels: List<TM>) {
        super.add(firstModels)
        super.add(secondModels)
        super.add(thirdModels)
    }

    open fun addFirstModels(models: List<FM>) {
        super.add(models)
    }

    open fun addSecondModels(models: List<SM>) {
        super.add(models)
    }

    open fun addThirdModels(models: List<TM>) {
        super.add(models)
    }

    /**
     * Builds the model for a given item. This must return a single model for each item. If you want
     * to inject headers etc, you can override [addModels] function.
     *
     * If the `item` is `null`, you should provide the placeholder. If your [PagedList] is
     * configured without placeholders, you don't need to handle the `null` case.
     */
    abstract fun buildFirstItemModel(currentPosition: Int, item: F?): FM
    abstract fun buildSecondItemModel(currentPosition: Int, item: S?): SM
    abstract fun buildThirdItemModel(currentPosition: Int, item: T?): TM

    override fun onModelBound(
        holder: EpoxyViewHolder,
        boundModel: EpoxyModel<*>,
        position: Int,
        previouslyBoundModel: EpoxyModel<*>?
    ) {
        Timber.d("onModelBound: $position : $boundModel")
        // TODO the position may not be a good value if there are too many injected items.
        if (isFirstModel(boundModel)) {
            Timber.d("onModelBound: loadAround :  $position")
            firstModelCache.loadAround(position)
        } else if (isSecondModel(boundModel)) {
            secondModelCache.loadAround(position)
        } else if (isThirdModel(boundModel)) {
            thirdModelCache.loadAround(position)
        }
    }

    private fun isFirstModel(boundModel: EpoxyModel<*>): Boolean {
        return boundModel::class == getFirstModelClass()
    }

    private fun isSecondModel(boundModel: EpoxyModel<*>): Boolean {
        return boundModel::class == getSecondModelClass()
    }

    private fun isThirdModel(boundModel: EpoxyModel<*>): Boolean {
        return boundModel::class == getThirdModelClass()
    }

    abstract fun getFirstModelClass(): KClass<FM>

    abstract fun getSecondModelClass(): KClass<SM>

    abstract fun getThirdModelClass(): KClass<TM>

    /**
     * Submit a new paged list.
     *
     * A diff will be calculated between this list and the previous list so you may still get calls
     * to [buildItemModel] with items from the previous list.
     */
    fun submitFirstList(newList: PagedList<F>?) {
        firstModelCache.submitList(newList)
    }

    fun submitSecondList(newList: PagedList<S>?) {
        secondModelCache.submitList(newList)
    }

    fun submitThirdList(newList: PagedList<T>?) {
        thirdModelCache.submitList(newList)
    }

    /**
     * Requests a model build that will run for every model, including the ones created for the paged
     * list.
     *
     * Clears the current model cache to make sure that happens.
     */
    fun requestForcedModelBuild() {
        firstModelCache.clearModels()
        secondModelCache.clearModels()
        requestModelBuild()
    }

    fun isInstanceOfGenericClass(obj: Any, genericClass: KClass<*>): Boolean {
        return obj::class.isInstance(genericClass)
    }

    companion object {
        /**
         * [PagedListEpoxyController] calculates a diff on top of the PagedList to check which
         * models are invalidated.
         * This is the default [DiffUtil.ItemCallback] which uses object equality.
         */
        val DEFAULT_ITEM_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any) = oldItem == newItem

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Any, newItem: Any) = oldItem == newItem
        }
    }
}
