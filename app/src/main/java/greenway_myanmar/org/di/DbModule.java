package greenway_myanmar.org.di;


import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import greenway_myanmar.org.db.GreenWayDb;

@InstallIn(SingletonComponent.class)
@Module
public class DbModule {

    @Singleton
    @Provides
    GreenWayDb provideDb(
            Application app
            ) {
        return Room.databaseBuilder(app, GreenWayDb.class, "greenway.db")
                .enableMultiInstanceInvalidation()
                .fallbackToDestructiveMigration()
                .build();
    }

}