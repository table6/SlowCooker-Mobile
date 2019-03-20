package com.table6.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.table6.activity.R;
import com.table6.object.RecipeContent;

import java.util.ArrayList;

public class RecipeEditFragment extends Fragment {
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

    private OnRecipeEditFragmentInteractionListener mListener;

    public RecipeEditFragment() {
        // Required empty public constructor
    }

    public static RecipeEditFragment newInstance(RecipeContent.Recipe recipe) {
        RecipeEditFragment fragment = new RecipeEditFragment();

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

    public static RecipeEditFragment newInstance(String title, String prepTime, String cookTime, String servingSize, String directions, ArrayList<String> ingredients) {
        RecipeEditFragment fragment = new RecipeEditFragment();

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText titleEditTxt = (TextInputEditText) view.findViewById(R.id.recipeEditTitleTextInput);
        titleEditTxt.setText(this.title);

        TextInputEditText prepTimeEditTxt = (TextInputEditText) view.findViewById(R.id.recipeEditPrepTimeTextInput);
        prepTimeEditTxt.setText(this.prepTime);

        TextInputEditText cookTimeEditTxt = (TextInputEditText) view.findViewById(R.id.recipeEditCookTimeTextInput);
        cookTimeEditTxt.setText(this.cookTime);

        TextInputEditText servingSizeEditTxt = (TextInputEditText) view.findViewById(R.id.recipeEditServingSizeTextInput);
        servingSizeEditTxt.setText(this.servingSize);

        TextInputEditText directionsEditTxt = (TextInputEditText) view.findViewById(R.id.recipeEditDirectionsTextInput);
        directionsEditTxt.setText(this.directions);

        LinearLayout ingredientsContainer = (LinearLayout) view.findViewById(R.id.recipeEditIngredientsContainer);
        for(String ingredient : this.ingredients) {
            TextInputEditText editText = new TextInputEditText(view.getContext());
            editText.setText(ingredient);
            ingredientsContainer.addView(editText);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeEditFragmentInteractionListener ) {
            mListener = (OnRecipeEditFragmentInteractionListener) context;
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
    public interface OnRecipeEditFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRecipeEditFragmentInteraction(Uri uri);
    }
}
