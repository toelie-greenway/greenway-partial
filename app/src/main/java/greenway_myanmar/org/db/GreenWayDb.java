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
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrCategoryExpenseDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrExpenseCategoryDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrExpenseDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFarmDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFcrDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFcrRecordDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrFishDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrProductionRecordDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.dao.FfrSeasonEndReasonDao;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FFrContractFarmingCompanyEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrCategoryExpenseEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrExpenseCategoryEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrExpenseEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFarmEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFcrEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFcrRecordEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrFishEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrProductionRecordEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrSeasonEndReasonEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.model.FfrSeasonEntity;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.FfrFarmInputExpenseListConverter;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.FfrFishListConverter;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.FfrProductionPerFishTypeListConverter;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.InstantConverter;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.LatLngTypeConverter;

@Database(entities = {
        FfrCategoryExpenseEntity.class,
        FFrContractFarmingCompanyEntity.class,
        FfrExpenseEntity.class,
        FfrExpenseCategoryEntity.class,
        FfrFarmEntity.class,
        FfrFcrEntity.class,
        FfrFcrRecordEntity.class,
        FfrFishEntity.class,
        FfrProductionRecordEntity.class,
        FfrSeasonEntity.class,
        FfrSeasonEndReasonEntity.class,
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
        FfrFarmInputExpenseListConverter.class,
        FfrProductionPerFishTypeListConverter.class
})
public abstract class GreenWayDb extends RoomDatabase {
    public abstract FfrCategoryExpenseDao ffrCategoryExpenseDao();
    public abstract FfrExpenseDao ffrExpenseDao();
    public abstract FfrExpenseCategoryDao ffrExpenseCategoryDao();
    public abstract FfrFarmDao ffrFarmDao();
    public abstract FfrFcrDao ffrFcrDao();
    public abstract FfrFcrRecordDao ffrFcrRecordDao();
    public abstract FfrFishDao ffrFishDao();
    public abstract FfrProductionRecordDao ffrProductionRecordDao();
    public abstract FfrSeasonDao ffrSeasonDao();
    public abstract FfrSeasonEndReasonDao ffrSeasonEndReasonDao();
}
