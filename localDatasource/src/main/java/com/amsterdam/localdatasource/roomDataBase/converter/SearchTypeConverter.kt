package com.amsterdam.localdatasource.roomDataBase.converter

import androidx.room.TypeConverter
import com.amsterdam.repository.dto.local.utils.SearchType

class SearchTypeConverter {
    @TypeConverter
    fun fromSearchType(searchType: SearchType): String = searchType.name

    @TypeConverter
    fun toSearchType(name: String): SearchType = SearchType.valueOf(name)
}