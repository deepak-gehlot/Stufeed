package com.stufeed.pro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.holder.FeedPostViewHolder;
import com.stufeed.pojo.FeedPost;

import static com.stufeed.utility.LocVari.DATABASE_REFERENCE;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;

/**
 * Created by sowmitras on 1/12/17.
 */

public class SavePost extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private TextView unFollow, block, hide_post, delete;
    private Toolbar toolbar;
    private RecyclerView feedView;
    private RecyclerView.LayoutManager feedManager;
    private DatabaseReference mFirebaseDatabaseReference;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder> feedAdapter;

    private DatabaseReference userDatabaseReference ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_recycler_layout);

        enableDisableAnalytics(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.saved_post));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        feedView = (RecyclerView) findViewById(R.id.common_recycler_view);
        feedManager = new GridLayoutManager(this.getApplicationContext(), 1);
        feedView.setLayoutManager(feedManager);
        mFirebaseDatabaseReference = setFireBaseKey(CST_USER_LISTS);
        mFirebaseDatabaseReference.child(getCurrentUser(SavePost.this)).child("saved_posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0){
                    DatabaseReference databaseReference = setFireBaseKey(CST_USER_LISTS).child(getCurrentUser(SavePost.this)).child("saved_posts");
                    //showToast(SavePost.this, dataSnapshot.getValue().toString()+"NOt Empty");
                    feedAdapter = new FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder>(
                            FeedPost.class,
                            R.layout.feed_post_item,
                            FeedPostViewHolder.class,
                            databaseReference){
                        @Override
                        protected void populateViewHolder(final FeedPostViewHolder viewHolder, final FeedPost model, int position) {
                            DATABASE_REFERENCE = setFireBaseKey(CST_POST);
                            DATABASE_REFERENCE.child(model.getGetKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getChildrenCount()!=0){
                                        enableLikeBtn(SavePost.this,viewHolder.like, viewHolder.heart, getDataSnapShotValue(dataSnapshot,CST_POST_REFERENCE_KEY));
                                        enableSaveBtn(SavePost.this,viewHolder.saveValue, viewHolder.savePostImgBtn, getDataSnapShotValue(dataSnapshot,CST_POST_REFERENCE_KEY));
                                        likeSaveOnClick(SavePost.this,viewHolder.like,viewHolder.heart,getDataSnapShotValue(dataSnapshot,CST_POST_REFERENCE_KEY), true);
                                        likeSaveOnClick(SavePost.this,viewHolder.saveValue,viewHolder.savePostImgBtn,getDataSnapShotValue(dataSnapshot,CST_POST_REFERENCE_KEY), false);
                                        showPostDetails(SavePost.this,
                                                getDataSnapShotValue(dataSnapshot,CST_POST_BY),getDataSnapShotValue(dataSnapshot,CST_POST_MEDIA_LINK),
                                                getDataSnapShotValue(dataSnapshot,CST_POST_PRIVACY),getDataSnapShotValue(dataSnapshot,CST_POST_REFERENCE_KEY),
                                                getDataSnapShotValue(dataSnapshot,CST_POST_TEXT),getDataSnapShotValue(dataSnapshot,CST_POST_TYPE),
                                                viewHolder.user_image, viewHolder.postBy,
                                                viewHolder.imageMedia, viewHolder.post_media_link,
                                                viewHolder.play, viewHolder.pause,
                                                viewHolder.docBtn);
                                        viewHolder.postText.setText(getDataSnapShotValue(dataSnapshot,"post_text"));
                                        viewHolder.time.setText(getRelativeTimeDisplay(getDataSnapShotValue(dataSnapshot,"post_time")));
                                        viewHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                               /* goToComment(SavePost.this,
                                                        getDataSnapShotValue(dataSnapshot,CST_POST_BY),
                                                        getDataSnapShotValue(dataSnapshot,CST_POST_COMMENTS),
                                                        getDataSnapShotValue(dataSnapshot,CST_POST_TIME),
                                                        getDataSnapShotValue(dataSnapshot,CST_POST_TEXT),
                                                        getDataSnapShotValue(dataSnapshot,CST_POST_PRIVACY),
                                                        getDataSnapShotValue(dataSnapshot,CST_POST_MEDIA_LINK),
                                                        getDataSnapShotValue(dataSnapshot,CST_POST_TYPE),
                                                        getDataSnapShotValue(dataSnapshot,CST_POST_REFERENCE_KEY),
                                                        String.valueOf(viewHolder.like.getText().toString()));*/
                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    };

                    feedAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onItemRangeInserted(int positionStart, int itemCount) {
                            super.onItemRangeInserted(positionStart, itemCount);
                            int historyCount = feedAdapter.getItemCount();
                            int lastHistory = layoutManager.findLastCompletelyVisibleItemPosition();
                            if ((lastHistory == -1 || (positionStart >= (historyCount - 1) && lastHistory == (positionStart - 1)))) {
                                feedView.scrollToPosition(positionStart);
                            }
                        }
                    });
                    feedView.setLayoutManager(layoutManager);
                    feedView.setAdapter(feedAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

   /* public void enableSaveBtn(final TextView save, final ImageButton common, String key) {
        userDatabaseReference.child(getCurrentUser(this)).child("saved_posts").child(key).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    save.setText("1");
                    common.setImageDrawable(getResources().getDrawable(R.drawable.bookmark_active));
                } else {
                    save.setText("");
                    common.setImageDrawable(getResources().getDrawable(R.drawable.bookmark));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void enableLikeBtn(final TextView save, final ImageButton common, String key) {
        userDatabaseReference.child(getCurrentUser(this)).child("likes").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    save.setText("1");
                    common.setImageDrawable(getResources().getDrawable(R.drawable.post_heart_active));
                } else {
                    save.setText("1");
                    common.setImageDrawable(getResources().getDrawable(R.drawable.heart));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
*/
    /*
        model.getPost_by(),model.getPost_media_link(),
                            model.getPost_privacy(),model.getPost_reference_key(),
                            model.getPost_text(),*/
    /*public void showPostDetails(final String post_by, final String postMediaLink,
                                final String postPrivacy, final String postReference,
                                final String postText, final String getPost_type,
                                final CircleImageView userImage, final TextView fullName,
                                final ImageView imageMedia, final TextView post_media_link,
                                final ImageButton play, final ImageButton pause,
                                final Button docBtn) {
        userDatabaseReference.child(post_by).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    Picasso.with(SavePost.this)
                            .load(getDataSnapShotValue(dataSnapshot, CST_USER_IMAGE))
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(userImage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(SavePost.this)
                                            .load(getDataSnapShotValue(dataSnapshot, CST_USER_IMAGE))
                                            .error(R.drawable.ic_launcher_web)
                                            .into(userImage, new Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError() {
                                                    Log.v("Picasso", "Could not fetch image");
                                                }
                                            });
                                }
                            });
                    fullName.setText(String.valueOf(getDataSnapShotValue(dataSnapshot, CST_USER_FULL_NAME)));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final MediaPlayer mediaplayer;
        mediaplayer = new MediaPlayer();
        final boolean playLoop = false;
        if(getPost_type.equals(CST_DOC_FILE)){
            play.setVisibility(View.GONE);
            pause.setVisibility(View.GONE);
            imageMedia.setVisibility(View.GONE);

            docBtn.setVisibility(View.VISIBLE);
                  *//* TODO: write file download code here*//*

        }else if(getPost_type.equals(CST_AUDIO_FILES)){
            post_media_link.setText(postMediaLink);
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.VISIBLE);

            imageMedia.setVisibility(View.GONE);
            docBtn.setVisibility(View.GONE);

            mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaplayer.setDataSource(postMediaLink);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mediaplayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            play.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View v) {
                    mediaplayer.start();
                    pause.setClickable(true);
                    play.setClickable(false);

                    play.setBackgroundColor(R.color.colorAccent);
                    pause.setBackgroundColor(android.R.color.transparent);

                }
            });
            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaplayer.pause();
                    play.setClickable(true);
                    pause.setClickable(false);
                    play.setBackgroundColor(android.R.color.transparent);
                    pause.setBackgroundColor(R.color.colorAccent);
                }
            });
        }else if(getPost_type.equals(CST_IMAGES_FILES)){
            play.setVisibility(View.GONE);
            pause.setVisibility(View.GONE);
            docBtn.setVisibility(View.GONE);

            imageMedia.setVisibility(View.VISIBLE);

            Picasso.with(SavePost.this)
                    .load(postMediaLink)
                    .fit()
                    .error(R.drawable.ic_launcher_web)
                    .into(imageMedia);

        }
    }*/
}
