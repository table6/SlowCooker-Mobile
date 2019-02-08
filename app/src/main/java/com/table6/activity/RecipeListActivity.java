package com.table6.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.table6.fragment.RecipeAddFragment;
import com.table6.fragment.RecipeDetailFragment;
import com.table6.fragment.RecipeListFragment;
import com.table6.object.RecipeContent;

public class RecipeListActivity extends AppCompatActivity implements RecipeListFragment.OnListFragmentInteractionListener,
        RecipeDetailFragment.OnFragmentInteractionListener, RecipeAddFragment.OnRecipeAddFragmentInteractionListener {

    private static final int NUMBER_COLUMNS = 1;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list_view);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        RecipeListFragment fragment = RecipeListFragment.newInstance(NUMBER_COLUMNS);
        fragmentTransaction.add(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

                RecipeAddFragment fragment = RecipeAddFragment.newInstance();
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                fab.hide();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        fab.show();
    }

    @Override
    public void onListFragmentInteraction(RecipeContent.Recipe item) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(item);
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        fab.hide();
    }

    @Override
    public void onRecipeDetailFragmentInteraction(String recipeTitle) {
        RecipeContent.removeItem(recipeTitle);
        RecipeContent.setUserSavedRecipes(getApplicationContext());

        fab.show();
    }

    @Override
    public void onRecipeAddFragmentInteraction(RecipeContent.Recipe recipe) {
        RecipeContent.addItem(recipe);
        RecipeContent.setUserSavedRecipes(getApplicationContext());

        fab.show();
    }
}
