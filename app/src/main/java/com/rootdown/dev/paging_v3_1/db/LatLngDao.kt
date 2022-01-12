package com.rootdown.dev.paging_v3_1.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rootdown.dev.paging_v3_1.data.DatabaseLatLng

@Dao
interface LatLngDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(latlng: List<DatabaseLatLng>)

    @Query("SELECT COUNT(*) FROM databaselatlng")
    fun getCount(): Int

    @Query("select * from databaselatlng")
    fun getLatLngLs(): LiveData<List<DatabaseLatLng>>
}