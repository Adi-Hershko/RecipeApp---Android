package com.tiodev.vegtummy.RoomDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class}, exportSchema = false, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
}
