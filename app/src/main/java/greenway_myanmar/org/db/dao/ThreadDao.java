package greenway_myanmar.org.db.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import greenway_myanmar.org.vo.Thread;

@Dao
public interface ThreadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Thread> posts);

    @Query("SELECT * FROM Thread WHERE id = :threadId")
    LiveData<Thread> loadThreadLiveData(String threadId);

    @Query("SELECT * FROM Thread WHERE id = :threadId")
    Thread loadThread(String threadId);

    @Query("SELECT * FROM Thread WHERE status = 'pending'")
    List<Thread> loadPendingThread();

    @Query("SELECT * FROM Thread WHERE status = 'deleting'")
    List<Thread> loadDeletingThread();

    @Query("SELECT * FROM Thread WHERE id in (:threadIds) ORDER BY createdAt DESC")
    LiveData<List<Thread>> loadThreadsByIds(List<String> threadIds);

    @RawQuery(observedEntities = Thread.class)
    DataSource.Factory<Integer, Thread> loadThreads(SupportSQLiteQuery query);

    @Query("SELECT * FROM Thread ORDER BY createdAt DESC")
    DataSource.Factory<Integer, Thread> loadThreads();


    @Query("SELECT * FROM Thread ORDER BY createdAt DESC LIMIT 7")
    DataSource.Factory<Integer, Thread> loadLatestThreads();

    @Query("SELECT * FROM Thread WHERE title = :categoryName ORDER BY createdAt DESC")
    DataSource.Factory<Integer, Thread> loadThreadsByCropName(String categoryName);

    @Query("DELETE FROM Thread WHERE status IS NOT 'pending'")
    void delete();

    @Query("DELETE FROM Thread WHERE clientId = :clientId")
    void deleteByClientId(String clientId);

    @Query("DELETE FROM Thread WHERE id = :threadId")
    void deleteById(String threadId);

    @Query(
            "UPDATE Thread SET isSolved = (CASE isSolved WHEN 0 THEN 1 ELSE 0 END) WHERE id ="
                    + " :threadId")
    void toggleSolved(String threadId);

    @Query("UPDATE Thread SET status = 'updating', body = :body WHERE id = :threadId")
    void updateThreadAndMarkAsUpdating(String threadId, String body);

    @Query("UPDATE Thread SET status = 'deleting' WHERE id = :threadId")
    void markAsDeleting(String threadId);

}
