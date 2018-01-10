package com.stufeed.pro;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stufeed.R;
import com.stufeed.holder.CommentViewHolder;
import com.stufeed.pojo.Comments;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;

/**
 * Created by sowmitras on 2/12/17.
 */

public class Comment  extends AppCompatActivity {

    private TextView like;
    private TextView save;

    private TextView postBy;
    private TextView postWithinBoard;
    private TextView postText;
    private TextView post_media_link;
    private EditText comment_editText;

    private ImageButton heart_like;
    private ImageButton commentBtn;
    private ImageButton sharePost;
    private ImageButton savePostImgBtn;
    private ImageButton send_common;
    private ImageButton postDown;
    private ImageButton play;
    private ImageButton pause;

    private CircleImageView user_image;

    private RecyclerView users_comment;

    private CardView comments;


    private ImageView imageMedia;

    private Button docBtn;

    private LinearLayout viewCountLinearLayout;
    private LinearLayout likeCountLinearLayout;
    private LinearLayout commentCountLinearLayout;
    private LinearLayout countMain;

    private TextView likeTVShow;
    private TextView commentTVShow;
    private TextView viewTVShow;

    private RecyclerView commentView;
    private RecyclerView.LayoutManager commentManager;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Comments, CommentViewHolder> commentAdapter;
    private DatabaseReference posts;

