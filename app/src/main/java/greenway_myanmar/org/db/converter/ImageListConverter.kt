/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greenway_myanmar.org.db.converter

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.greenwaymyanmar.common.data.api.gson.adapter.UriTypeAdapter
import greenway_myanmar.org.util.Image

object ImageListConverter {

  @TypeConverter
  @JvmStatic
  fun jsonToList(data: String?): List<Image>? {
    if (data == null) {
      return emptyList()
    }
    val listType = object : TypeToken<List<Image?>?>() {}.type

    return createGson().fromJson(data, listType)
  }

  @TypeConverter
  @JvmStatic
  fun listToJsonString(list: List<Image>?): String? {
    return createGson().toJson(list)
  }

  private fun createGson(): Gson {
    return GsonBuilder()
      .registerTypeAdapter(Uri::class.java, UriTypeAdapter())
      .create()
  }
}
