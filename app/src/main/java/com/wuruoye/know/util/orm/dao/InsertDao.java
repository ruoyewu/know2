package com.wuruoye.know.util.orm.dao;

import android.content.Context;
import androidx.room.EntityInsertionAdapter;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.wuruoye.know.util.orm.AppDB;
import com.wuruoye.know.util.orm.table.*;

/**
 * Created at 2019-04-25 21:39 by wuruoye
 * Description:
 */
public class InsertDao {
    private static InsertDao sInstance;

    public static InsertDao getInstance(Context context) {
        if (sInstance == null) {
            synchronized (InsertDao.class) {
                if (sInstance == null) {
                    sInstance = new InsertDao(context);
                }
            }
        }
        return sInstance;
    }

    private AppDB db;
    private EntityInsertionAdapter<Record> recordInsert;
    private EntityInsertionAdapter<RecordImageView> recordImageViewInsert;
    private EntityInsertionAdapter<RecordItem> recordItemInsert;
    private EntityInsertionAdapter<RecordLayoutView> recordLayoutViewInsert;
    private EntityInsertionAdapter<RecordTag> recordTagInsert;
    private EntityInsertionAdapter<RecordTextView> recordTextViewInsert;
    private EntityInsertionAdapter<RecordType> recordTypeInsert;
    private EntityInsertionAdapter<ReviewStrategy> reviewStrategyInsert;

    private InsertDao(Context context) {
        db = AppDB.Companion.getInstance(context);

        recordInsert = new EntityInsertionAdapter<Record>(db) {
            @Override
            protected void bind(SupportSQLiteStatement stmt, Record value) {
                if (value.getId() == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindLong(1, value.getId());
                }
                stmt.bindLong(2, value.getType());
                stmt.bindLong(3, value.getRemNum());
                stmt.bindLong(4, value.getFailNum());
                stmt.bindLong(5, value.getLastReview());
                stmt.bindLong(6, value.getLastFailReview());
                stmt.bindLong(7, value.getTag());
                stmt.bindLong(8, value.getCreateTime());
                stmt.bindLong(9, value.getUpdateTime());
            }

            @Override
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `record`(`id`,`type`,`remNum`,`failNum`," +
                        "`lastReview`,`lastFailReview`,`tag`,`createTime`,`updateTime`)" +
                        " VALUES (?,?,?,?,?,?,?,?,?)";
            }
        };
        recordImageViewInsert = new EntityInsertionAdapter<RecordImageView>(db) {
            @Override
            protected void bind(SupportSQLiteStatement stmt, RecordImageView value) {
                if (value.getId() == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindLong(1, value.getId());
                }
                stmt.bindLong(2, value.getShape());
                final int _tmp;
                _tmp = value.getBlur() ? 1 : 0;
                stmt.bindLong(3, _tmp);
                stmt.bindLong(4, value.getTint());
                stmt.bindLong(5, value.getWidth());
                stmt.bindLong(6, value.getHeight());
                stmt.bindLong(7, value.getMarginLeft());
                stmt.bindLong(8, value.getMarginRight());
                stmt.bindLong(9, value.getMarginTop());
                stmt.bindLong(10, value.getMarginBottom());
                stmt.bindLong(11, value.getPaddingLeft());
                stmt.bindLong(12, value.getPaddingRight());
                stmt.bindLong(13, value.getPaddingTop());
                stmt.bindLong(14, value.getPaddingBottom());
                stmt.bindLong(15, value.getCreateTime());
                stmt.bindLong(16, value.getUpdateTime());
            }

            @Override
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `image_view`(`id`,`shape`,`blur`,`tint`,`width`,`height`,`marginLeft`,`marginRight`,`marginTop`,`marginBottom`,`paddingLeft`,`paddingRight`,`paddingTop`,`paddingBottom`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }
        };
        recordItemInsert = new EntityInsertionAdapter<RecordItem>(db) {
            @Override
            protected void bind(SupportSQLiteStatement stmt, RecordItem value) {
                if (value.getId() == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindLong(1, value.getId());
                }
                stmt.bindLong(2, value.getRecordId());
                stmt.bindLong(3, value.getType());
                stmt.bindLong(4, value.getTypeId());
                if (value.getContent() == null) {
                    stmt.bindNull(5);
                } else {
                    stmt.bindString(5, value.getContent());
                }
                stmt.bindLong(6, value.getCreateTime());
                stmt.bindLong(7, value.getUpdateTime());
            }

            @Override
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `record_item`(`id`,`recordId`,`type`,`typeId`,`content`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?,?)";
            }
        };
        recordLayoutViewInsert = new EntityInsertionAdapter<RecordLayoutView>(db) {
            @Override
            protected void bind(SupportSQLiteStatement stmt, RecordLayoutView value) {
                if (value.getId() == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindLong(1, value.getId());
                }
                stmt.bindLong(2, value.getOrientation());
                if (value.getItems() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getItems());
                }
                stmt.bindLong(4, value.getBgColor());
                stmt.bindLong(5, value.getGravity());
                stmt.bindLong(6, value.getWidth());
                stmt.bindLong(7, value.getHeight());
                stmt.bindLong(8, value.getMarginLeft());
                stmt.bindLong(9, value.getMarginRight());
                stmt.bindLong(10, value.getMarginTop());
                stmt.bindLong(11, value.getMarginBottom());
                stmt.bindLong(12, value.getPaddingLeft());
                stmt.bindLong(13, value.getPaddingRight());
                stmt.bindLong(14, value.getPaddingTop());
                stmt.bindLong(15, value.getPaddingBottom());
                stmt.bindLong(16, value.getCreateTime());
                stmt.bindLong(17, value.getUpdateTime());
            }

