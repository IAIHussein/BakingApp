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

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    VideoView mVideoView;
    TextView mDescTextView;
    Button mPrevButton, mNextButton;
    /**
     * The dummy content this fragment is presenting.
     */
    private Step mStep;
    private Recipe mRecipe;

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

            Activity activity = this.getActivity();
            AppBarLayout appBarLayout = (AppBarLayout) activity.findViewById(R.id.app_bar);
            if (appBarLayout != null&&getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                appBarLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        mVideoView = (VideoView) rootView.findViewById(R.id.videoView);
        mDescTextView = (TextView) rootView.findViewById(R.id.item_detail);
        mPrevButton = (Button) rootView.findViewById(R.id.btn_previous);
        mNextButton = (Button) rootView.findViewById(R.id.btn_next);

        setData();

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStep = mRecipe.getSteps().get(mStep.getId() - 1);
                getArguments().putSerializable(Variables.ARG_ITEM, mStep);
                setData();

            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStep = mRecipe.getSteps().get(mStep.getId() + 1);
                getArguments().putSerializable(Variables.ARG_ITEM, mStep);
                setData();

            }
        });

        return rootView;
    }

    void setData() {
        mDescTextView.setText(mStep.getDescription());
        if (mStep.getVideoURL() != null && !mStep.getVideoURL().isEmpty()) {
            mVideoView.setVisibility(View.VISIBLE);
            final MediaController mMediaController = new MediaController(getContext(), true);
            mMediaController.setEnabled(false);
            mVideoView.setMediaController(mMediaController);
            mVideoView.setVideoPath(mStep.getVideoURL());
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mMediaController.setEnabled(true);
                    mVideoView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
        }
        super.onPause();
    }
}
