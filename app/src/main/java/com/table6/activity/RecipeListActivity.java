package com.table6.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.table6.fragment.RecipeDetailFragment;
import com.table6.fragment.RecipeListFragment;
import com.table6.object.RecipeContent;

public class RecipeListActivity extends AppCompatActivity implements RecipeListFragment.OnListFragmentInteractionListener, RecipeDetailFragment.OnFragmentInteractionListener {

    private static final int NUMBER_COLUMNS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list_view);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RecipeListFragment fragment = RecipeListFragment.newInstance(NUMBER_COLUMNS);
        fragmentTransaction.add(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onListFragmentInteraction(RecipeContent.Recipe item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(item);
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
