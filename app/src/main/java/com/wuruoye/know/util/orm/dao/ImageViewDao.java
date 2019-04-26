package com.wuruoye.know.util.orm.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wuruoye.know.util.orm.table.RecordImageView;

import java.util.List;

/**
 * Created at 2019/4/11 17:45 by wuruoye
 * Description:
 */
@Dao
public interface ImageViewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RecordImageView view);

    @Query("SELECT * FROM image_view ORDER BY createTime DESC")
    List<RecordImageView> queryAll();

    @Query("SELECT * FROM image_view WHERE id = :id")
    RecordImageView query(long id);

    @Query("DELETE FROM image_view WHERE id = :id")
    int delete(long id);
}
