package cz.covid19cz.app.db

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Single

@Dao
interface ScanDataDao {

    companion object{
        const val TABLE_NAME = "scan_data"
    }

    @Query("SELECT * FROM $TABLE_NAME")
    fun findAll(): LiveData<List<ScanDataEntity>>

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): Single<List<ScanDataEntity>>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY ${ScanDataEntity.COLUMN_TIMESTAMP_END} DESC")
    fun getAllDesc(): Single<List<ScanDataEntity>>


    @Query("SELECT * FROM $TABLE_NAME WHERE ${ScanDataEntity.COLUMN_TIMESTAMP_END} > :since")
    fun getAllFromTimestamp(since: Long): Single<List<ScanDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(devices: List<ScanDataEntity>) : List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(devices: ScanDataEntity) : Long

    @Delete
    fun delete(device: ScanDataEntity)

    @Query("DELETE FROM $TABLE_NAME WHERE ${ScanDataEntity.COLUMN_TIMESTAMP_END} < :timestamp")
    fun deleteOldData(timestamp: Long) : Int

    @Query("DELETE FROM $TABLE_NAME")
    fun clear()

    @Query("SELECT count(DISTINCT ${ScanDataEntity.COLUMN_BUID}) FROM $TABLE_NAME WHERE ${ScanDataEntity.COLUMN_TIMESTAMP_END} > :since")
    fun getDistinctCount(since: Long) : Single<Int>

    @Query("SELECT * FROM (SELECT ${ScanDataEntity.COLUMN_BUID}, sum(${ScanDataEntity.COLUMN_TIMESTAMP_END} - ${ScanDataEntity.COLUMN_TIMESTAMP_START}) AS expositionTime FROM $TABLE_NAME WHERE ${ScanDataEntity.COLUMN_TIMESTAMP_END} > :since AND ${ScanDataEntity.COLUMN_RSSI_MED} >= :criticalRssi GROUP BY ${ScanDataEntity.COLUMN_BUID}) WHERE expositionTime > (:criticalMinutes*60*1000)")
    fun getCritical(since: Long, criticalRssi : Int, criticalMinutes : Int) : Single<List<ExpositionEntity>>
}