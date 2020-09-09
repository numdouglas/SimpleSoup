package com.example.simplesoup.data

import android.content.Context
import androidx.room.*
import androidx.room.Database

@Entity
data class ID(
    @PrimaryKey
    val id: Int
)

@Dao
interface idDao {
    @Transaction
    @Query("select * from ID")
    fun getAll(): List<ID>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg IDs: ID)

    @Delete
    fun delete(ID: ID)

    @Query("delete from ID")
    fun deleteAll()

    //Anything inside here runs as a single transaction
    @Transaction
    fun deleteAllAndInsertInTransaction(vararg IDs: ID) {
        deleteAll()
        insertAll(*IDs)
    }
}

@Database(entities = [ID::class], version = 1)
abstract class IDRepo : RoomDatabase() {
    abstract fun idDao(): idDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: IDRepo? = null

        fun getDatabase(context: Context): IDRepo {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IDRepo::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}