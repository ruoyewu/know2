package com.wuruoye.know.util.orm.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wuruoye.know.util.orm.table.RecordTag;

import java.util.List;

/**
 * Created at 2019/4/12 11:29 by wuruoye
 * Description:
 */
@Dao
public interface RecordTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RecordTag tag);

    @Query("SELECT * FROM record_tag ORDER BY createTime DESC")
    List<RecordTag> queryAll();

    @Query("SELECT * FROM record_tag WHERE id = :id")
    RecordTag query(long id);

    @Query("SELECT * FROM record_tag WHERE title = :title")
    RecordTag queryByTitle(String title);

    @Query("SELECT COUNT(*) FROM record_tag")
    long queryCount();

    @Query("DELETE FROM record_tag WHERE id = :id")
    int delete(long id);
}
