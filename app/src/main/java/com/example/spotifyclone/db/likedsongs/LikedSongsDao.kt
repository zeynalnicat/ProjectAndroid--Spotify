package com.example.spotifyclone.db.likedsongs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface LikedSongsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(likedSongsEntity: LikedSongsEntity):Long


    @Query("Select * from `liked songs`")
    suspend fun selectAll():List<LikedSongsEntity>

    @Query("SELECT * FROM `liked songs` WHERE name LIKE '%' || :searchText || '%'")
    suspend fun search(searchText:String):List<LikedSongsEntity>

}