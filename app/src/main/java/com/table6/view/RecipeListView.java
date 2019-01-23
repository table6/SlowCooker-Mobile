package com.table6.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.table6.activity.R;
import com.table6.fragment.RecipeFragment;
import com.table6.object.Recipe;

import java.util.ArrayList;

public class RecipeListView extends AppCompatActivity {

    private ArrayList<Recipe> recipes;
    private LinearLayout recipeViews;

    public RecipeListView () {
        this.recipes = new ArrayList<Recipe>();
    }

    public RecipeListView (ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recipeViews = (LinearLayout) findViewById(R.id.recipeListView);

        // Show a generic recipe if the user hasn't saved any yet.
        if(recipes.isEmpty() == true) {
            recipes.add(new Recipe());
            recipes.add(new Recipe());
        }

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        for (Recipe r : recipes) {
            RecipeFragment recipeFragment = RecipeFragment.newInstance(r.getTitle(),
                    r.getPrepTimeMinutes(), r.getCookTimeMinutes(), r.getServingSize());

            fragmentTransaction.add(R.id.recipeListView, recipeFragment);
        }

        fragmentTransaction.commit();
    }
}
