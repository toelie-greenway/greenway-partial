package greenway_myanmar.org.di;


import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import greenway_myanmar.org.db.GreenWayDb;
import greenway_myanmar.org.features.fishfarmrecord.data.source.database.util.LatLngTypeConverter;

@InstallIn(SingletonComponent.class)
@Module
public class DbModule {

    @Singleton
    @Provides
    GreenWayDb provideDb(Application app, LatLngTypeConverter latLngTypeConverter) {
        return Room.databaseBuilder(app, GreenWayDb.class, "greenway.db")
                .enableMultiInstanceInvalidation()
                .fallbackToDestructiveMigration()
                .addTypeConverter(latLngTypeConverter)
                .build();
    }

//    @Singleton
//    @Provides
//    AnimalDao provideAnimalDao(GreenWayDb db) {
//        return db.animalDao();
//    }

}