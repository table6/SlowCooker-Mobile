package com.table6.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.table6.activity.R;
import com.table6.object.Recipe;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_RECIPE_TITLE = "recipeTitle";
    private static final String ARG_RECIPE_PREP_TIME = "recipePrepTime";
    private static final String ARG_RECIPE_COOK_TIME = "recipeCookTime";
    private static final String ARG_RECIPE_SERVING_SIZE = "recipeServingSize";

    // TODO: Rename and change types of parameters
    private String recipeTitle;
    private String  recipePrepTime;
    private String recipeCookTime;
    private String recipeServingSize;

    private OnFragmentInteractionListener mListener;

    public RecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecipeFragment.
     */
    public static RecipeFragment newInstance(String recipeTitle, int recipePrepTime, int recipeCookTime, int recipeServingSize) {
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_TITLE, recipeTitle);
        args.putInt(ARG_RECIPE_PREP_TIME, recipePrepTime);
        args.putInt(ARG_RECIPE_COOK_TIME, recipeCookTime);
        args.putInt(ARG_RECIPE_SERVING_SIZE, recipeServingSize);

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static RecipeFragment newInstance(Recipe recipe) {
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_TITLE, recipe.getTitle());
        args.putString(ARG_RECIPE_PREP_TIME, recipe.getPrepTime());
        args.putString(ARG_RECIPE_COOK_TIME, recipe.getCookTime());
        args.putString(ARG_RECIPE_SERVING_SIZE, recipe.getServingSize());

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.recipeTitle = getArguments().getString(ARG_RECIPE_TITLE);
            this.recipePrepTime = getArguments().getString(ARG_RECIPE_PREP_TIME);
            this.recipeCookTime = getArguments().getString(ARG_RECIPE_COOK_TIME);
            this.recipeServingSize = getArguments().getString(ARG_RECIPE_SERVING_SIZE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView recipeTitleTxt = (TextView) view.findViewById(R.id.recipeTitle);
        recipeTitleTxt.setText(this.recipeTitle);

        // Breaks here because there are several of these.
        TextView recipeServingSizeTxt = (TextView) view.findViewById(R.id.recipeServingSize);
        recipeServingSizeTxt.setText(this.recipeServingSize);

        TextView recipePrepTimeTxt = (TextView) view.findViewById(R.id.recipePrepTime);
        recipePrepTimeTxt.setText(this.recipePrepTime);

        TextView recipeCookTimeTxt = (TextView) view.findViewById(R.id.recipeCookTime);
        recipeCookTimeTxt.setText(this.recipeCookTime);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onClick(View v) {
        System.out.println(v.getId());
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }
}
