package com.wuruoye.know.util.orm.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wuruoye.know.util.orm.table.RecordType;

import java.util.List;

/**
 * Created at 2019/4/11 14:47 by wuruoye
 * Description:
 */
@Dao
public interface RecordTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RecordType table);

    @Query("SELECT * FROM record_type")
    List<RecordType> queryAll();

    @Query("SELECT * FROM record_type WHERE id = :id LIMIT 0,1")
    RecordType query(long id);

    @Query("DELETE FROM record_type WHERE id = :id")
    void delete(long id);
}
