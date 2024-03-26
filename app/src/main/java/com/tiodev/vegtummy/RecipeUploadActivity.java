package com.tiodev.vegtummy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.tiodev.vegtummy.RoomDB.AppDatabase;
import com.tiodev.vegtummy.RoomDB.Recipe;
import com.tiodev.vegtummy.RoomDB.RecipeDao;

public class RecipeUploadActivity extends AppCompatActivity {

    private Spinner category;
    private EditText title, ingredients, description, hours, minutes;
    private Button uploadPhotoButton, uploadRecipeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_form);

        // Initialize UI components
        category = findViewById(R.id.category);
        title = findViewById(R.id.title);
        ingredients = findViewById(R.id.ingridients); // Check for typos in 'ingredients'
        description = findViewById(R.id.description);
        hours = findViewById(R.id.hours);
        minutes = findViewById(R.id.minutes);
        uploadPhotoButton = findViewById(R.id.button2);
        uploadRecipeButton = findViewById(R.id.button);

        // Populate the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recipe_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        // Handle spinner selection
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Here you can handle the selection event
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Another interface callback
            }

        });

        // Setup button listeners
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle photo upload
            }
        });

        uploadRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate and collect form data, then handle recipe upload
                handleRecipeUpload();
            }
        });
    }

    private void handleRecipeUpload() {
        String recipeImage = "default_image_path"; // Adjust as needed for image handling
        String recipeTitle = title.getText().toString().trim();
        String recipeDescription = description.getText().toString().trim();
        String recipeIngredients = ingredients.getText().toString().trim();
        String selectedCategory = category.getSelectedItem().toString();
        String hoursText = hours.getText().toString().trim();
        String minutesText = minutes.getText().toString().trim();

        // Initialize missing fields message builder
        StringBuilder missingFields = new StringBuilder();
        if (recipeTitle.isEmpty()) missingFields.append("Title, ");
        if (recipeIngredients.isEmpty()) missingFields.append("Ingredients, ");
        if (recipeDescription.isEmpty()) missingFields.append("Description, ");
        if (selectedCategory.equals("Please choose a category")) missingFields.append("Category, ");
        if (hoursText.isEmpty() && minutesText.isEmpty()) missingFields.append("Time, ");

        // Check for missing fields
        if (missingFields.length() > 0) {
            missingFields.setLength(missingFields.length() - 2); // Remove trailing comma and space
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Missing fields: " + missingFields, Toast.LENGTH_LONG).show());
            return;
        }

        // Parse and prepare the cooking time
        int totalMinutes = parseCookingTime(hoursText, minutesText);
        recipeIngredients = (totalMinutes > 0 ? totalMinutes + " min\n" : "") + recipeIngredients;

        Recipe newRecipe = new Recipe(recipeImage, recipeTitle, recipeDescription, recipeIngredients, selectedCategory);

        insertRecipeIntoDb(newRecipe);
    }

    private int parseCookingTime(String hours, String minutes) {
        int hoursToMinutes = hours.isEmpty() ? 0 : Integer.parseInt(hours) * 60;
        int minutesToInt = minutes.isEmpty() ? 0 : Integer.parseInt(minutes);
        return hoursToMinutes + minutesToInt;
    }

    private void navigateBackHome() {
        // Introducing a slight delay before navigating back
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(RecipeUploadActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }, 2000); // 2-second delay for the user to see the toast
    }

    private void insertRecipeIntoDb(Recipe recipe) {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "recipe.db")
                    .allowMainThreadQueries() // Caution: It's generally not recommended to allow main thread queries
                    .createFromAsset("database/recipe.db") // This assumes your database file is in the "assets/database" directory
                    .fallbackToDestructiveMigration() // This will clear the database on version mismatch. Use with caution.
                    .build();

            RecipeDao recipeDao = db.recipeDao();

            // Insert the recipe into the database
            recipeDao.insert(recipe);

            // Since the database operations are performed in a background thread, make sure to switch
            // back to the main thread to update the UI or navigate to another activity
            runOnUiThread(() -> {
                Toast.makeText(RecipeUploadActivity.this, "Recipe added successfully", Toast.LENGTH_SHORT).show();
                navigateBackHome();
            });
        }).start();
    }
}

