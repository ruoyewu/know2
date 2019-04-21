package com.wuruoye.know.util.orm.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.wuruoye.know.util.orm.table.ReviewStrategy;

import java.util.List;

/**
 * Created at 2019-04-20 15:28 by wuruoye
 * Description:
 */
@Dao
public interface ReviewStrategyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ReviewStrategy strategy);

    @Query("SELECT * FROM review_strategy ORDER BY createTime DESC")
    List<ReviewStrategy> queryAll();

    @Query("SELECT * FROM review_strategy WHERE id = :id")
    ReviewStrategy query(long id);

    @Query("SELECT * FROM review_strategy WHERE title = :title")
    ReviewStrategy queryByTitle(String title);

    @Query("DELETE FROM review_strategy WHERE id = :id")
    int delete(long id);
}
