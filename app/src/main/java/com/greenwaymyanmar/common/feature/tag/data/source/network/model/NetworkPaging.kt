package com.greenwaymyanmar.common.feature.tag.data.source.network.model

import com.greenwaymyanmar.common.data.api.v1.Pagination

data class NetworkPaging(
  val total: Int? = 0,
  val per_page: Int? = 0,
  val current_page: Int? = 0,
  val last_page: Int? = 0,
  val from: Int? = 0,
  val to: Int? = 0
)

fun NetworkPaging.toPaginationModel() =
  Pagination().apply {
    total = total
    perPage = per_page ?: 0
    currentPage = current_page ?: 0
    lastPage = last_page ?: 0
    from = from
    to = to
  }
