package greenway_myanmar.org.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.greenwaymyanmar.common.data.api.v1.Pagination;

import greenway_myanmar.org.db.converter.BigDecimalStringConverter;
import greenway_myanmar.org.db.converter.DateConverter;
import greenway_myanmar.org.db.converter.FloatUnitConverter;
import greenway_myanmar.org.db.converter.ImageListConverter;
import greenway_myanmar.org.db.converter.IntUnitConverter;
import greenway_myanmar.org.db.converter.IntegerListConverter;
import greenway_myanmar.org.db.converter.LatLngListConverter;
import greenway_myanmar.org.db.converter.LocalDateConverter;
import greenway_myanmar.org.db.converter.LongListConverter;
import greenway_myanmar.org.db.converter.PendingActionConverter;
import greenway_myanmar.org.db.converter.StringListConverter;
import greenway_myanmar.org.db.converter.UUIDStringConverter;
import greenway_myanmar.org.db.converter.UriStringConverter;
import greenway_myanmar.org.db.dao.PaginationDao;
import greenway_myanmar.org.db.dao.ThreadDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.InstantConverter;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.LatLngTypeConverter;
import greenway_myanmar.org.features.template.data.source.database.dao.TemplateDao;
import greenway_myanmar.org.features.template.data.source.database.model.TemplateEntity;
import greenway_myanmar.org.vo.Thread;
import greenway_myanmar.org.vo.ThreadComment;

@Database(entities = {
        TemplateEntity.class,
        Thread.class,
        ThreadComment.class,
        Pagination.class
}, version = 1)
@TypeConverters({
        DateConverter.class,
        IntegerListConverter.class,
        LongListConverter.class,
        StringListConverter.class,
        UriStringConverter.class,
        UUIDStringConverter.class,
        LocalDateConverter.class,
        IntUnitConverter.class,
        FloatUnitConverter.class,
        ImageListConverter.class,
        PendingActionConverter.class,
        LatLngListConverter.class,
        BigDecimalStringConverter.class,
        LatLngTypeConverter.class,
        InstantConverter.class,
})
public abstract class GreenWayDb extends RoomDatabase {
    public abstract TemplateDao templateDao();

    public abstract ThreadDao threadDao();

    public abstract PaginationDao paginationDao();
}
