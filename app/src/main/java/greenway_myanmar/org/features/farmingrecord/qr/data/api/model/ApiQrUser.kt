package greenway_myanmar.org.features.farmingrecord.qr.data.api.model

import com.google.gson.annotations.SerializedName
import greenway_myanmar.org.features.farmingrecord.qr.domain.model.QrUser

data class ApiQrUser(
    @SerializedName("crops")
    val crops: List<ApiCrop>? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("state")
    val stateRegion: ApiStateRegion? = null,
    @SerializedName("township")
    val township: ApiTownship? = null,
    @SerializedName("vt_town")
    val villageTractTown: ApiVillageTractTown? = null,
    @SerializedName("village_ward")
    val villageWard: ApiVillageWard? = null
) {
    fun toDomain() = QrUser(
        id = id.orEmpty(),
        crops = crops.orEmpty().map { it.toDomain() },
        name = name.orEmpty(),
        phone = phone.orEmpty(),
        avatar = avatar.orEmpty(),
        stateRegion = stateRegion?.toDomain(),
        township = township?.toDomain(),
        villageTractTown = villageTractTown?.toDomain(),
        villageWard = villageWard?.toDomain()
    )
}
