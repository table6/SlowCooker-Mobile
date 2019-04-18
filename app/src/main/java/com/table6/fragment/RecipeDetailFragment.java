package com.table6.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.table6.activity.R;
import com.table6.object.RecipeContent;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_PREP_TIME = "prepTime";
    private static final String ARG_COOK_TIME = "cookTime";
    private static final String ARG_SERVING_SIZE = "servingSize";
    private static final String ARG_DIRECTIONS = "directions";
    private static final String ARG_INGREDIENTS = "ingredients";

    private String title;
    private String prepTime;
    private String cookTime;
    private String servingSize;
    private String directions;
    private ArrayList<String> ingredients;

    private OnFragmentInteractionListener mListener;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailFragment newInstance(String title, String prepTime, String cookTime, String servingSize, String directions, ArrayList<String> ingredients) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_PREP_TIME, prepTime);
        args.putString(ARG_COOK_TIME, cookTime);
        args.putString(ARG_SERVING_SIZE, servingSize);
        args.putString(ARG_DIRECTIONS, directions);
        args.putStringArrayList(ARG_INGREDIENTS, ingredients);
        fragment.setArguments(args);

        return fragment;
    }

    public static RecipeDetailFragment newInstance(RecipeContent.Recipe recipe) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, recipe.title);
        args.putString(ARG_PREP_TIME, recipe.prepTime);
        args.putString(ARG_COOK_TIME, recipe.cookTime);
        args.putString(ARG_SERVING_SIZE, recipe.servingSize);
        args.putString(ARG_DIRECTIONS, recipe.directions);
        args.putStringArrayList(ARG_INGREDIENTS, recipe.ingredients);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.title = getArguments().getString(ARG_TITLE);
            this.prepTime = getArguments().getString(ARG_PREP_TIME);
            this.cookTime = getArguments().getString(ARG_COOK_TIME);
            this.servingSize = getArguments().getString(ARG_SERVING_SIZE);
            this.directions = getArguments().getString(ARG_DIRECTIONS);
            this.ingredients = getArguments().getStringArrayList(ARG_INGREDIENTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView recipeTitleTxt = (TextView) view.findViewById(R.id.recipeDetailTitle);
        recipeTitleTxt.setText(this.title);

        LinearLayout ingredientContainer = (LinearLayout) view.findViewById(R.id.recipeDetailIngredientContainer);
        TextView ingredientTextView;
        if (!ingredients.isEmpty()) {
            for (String ingredient : ingredients) {
                ingredientTextView = new TextView(getContext());
                ingredientTextView.setText(ingredient);
                ingredientContainer.addView(ingredientTextView);
            }
        } else {
            ingredientTextView = new TextView(getContext());
            ingredientTextView.setText("No ingredients to show");
        }

        TextView recipeDirectionsTxt = (TextView) view.findViewById(R.id.recipeDetailDirections);
        if (!directions.isEmpty()) {
            recipeDirectionsTxt.setText(this.directions);
        } else {
            recipeDirectionsTxt.setText("No directions to show");
        }

        TextView recipeServingSizeTxt = (TextView) view.findViewById(R.id.recipeDetailServingSize);
        recipeServingSizeTxt.setText(this.servingSize);

        TextView recipePrepTimeTxt = (TextView) view.findViewById(R.id.recipeDetailPrepTime);
        recipePrepTimeTxt.setText(this.prepTime);

        TextView recipeCookTimeTxt = (TextView) view.findViewById(R.id.recipeDetailCookTime);
        recipeCookTimeTxt.setText(this.cookTime);

        final String recipeTitle = this.title;

        Button recipeEditBtn = (Button) view.findViewById(R.id.recipeDetailEditBtn);
        recipeEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecipeDetailFragmentEditInteraction(new RecipeContent.Recipe(title, prepTime, cookTime, servingSize, directions, ingredients));
            }
        });

        Button recipeRemoveBtn = (Button) view.findViewById(R.id.recipeDetailRemoveBtn);
        recipeRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), recipeTitle + " deleted from recipe list", Toast.LENGTH_LONG).show();
                mListener.onRecipeDetailFragmentDeleteInteraction(recipeTitle);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onRecipeDetailFragmentDeleteInteraction(String recipeTitleToRemove);
        void onRecipeDetailFragmentEditInteraction(RecipeContent.Recipe recipe);
    }
}
