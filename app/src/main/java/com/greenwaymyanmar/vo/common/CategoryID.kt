package com.greenwaymyanmar.vo.common

enum class CategoryID(val id: Int) {
  UNKNOWN(-1) {
    override fun title() = ""
  },
  ARGI(1) {
    override fun title() = "စိုက်ပျိုးရေး"
  },
  LIVESTOCK(45) {
    override fun title() = "မွေးမြူရေး"
  },
  AQUACULTURE(41) {
    override fun title() = "ရေလုပ်ငန်း"
  };

  abstract fun title(): String
}
