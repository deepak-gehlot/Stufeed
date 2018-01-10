package com.stufeed.pro;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stufeed.R;
import com.stufeed.adapters.BoardListAdapter;
import com.stufeed.pojo.Board;
//import com.stufeed.frag.ShareToBoard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;

/**
 * Created by sowmitras on 3/11/17.
 */

public class Post extends AppCompatActivity implements View.OnClickListener{

    private List<String> createdBoard = new ArrayList<String>();
    private List<String> publicBoard = new ArrayList<String>();
    private ArrayList<String> boardKey = new ArrayList<String>();
    private ArrayList<String> boardName = new ArrayList<String>();
    private ArrayList<String> nameTemp = new ArrayList<String>();
    private SharedPreferences sharedPref;
    private String username;
    private ArrayAdapter<String> boardAdapter;
    private ArrayAdapter<String> publiAdapter;
    private ListView board_list,singleItem;
    private MediaPlayer mediaPlayer = null;
    private AlertDialog.Builder builder;
    private String documentFilePath,audioFilePath,imageFilePath;
    private ImageView attach_file, post_audio, post_Images, postImg, cancelImg, startStopImg,documentImg;
    private LinearLayout linearForAudio, documentLayout;
    private TextView audioText, documentPath;
    private RelativeLayout imageRL;
    private LinearLayout linearLayout;

