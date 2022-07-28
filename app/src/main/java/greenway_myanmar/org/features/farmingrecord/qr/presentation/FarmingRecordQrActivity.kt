package greenway_myanmar.org.features.farmingrecord.qr.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import greenway_myanmar.org.R

@AndroidEntryPoint
class FarmingRecordQrActivity : AppCompatActivity() {

  private val navController by lazy { findNavController() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.farming_record_qr_activity)

    val navInflater = navController.navInflater
    val graph = navInflater.inflate(R.navigation.farming_record_qr_nav_graph)
    graph.setStartDestination(R.id.addEditFarmingRecordQrFragment)
    navController.graph = graph
  }

  private fun findNavController(): NavController {
    val navHost =
      supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
    return navHost!!.navController
  }

  companion object {

    private const val EXTRA_DESTINATION_RES_ID = "extra.DESTINATION_RES_ID"

    @JvmStatic
    fun start(context: Context) {
      start(context, R.id.farmingRecordQrHomeFragment)
    }

    @JvmStatic
    fun start(
      context: Context,
      @IdRes destinationResId: Int = R.id.farmingRecordQrHomeFragment,
      extras: Bundle? = null
    ) {
      val starter = getStartIntent(context, destinationResId, extras)
      context.startActivity(starter)
    }

    @JvmStatic
    fun getStartIntent(
      context: Context,
      @IdRes destinationResId: Int = R.id.farmingRecordQrHomeFragment,
      extras: Bundle? = null
    ): Intent {
      val starter = Intent(context, FarmingRecordQrActivity::class.java)
      starter.putExtra(EXTRA_DESTINATION_RES_ID, destinationResId)
      if (extras != null) {
        starter.putExtras(extras)
      }
      return starter
    }
  }
}
