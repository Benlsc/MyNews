package com.lescour.ben.mynews.controller.fragment;

import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.lescour.ben.mynews.model.TheNewYorkTimesResponse;
import com.lescour.ben.mynews.utils.TheNewYorkTimesStreams;

import io.reactivex.observers.DisposableObserver;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TopStoriesFragment extends BaseFragment {

    private TopStoriesRecyclerViewAdapter mTopStoriesRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TopStoriesFragment() {
    }

    public static TopStoriesFragment newInstance(int columnCount) {
        TopStoriesFragment fragment = new TopStoriesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return new TopStoriesFragment();
    }

    @Override
    protected void setAppropriateAdapter() {
        this.mTopStoriesRecyclerViewAdapter = new TopStoriesRecyclerViewAdapter(this.articles, mListener, Glide.with(this));
        recyclerView.setAdapter(this.mTopStoriesRecyclerViewAdapter);
    }

    @Override
    protected void notifyRecyclerView() {
        mTopStoriesRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void executeHttpRequestWithRetrofit(){
        this.disposable = TheNewYorkTimesStreams.streamFetchTopStories("home").subscribeWith(new DisposableObserver<TheNewYorkTimesResponse>() {
            @Override
            public void onNext(TheNewYorkTimesResponse response) {
                Log.e("TAG", "On Next");
                updateUI(response);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "On Error" + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG", "On Complete !!");
            }
        });
    }

}