    private EditText postEditText;
    private Button attachBtn;
    private Toolbar toolbar;
    private boolean backPressCheck;
    private RelativeLayout userData, share_feed_post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.post);
        attachBtn = (Button) findViewById(R.id.attachButton);
        attachBtn.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle(getResources().getString(R.string.post));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        board_list = (ListView) findViewById(R.id.board_list);
        userData = (RelativeLayout) findViewById(R.id.userData);
        share_feed_post = (RelativeLayout) findViewById(R.id.share_feed_post);
        singleItem = (ListView) findViewById(R.id.singleItem);

        sharedPref = getSharedPreferences(CST_SHARED_REFERENCE, Activity.MODE_PRIVATE);
        username = sharedPref.getString(CST_USER_NAME, "");

        publicBoard.clear();
        publicBoard.add("Public Post");
        publiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, publicBoard);
        publiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        singleItem.setAdapter(publiAdapter);
        singleItem.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);




        singleItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SparseBooleanArray checked = singleItem.getCheckedItemPositions();
                if (checked.get(0)) {
                    for (int i = 0; i < board_list.getAdapter().getCount(); i++) {
                        board_list.setItemChecked(i,false);
                        board_list.setClickable(false);
                        board_list.setEnabled(false);
                    }
                }else {
                    singleItem.setItemChecked(0, false);
                    board_list.setClickable(true);
                    board_list.setEnabled(true);

                }
            }
        });
        board_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        board_list.setAdapter(new BoardListAdapter(this, getCities()));

        board_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] strings = new String[boardKey.size()];
                Log.i(LOGCAT, String.valueOf(strings.length));
                SparseBooleanArray checked = board_list.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    if(checked.valueAt(i) == false){
                        Log.i(LOGCAT, String.valueOf(false));

                        return;
                    }if(checked.valueAt(i) == true){
                        Log.i(LOGCAT, String.valueOf(true));
                        nameTemp.add(boardKey.get(i));
                        Log.i(LOGCAT, String.valueOf(nameTemp));
                        return;
                    }


                }
            }
        });

        postImg = (ImageView) findViewById(R.id.postImg);
        linearForAudio = (LinearLayout) findViewById(R.id.musicText);
        audioText = (TextView) findViewById(R.id.audioText);
        documentLayout = (LinearLayout) findViewById(R.id.documentLayout);
        imageRL = (RelativeLayout)findViewById(R.id.imageRL);
        postEditText= (EditText) findViewById(R.id.postEditText);
        startStopImg= (ImageView) findViewById(R.id.startStopImg);
        documentImg= (ImageView) findViewById(R.id.documentImg);
        documentPath= (TextView) findViewById(R.id.documentPath);


        startStopImg.setOnClickListener(this);

        mediaPlayer = null;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        cancelImg = (ImageView) findViewById(R.id.cancelImg);
        cancelImg.setOnClickListener(this);


    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.attachButton:
                builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                builder.setTitle("Select Your Attachment");
                builder.setMessage("Allowed file size 4 megabyte");
                View dialogView= inflater.inflate(R.layout.attach_dialog_layout, null);
                builder.setView(dialogView);
                attach_file = (ImageView) dialogView.findViewById(R.id.attach_file);
                attach_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        audioFilePath = null;
                        if (android.os.Build.VERSION.SDK_INT >= 22) {
                            if (checkPermissionS(Post.this)) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("application/msword,application/pdf");
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                startActivityForResult(intent, FILE_REQUEST);
                                return;
                            }askForPermission(Post.this);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword,application/pdf");
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            startActivityForResult(intent, FILE_REQUEST);
                        }
                    }
                });
                post_audio = (ImageView) dialogView.findViewById(R.id.post_audio1);
                post_audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        documentFilePath = null;
                        if (android.os.Build.VERSION.SDK_INT >= 22) {
                            if (checkPermissionS(Post.this)) {
                                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, AUDIO_REQUEST);
                                return;
                            }askForPermission(Post.this);
                        } else {
                            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, AUDIO_REQUEST);
                        }
                    }
                });

                post_Images = (ImageView)dialogView.findViewById(R.id.post_image);
                post_Images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageFilePath = null;
                        final Dialog student_dialog = new Dialog(Post.this);
                        student_dialog.setContentView(R.layout.student_image_layout);

                        TextView take_photo = (TextView) student_dialog.findViewById(R.id.take_photo);
                        TextView gallery = (TextView) student_dialog.findViewById(R.id.gallery);
                        TextView cancel_image = (TextView) student_dialog.findViewById(R.id.img_cancel);

                        take_photo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (android.os.Build.VERSION.SDK_INT >= 22) {
                                    if (isCameraPermission(Post.this)) {
                                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, CAMERA_REQUEST);
                                        student_dialog.dismiss();
                                        return;
                                    }
                                    askForPermisssion(Post.this);
                                } else {
                                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                    student_dialog.dismiss();
                                }
                                student_dialog.dismiss();
                            }


                        });
                        gallery.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, REQUEST_CODE);
                                student_dialog.dismiss();
                            }
                        });
                        cancel_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                student_dialog.dismiss();
                            }
                        });
                        student_dialog.show();
                    }
                });
                builder.create();
                builder.show();
                break;
            case R.id.startStopImg:
                playPauseMusic(Post.this,mediaPlayer,startStopImg);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == FILE_REQUEST){
                documentFilePath = getFileNameByUri(this, getFileUri(data));
                documentLayout.setVisibility(View.VISIBLE);

                documentPath.setText(documentFilePath);
                linearForAudio.setVisibility(View.VISIBLE);
                postImg.setVisibility(View.INVISIBLE);
                cancelImg.setVisibility(View.GONE);
                linearForAudio.setVisibility(View.GONE);

            }else if(requestCode == AUDIO_REQUEST){
                String[] FILE = {MediaStore.Audio.Media.DATA};
                Cursor cursor = getContentResolver().query(getFileUri(data), FILE, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(FILE[0]);
                String audio = cursor.getString(columnIndex);
                cursor.close();
                audioFilePath = audio;
                try {
                    mediaPlayer = null;
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(audioFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                audioText.setText(audioFilePath);
                linearForAudio.setVisibility(View.VISIBLE);
                documentLayout.setVisibility(View.GONE);
                postImg.setVisibility(View.INVISIBLE);

                cancelImg.setVisibility(View.VISIBLE);
                linearForAudio.setVisibility(View.VISIBLE);
            }else if(requestCode == CAMERA_REQUEST){
                imageFilePath = getImageFromCamera(data,postImg);
                postImg.setVisibility(View.VISIBLE);
                documentLayout.setVisibility(View.GONE);
                linearForAudio.setVisibility(View.GONE);

                cancelImg.setVisibility(View.VISIBLE);
                imageRL.setVisibility(View.VISIBLE);
            }else if(requestCode == REQUEST_CODE){
                Bitmap bitmap = null;
                InputStream stream = null;
                imageFilePath = getFileNameByUri(this, getFileUri(data));
                try {
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                    stream = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    postImg.setImageBitmap(bitmap);
                    postImg.setVisibility(View.VISIBLE);
                    documentLayout.setVisibility(View.GONE);
                    linearForAudio.setVisibility(View.GONE);

                    cancelImg.setVisibility(View.VISIBLE);
                    imageRL.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    if (stream != null)
                        try {
                            stream.close();
                        } catch (IOException e1) {
                            e.printStackTrace();
                        }
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if(backPressCheck){
            super.onBackPressed();
            return;
        }
        this.backPressCheck = true;
        goToNext(this, BOARD_SCREEN,true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_next, menu);
        final MenuItem next = menu.findItem(R.id.next_button);
        next.setVisible(true);
        final MenuItem post_feed = menu.findItem(R.id.post_feed);
        next.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                setNextBtn(post_feed, next);
                return false;
            }
        });
        post_feed.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //setPostFeedBtn(post_feed, next);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.next_button) {
            setNextBtn(item, item);
            return true;
        }else if(id == R.id.post_feed){
            setPostFeedBtn(item, item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setPostFeedBtn(MenuItem post_feed, MenuItem next) {
        DATABASE_REFERENCE = setFireBaseKey(CST_POST);
        String key = DATABASE_REFERENCE.push().getKey();
        post_feed.setVisible(true);
        next.setVisible(false);
        if(singleItem.getCheckedItemCount()==1){nameTemp.clear();
            if(documentFilePath!=null){
                uploadFile(this,CST_DOC_FILE, documentFilePath, CST_FIREBASE_STORAGE, String.valueOf(postEditText.getText()), nameTemp);
            }else if (audioFilePath!=null){
                uploadAudio(this,CST_AUDIO_FILES, audioFilePath, CST_FIREBASE_STORAGE, String.valueOf(postEditText.getText()),nameTemp);
            }else if(imageFilePath!=null){
                uploadImage(this,CST_IMAGES_FILES, imageFilePath, CST_FIREBASE_STORAGE, String.valueOf(postEditText.getText()),nameTemp);
            }else{
                saveAndUpdateToServer("",Post.this,String.valueOf(postEditText.getText()),"",key);
            }
        }else{
            if(documentFilePath!=null){
                uploadFile(this,CST_DOC_FILE, documentFilePath, CST_FIREBASE_STORAGE, String.valueOf(postEditText.getText()),nameTemp);
            }else if (audioFilePath!=null){
                uploadAudio(this,CST_AUDIO_FILES, audioFilePath, CST_FIREBASE_STORAGE, String.valueOf(postEditText.getText()),nameTemp);
            }else if(imageFilePath!=null){
                uploadImage(this,CST_IMAGES_FILES, imageFilePath, CST_FIREBASE_STORAGE, String.valueOf(postEditText.getText()),nameTemp);
            }else{
                saveAndUpdateToServer("",Post.this,String.valueOf(postEditText.getText()),"", key, nameTemp);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void setNextBtn(MenuItem postFeed, MenuItem next){
        if(postEditText.getText().toString().isEmpty()){
            showSnackBar(attachBtn, "Please write some thing", null);
            postFeed.setVisible(false);
            next.setVisible(true);
        }else{
            postFeed.setVisible(true);
            next.setVisible(false);

            attachBtn.setVisibility(View.GONE);

            userData.setVisibility(View.GONE);
            share_feed_post.setVisibility(View.VISIBLE);
            hideKeyBoard(postEditText,this);
        }
    }

    private static ArrayList<String>  getSelectedValue(ListView list) {
        SparseBooleanArray checked = list.getCheckedItemPositions();
        ArrayList<String> arraylist=new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            if(checked.valueAt(i) == true) {
                String tag = list.getItemAtPosition(i).toString();
                arraylist.add(tag);
            }
        }
        return arraylist;
    }

    private List<Board> getCities() {
        final List<Board> boards = new LinkedList<Board>();
        DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
        DATABASE_REFERENCE.orderByChild(CST_BOARD_CREATED_BY).equalTo(getCurrentUser(this)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren().iterator().hasNext();
                createdBoard.clear();
                boardKey.clear();
                boardName.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    createdBoard.add(snapshot.child(CST_BOARD_NAME).getValue().toString());
                    boardKey.add(snapshot.getKey().toString());
                    boards.add(new Board(String.valueOf(snapshot.child("board_name").getValue().toString()), String.valueOf(snapshot.getKey().toString())));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return boards;
    }
}
