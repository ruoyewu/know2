package com.wuruoye.know.util.orm.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wuruoye.know.util.orm.table.RecordLayoutView;

import java.util.List;

/**
 * Created at 2019/4/11 17:47 by wuruoye
 * Description:
 */
@Dao
public interface LayoutViewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RecordLayoutView view);

    @Query("SELECT * FROM layout_view ORDER BY createTime DESC")
    List<RecordLayoutView> queryAll();

    @Query("SELECT * FROM layout_view WHERE id = :id")
    RecordLayoutView query(long id);

    @Query("DELETE FROM layout_view WHERE id = :id")
    int delete(long id);
}
