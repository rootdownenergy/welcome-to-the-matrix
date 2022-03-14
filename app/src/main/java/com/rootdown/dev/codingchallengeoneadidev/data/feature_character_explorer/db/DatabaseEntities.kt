package com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db

import android.os.Parcelable
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "characters")
data class BreakingBadChar(
    @PrimaryKey
    @SerializedName("char_id") val charId: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("birthday") val birthday: String = "",
    @SerializedName("occupation") val occupation:  MutableList<String> = mutableListOf(),
    @SerializedName("img") val img: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("nickname") val nickname: String = "",
    @SerializedName("portrayed") val portrayed: String = "",
    @SerializedName("category") val category: String = "",
    @SerializedName("appearance") val appearance: MutableList<String> = mutableListOf()
) : Parcelable
@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val repoId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
//@Entity(tableName = "occupation")
//data class Occupation(
    //val occup: String = "",
//)
//@Entity(tableName = "appearance")
//data class Appearance(
    //val appr: String = "",
//)
private const val SEPARATOR = ","
class ListStringConverter {
    @TypeConverter
    fun fromString(value: String?): MutableList<String> {
        val listType = object :
            TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }
    @TypeConverter
    fun fromList(list: MutableList<String?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
class ListIntConverter {
    @TypeConverter
    fun fromInt(value: Int?): MutableList<Int> {
        val listType = object :
            TypeToken<ArrayList<Int?>?>() {}.type
        return Gson().fromJson(value.toString(), listType)
    }
    @TypeConverter
    fun fromList(list: MutableList<Int?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}