            @Override
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `layout_view`(`id`,`orientation`,`items`,`bgColor`,`gravity`,`width`,`height`,`marginLeft`,`marginRight`,`marginTop`,`marginBottom`,`paddingLeft`,`paddingRight`,`paddingTop`,`paddingBottom`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }
        };
        recordTagInsert = new EntityInsertionAdapter<RecordTag>(db) {
            @Override
            protected void bind(SupportSQLiteStatement stmt, RecordTag value) {
                if (value.getId() == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindLong(1, value.getId());
                }
                if (value.getTitle() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getTitle());
                }
                if (value.getComment() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getComment());
                }
                stmt.bindLong(4, value.getCreateTime());
                stmt.bindLong(5, value.getUpdateTime());
            }

            @Override
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `record_tag`(`id`,`title`,`comment`,`createTime`,`updateTime`) VALUES (?,?,?,?,?)";
            }
        };
        recordTextViewInsert = new EntityInsertionAdapter<RecordTextView>(db) {
            @Override
            protected void bind(SupportSQLiteStatement stmt, RecordTextView value) {
                if (value.getId() == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindLong(1, value.getId());
                }
                if (value.getText() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getText());
                }
                stmt.bindLong(3, value.getTextSize());
                stmt.bindLong(4, value.getTextColor());
                if (value.getHint() == null) {
                    stmt.bindNull(5);
                } else {
                    stmt.bindString(5, value.getHint());
                }
                stmt.bindLong(6, value.getHintSize());
                stmt.bindLong(7, value.getHintColor());
                stmt.bindLong(8, value.getBgColor());
                stmt.bindLong(9, value.getGravity());
                stmt.bindLong(10, value.getTextStyle());
                stmt.bindLong(11, value.getInputType());
                stmt.bindLong(12, value.getMinLine());
                stmt.bindLong(13, value.getMaxLine());
                final int _tmp;
                _tmp = value.getEditable() ? 1 : 0;
                stmt.bindLong(14, _tmp);
                stmt.bindLong(15, value.getWidth());
                stmt.bindLong(16, value.getHeight());
                stmt.bindLong(17, value.getMarginLeft());
                stmt.bindLong(18, value.getMarginRight());
                stmt.bindLong(19, value.getMarginTop());
                stmt.bindLong(20, value.getMarginBottom());
                stmt.bindLong(21, value.getPaddingLeft());
                stmt.bindLong(22, value.getPaddingRight());
                stmt.bindLong(23, value.getPaddingTop());
                stmt.bindLong(24, value.getPaddingBottom());
                stmt.bindLong(25, value.getCreateTime());
                stmt.bindLong(26, value.getUpdateTime());
            }

            @Override
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `text_view`(`id`,`text`,`textSize`,`textColor`,`hint`,`hintSize`,`hintColor`,`bgColor`,`gravity`,`textStyle`,`inputType`,`minLine`,`maxLine`,`editable`,`width`,`height`,`marginLeft`,`marginRight`,`marginTop`,`marginBottom`,`paddingLeft`,`paddingRight`,`paddingTop`,`paddingBottom`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }
        };
        recordTypeInsert = new EntityInsertionAdapter<RecordType>(db) {
            @Override
            protected void bind(SupportSQLiteStatement stmt, RecordType value) {
                if (value.getId() == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindLong(1, value.getId());
                }
                if (value.getTitle() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getTitle());
                }
                if (value.getItems() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getItems());
                }
                stmt.bindLong(4, value.getStrategy());
                stmt.bindLong(5, value.getCreateTime());
                stmt.bindLong(6, value.getUpdateTime());
            }

            @Override
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `record_type`(`id`,`title`,`items`,`strategy`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?)";
            }
        };
        reviewStrategyInsert = new EntityInsertionAdapter<ReviewStrategy>(db) {
            @Override
            protected void bind(SupportSQLiteStatement stmt, ReviewStrategy value) {
                if (value.getId() == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindLong(1, value.getId());
                }
                if (value.getTitle() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getTitle());
                }
                stmt.bindLong(3, value.getRememberTime());
                stmt.bindLong(4, value.getGapTime());
                stmt.bindLong(5, value.getCreateTime());
                stmt.bindLong(6, value.getUpdateTime());
            }

            @Override
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `review_strategy`(`id`,`title`,`rememberTime`,`gapTime`,`createTime`,`updateTime`) VALUES (?,?,?,?,?,?)";
            }
        };
    }

    public void runInTransaction(Runnable runnable) {
        db.runInTransaction(runnable);
    }

    public long insertRecord(Record record) {
        return recordInsert.insertAndReturnId(record);
    }

    public long insertRecordType(RecordType type) {
        return recordTypeInsert.insertAndReturnId(type);
    }

    public long insertRecordItem(RecordItem item) {
        return recordItemInsert.insertAndReturnId(item);
    }

    public long insertRecordTag(RecordTag tag) {
        return recordTagInsert.insertAndReturnId(tag);
    }

    public long insertTextView(RecordTextView view) {
        return recordTextViewInsert.insertAndReturnId(view);
    }

    public long insertImageView(RecordImageView view) {
        return recordImageViewInsert.insertAndReturnId(view);
    }

    public long insertLayoutView(RecordLayoutView view) {
        return recordLayoutViewInsert.insertAndReturnId(view);
    }

    public long insertReviewStrategy(ReviewStrategy strategy) {
        return reviewStrategyInsert.insertAndReturnId(strategy);
    }
}
