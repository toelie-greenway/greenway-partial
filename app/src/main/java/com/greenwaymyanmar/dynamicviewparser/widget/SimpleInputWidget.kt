package com.greenwaymyanmar.dynamicviewparser.widget

interface SimpleInputWidget {

  fun setMandatory(mandatory: Boolean)

  fun isMandatory(): Boolean

  fun getValue(): Any?

  fun validate(): Boolean

  fun isEmpty(): Boolean

  fun showError()

  fun clearError()
}
