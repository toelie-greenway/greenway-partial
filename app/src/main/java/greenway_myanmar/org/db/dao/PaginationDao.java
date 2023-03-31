package greenway_myanmar.org.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.greenwaymyanmar.common.data.api.v1.Pagination;

@Dao
public interface PaginationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Pagination result);

    @Query("SELECT * FROM Pagination WHERE id = :paginationKey AND resourceType = :resourceType")
    Pagination findLoadResult(String paginationKey, String resourceType);

    @Query("DELETE FROM Pagination WHERE resourceType = :resourceType")
    void deleteByResourceType(String resourceType);
}
