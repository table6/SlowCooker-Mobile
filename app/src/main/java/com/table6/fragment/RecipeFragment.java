package com.table6.fragment;

import android.app.Notification;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.table6.object.Recipe;
import com.table6.slowcooker.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment {
    private static final String ARG_RECIPE_TITLE = "recipeTitle";
    private static final String ARG_RECIPE_PREP_TIME = "recipePrepTime";
    private static final String ARG_RECIPE_COOK_TIME = "recipeCookTime";
    private static final String ARG_RECIPE_SERVING_SIZE = "recipeServingSize";

    // TODO: Rename and change types of parameters
    private String recipeTitle;
    private int recipePrepTime;
    private int recipeCookTime;
    private int recipeServingSize;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.recipeTitle = getArguments().getString(ARG_RECIPE_TITLE);
            this.recipePrepTime = getArguments().getInt(ARG_RECIPE_PREP_TIME);
            this.recipeCookTime = getArguments().getInt(ARG_RECIPE_COOK_TIME);
            this.recipeServingSize = getArguments().getInt(ARG_RECIPE_SERVING_SIZE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set textviews appropriately
        TextView recipeTitleTxt = (TextView) getView().findViewById(R.id.recipeTitle);
        recipeTitleTxt.setText(this.recipeTitle);

        TextView recipeServingSizeTxt = (TextView) getView().findViewById(R.id.recipeServingSize);
        recipeServingSizeTxt.setText(this.recipeServingSize);

        TextView recipePrepTimeTxt = (TextView) getView().findViewById(R.id.recipePrepTime);
        recipeServingSizeTxt.setText(this.recipePrepTime);

        TextView recipeCookTimeTxt = (TextView) getView().findViewById(R.id.recipeCookTime);
        recipeServingSizeTxt.setText(this.recipeCookTime);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false);
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
