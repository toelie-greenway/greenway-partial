package greenway_myanmar.org.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

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
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFarmDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFcrDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFcrRecordDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFishDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FFrContractFarmingCompanyEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFarmEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFcrEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFcrRecordEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFishEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrSeasonEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.FfrFishListConverter;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.InstantConverter;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.LatLngTypeConverter;

@Database(entities = {
        FfrFarmEntity.class,
        FfrSeasonEntity.class,
        FfrFishEntity.class,
        FfrFcrRecordEntity.class,
        FfrFcrEntity.class,
        FFrContractFarmingCompanyEntity.class
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
        FfrFishListConverter.class,
})
public abstract class GreenWayDb extends RoomDatabase {

    public abstract FfrFarmDao ffrFarmDao();

    public abstract FfrSeasonDao ffrSeasonDao();

    public abstract FfrFcrRecordDao ffrFcrRecordDao();

    public abstract FfrFcrDao ffrFcrDao();

    public abstract FfrFishDao ffrFishDao();
}
