package greenway_myanmar.org.db.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

public abstract class BaseDao<T> {
    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(T obj);

    /**
     * Insert object list in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<T> obj);


    /**
     * Insert an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     */
    @SuppressWarnings("unchecked")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(T... obj);

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    public abstract void update(T obj);

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    public abstract void delete(T obj);

}
