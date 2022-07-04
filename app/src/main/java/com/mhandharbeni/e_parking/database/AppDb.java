package com.mhandharbeni.e_parking.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mhandharbeni.e_parking.database.helpers.DataConverter;
import com.mhandharbeni.e_parking.database.helpers.DateConverter;
import com.mhandharbeni.e_parking.database.interfaces.InterfaceParked;
import com.mhandharbeni.e_parking.database.migrations.Migrations;
import com.mhandharbeni.e_parking.database.models.Parked;
import com.mhandharbeni.e_parking.utils.Constant;

@TypeConverters({
        DateConverter.class,
        DataConverter.class
})
@Database(
        entities = {
                Parked.class
        },
        version = Constant.DB_VERSION,
        exportSchema = Constant.DB_EXPORT
)
public abstract class AppDb extends RoomDatabase {
    public abstract InterfaceParked parked();

    private static volatile AppDb INSTANCE;

    public static AppDb getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDb.class,
                                    Constant.DB_NAME
                            )
                            .addMigrations(
                                    Migrations.MIGRATION_0_1
                            )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}