package com.stufeed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stufeed.holder.FeedPostViewHolder;
import com.stufeed.pojo.FeedPost;
import com.stufeed.pojo.LikesAndSave;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.stufeed.utility.SowmitrasStrings.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.LocVari.*;

/**
 * Created by sowmitras on 28/11/17.
 */

public class PostView extends Activity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private TextView unFollow, block, hide_post, delete;

    private RecyclerView feedView;
    private RecyclerView.LayoutManager feedManager;
    private DatabaseReference mFirebaseDatabaseReference;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder> feedAdapter;

    private DatabaseReference userDatabaseReference = setFireBaseKey(CST_USER_LISTS);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_recycler_layout);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);


        feedView = (RecyclerView) findViewById(R.id.common_recycler_view);
        feedManager = new GridLayoutManager(this.getApplicationContext(), 1);
        feedView.setLayoutManager(feedManager);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(CST_POST);
        feedAdapter = new FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder>(
                FeedPost.class,
                R.layout.feed_post_item,
                FeedPostViewHolder.class,
                mFirebaseDatabaseReference) {
            @Override
            protected void populateViewHolder(final FeedPostViewHolder viewHolder, final FeedPost model, final int position) {
                /*              final String post_by, final String postMediaLink,
                                final String postPrivacy, final String postReference,
                                final String postText, final String getPost_type,
                                final CircleImageView userImage, final TextView fullName,
                                final ImageButton imageMedia, final TextView post_media_link,
                                final ImageButton play, final ImageButton pause*/


                showPostDetails(
                        model.getPost_by(),model.getPost_media_link(),
                        model.getPost_privacy(),model.getPost_reference_key(),
                        model.getPost_text(), model.getPost_type(),
                        viewHolder.user_image, viewHolder.postBy,
                        viewHolder.imageMedia, viewHolder.post_media_link,
                        viewHolder.play, viewHolder.pause,
                        viewHolder.docBtn);
                viewHolder.postText.setText(model.getPost_text());
                viewHolder.time.setText(getRelativeTimeDisplay(model.getPost_time()));
                enableLikeBtn(viewHolder.like, viewHolder.heart, model.getPost_reference_key());
                enableSaveBtn(viewHolder.saveValue, viewHolder.savePostImgBtn, model.getPost_reference_key());
                viewHolder.heart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewHolder.likeValue.getText().toString().equals("1")) {
                            viewHolder.likeValue.setText("");
                            viewHolder.heart.setImageDrawable(getResources().getDrawable(R.drawable.heart));

                            userDatabaseReference.child(getCurrentUser(PostView.this)).child("likes").child(model.getPost_reference_key()).removeValue();
                        } else {
                            viewHolder.likeValue.setText("1");
                            viewHolder.heart.setImageDrawable(getResources().getDrawable(R.drawable.post_heart_active));
                            LikesAndSave likesAndSave = new LikesAndSave(String.valueOf(System.currentTimeMillis()), "1",model.getPost_reference_key());
                            userDatabaseReference.child(getCurrentUser(PostView.this)).child("likes").child(model.getPost_reference_key()).setValue(likesAndSave);
                        }
                    }
                });


                viewHolder.savePostImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewHolder.saveValue.getText().toString().equals("1")) {
                            viewHolder.saveValue.setText("");
                            viewHolder.savePostImgBtn.setImageDrawable(getResources().getDrawable(R.drawable.bookmark));
                            userDatabaseReference.child(getCurrentUser(PostView.this)).child("saved_posts").child(model.getPost_reference_key()).removeValue();
                        } else {
                            viewHolder.saveValue.setText("1");
                            viewHolder.savePostImgBtn.setImageDrawable(getResources().getDrawable(R.drawable.bookmark_active));

                            LikesAndSave likesAndSave = new LikesAndSave(String.valueOf(System.currentTimeMillis()), "1",model.getPost_reference_key());
                            userDatabaseReference.child(getCurrentUser(PostView.this)).child("saved_posts").child(model.getPost_reference_key()).setValue(likesAndSave);
                        }
                    }
                });


                if (model.getPost_by().equals(getCurrentUser(PostView.this))) {
                    viewHolder.Layout_hide();
                    viewHolder.Layout_show();
                    viewHolder.post_item.setVisibility(View.VISIBLE);
                    viewHolder.postDownAeroText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            View v1, v2, v3;
                            final Dialog feedDialog = new Dialog(PostView.this);
                            Window window = feedDialog.getWindow();
                            WindowManager.LayoutParams wlp = window.getAttributes();
                            //wlp.gravity = Gravity.CENTER;
                            window.setAttributes(wlp);
                            feedDialog.setContentView(R.layout.post_dialog_layout);
                            unFollow = (TextView) feedDialog.findViewById(R.id.unfollow);
                            block = (TextView) feedDialog.findViewById(R.id.block);
                            hide_post = (TextView) feedDialog.findViewById(R.id.hide_post);
                            delete = (TextView) feedDialog.findViewById(R.id.delete);
                            v1 = (View) feedDialog.findViewById(R.id.v1);
                            v2 = (View) feedDialog.findViewById(R.id.v2);
                            v3 = (View) feedDialog.findViewById(R.id.v3);
                            unFollow.setVisibility(View.GONE);
                            block.setVisibility(View.GONE);
                            hide_post.setVisibility(View.GONE);
                            v1.setVisibility(View.GONE);
                            v2.setVisibility(View.GONE);
                            v3.setVisibility(View.GONE);

                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DATABASE_REFERENCE = null;
                                    DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference("posts");
                                    DATABASE_REFERENCE.child(model.getPost_reference_key()).removeValue();
                                    feedDialog.dismiss();
                                }
                            });
                            feedDialog.show();
                        }
                    });
                }else{
                    viewHolder.Layout_hide();

                    viewHolder.postDownAeroText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog feedDialog = new Dialog(PostView.this);
                            Window window = feedDialog.getWindow();
                            WindowManager.LayoutParams wlp = window.getAttributes();
                            wlp.gravity = Gravity.CENTER;
                            window.setAttributes(wlp);
                            feedDialog.setContentView(R.layout.post_dialog_layout);
                            unFollow = (TextView) feedDialog.findViewById(R.id.unfollow);
                            block = (TextView) feedDialog.findViewById(R.id.block);
                            hide_post = (TextView) feedDialog.findViewById(R.id.hide_post);
                            delete = (TextView) feedDialog.findViewById(R.id.delete);
                            delete.setVisibility(View.GONE);
                            View v3 = (View) feedDialog.findViewById(R.id.v3);
                            v3.setVisibility(View.GONE);
                            unFollow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //removeFollower(username, model.getPost_by());
                                    //removeFollowing(username, model.getPost_by());
                                    unFollow.setText("Follow");
                                    feedAdapter.notifyDataSetChanged();

                                    feedDialog.dismiss();
                                }
                            });

                            block.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //removeFollower(username, model.getPost_by());
                                    //removeFollowing(username, model.getPost_by());
                                    unFollow.setText("Follow");

                                    String key = userDatabaseReference.push().getKey();
                                    Map<String, Object> taskMap = new HashMap<String, Object>();
                                    taskMap.put("username", model.getPost_by());
                                    userDatabaseReference.child(getCurrentUser(PostView.this)).child("blocked").child(key).updateChildren(taskMap);

                                    feedAdapter.notifyDataSetChanged();

                                    feedDialog.dismiss();
                                }
                            });

                            hide_post.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    feedDialog.dismiss();
                                }
                            });
                            feedDialog.show();
                        }
                    });
                }




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

    public void enableSaveBtn(final TextView save, final ImageButton common, String key) {
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


    /*model.getPost_by(),model.getPost_media_link(),
                        model.getPost_privacy(),model.getPost_reference_key(),
                        model.getPost_text(),*/
    public void showPostDetails(final String post_by, final String postMediaLink,
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
                    Picasso.with(PostView.this)
                            .load(getDataSnapShotValue(dataSnapshot, CST_USER_IMAGE))
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(userImage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(PostView.this)
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
                   /*TODO: write file download code here*/

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

            Picasso.with(PostView.this)
                    .load(postMediaLink)
                    .fit()
                    .error(R.drawable.ic_launcher_web)
                    .into(imageMedia);

        }
    }
}
