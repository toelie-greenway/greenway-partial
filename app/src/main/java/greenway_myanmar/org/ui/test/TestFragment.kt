package greenway_myanmar.org.ui.test

import android.Manifest
import android.app.DownloadManager
import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.common.domain.entities.ValidationResult
import greenway_myanmar.org.databinding.TestFragmentBinding
import greenway_myanmar.org.ui.common.OnCompanyItemClickListener
import greenway_myanmar.org.ui.market_place.buyer.productdetail.shop.views.ProductShopCompanyItemView
import greenway_myanmar.org.util.kotlin.autoCleared
import greenway_myanmar.org.vo.marketplace.Company
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class TestFragment : Fragment() {

    private var binding by autoCleared<TestFragmentBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TestFragmentBinding.inflate(layoutInflater)
        binding.testTextView.text = "hasWriteExternalStoragePermission: ${hasWriteExternalStoragePermission()}"
        return binding.root
    }

    private fun company(id: String, name: String, logo: String? = null) = Company(
        id = id,
        name = name,
        logo = logo
    )

    @AfterPermissionGranted(RC_STORAGE_PERMISSION)
    private fun handleDownloadBook() {
        Build.VERSION_CODES.
        download()
//        if (hasWriteExternalStoragePermission()) {
//            binding.testTextView.text = "Ready to download"
//            download()
//        } else {
//            binding.testTextView.text = "Ask for storage permissions"
//            // Ask for storage permissions
//            EasyPermissions.requestPermissions(
//                this,
//                getString(R.string.rationale_storage_write_permission_book),
//                RC_STORAGE_PERMISSION,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            )
//        }
    }

    private fun hasWriteExternalStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private fun download() {
        val url = "https://file-examples.com/storage/fe0d875dfd645260e96b346/2017/10/file-example_PDF_1MB.pdf"
        val request = DownloadManager.Request(Uri.parse(url))
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS, "file-example_PDF_1MB"
        )
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
        )
        request.setTitle("Sample .pdf download")
        request.setNotificationVisibility(
            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
        )
        request.setVisibleInDownloadsUi(false)

        val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun validate(: ?): ValidationResult<> {
        return if ( == null) {
            ValidationResult.Error(Text.ResourceText(R.string.error_field_required))
        } else {
            ValidationResult.Success()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.companies = listOf(
            company("1", "မြန်မာကောင်းသုခ", "https://greenway.sgp1.digitaloceanspaces.com/shop_logos/d2e8cf9325e883d5a0f93215df32a7c1.png"),
            company("2", "သြဘာ"),
        )
        binding.clickListener =object : OnCompanyItemClickListener {
            override fun onItemClick(company: Company) {
                handleDownloadBook()
            }
        }

//        lifecycleScope.launchWhenStarted {
//            delay(5000)
//
//            binding.companies = listOf(
//                "When calling",
//                "NullPointerException",
//                "Unfortunately",
//                "UserView",
//                "Wrapping up",
//            )
//        }

//        val textViewA = TextView(requireContext()).apply { text = "AAAAAAAA" }
//        textViewA.id = ViewCompat.generateViewId()
//        val textViewB = TextView(requireContext()).apply { text = "BBBBBBBB" }
//        textViewB.id = ViewCompat.generateViewId()
//        val textViewC = TextView(requireContext()).apply { text = "CCCCCCCC" }
//        textViewC.id = ViewCompat.generateViewId()
//        val textViewD = TextView(requireContext()).apply { text = "DDDDDDDD" }
//        textViewD.id = ViewCompat.generateViewId()
//        val textViewE = TextView(requireContext()).apply { text = "EEEEEEEEEEEEEEEEAAAAA" }
//        textViewE.id = ViewCompat.generateViewId()
//        binding.constraintLayout.addView(textViewA)
//        binding.constraintLayout.addView(textViewB)
//        binding.constraintLayout.addView(textViewC)
//        binding.constraintLayout.addView(textViewD)
//        binding.constraintLayout.addView(textViewE)
//        binding.componentFlow.referencedIds = arrayOf(
//            textViewA.id,
//            textViewB.id,
//            textViewC.id,
//            textViewD.id,
//            textViewE.id,
//        ).toIntArray()
    }

    companion object {
        private const val RC_STORAGE_PERMISSION = 100

    }
}

@BindingAdapter("companies", "clickListener", requireAll = true)
fun Flow.bindCompanies(companies: List<Company>?, clickListener: OnCompanyItemClickListener) {
    if (companies.isNullOrEmpty()) return

    val constraintLayout = (this.parent as? ConstraintLayout) ?: return
    removeAllViews(constraintLayout)

    companies.forEach {
        val itemView = createCompanyItemView(this.context, it, clickListener)
        constraintLayout.addView(itemView)
        this.referencedIds = this.referencedIds + itemView.id
    }
}

private fun Flow.removeAllViews(parent: ConstraintLayout) {
    this.referencedIds?.forEach { viewId ->
        parent.findViewById<View?>(viewId)?.let { view ->
            parent.removeView(view)
        }
    }
    this.referencedIds = emptyArray<Int>().toIntArray()
}

private fun createCompanyItemView(context: Context, company: Company, clickListener: OnCompanyItemClickListener): ProductShopCompanyItemView {
    val itemView = ProductShopCompanyItemView(context)
    itemView.company = company
    itemView.id = ViewCompat.generateViewId()
    itemView.clickListener = clickListener
    return itemView
}