    private Bundle bundle = new Bundle();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.feed_post_item);

        posts = setFireBaseKey(CST_POST);

        bundle = getIntent().getExtras();


        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        commentView = (RecyclerView) findViewById(R.id.users_comment);
        commentManager = new GridLayoutManager(this.getApplicationContext(), 1);
        commentView.setLayoutManager(commentManager);


        postBy = (TextView) findViewById(R.id.postBy);
        postWithinBoard = (TextView) findViewById(R.id.postWithinBoard);
        post_media_link = (TextView) findViewById(R.id.post_media_link);
        comment_editText = (EditText) findViewById(R.id.comment_editText);
        postText = (TextView) findViewById(R.id.postText);
        showToast(this, String.valueOf(postText.getLineCount()));
        heart_like = (ImageButton) findViewById(R.id.heart_like);
        commentBtn = (ImageButton) findViewById(R.id.commentBtn);

        viewCountLinearLayout = (LinearLayout) findViewById(R.id.viewCountLinearLayout);
        commentCountLinearLayout = (LinearLayout) findViewById(R.id.likeCountLinearLayout);
        likeCountLinearLayout = (LinearLayout) findViewById(R.id.commentCountLinearLayout);
        countMain = (LinearLayout) findViewById(R.id.countMain);

        likeTVShow = (TextView) findViewById(R.id.likeTV);
        commentTVShow = (TextView) findViewById(R.id.commentTV);
        viewTVShow = (TextView) findViewById(R.id.viewTV);

        sharePost = (ImageButton) findViewById(R.id.sharePost);
        savePostImgBtn = (ImageButton) findViewById(R.id.savePostImgBtn);
        user_image = (CircleImageView) findViewById(R.id.user_image);
        imageMedia = (ImageView) findViewById(R.id.imageMedia);
        play = (ImageButton) findViewById(R.id.playBtn);
        pause = (ImageButton) findViewById(R.id.pauseBtn);
        send_common = (ImageButton) findViewById(R.id.send_common);
        docBtn = (Button) findViewById(R.id.docBtn);
        like = (TextView) findViewById(R.id.likeValue);
        save = (TextView) findViewById(R.id.saveValue);

        users_comment = (RecyclerView) findViewById(R.id.users_comment);

        comments = (CardView) findViewById(R.id.userComment);
        comments.setVisibility(View.VISIBLE);
        commentBtn.setVisibility(View.GONE);
        users_comment.setVisibility(View.VISIBLE);

        if(!bundle.isEmpty()&& bundle!=null){
            postBy.setText(String.valueOf(bundle.get(CST_POST_BY).toString()));

            showCountDetails(Integer.valueOf(String.valueOf(bundle.get(CST_COMMENTS))),String.valueOf(bundle.get(CST_POST_REFERENCE_KEY)), countMain, commentCountLinearLayout, commentTVShow, 1);
            enableLikeBtn(Comment.this,like, heart_like, String.valueOf(bundle.get(CST_POST_REFERENCE_KEY)));
            enableSaveBtn(Comment.this,save, savePostImgBtn, String.valueOf(bundle.get(CST_POST_REFERENCE_KEY)));
            likeSaveOnClick(Comment.this,like, heart_like, String.valueOf(bundle.get(CST_POST_REFERENCE_KEY)), true);
            likeSaveOnClick(Comment.this,save, savePostImgBtn, String.valueOf(bundle.get(CST_POST_REFERENCE_KEY)), false);

            showPostDetails(Comment.this,
                    String.valueOf(bundle.get(CST_POST_BY).toString()),
                    String.valueOf(bundle.get(CST_POST_MEDIA_LINK)),
                    String.valueOf(bundle.get(CST_POST_PRIVACY)),
                    String.valueOf(bundle.get(CST_POST_REFERENCE_KEY)),
                    String.valueOf(bundle.get(CST_POST_TEXT)),
                    String.valueOf(bundle.get(CST_POST_TYPE)),
                    user_image, postBy,
                    imageMedia,post_media_link,
                    play, pause,
                    docBtn);
            send_common.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(TextUtils.isEmpty(comment_editText.getText())){
                        showSnackBar(view,"Empty","");
                    }else {

//                        String post_by, String time, String comments, String replied
                        Comments feedPost = new Comments(getCurrentUser(Comment.this),String.valueOf(System.currentTimeMillis()),String.valueOf(comment_editText.getText().toString()),"");


                        posts.child(String.valueOf(bundle.get(CST_POST_REFERENCE_KEY))).child("comments").push().setValue(feedPost);
                        comment_editText.setText("");
                    }


                }
            });

            commentAdapter = new FirebaseRecyclerAdapter<Comments, CommentViewHolder>(
                    Comments.class,
                    R.layout.comment_item,
                    CommentViewHolder.class,
                    posts.child(String.valueOf(bundle.get(CST_POST_REFERENCE_KEY))).child("comments")) {
                @Override
                protected void populateViewHolder(final CommentViewHolder viewHolder, final Comments model, final int position) {
                    /*String post_by, String time, String comments, String replied*/
                    //showToast(Comment.this, String.valueOf(commentAdapter.getRef(position).getKey()));
                   // viewHolder.Layout_hide();
                    if(!String.valueOf(commentAdapter.getRef(position).getKey()).equals(String.valueOf(bundle.get(CST_POST_REFERENCE_KEY)))){
                        //viewHolder.Layout_show();
                        viewHolder.comment_RL.setVisibility(View.VISIBLE);
                        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
                        DATABASE_REFERENCE.child(model.getPost_by()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getChildrenCount()!=0){
                                    Picasso.with(Comment.this)
                                            .load(getDataSnapShotValue(dataSnapshot, CST_USER_IMAGE))
                                            .networkPolicy(NetworkPolicy.OFFLINE)

                                            .into(viewHolder.comment_user_image, new Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError() {
                                                    Picasso.with(Comment.this)
                                                            .load(getDataSnapShotValue(dataSnapshot, CST_USER_IMAGE))
                                                            .error(R.drawable.ic_launcher_web)
                                                            .into(viewHolder.comment_user_image, new Callback() {
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
                                    viewHolder.comment_by.setText(String.valueOf(getDataSnapShotValue(dataSnapshot, CST_USER_FULL_NAME)));
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        viewHolder.comment_time.setText(getRelativeTimeDisplay(model.getTime()));
                        viewHolder.comment.setText(model.getComments());
                        viewHolder.comment_reply.setText(model.getReplied());
                    }else{
                        viewHolder.comment_RL.setVisibility(View.GONE);
                        //viewHolder.Layout_hide();
                    }



                }
            };

            commentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    int historyCount = commentAdapter.getItemCount();
                    int lastHistory = layoutManager.findLastCompletelyVisibleItemPosition();
                    if ((lastHistory == -1 || (positionStart >= (historyCount - 1) && lastHistory == (positionStart - 1)))) {
                        commentView.scrollToPosition(positionStart);
                    }
                }
            });
            commentView.setLayoutManager(layoutManager);
            commentView.setAdapter(commentAdapter);

        }
    }
}
