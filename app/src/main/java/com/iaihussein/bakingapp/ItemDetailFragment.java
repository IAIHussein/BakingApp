package com.iaihussein.bakingapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    @BindView(R.id.video_view)
    EMVideoView mVideoView;
    @BindView(R.id.item_detail)
    TextView mDescTextView;
    @BindView(R.id.btn_previous)
    Button mPrevButton;
    @BindView(R.id.btn_next)
    Button mNextButton;
    /**
     * The dummy content this fragment is presenting.
     */
    private Step mStep;
    private Recipe mRecipe;
    private static int playerPosition;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(Variables.ARG_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mRecipe = (Recipe) getArguments().getSerializable(Variables.SELECTED_RECIPE);
            mStep = (Step) getArguments().getSerializable(Variables.ARG_ITEM);


            AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
            if (appBarLayout != null&&getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                appBarLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        ButterKnife.bind(this,rootView);

        setData();

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStep = mRecipe.getSteps().get(mStep.getId() - 1);
                getArguments().putSerializable(Variables.ARG_ITEM, mStep);
                playerPosition=0;
                setData();

            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStep = mRecipe.getSteps().get(mStep.getId() + 1);
                getArguments().putSerializable(Variables.ARG_ITEM, mStep);
                playerPosition=0;
                setData();

            }
        });

        return rootView;
    }

    void setData() {
        mDescTextView.setText(mStep.getDescription());
        if (mStep.getVideoURL() != null && !mStep.getVideoURL().isEmpty()) {
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.setVideoPath(mStep.getVideoURL());
            mVideoView.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared() {
                    mVideoView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    if(playerPosition!=0)
                        mVideoView.seekTo(playerPosition);
                    mVideoView.start();
                }
            });
        }
        mPrevButton.setEnabled(mStep.getId() != 0);
        mNextButton.setEnabled(mStep.getId() + 1 != mRecipe.getSteps().size());
    }

    @Override
    public void onPause() {
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
            mVideoView = null;
            playerPosition = mVideoView.getCurrentPosition();
        }
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        playerPosition = mVideoView.getCurrentPosition();
        super.onSaveInstanceState(outState);
    }
}
