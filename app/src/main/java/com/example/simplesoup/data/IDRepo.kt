package com.example.simplesoup.data

import androidx.room.*

@Entity
data class ID(
    @PrimaryKey
    val id:Int
)

@Dao
interface idDao{
    @Query("select * from ID")
    fun getAll():ArrayList<ID>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg IDs:ID)
    @Delete
    fun delete(ID:ID)
    @Query("delete from ID")
    fun deleteAll()
}

@Database(entities = arrayOf(ID::class),version = 1)
abstract class IDRepo:RoomDatabase(){
    abstract fun idDao():idDao
}