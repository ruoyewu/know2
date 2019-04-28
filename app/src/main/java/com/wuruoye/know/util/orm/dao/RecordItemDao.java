package com.wuruoye.know.util.orm.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wuruoye.know.util.orm.table.RecordItem;

import java.util.List;

/**
 * Created at 2019/4/11 17:53 by wuruoye
 * Description:
 */
@Dao
public interface RecordItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RecordItem item);

    @Query("SELECT * FROM record_item ORDER BY createTime DESC")
    List<RecordItem> queryAll();

    @Query("SELECT * FROM record_item WHERE id = :id")
    RecordItem query(long id);

    @Query("SELECT * FROM record_item WHERE recordId = :recordId AND typeId = :typeId and type = :type")
    RecordItem query(long recordId, long typeId, int type);

    @Query("SELECT * FROM record_item WHERE recordId = :recordId AND type = :type")
    RecordItem queryByType(long recordId, int type);

    @Query("SELECT * FROM record_item WHERE recordId = :recordId ORDER BY createTime DESC")
    List<RecordItem> queryByRecord(long recordId);

    @Query("DELETE FROM record_item WHERE id = :id")
    int delete(long id);

    @Query("DELETE FROM record_item WHERE recordId = :recordId")
    int deleteByRecord(long recordId);

    @Query("DELETE FROM record_item WHERE typeId = :typeId")
    int deleteByTypeId(long typeId);
}
