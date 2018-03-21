package com.ggslk.ggslk;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class ArticlesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private RequestQueue mRequestQueue;

    //    Accessories
    private ImageView accessoriesImageView;
    private TextView accessoriesArticleName;
    private TextView accessoriesAuthor;

    //    Android
    private ImageView androidImageView;
    private TextView androidArticleName;
    private TextView androidAuthor;

    //    Chromebooks
    private ImageView chromebooksImageView;
    private TextView chromebooksArticleName;
    private TextView chromebooksAuthor;

    //    Communication and Publishing
    private ImageView communicationAndPublishingImageView;
    private TextView communicationAndPublishingArticleName;
    private TextView communicationAndPublishingAuthor;

    public ArticlesFragment() {
        // Required empty public constructor
    }

    public static ArticlesFragment newInstance() {
        ArticlesFragment fragment = new ArticlesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        updateCategories();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_articles, container, false);

        //        Accessories
        accessoriesImageView = view.findViewById(R.id.accessoriesImageView);
        accessoriesArticleName = view.findViewById(R.id.accessoriesArticleNameTextView);
        accessoriesAuthor = view.findViewById(R.id.accessoriesAuthorTextView);

        //        Android
        androidImageView = view.findViewById(R.id.androidImageView);
        androidArticleName = view.findViewById(R.id.androidArticleNameTextView);
        androidAuthor = view.findViewById(R.id.androidAuthorTextView);

        //        Chromebooks
        chromebooksImageView = view.findViewById(R.id.chromebooksImageView);
        chromebooksArticleName = view.findViewById(R.id.chromebooksArticleNameTextView);
        chromebooksAuthor = view.findViewById(R.id.chromebooksAuthorTextView);

        //        Communication and Publishing
        communicationAndPublishingImageView = view.findViewById(R.id.communicationAndPublishingImageView);
        communicationAndPublishingArticleName = view.findViewById(R.id.communicationAndPublishingArticleNameTextView);
        communicationAndPublishingAuthor = view.findViewById(R.id.communicationAndPublishingAuthorTextView);

       return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onArticlesFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onArticlesFragmentInteraction(Uri uri);
    }

    private void updateCategories(){
        // Update Accessories
        mRequestQueue.add(new JsonObjectRequest
                (Request.Method.GET, "https://ggslk.com/api/get_category_posts?slug=" + "accessories" + "&count=1", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            accessoriesArticleName.setText(response.getJSONArray("posts").getJSONObject(0).get("title").toString());
                            accessoriesAuthor.setText(response.getJSONArray("posts").getJSONObject(0).getJSONObject("author").get("name").toString());

                            String thumbnailURL = response.getJSONArray("posts").getJSONObject(0)
                                    .getJSONArray("attachments").getJSONObject(0)
                                    .getJSONObject("images")
                                    .getJSONObject("full").get("url").toString();
                            Picasso.get().load(thumbnailURL).fit().centerCrop().into(accessoriesImageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }));

        // Update Android
        mRequestQueue.add(new JsonObjectRequest
                (Request.Method.GET, "https://ggslk.com/api/get_category_posts?slug=" + "android" + "&count=1", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            androidArticleName.setText(response.getJSONArray("posts").getJSONObject(0).get("title").toString());
                            androidAuthor.setText(response.getJSONArray("posts").getJSONObject(0).getJSONObject("author").get("name").toString());

                            String thumbnailURL = response.getJSONArray("posts").getJSONObject(0)
                                    .getJSONArray("attachments").getJSONObject(0)
                                    .getJSONObject("images")
                                    .getJSONObject("full").get("url").toString();
                            Picasso.get().load(thumbnailURL).fit().centerCrop().into(androidImageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }));

        // Update Chromebooks
        mRequestQueue.add(new JsonObjectRequest
                (Request.Method.GET, "https://ggslk.com/api/get_category_posts?slug=" + "chromebooks" + "&count=1", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            chromebooksArticleName.setText(response.getJSONArray("posts").getJSONObject(0).get("title").toString());
                            chromebooksAuthor.setText(response.getJSONArray("posts").getJSONObject(0).getJSONObject("author").get("name").toString());

                            String thumbnailURL = response.getJSONArray("posts").getJSONObject(0)
                                    .getJSONArray("attachments").getJSONObject(0)
                                    .getJSONObject("images")
                                    .getJSONObject("full").get("url").toString();
                            Picasso.get().load(thumbnailURL).fit().centerCrop().into(chromebooksImageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }));

        // Update Communication and publishing
        mRequestQueue.add(new JsonObjectRequest
                (Request.Method.GET, "https://ggslk.com/api/get_category_posts?slug=" + "communication_and_publishing" + "&count=1", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            communicationAndPublishingArticleName.setText(response.getJSONArray("posts").getJSONObject(0).get("title").toString());
                            communicationAndPublishingAuthor.setText(response.getJSONArray("posts").getJSONObject(0).getJSONObject("author").get("name").toString());

                            String thumbnailURL = response.getJSONArray("posts").getJSONObject(0)
                                    .getJSONArray("attachments").getJSONObject(0)
                                    .getJSONObject("images")
                                    .getJSONObject("full").get("url").toString();
                            Picasso.get().load(thumbnailURL).fit().centerCrop().into(communicationAndPublishingImageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }));
    }
}
