package greenway_myanmar.org.vo.marketplace

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(
  @SerializedName("id") val id: String,
  @SerializedName("about") val about: String? = null,
  @SerializedName("address") val address: String? = null,
  @SerializedName("country") val country: String? = null,
  @SerializedName("cover_photo") val coverPhoto: String? = null,
  @SerializedName("fb_url") val facebookUrl: String? = null,
  @SerializedName("fb_page_id") val facebookPageId: String? = null,
  @SerializedName("is_distributor") val isDistributor: Boolean = false,
  @SerializedName("is_manufacturer") val isManufacturer: Boolean = false,
  @SerializedName("is_partnership") val isPartnership: Boolean = false,
  @SerializedName("logo") val logo: String? = null,
  @SerializedName("name") val name: String,
  @SerializedName("phones") val phones: List<String>? = null,
  @SerializedName("website") val website: String? = null
) : Parcelable
