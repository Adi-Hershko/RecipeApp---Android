package com.tiodev.vegtummy.RoomDB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    // TODO: change to img type
    @NonNull
    public String img;

    @NonNull
    public String tittle;

    @NonNull
    public String description;

    @NonNull
    public String ingredients;

    @NonNull
    public String category;

    public Recipe(@NonNull String img, @NonNull String tittle, @NonNull String description, @NonNull String ingredients, @NonNull String category) {
        this.img = img;
        this.tittle = tittle;
        this.description = description;
        this.ingredients = ingredients;
        this.category = category;
    }

    @NonNull
    public String getImg() {
        return img;
    }

    public void setImg(@NonNull String img) {
        this.img = img;
    }

    @NonNull
    public String getTittle() {
        return tittle;
    }

    public void setTittle(@NonNull String tittle) {
        this.tittle = tittle;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(@NonNull String ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }
}

