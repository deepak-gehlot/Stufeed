package com.stufeed.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.stufeed.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sowmitras on 27/11/17.
 */

public class FeedPostViewHolder extends RecyclerView.ViewHolder {

        public TextView post_by;
        public TextView post_reference_key;
        public TextView post_like_key;
        public TextView post_comments;
        public TextView post_like;
        public TextView post_media_link;
        public TextView post_privacy;
        public TextView post_text;
        public TextView comment;

        public TextView post_time;
        public TextView post_user_image;
        public TextView post_within_board;
        public ImageButton commentBtn;


        public CircleImageView user_image;
        public TextView postBy;
        public TextView postWithinBoard;
        public TextView time;
        public TextView postText;
        public TextView like;//        post_item = (Lin)
        public TextView likeValue;
        public ImageButton postDownAeroText;
        public ImageButton play;
        public ImageButton pause;
        public ImageButton heart;
        public ImageButton savePostImgBtn;
        public Button docBtn;
        public ImageView imageMedia;

        public TextView liveCount;
        public TextView commentCounts;
        public TextView viewTVShow;
        public TextView save;
        public TextView saveValue;


        public RelativeLayout post_item;
        public RelativeLayout.LayoutParams params;

        public LinearLayout viewCountLinearLayout;
        public LinearLayout likeCountLinearLayout;
        public LinearLayout commentCountLinearLayout;
        public LinearLayout countMain;

        public CardView userComment;
        public RecyclerView users_comments;



        public FeedPostViewHolder(View v) {
                super(v);
                post_item = (RelativeLayout) itemView.findViewById(R.id.post_item);
                params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                post_comments = (TextView) itemView.findViewById(R.id.post_comments);
                post_like = (TextView) itemView.findViewById(R.id.post_like);
                post_media_link = (TextView) itemView.findViewById(R.id.post_media_link);
                post_privacy = (TextView) itemView.findViewById(R.id.post_privacy);
                post_text = (TextView) itemView.findViewById(R.id.post_text);
                post_time = (TextView) itemView.findViewById(R.id.post_time);
                post_within_board = (TextView) itemView.findViewById(R.id.post_within_image);
                post_user_image = (TextView) itemView.findViewById(R.id.post_user_image);
                post_reference_key = (TextView) itemView.findViewById(R.id.post_reference_key);
                likeValue = (TextView) itemView.findViewById(R.id.likeValue);
                // comment = (TextView) itemView.findViewById(R.id.comment);
                commentBtn = (ImageButton) itemView.findViewById(R.id.commentBtn);

                user_image = (CircleImageView) itemView.findViewById(R.id.user_image);
                postBy = (TextView) itemView.findViewById(R.id.postBy);
                postDownAeroText = (ImageButton) itemView.findViewById(R.id.postDown);
                postWithinBoard = (TextView) itemView.findViewById(R.id.postWithinBoard);
                time = (TextView) itemView.findViewById(R.id.time);
                postText = (TextView) itemView.findViewById(R.id.postText);
                like = (TextView) itemView.findViewById(R.id.likeValue);
                post_like_key = (TextView) itemView.findViewById(R.id.post_like_key);

                liveCount = (TextView) itemView.findViewById(R.id.likeTV);
                commentCounts = (TextView) itemView.findViewById(R.id.commentTV);
                viewTVShow = (TextView) itemView.findViewById(R.id.viewTV);
                savePostImgBtn = (ImageButton) itemView.findViewById(R.id.savePostImgBtn);
                save = (TextView) itemView.findViewById(R.id.save);
                saveValue = (TextView) itemView.findViewById(R.id.saveValue);

                play = (ImageButton)itemView.findViewById(R.id.playBtn);
                pause = (ImageButton) itemView.findViewById(R.id.pauseBtn);
                heart = (ImageButton) itemView.findViewById(R.id.heart_like);

                docBtn = (Button) itemView.findViewById(R.id.docBtn);

                imageMedia = (ImageView) itemView.findViewById(R.id.imageMedia);

                viewCountLinearLayout = (LinearLayout) itemView.findViewById(R.id.viewCountLinearLayout);
                commentCountLinearLayout = (LinearLayout) itemView.findViewById(R.id.likeCountLinearLayout);
                likeCountLinearLayout = (LinearLayout) itemView.findViewById(R.id.commentCountLinearLayout);
                countMain = (LinearLayout) itemView.findViewById(R.id.countMain);

                userComment = (CardView)itemView.findViewById(R.id.userComment);
                users_comments = (RecyclerView) itemView.findViewById(R.id.users_comment);

        }

        public void Layout_hide() {
                params.height = 0;
                //itemView.setLayoutParams(params); //This One.
                post_item.setLayoutParams(params);   //Or This one.
        }

        public void Layout_show() {
                params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                params.width = RecyclerView.LayoutParams.MATCH_PARENT;
                //itemView.setLayoutParams(params); //This One.
                params.setMargins(10,5,10,5);//Todo (Left,Top, Right,Bottom)
                post_item.setLayoutParams(params);   //Or This one.
        }


}