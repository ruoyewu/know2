package com.wuruoye.know.util.orm.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wuruoye.know.util.orm.table.RecordTextView;

/**
 * Created at 2019/4/11 17:59 by wuruoye
 * Description:
 */
@Dao
public interface TextViewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RecordTextView view);

    @Query("SELECT * FROM text_view WHERE id = :id")
    RecordTextView query(long id);

    @Query("DELETE FROM text_view WHERE id = :id")
    int delete(long id);
}
