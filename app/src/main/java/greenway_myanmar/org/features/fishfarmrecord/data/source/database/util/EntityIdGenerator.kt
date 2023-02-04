package greenway_myanmar.org.features.fishfarmrecord.data.source.database.util

import java.util.*

object EntityIdGenerator {
    fun generateIdIfRequired(id: String?) =
        if (id.isNullOrEmpty()) UUID.randomUUID().toString() else id
}