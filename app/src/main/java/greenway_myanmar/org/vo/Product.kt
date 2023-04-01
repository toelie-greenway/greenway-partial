package greenway_myanmar.org.vo

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.vo.marketplace.Company
import kotlinx.android.parcel.Parcelize

@Parcelize
@JvmSuppressWildcards
data class Product(
  @PrimaryKey @SerializedName("id") val id: String,
  @SerializedName("thumbnail") val thumbnail: String? = null,
  @SerializedName("name") val name: String? = null,
  @SerializedName("en_name") val nameInEnglish: String? = null,
  @SerializedName("description") val description: String? = null,
  @SerializedName("status") val status: String? = null,
  @SerializedName("ghs_class") val ghsClass: String? = null,
  @SerializedName("registration_no") val registrationNo: String? = null,
  @SerializedName("specification") val specification: String? = null,
  @SerializedName("caution") val caution: String? = null,
  @SerializedName("instruction") val instruction: String? = null,
  @SerializedName("price_range") val priceRange: String? = null,
  @SerializedName("images") val coverPhotos: List<Image>? = null,
  @SerializedName("units") val units: List<String>? = null,
  @SerializedName("view_count") val viewCount: Int? = 0,
  @Embedded(prefix = "manufacture_company_")
  @SerializedName("manufacturer")
  val manufacture: Company? = null,
  @SerializedName("distributors") val distributors: List<Company>? = null
) : Parcelable