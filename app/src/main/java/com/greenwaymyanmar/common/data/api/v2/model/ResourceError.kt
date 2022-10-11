package com.greenwaymyanmar.common.data.api.v2.model

import greenway_myanmar.org.R
import greenway_myanmar.org.common.domain.entities.Text

//sealed class ResourceError(open val error: Text) {
//
//  data class IoMessageError(override val error: Text) : ResourceError(error)
//
//  data class IoBodyError(override val error: Text = Text.StringText("Oops!"), val body: String) :
//    ResourceError(error)
//
//  data class NoInternetConnectionError(
//    override val error: Text = Text.ResourceText(R.string.error_no_network)
//  ) : ResourceError(error)
//}
