package com.wuruoye.know.util.orm.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wuruoye.know.util.orm.table.Record;

import java.util.List;

/**
 * Created at 2019/4/11 17:49 by wuruoye
 * Description:
 */
@Dao
public interface RecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Record record);

    @Query("SELECT * FROM record ORDER BY createTime DESC")
    List<Record> queryAll();

    @Query("SELECT * FROM record WHERE id = :id")
    Record query(long id);

    @Query("SELECT * FROM record WHERE type = :type ORDER BY createTime DESC")
    List<Record> queryByType(long type);

    @Query("SELECT * FROM record WHERE createTime > :time ORDER BY createTime DESC")
    List<Record> queryByTime(long time);

    @Query("SELECT * FROM record WHERE tag = :tag ORDER BY createTime DESC")
    List<Record> queryByTag(long tag);

    @Query("SELECT * FROM record WHERE tag = :tag AND createTime > :time ORDER BY createTime DESC")
    List<Record> queryByTagTime(long tag, long time);

    @Query("SELECT * FROM record WHERE type = :type AND createTime > :time ORDER BY createTime DESC")
    List<Record> queryByTimeType(long time, long type);

    @Query("SELECT * FROM record WHERE type = :type AND tag = :tag AND createTime > :time ORDER BY createTime DESC")
    List<Record> queryByTypeTagTime(long type, long tag, long time);

    @Query("SELECT COUNT(*) FROM record")
    long queryCount();

    @Query("DELETE FROM record WHERE id = :id")
    int delete(long id);

    @Query("INSERT INTO record VALUES (:id, :type, :remNum, :failNum, :lastReview, :lastFailReview, :tag, :createTime, :updateTime)")
    void insertNotInTransaction(long id, long type, int remNum, int failNum, long lastReview,
                                long lastFailReview, long tag, long createTime, long updateTime);
}
