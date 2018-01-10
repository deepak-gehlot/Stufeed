package com.stufeed.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stufeed.R;
import com.stufeed.frag.DatePickerFragment;
import com.stufeed.holder.BoardViewHolder;
import com.stufeed.holder.FeedPostViewHolder;
import com.stufeed.pojo.Board;
import com.stufeed.pojo.Comments;
import com.stufeed.pojo.FeedPost;
import com.stufeed.pojo.JoinBoard;
import com.stufeed.pojo.LikesAndSave;
import com.stufeed.pojo.User;
import com.stufeed.pro.Archive;
import com.stufeed.pro.Blocked;
import com.stufeed.pro.Comment;
import com.stufeed.pro.EditProfile;
import com.stufeed.pro.FollowerFollowing;
import com.stufeed.pro.GetFeed;
import com.stufeed.pro.Home;
import com.stufeed.pro.CollegeApproval;
import com.stufeed.pro.GetStarted;
import com.stufeed.pro.InstituteRegistration;
import com.stufeed.pro.JoinBoards;
import com.stufeed.pro.Login;
import com.stufeed.pro.Notifiy;
import com.stufeed.pro.Post;
import com.stufeed.pro.Register;
import com.stufeed.Splash;
import com.stufeed.pro.SavePost;
import com.stufeed.pro.Setting;
import com.stufeed.pro.UserSearch;
import com.stufeed.pro.UserView;
import com.stufeed.pro.ViewBoard;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasStrings.*;

/**
 * Created by sowmitras on 24-10-2017.
 */

public class SowmitrasMethod {

    /**
     * TODO getUser Count
     *
     * @param rootKey
     * @param role
     **/
    public static boolean findUserAvailability(String rootKey, String role) {
        final boolean[] totalCount = {false};
        DATABASE_REFERENCE = setFireBaseKey(rootKey);
        DATABASE_REFERENCE.child(rootKey).child(role).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    totalCount[0] = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return totalCount[0];
    }

    /**
     * TODO: Get Follower and Following
     */
    public static ArrayList<String> getFollowerFollowingUserName(String username, String followerfollowing) {
        final ArrayList<String> strings = new ArrayList<String>();
        DATABASE_REFERENCE = setFireBaseKey(followerfollowing);
        DATABASE_REFERENCE.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    dataSnapshot.getChildren().iterator().hasNext();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getChildren().iterator().hasNext();
                        strings.add(snapshot.child(USER_ID).getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return strings;
    }

    /**
     * TOdo:// Read Bytes From File
     */
    public static byte[] readBytesFromFile(String filePath) {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;
        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }

    /**
     * TODO ProgressDialog With Position
     *
     * @param context
     * @param message
     * @param position
     */
    public static ProgressDialog loadProgressDialog(Context context, String message, int position) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.getWindow().setGravity(position);
        //progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return progressDialog;
    }

    /**
     * TODO Set Value TO Shared Preference with Field Name Input
     *
     * @param spEditor
     * @param preferences
     * @param field
     * @param value
     */
    public static boolean setToLocalDataBase(SharedPreferences.Editor spEditor, SharedPreferences preferences, String field, String value) {
        spEditor = preferences.edit();
        spEditor.putString(field, value);
        spEditor.commit();
        return true;
    }


    /**
     * TODO Get Values From SharePreference with return String
     */
    public static String getFromLocalDataBase(SharedPreferences preferences, String keyName) {
        String value = String.valueOf(preferences.getString(keyName, ""));
        return value;
    }

    /**
     * TODO Get Current User
     *
     * @param activity
     */
    public static String getCurrentUser(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(CST_SHARED_REFERENCE, Activity.MODE_PRIVATE);
        String username = sharedPreferences.getString(CST_USER_NAME, "");
        return username;
    }

    /**
     * TODO Hide KeyBoard
     *
     * @params view
     * @params activity
     */
    @SuppressLint("NewApi")
    public static void hideKeyBoard(View view, Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * TODO DatabaseFireBase Reference
     *
     * @params root
     */
    public static DatabaseReference setFireBaseKey(String root) {
        DatabaseReference databaseReference = null;
        databaseReference = FirebaseDatabase.getInstance().getReference(root);
        return databaseReference;
    }


    /**
     * TODO SnackBar
     *
     * @params view
     * @params yourComment
     * @params yourAction
     **/
    public static void showSnackBar(View view, String yourComment, String yourAction) {
        Snackbar.make(view, yourComment, Snackbar.LENGTH_LONG).setAction(yourAction, null).show();
    }


    /**
     * ToDO Delete Firebase Emails
     */
    public static void deleteEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    } else {
                    }
                }
            });
        }
    }

    /**
     * TODO Check EMAIL Verification
     */
    public static boolean checkIfEmailVerified(FirebaseAuth firebaseAuth, FirebaseUser firebaseUser) {
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        boolean check = firebaseUser.isEmailVerified();
        Log.i(LOGCAT, "Check = " + check);
        return check;
    }

    /**
     * TOdo Check Login Status
     */
    public static boolean checkLoginStatus(FirebaseAuth firebaseAuth, FirebaseUser firebaseUser) {
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        boolean check = false;
        if (firebaseUser == null) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }

    /**
     * TODO Get User College and ID
     */
    public static void getUserDetails(FirebaseAuth mFireBaseAuth, final FirebaseUser mFireBaseUser,
                                      final ProgressDialog progressDialog, final View view,
                                      final SharedPreferences sharedPref, final SharedPreferences.Editor editor,
                                      final String passProtection,
                                      final Activity activity) {
        progressDialog.setMessage(LOADING_YOUR_PROFILE);
        progressDialog.show();
        DATABASE_REFERENCE = setFireBaseKey(CST_ACCOUNTS);
        DATABASE_REFERENCE.child(mFireBaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    showSnackBar(view, "Invalid Login", "");
                } else {
                    String college_id = String.valueOf(dataSnapshot.child(CST_CG_ID).getValue().toString());
                    String college_name = String.valueOf(dataSnapshot.child(CST_COLLEGE).getValue().toString());
                    String userNameStr = String.valueOf(dataSnapshot.child(CST_USER_NAME).getValue().toString());
                    String roleStr = String.valueOf(dataSnapshot.child(CST_ROLE).getValue().toString());
                    String emailStr = String.valueOf(dataSnapshot.child(CST_EMAIL_ID).getValue().toString());
                    String contactStr = String.valueOf(dataSnapshot.child(CST_CONTACT_NUMBER).getValue().toString());
                    if (college_id.equals("") & college_name.equals("")) {
                        boolean b2 = setToLocalDataBase(editor, sharedPref, CST_USER_NAME, userNameStr);
                        boolean b3 = setToLocalDataBase(editor, sharedPref, CST_ROLE, roleStr);
                        boolean b4 = setToLocalDataBase(editor, sharedPref, CST_EMAIL_ID, emailStr);
                        boolean b5 = setToLocalDataBase(editor, sharedPref, CST_CONTACT_NUMBER, contactStr);
                        boolean b6 = setToLocalDataBase(editor, sharedPref, PASSWORD, passProtection);
                        boolean b7 = setToLocalDataBase(editor, sharedPref, CST_ACCOUNT_KEY, mFireBaseUser.getUid());
                        if (b2 & b3 & b4 & b5 & b6 & b7) {
                            activity.startActivity(new Intent(activity, CollegeApproval.class));
                            activity.finish();
                        }
                    } else {
                        getUserValues(sharedPref, editor, userNameStr, college_id, activity, progressDialog);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * ToDO Save USER DETAILs TO TEMP
     */
    public static void getUserValues(final SharedPreferences sharedPref, SharedPreferences.Editor editor,
                                     String tempUser, final String collegeId,
                                     final Activity activity, final ProgressDialog progressDialog) {
        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
        DATABASE_REFERENCE.child(tempUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    setToLocalDataBase(editor, sharedPref, CST_USER_NAME, String.valueOf(dataSnapshot.getKey().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_BRANCH, String.valueOf(dataSnapshot.child(CST_BRANCH).getValue().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_CONTACT_NUMBER, String.valueOf(dataSnapshot.child(CST_CONTACT_NUMBER).getValue().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_COLLEGE, String.valueOf(dataSnapshot.child(CST_COLLEGE).getValue().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_EMAIL_ID, String.valueOf(dataSnapshot.child(CST_EMAIL_ID).getValue().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_GRADUATION_YEAR, String.valueOf(dataSnapshot.child(CST_GRADUATION_YEAR).getValue().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_GENDER, String.valueOf(dataSnapshot.child(CST_GENDER).getValue().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_PROGRAM, String.valueOf(dataSnapshot.child(CST_PROGRAM).getValue().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_ABOUT_STATUS, String.valueOf(dataSnapshot.child(CST_ABOUT_STATUS).getValue().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_BIRTHDAY, String.valueOf(dataSnapshot.child(CST_BIRTHDAY).getValue().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_ROLE, String.valueOf(dataSnapshot.child(CST_ROLE).getValue().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_USER_FULL_NAME, String.valueOf(dataSnapshot.child(CST_USER_FULL_NAME).getValue().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_USER_IMAGE, String.valueOf(dataSnapshot.child(CST_USER_IMAGE).getValue().toString()));
                    setToLocalDataBase(editor, sharedPref, CST_CG_ID, collegeId);

                    editor.putString(COMMON_FLAG, MY_PROFILE_FLAG);
                    editor.commit();
                    boolean b2 = setToLocalDataBase(editor, sharedPref, CST_USER_NAME, String.valueOf(dataSnapshot.getKey().toString()));
                    if (b2) {
                        progressDialog.dismiss();
                        goToNext(activity, BOARD_SCREEN, true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * TODO Go to Next
     */
    public static void goToNext(Activity from, String classname, boolean finish) {
        switch (classname) {
            case BOARD_SCREEN:
                from.startActivity(new Intent(from, Home.class));
                break;
            case COLLEGE_APPROVAL_SCREEN:
                from.startActivity(new Intent(from, CollegeApproval.class));
                break;
            case GET_STARTED_SCREEN:
                from.startActivity(new Intent(from, GetStarted.class));
                break;
            case LOGIN_SCREEN:
                from.startActivity(new Intent(from, Login.class));
                break;
            case REGISTER_SCREEN:
                from.startActivity(new Intent(from, Register.class));
                break;
            case SPLASH_SCREEN:
                from.startActivity(new Intent(from, Splash.class));
                break;
            case FOLLOWER_FOLLOWING:
                from.startActivity(new Intent(from, FollowerFollowing.class));
                break;
            case USER_VIEW:
                from.startActivity(new Intent(from, UserView.class));
                break;
            case POST:
                from.startActivity(new Intent(from, Post.class));
                break;
            case NOTIFICATION:
                from.startActivity(new Intent(from, Notifiy.class));
                break;
            case EDIT_PROFILE:
                from.startActivity(new Intent(from, EditProfile.class));
                break;
            case GET_FEEDS_SCREEN:
                from.startActivity(new Intent(from, GetFeed.class));
                break;
            case JOIN_BOARD_SCREEN:
                from.startActivity(new Intent(from, JoinBoards.class));
                break;
            case SETTING_SCREEN:
                from.startActivity(new Intent(from, Setting.class));
                break;
            case SAVEPOST_SCREEN:
                from.startActivity(new Intent(from, SavePost.class));
                break;
            case COMMENT_SCREEN:
                from.startActivity(new Intent(from, Comment.class));
                break;
            case VIEW_BORAD:
                from.startActivity(new Intent(from, ViewBoard.class));
                break;
            case ARCHIVE_SCREEN:
                from.startActivity(new Intent(from, Archive.class));
                break;
            case INSTITUTE_REGISTRATION:
                from.startActivity(new Intent(from, InstituteRegistration.class));
                break;
            case BLOCKED_SCREEN:
                from.startActivity(new Intent(from, Blocked.class));
                break;
            case USER_SEARCH:
                from.startActivity(new Intent(from, UserSearch.class));
                break;
            default:
                break;
        }
        if (finish) {
            from.finish();
            Log.i(LOGCAT, "FINISH");
        }else{
            Log.i(LOGCAT, "FINISH un");
        }
    }

    /**
     * TODO Logout
     */
    public static void logOut(SharedPreferences.Editor editor, String key, String flag, int identifier) {
        switch (identifier) {
            /**TODO Landing From CollegeApproval*/
            case 0:
                editor.putString(key, flag);
                editor.commit();
                break;
            /**TODO Landing From SignOut*/
            case 1:
                editor.clear();
                editor.commit();
                break;
        }
    }

    /**
     * TODO Shared Preference initial
     */
    public static SharedPreferences setSharedPreference(Activity activity, String preferenceName) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
        return sharedPreferences;
    }

    /**
     * TODO: Show Toast
     *
     * @param activity
     * @param msg
     **/
    public static void showToast(Activity activity, String msg) {
        Toast.makeText(activity, String.valueOf(msg), Toast.LENGTH_SHORT).show();
    }

    /**
     * TODO: Save User Details To FireBase
     *
     * @param user
     * @param userName
     */
    public static void saveUserDetails(User user, String userName) {
        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
        DATABASE_REFERENCE.child(userName).setValue(user);
    }

    /**
     * TODO: Set Map
     *
     * @param mapKey
     * @param mapValue
     */
    public static Map<String, Object> setMapValue(String mapKey, String mapValue) {
        final Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put(mapKey, mapValue);
        return taskMap;
    }

    /**
     * TODO: ProgressBar
     *
     * @param activity
     * @param progressBarId
     */
    public static ProgressBar setProgressBar(Activity activity, int progressBarId) {
        ProgressBar progressBar = (ProgressBar) activity.findViewById(progressBarId);
        return progressBar;
    }

    /**
     * TODO: AdView
     *
     * @param activity
     * @param mAdViewId
     * @param progressBarAds
     */
    public static AdView showAds(Activity activity, int mAdViewId, final ProgressBar progressBarAds) {
        final AdView adView = (AdView) activity.findViewById(mAdViewId);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                progressBarAds.setVisibility(View.GONE);
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                progressBarAds.setVisibility(View.GONE);
                adView.setVisibility(View.GONE);
            }
        });
        return adView;
    }

    /**
     * TODO: set RecyclerView
     *
     * @param columnCount
     * @param
     * @param
     */
    public static RecyclerView setRecyclerViewFragment(Activity activity, View view, int columnCount, int recyclerViewId) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(recyclerViewId);
        RecyclerView.LayoutManager recyclerLayoutManager = new GridLayoutManager(activity.getApplicationContext(), columnCount);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        return recyclerView;
    }

    /**
     * TODO: set RecyclerView
     *
     * @param columnCount
     * @param
     * @param
     */
    public static RecyclerView setRecyclerViewActivity(Activity activity, int columnCount, int recyclerViewId) {
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(recyclerViewId);
        RecyclerView.LayoutManager recyclerLayoutManager = new GridLayoutManager(activity.getApplicationContext(), columnCount);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        return recyclerView;
    }

    /**
     * TODO DataSnapShot For FireBase Single Value
     *
     * @param key
     * @param dataSnapshot
     */
    public static String getDataSnapShotValue(DataSnapshot dataSnapshot, String key) {
        String s = String.valueOf(dataSnapshot.child(key).getValue().toString());
        return s;
    }

    /**
     * TODO: SetUserDetails in View
     *
     * @param activity
     * @param userId
     * @param circleImageView
     * @param progressBar
     * @param userFullName
     */
    public static void showUserDetails(final Activity activity, final String userId, final TextView accountKeyTextView, final CircleImageView circleImageView, final ProgressBar progressBar, final TextView userFullName) {
        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
        DATABASE_REFERENCE.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picasso.with(activity)
                        .load(getDataSnapShotValue(dataSnapshot, CST_USER_IMAGE))
                        .into(circleImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                userFullName.setText(getDataSnapShotValue(dataSnapshot, CST_USER_FULL_NAME));
                accountKeyTextView.setText(getDataSnapShotValue(dataSnapshot, CST_ACCOUNT_KEY));
                //showToast(activity, getDataSnapShotValue(dataSnapshot, CST_ACCOUNT_KEY));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * TODO: get Count For Post, Follower, JoinBoards
     */
    public static TextView getCount(String getCountFor, String fireBaseKey, String username, int textFieldId, Activity activity) {
        final TextView textView = (TextView) activity.findViewById(textFieldId);
        DATABASE_REFERENCE = setFireBaseKey(fireBaseKey);
        switch (getCountFor) {
            case CST_JOINED:
                DATABASE_REFERENCE.child(username).child(CST_JOIN_BOARD).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            textView.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                            Log.i(LOGCAT, dataSnapshot.getValue().toString());
                        } else {
                            textView.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case CST_POST:
                DATABASE_REFERENCE.orderByChild(CST_POST_BY).equalTo(username).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            textView.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        } else {
                            textView.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case CST_FOLLOWER:
                DATABASE_REFERENCE.child(username).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            textView.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        } else {
                            textView.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
        }
        return textView;
    }

    /**
     * TODO: Get Real Path Name By URI
     */
    public static String getFileNameByUri(Context context, Uri uri) {
        String filepath = "";
        File file;
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION}, null, null, null);
            int column_index = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                column_index = cursor.getColumnIndexOrThrow(String.valueOf(MediaStore.getDocumentUri(context, uri)));
            }
            //int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String mFilePath = cursor.getString(column_index);
            cursor.close();
            filepath = mFilePath;
        } else if (uri.getScheme().compareTo("file") == 0) {
            try {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            filepath = uri.getPath();
        }
        return filepath;
    }

    /**
     * TODO: Check Permission
     */
    public static boolean checkPermissionS(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA);
        int gallery_write = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int gallery_read = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else if (gallery_write == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else if (gallery_read == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * ToDo: Ask For Permission
     */
    public static void askForPermission(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.CAMERA)) {
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        }
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);
    }

    /**
     * ToDo:// Upload File To Storage
     */
    public static void uploadFile(final Activity activity, final String fileFormat, String filePath, String StorageLocation, final String feedText, final ArrayList<String> list) {
        final ProgressDialog dialog = loadProgressDialog(activity, "Uploading...", CENTER_DISPLAY);
        dialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(StorageLocation);
        Uri file = Uri.fromFile(new File(filePath));
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("application/docx")
                .setCustomMetadata("StuFeed", "Private Data")
                .build();
        UploadTask uploadTask = storageReference.child(fileFormat + file.getLastPathSegment()).putFile(file, metadata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast(activity, "Unsuccessfully");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String url = String.valueOf(downloadUrl);
                DATABASE_REFERENCE = setFireBaseKey(CST_POST);
                String key = DATABASE_REFERENCE.push().getKey();
                if(list!=null&& list.isEmpty()){
                    saveAndUpdateToServer(url,activity, feedText,fileFormat,key, list);
                }else{
                    saveAndUpdateToServer(url,activity, feedText,fileFormat,key);
                }

                dialog.dismiss();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                dialog.setMessage("Uploading..." + String.valueOf(progress.intValue()) + "%");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }

    public static void uploadImage(final Activity activity, final String fileFormat, String filePath, String StorageLocation, final String feedText, final ArrayList<String> list) {
        final ProgressDialog dialog = loadProgressDialog(activity, "Uploading...", CENTER_DISPLAY);
        dialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(StorageLocation);
        Uri file = Uri.fromFile(new File(filePath));
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .setCustomMetadata("StuFeed", "Private Data")
                .build();
        UploadTask uploadTask = storageReference.child(fileFormat + file.getLastPathSegment()).putFile(file, metadata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast(activity, "Unsuccessfully");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String url = String.valueOf(downloadUrl);
                DATABASE_REFERENCE = setFireBaseKey(CST_POST);
                String key = DATABASE_REFERENCE.push().getKey();
                if(list!=null&& list.isEmpty()){
                    saveAndUpdateToServer(url,activity, feedText,fileFormat,key, list);
                }else{
                    saveAndUpdateToServer(url,activity, feedText,fileFormat,key);
                }
                dialog.dismiss();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                dialog.setMessage("Uploading..." + String.valueOf(progress.intValue()) + "%");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }

    public static void saveAndUpdateToServer(String downloadUrl, Activity activity, String feedText,String fileFormat, String key) {
        feedData(activity, feedText, downloadUrl,fileFormat, key);
    }

    private static void feedData(Activity activity, String feedText, String downloadUrl,String fileFormat, String key) {
        DATABASE_REFERENCE = setFireBaseKey(CST_POST);
        Comments comments = new Comments(""+getCurrentUser(activity),""+System.currentTimeMillis(),"","");
        Map<String,Comments> map=new HashMap<String, Comments>();
        map.put(key,comments);
        FeedPost post = new FeedPost(
                ""+getCurrentUser(activity),
                "0",
                ""+System.currentTimeMillis(),
                ""+feedText,
                ""+"post_privacy",
                ""+downloadUrl,
                ""+fileFormat,
                ""+key,
                map);
        DATABASE_REFERENCE.child(key).setValue(post);
        activity.finish();
    }

    public static void saveAndUpdateToServer(String downloadUrl, Activity activity, String feedText,String fileFormat, String key,ArrayList<String> board) {
        feedData(activity, feedText, downloadUrl,fileFormat,key);
        for(int i=0; i<board.size();i++){
            if(i!=board.size()){
                putDateBoardList(activity,board.get(i), key);
            }else{

            }
        }

    }

    public static void putDateBoardList(Activity activity, String boardName, String key) {
        DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put(key, getCurrentUser(activity));
        DATABASE_REFERENCE.child(boardName).child("post_index").updateChildren(taskMap);
    }


    /**
     * TODO: Play Pause Music
     */
    public static void playPauseMusic(Activity activity, MediaPlayer mediaPlayer, ImageView startStopImg) {
        if (mediaPlayer.isPlaying() == true) {
            mediaPlayer.pause();
            startStopImg.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_launcher_web));
        } else {
            mediaPlayer.start();
            startStopImg.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_launcher_web));
        }
    }

    /**
     * TODO: Upload Audio file
     */
    public static void uploadAudio(final Activity activity, final String fileFormat, String filePath, String StorageLocation, final String feedText, final ArrayList<String>list) {
        final ProgressDialog dialog = loadProgressDialog(activity, "Uploading...", CENTER_DISPLAY);
        dialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(StorageLocation);
        Uri file = Uri.fromFile(new File(filePath));
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/mpeg")
                .build();
        UploadTask uploadTask = storageReference.child(fileFormat + file.getLastPathSegment().trim()).putFile(file, metadata);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Double progress = Double.valueOf((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                dialog.setMessage("Uploading..." + String.valueOf(progress.intValue()) + "%");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                showToast(activity, "fail");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String url = String.valueOf(downloadUrl);
                DATABASE_REFERENCE = setFireBaseKey(CST_POST);
                String key = DATABASE_REFERENCE.push().getKey();
                if(list!=null&& list.isEmpty()){
                    saveAndUpdateToServer(url,activity, feedText,fileFormat,key, list);
                }else{
                    saveAndUpdateToServer(url,activity, feedText,fileFormat,key);
                }
                dialog.dismiss();

            }
        });
    }

    /**
     * TODO: Check Empty EditText
     */
    public static void showEmptyField(EditText text, String customText) {
        text.setError(customText);
        text.setHint(customText);
    }

    /**
     * TODO: Load Board List
     */
    public static FirebaseRecyclerAdapter<Board, BoardViewHolder> loadBoard(final Query reference, final Activity activity, final String userName, final String UID, final String operation, final String privacyCheck, final boolean archive, final boolean joinBoard) {
        FirebaseRecyclerAdapter<Board, BoardViewHolder> adapter = new FirebaseRecyclerAdapter<Board, BoardViewHolder>
                (Board.class,
                        R.layout.board_item_view,
                        BoardViewHolder.class,
                        reference) {
            @Override
            protected void populateViewHolder(final BoardViewHolder viewHolder, final Board model, final int position) {
                final String[] temp = {CST_POST};
                if(joinBoard){
                    DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                    DATABASE_REFERENCE.child(model.getJoined()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount()!=0){
                                viewHolder.getReferenceKey.setText(getDataSnapShotValue(dataSnapshot, CST_BOARD_KEY));
                                viewHolder.createdBy.setText(getDataSnapShotValue(dataSnapshot, CST_BOARD_CREATED_BY));
                                viewHolder.nameOfBoard.setText(getDataSnapShotValue(dataSnapshot, CST_BOARD_NAME));
                                viewHolder.getBoardKey.setText(getDataSnapShotValue(dataSnapshot, CST_BOARD_KEY));


                                viewHolder.memberCount.setText(String.valueOf(dataSnapshot.child(CST_MEMBERS).getChildrenCount()-1));
                                viewHolder.postCount.setText(String.valueOf(dataSnapshot.child(CST_POST_INDEX).getChildrenCount()-1));

                                viewHolder.lockImg.setVisibility(View.GONE);
                                viewHolder.editTextView.setVisibility(View.GONE);

                                viewHolder.boardR.setVisibility(View.VISIBLE);
                                viewHolder.boardR.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(activity, ViewBoard.class);
                                        intent.putExtra("board_key", model.getJoined());
                                        activity.startActivity(intent);
                                        // activity.finish();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    switch (operation){
                        case BOARD_EDIT_ON:
                            if(privacyCheck.equals(BOARD_PRIVACY_ON)){
                                viewHolder.getReferenceKey.setText(model.getBoard_key());
                                viewHolder.createdBy.setText(model.getCreated_by());
                                viewHolder.nameOfBoard.setText(model.getBoard_name());
                                viewHolder.getBoardKey.setText(model.getBoard_key());
                                viewHolder.postCount.setText(String.valueOf(model.getPost_index().size()-1));
                                viewHolder.memberCount.setText(String.valueOf(model.getMembers().size()));
                                if (model.getBoard_privacy().equals("1")) {
                                    viewHolder.lockImg.setVisibility(View.VISIBLE);
                                }else {
                                    viewHolder.lockImg.setVisibility(View.GONE);
                                }
                                viewHolder.boardR.setVisibility(View.VISIBLE);
                                viewHolder.boardR.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(activity, ViewBoard.class);
                                        intent.putExtra("board_key", model.getBoard_key());
                                        activity.startActivity(intent);
                                        // activity.finish();
                                    }
                                });

                                viewHolder.editTextView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        editBoard(activity,model.getBoard_key(),model.getCreated_by(),model.getBoard_name(),model.getBoard_description(),model.getBoard_privacy(), archive);
                                    }
                                });
                                viewHolder.commonBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        goToNext(activity,POST,false);
                                    }
                                });
                            }else{

                            }
                            break;
                        case BOARD_EDIT_OFF:
                            if(privacyCheck.equals(BOARD_PRIVACY_ON)) {

                                viewHolder.commonBtn.setVisibility(View.GONE);
                                viewHolder.editTextView.setVisibility(View.GONE);
                                viewHolder.boardR.setClickable(false);
                                if (model.getBoard_privacy().equals("1")) {
                                    viewHolder.lockImg.setVisibility(View.VISIBLE);
                                    DATABASE_REFERENCE = setFireBaseKey("join_board");
                                    DATABASE_REFERENCE.child(model.getBoard_key()).orderByChild("userUI").equalTo(getCurrentUser(activity)).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getChildrenCount() != 0) {
                                                temp[0] = "RemoveFromBoard";
                                                viewHolder.commonBtn.setText("Unjoin");
                                                viewHolder.commonBtn.setVisibility(View.VISIBLE);
                                                viewHolder.boardR.setClickable(true);
                                                return;
                                            } else {
                                                DATABASE_REFERENCE = setFireBaseKey("board_requested");
                                                DATABASE_REFERENCE.child(model.getCreated_by()).child(model.getBoard_key()).child(UID).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.getChildrenCount() != 0) {
                                                            temp[0] = "RemoveRequest";
                                                            viewHolder.commonBtn.setText("Requested");
                                                            viewHolder.commonBtn.setVisibility(View.VISIBLE);
                                                            viewHolder.boardR.setClickable(false);
                                                        } else {
                                                            temp[0] = "SendRequest";
                                                            viewHolder.commonBtn.setText("Join");
                                                            viewHolder.commonBtn.setVisibility(View.VISIBLE);
                                                            viewHolder.boardR.setClickable(false);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    viewHolder.lockImg.setVisibility(View.GONE);
                                    DATABASE_REFERENCE = setFireBaseKey("join_board");
                                    DATABASE_REFERENCE.child(model.getBoard_key()).child(UID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getChildrenCount() != 0) {
                                                temp[0] = "Unjoin";
                                                viewHolder.commonBtn.setText("Unjoin");
                                                viewHolder.commonBtn.setVisibility(View.VISIBLE);
                                                return;
                                            } else {
                                                temp[0] = "Join";
                                                viewHolder.commonBtn.setText("Join");
                                                viewHolder.commonBtn.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                            viewHolder.boardR.setVisibility(View.VISIBLE);
                            viewHolder.boardR.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(activity, ViewBoard.class);
                                    intent.putExtra("board_key", model.getBoard_key());
                                    activity.startActivity(intent);
                                    // activity.finish();
                                }
                            });
                            viewHolder.getReferenceKey.setText(model.getBoard_key());
                            viewHolder.createdBy.setText(model.getCreated_by());
                            viewHolder.nameOfBoard.setText(model.getBoard_name());
                            viewHolder.getBoardKey.setText(model.getBoard_key());
                            viewHolder.postCount.setText(String.valueOf(model.getPost_index().size()-1));
                            viewHolder.memberCount.setText(String.valueOf(model.getMembers().size()));
                            viewHolder.commonBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    switch (temp[0]) {
                                        case "Join":
                                            temp[0] = "Unjoin";
                                            viewHolder.commonBtn.setText("Unjoin");

                                            Map<String, Object> userUID = setMapValue("userUI", getCurrentUser(activity));
                                            DATABASE_REFERENCE = setFireBaseKey("join_board");
                                            DATABASE_REFERENCE.child(model.getBoard_key()).child(UID).updateChildren(userUID);

                                            Map<String, Object> userU = setMapValue(UID, getCurrentUser(activity));
                                            DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                                            DATABASE_REFERENCE.child(model.getBoard_key()).child("members").updateChildren(userU);

                                            DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
                                            Map<String, Object> updateBoardKey = setMapValue(CST_JOINED,model.getBoard_key());
                                            DATABASE_REFERENCE.child(getCurrentUser(activity)).child(CST_JOIN_BOARD).child(model.getBoard_key()).updateChildren(updateBoardKey);

                                            break;
                                        case "Unjoin":
                                            temp[0] = "Join";
                                            viewHolder.commonBtn.setText("Join");
                                            DATABASE_REFERENCE = setFireBaseKey("join_board");
                                            DATABASE_REFERENCE.child(model.getBoard_key()).child(UID).removeValue();

                                            DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                                            DATABASE_REFERENCE.child(model.getBoard_key()).child("members").child(UID).removeValue();

                                            DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
                                            DATABASE_REFERENCE.child(getCurrentUser(activity)).child(CST_JOIN_BOARD).child(model.getBoard_key()).removeValue();

                                            break;
                                        case "Requested":
                                            temp[0] = "Join";
                                            viewHolder.commonBtn.setText("Join");
                                            DATABASE_REFERENCE = setFireBaseKey("board_requested");
                                            DATABASE_REFERENCE.child(model.getBoard_key()).child(UID).removeValue();

                                            DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                                            DATABASE_REFERENCE.child(model.getBoard_key()).child("requested").child(UID).removeValue();
                                            break;
                                        case "RemoveRequest":
                                            temp[0] = "SendRequest";
                                            viewHolder.commonBtn.setText("Join");

                                            DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                                            DATABASE_REFERENCE.child(model.getBoard_key()).child("requested").orderByChild("userUI").equalTo(getCurrentUser(activity)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.getChildrenCount()!=0){
                                                        dataSnapshot.getChildren().iterator().hasNext();
                                                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                                            if(snapshot.child("userUI").getValue().toString().equals(getCurrentUser(activity))){
                                                                String Te = snapshot.child("request_key").getValue().toString();
                                                                //showToast(activity, String.valueOf(Te));
                                                                DATABASE_REFERENCE = setFireBaseKey("approve_reject");
                                                                DATABASE_REFERENCE.child(model.getCreated_by()).child(Te).removeValue();
                                                                DATABASE_REFERENCE = setFireBaseKey("board_requested");
                                                                DATABASE_REFERENCE.child(model.getCreated_by()).child(model.getBoard_key()).child(UID).removeValue();
                                                                DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                                                                DATABASE_REFERENCE.child(model.getBoard_key()).child("requested").child(UID).removeValue();
                                                            }
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            break;

                                        case "SendRequest":
                                            temp[0] = "RemoveRequest";
                                            viewHolder.commonBtn.setText("Join");
                                            DATABASE_REFERENCE = setFireBaseKey("board_requested");
                                            String request_key =  DATABASE_REFERENCE.push().getKey();
                                            JoinBoard joinBoard = new JoinBoard(model.getCreated_by(),getCurrentUser(activity),request_key,model.getBoard_key(),String.valueOf(System.currentTimeMillis()),UID);

                                            Map<String, Object> userUID1 = setMapValue("userUI", getCurrentUser(activity));
                                            Map<String, Object> requestMapKey = setMapValue("request_key", request_key);

                                            DATABASE_REFERENCE.child(model.getCreated_by()).child(model.getBoard_key()).child(UID).updateChildren(userUID1);
                                            DATABASE_REFERENCE.child(model.getCreated_by()).child(model.getBoard_key()).child(UID).updateChildren(requestMapKey);

                                            DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                                            DATABASE_REFERENCE.child(model.getBoard_key()).child("requested").child(UID).updateChildren(userUID1);
                                            DATABASE_REFERENCE.child(model.getBoard_key()).child("requested").child(UID).updateChildren(requestMapKey);

                                            DATABASE_REFERENCE = setFireBaseKey("approve_reject");
                                            DATABASE_REFERENCE.child(model.getCreated_by()).child(request_key).setValue(joinBoard);
                                            break;

                                        case "RemoveFromBoard":

                                            //showToast(activity, model.getBoard_key()+"=UID="+UID);
                                            temp[0] = "SendRequest";
                                            viewHolder.commonBtn.setText("Join");
                                            DATABASE_REFERENCE = setFireBaseKey("join_board");
                                            DATABASE_REFERENCE.child(model.getBoard_key()).child(UID).removeValue();

                                            DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                                            DATABASE_REFERENCE.child(model.getBoard_key()).child("members").child(UID).removeValue();
                                            break;
                                        default:
                                            showToast(activity, "Everyone is a Joker");
                                            break;
                                    }
                                }
                            });
                            break;
                        case CST_JOIN_BOARD:
                            DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                            DATABASE_REFERENCE.child(model.getJoined()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getChildrenCount()!=0){
                                        showToast(activity, dataSnapshot.toString());
//                            viewHolder.getReferenceKey.setText(getDataSnapShotValue(dataSnapshot, CST_POST_REFERENCE_KEY));
                                        viewHolder.createdBy.setText(getDataSnapShotValue(dataSnapshot, CST_BOARD_CREATED_BY));
                                        viewHolder.nameOfBoard.setText(getDataSnapShotValue(dataSnapshot, CST_BOARD_NAME));
                                        viewHolder.getBoardKey.setText(getDataSnapShotValue(dataSnapshot, CST_BOARD_KEY));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            break;
                    }
                }

            }
        };
        return adapter;
    }

    public static String joinBoardRequest(final String userName, final String boardKey) {
        final String[] check = {""};
        Log.i(LOGCAT, userName + " " + boardKey);

        return check[0];
    }

    public static void getFirebaseKey(final Activity activity, final String UID, final String getBoard_key, final String getCreated_by, String getBoard_name, String getBoard_description, final String getBoard_privacy) {
        final Dialog board_dialog = new Dialog(activity);
        board_dialog.setContentView(R.layout.edit_board);
        Button archive_board = (Button) board_dialog.findViewById(R.id.archive_board);
        final EditText board_name = (EditText) board_dialog.findViewById(R.id.edit_nameOfBoardEdit);
        final EditText board_description = (EditText) board_dialog.findViewById(R.id.edit_board_description);
        final Switch swtUpdate = (Switch) board_dialog.findViewById(R.id.swtUpdate);
        Button cancel = (Button) board_dialog.findViewById(R.id.edit_cancel);
        TextView commonTitle = (TextView) board_dialog.findViewById(R.id.commonTitle);
        View viewBDA = (View) board_dialog.findViewById(R.id.viewBDA);
        Button done = (Button) board_dialog.findViewById(R.id.edit_done);

        board_dialog.show();

        viewBDA.setVisibility(View.VISIBLE);
        archive_board.setVisibility(View.VISIBLE);
        commonTitle.setText("Edit Board");

        board_name.setText(getBoard_name);
        board_description.setText(getBoard_description);

        if (getBoard_privacy.equals("1")) {
            swtUpdate.setChecked(true);
            swtUpdate.isChecked();
        } else {

        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_dialog.dismiss();
            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (board_name.getText().toString().isEmpty()) {
                    showEmptyField(board_name, "Enter Board Name");
                } else if (board_description.getText().toString().isEmpty()) {
                    showEmptyField(board_description, "Enter Description");
                } else {
                    saveBoard(activity, swtUpdate, board_dialog, getBoard_key, board_name.getText().toString(), board_description.getText().toString(), UID, 2);
                }
            }
        });


        final Button delete_board = (Button) board_dialog.findViewById(R.id.delete_board);
        delete_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), getBoard_key, Toast.LENGTH_LONG).show();
                DATABASE_REFERENCE = null;
                DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference(CST_BOARD);
                DATABASE_REFERENCE.child(getBoard_key).removeValue();
                board_dialog.dismiss();
            }
        });

        archive_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (board_name.getText().toString().isEmpty()) {
                    showSnackBar(delete_board, "Enter Board Name", "");
                } else if (board_description.getText().toString().isEmpty()) {
                    showSnackBar(delete_board, "Enter Description", "");
                } else {
                    saveBoard(activity, swtUpdate, board_dialog, getBoard_key, board_name.getText().toString(), board_description.getText().toString(), UID, 3);
                }


            }
        });
    }

    public static void saveBoard(Activity activity, Switch privacySwt, Dialog dialog, String key, String name, String UID, String description, int check) {
        String tempPrivacy;
        if (privacySwt == null) {
            privacySwt.setChecked(false);
            tempPrivacy = "0";
        } else {
            if (privacySwt.isChecked()) {
                tempPrivacy = "1";
            } else {
                tempPrivacy = "0";
            }
        }

        Map<String, Object> post_index = setMapValue(key, getCurrentUser(activity));
//        post_index.put(key, getCurrentUser(activity));

        Map<String, Object> member = setMapValue(key, getCurrentUser(activity));
        Map<String, Object> join_board = setMapValue("userUI", getCurrentUser(activity));
//        member.put(key, getCurrentUser(activity));


        /*TODO: Delete Board*/
        if (check == 1) {
            remove(key);
            dialog.dismiss();
        }/*TODO: Create and Update Board*/ else if (check == 2) {
            DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
            Board boardField = new Board(name,
                    description,
                    key,
                    getCurrentUser(activity), tempPrivacy,
                    post_index, member);
            DATABASE_REFERENCE.child(key).setValue(boardField);

            DATABASE_REFERENCE = setFireBaseKey(CST_JOIN_BOARD);
            DATABASE_REFERENCE.child(key).push().setValue(join_board);

            DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
            DATABASE_REFERENCE.child(getCurrentUser(activity)).child("board_created").child(key).setValue(setMapValue("admin", key));

            dialog.dismiss();
        }/*TODO: Archive Board*/ else {
            DATABASE_REFERENCE = null;
            DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference("archive_boards");
            Board boardField = new Board(name,
                    description,
                    key,
                    getCurrentUser(activity), tempPrivacy,
                    post_index, member);
            DATABASE_REFERENCE.child(key).setValue(boardField);
            remove(key);
            dialog.dismiss();
        }
    }

    public static void remove(String key) {
        DATABASE_REFERENCE = null;
        DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference(CST_BOARD);
        DATABASE_REFERENCE.child(key).removeValue();
    }

    public static FirebaseRecyclerAdapter<Board, BoardViewHolder>
    showBoard(ProgressDialog progressDialog, Activity activity,
              int RecyclerViewId, int spanCount, String userName,
              final String UID, String privacy, String editmode, boolean joinBoard) {
        progressDialog.setMessage("Loading Board Details");
        RecyclerView boardRecyclerView = (RecyclerView) activity.findViewById(RecyclerViewId);
        LinearLayoutManager recyclerViewLayoutManager = new GridLayoutManager(activity.getApplicationContext(), spanCount);
        boardRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewLayoutManager = new LinearLayoutManager(activity);
        //recyclerViewLayoutManager.setReverseLayout(true);
        recyclerViewLayoutManager.setStackFromEnd(true);


        Query query = null;
        if(joinBoard){
            DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
            query = DATABASE_REFERENCE.child(userName).child(CST_JOIN_BOARD).orderByChild(CST_JOINED);
        }else{
            switch (privacy){
                case BOARD_PRIVACY_ON:
                    DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                    query = DATABASE_REFERENCE.orderByChild(CST_BOARD_CREATED_BY).equalTo(userName);
                    break;
                case BOARD_PRIVACY_OFF:
                    DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                    query = DATABASE_REFERENCE.orderByChild(CST_BOARD_CREATED_BY).equalTo(getCurrentUser(activity));
                    break;
                case CST_JOIN_BOARD:
                    DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
                    query = DATABASE_REFERENCE.child(userName).child(CST_JOIN_BOARD).orderByChild(CST_JOINED);
                    break;
            }
        }

        FirebaseRecyclerAdapter<Board, BoardViewHolder> mFireBaseAdapter = loadBoard(query,activity, userName, UID, editmode, privacy,false, joinBoard);
        mFireBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });
        boardRecyclerView.setAdapter(mFireBaseAdapter);
        boardRecyclerView.setNestedScrollingEnabled(false);
        progressDialog.dismiss();
        return mFireBaseAdapter;
    }

    /**TODO: Set Feed For All */
    public static void/*FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder>*/
    showPost(final ArrayList<String> userList, ProgressDialog progressDialog, final Activity activity,
             int RecyclerViewId, int spanCount, String userName,
             final String UID, final boolean privacy, final boolean editmode){
        final TextView[] unFollow = new TextView[1];
        final TextView[] block = new TextView[1];
        final TextView[] hide_post = new TextView[1];
        final TextView[] delete = new TextView[1];

        final RecyclerView postRecyclerView = (RecyclerView) activity.findViewById(RecyclerViewId);
        LinearLayoutManager recyclerViewLayoutManager = new GridLayoutManager(activity.getApplicationContext(), spanCount);
        postRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewLayoutManager = new LinearLayoutManager(activity);
        recyclerViewLayoutManager.setReverseLayout(true);
        recyclerViewLayoutManager.setStackFromEnd(true);

        Query query = null;
        DatabaseReference userDatabaseReference = null;
        postRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        if(privacy){
            DatabaseReference mFireBaseDatabaseReference = setFireBaseKey(CST_POST);
            userDatabaseReference = setFireBaseKey(CST_USER_LISTS);
            query = mFireBaseDatabaseReference;
        }else{
            DATABASE_REFERENCE = setFireBaseKey(CST_POST);
            query = DATABASE_REFERENCE;
        }
        final DatabaseReference finalUserDatabaseReference = userDatabaseReference;

        final FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder> feedAdapter= new FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder>(
                FeedPost.class,
                R.layout.feed_post_item,
                FeedPostViewHolder.class,
                query) {
            @Override
            protected void populateViewHolder(final FeedPostViewHolder viewHolder, final FeedPost model, final int position) {
                if(privacy){
                    if (editmode) {
                        if (model.getPost_by().equals(getCurrentUser(activity))) {
                            viewHolder.Layout_hide();
                            viewHolder.Layout_show();
                            viewHolder.post_item.setVisibility(View.VISIBLE);
                            showDelete(activity,viewHolder.postDownAeroText,model.getPost_reference_key());
                        } else {
                            viewHolder.Layout_hide();
                            if (userList.size() != 0) {
                                for (int i = 0; i < userList.size(); i++) {
                                    if (model.getPost_by().equals(userList.get(i))) {
                                        viewHolder.Layout_show();
                                        showBlockUnFollowReport(activity,viewHolder.postDownAeroText,model.getPost_reference_key(),finalUserDatabaseReference, model.getPost_by());
                                    }
                                }
                            }

                        }
                    } else {

                    }
                }else{
                    viewHolder.Layout_hide();
                    if(userList.size()!=0){
                        for(int i=0; i<userList.size();i++){
                            if(model.getPost_reference_key().equals(userList.get(i))) {
                                viewHolder.Layout_show();
                                viewHolder.postDownAeroText.setVisibility(View.GONE);
                            }
                        }
                    }

                }

                showPostDetails(activity,
                        model.getPost_by(), model.getPost_media_link(),
                        model.getPost_privacy(), model.getPost_reference_key(),
                        model.getPost_text(), model.getPost_type(),
                        viewHolder.user_image, viewHolder.postBy,
                        viewHolder.imageMedia, viewHolder.post_media_link,
                        viewHolder.play, viewHolder.pause,
                        viewHolder.docBtn);
                viewHolder.postText.setText(model.getPost_text());
                viewHolder.time.setText(getRelativeTimeDisplay(model.getPost_time()));
                enableLikeBtn(activity, viewHolder.like, viewHolder.heart, model.getPost_reference_key());
                enableSaveBtn(activity, viewHolder.saveValue, viewHolder.savePostImgBtn, model.getPost_reference_key());
                likeSaveOnClick(activity, viewHolder.like, viewHolder.heart, model.getPost_reference_key(), true);
                likeSaveOnClick(activity, viewHolder.saveValue, viewHolder.savePostImgBtn, model.getPost_reference_key(), false);
                viewHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToComment(activity,
                                model.getPost_by(), model.getPost_comments(),
                                model.getPost_time(), model.getPost_text(),
                                model.getPost_privacy(), model.getPost_media_link(),
                                model.getPost_type(), model.getPost_reference_key(),
                                viewHolder.like.getText().toString(),
                                String.valueOf(model.comments.size()));
                    }
                });
                showCountDetails(model.comments.size()-1, model.getPost_reference_key(), viewHolder.countMain, viewHolder.commentCountLinearLayout, viewHolder.commentCounts, 1);
                // showCountDetails(model., model.getPost_reference_key(), viewHolder.countMain, viewHolder.commentCountLinearLayout, viewHolder.commentCounts, 2);

            }
        };
        final FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder> finalFeedAdapter = feedAdapter;
        final LinearLayoutManager finalRecyclerViewLayoutManager = recyclerViewLayoutManager;
        feedAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int historyCount = finalFeedAdapter.getItemCount();
                int lastHistory = finalRecyclerViewLayoutManager.findLastCompletelyVisibleItemPosition();
                if ((lastHistory == -1 || (positionStart >= (historyCount - 1) && lastHistory == (positionStart - 1)))) {
                    postRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        postRecyclerView.setLayoutManager(finalRecyclerViewLayoutManager);
        postRecyclerView.setAdapter(feedAdapter);
        postRecyclerView.setNestedScrollingEnabled(false);

    }

    public static void showBlockUnFollowReport(final Activity activity, ImageButton postDownAeroText, final String post_reference_key, final DatabaseReference reference, final String post_by) {
        postDownAeroText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog feedDialog = new Dialog(activity);
                Window window = feedDialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                window.setAttributes(wlp);
                feedDialog.setContentView(R.layout.post_dialog_layout);
                final TextView unFollow = (TextView) feedDialog.findViewById(R.id.unfollow);
                TextView block = (TextView) feedDialog.findViewById(R.id.block);
                TextView hide_post = (TextView) feedDialog.findViewById(R.id.hide_post);
                TextView delete = (TextView) feedDialog.findViewById(R.id.delete);
                delete.setVisibility(View.GONE);
                View v3 = (View) feedDialog.findViewById(R.id.v3);
                v3.setVisibility(View.GONE);
                unFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //removeFollower(username, model.getPost_by());
                        //removeFollowing(username, model.getPost_by());
                        unFollow.setText("Follow");

                        //feedAdapter.notifyDataSetChanged();

                        feedDialog.dismiss();
                    }
                });

                block.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //removeFollower(username, model.getPost_by());
                        //removeFollowing(username, model.getPost_by());
                        unFollow.setText("Follow");

                        String key = reference.push().getKey();
                        Map<String, Object> taskMap = new HashMap<String, Object>();
                        taskMap.put("username", post_by);
                        reference.child(getCurrentUser(activity)).child("blocked").child(key).updateChildren(taskMap);

                        // feedAdapter.notifyDataSetChanged();

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

    public static void showDelete(final Activity activity, ImageButton postDownAeroText, final String post_reference_key) {
        postDownAeroText.setOnClickListener(new View.OnClickListener()
        {@Override
        public void onClick(View v) {
            View v1, v2, v3;
            final Dialog feedDialog = new Dialog(activity);
            Window window = feedDialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            //wlp.gravity = Gravity.CENTER;
            window.setAttributes(wlp);
            feedDialog.setContentView(R.layout.post_dialog_layout);
            TextView unFollow = (TextView) feedDialog.findViewById(R.id.unfollow);
            TextView block = (TextView) feedDialog.findViewById(R.id.block);
            TextView hide_post = (TextView) feedDialog.findViewById(R.id.hide_post);
            TextView delete = (TextView) feedDialog.findViewById(R.id.delete);
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
                    DATABASE_REFERENCE.child(post_reference_key).removeValue();
                    feedDialog.dismiss();
                }
            });
            feedDialog.show();
        }
        });
    }

    public static void showCountDetails(final int comments,final String postKey, LinearLayout masterCount, LinearLayout countLayout, TextView count, int whichCount) {
        switch (whichCount){
            case 1:
                if(comments!=0){
                    masterCount.setVisibility(View.VISIBLE);
                    countLayout.setVisibility(View.VISIBLE);
                    count.setText(String.valueOf(comments));
                }else{
                    countLayout.setVisibility(View.GONE);
                    masterCount.setVisibility(View.GONE);
                }
                break;
        }
    }

    public static void UpdateValve(String likes, String ref_key, String typeofCount,String MapKey) {
        DATABASE_REFERENCE = setFireBaseKey(CST_POST);
        DATABASE_REFERENCE.child(ref_key).child(typeofCount);
        final Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put(MapKey, String.valueOf(likes));
        DATABASE_REFERENCE.child(ref_key).updateChildren(taskMap);
    }

    public static FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder> loadPost(final Activity activity, String username){
        final TextView[] unFollow = new TextView[1];
        final TextView[] block = new TextView[1];
        final TextView[] hide_post = new TextView[1];
        final TextView[] delete = new TextView[1];
        final DatabaseReference userDatabaseReference = setFireBaseKey(CST_USER_LISTS);
        final DatabaseReference feedPosts = setFireBaseKey(CST_POST);
        FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder> feedAdapter = new FirebaseRecyclerAdapter<FeedPost, FeedPostViewHolder>(
                FeedPost.class,
                R.layout.feed_post_item,
                FeedPostViewHolder.class,
                feedPosts.orderByChild(CST_POST_BY).equalTo(username)) {
            @Override
            protected void populateViewHolder(final FeedPostViewHolder viewHolder, final FeedPost model, final int position) {
                viewHolder.Layout_hide();
                viewHolder.Layout_show();
                showPostDetails(activity,
                        model.getPost_by(),model.getPost_media_link(),
                        model.getPost_privacy(),model.getPost_reference_key(),
                        model.getPost_text(), model.getPost_type(),
                        viewHolder.user_image, viewHolder.postBy,
                        viewHolder.imageMedia, viewHolder.post_media_link,
                        viewHolder.play, viewHolder.pause,
                        viewHolder.docBtn);
                viewHolder.postText.setText(model.getPost_text());
                viewHolder.time.setText(getRelativeTimeDisplay(model.getPost_time()));
                enableLikeBtn(activity, viewHolder.like, viewHolder.heart, model.getPost_reference_key());
                enableSaveBtn(activity, viewHolder.saveValue, viewHolder.savePostImgBtn, model.getPost_reference_key());
                viewHolder.heart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewHolder.likeValue.getText().toString().equals("1")) {
                            viewHolder.likeValue.setText("");
                            viewHolder.heart.setImageDrawable(activity.getResources().getDrawable(R.drawable.heart));

                            userDatabaseReference.child(getCurrentUser(activity)).child("likes").child(model.getPost_reference_key()).removeValue();
                        } else {
                            viewHolder.likeValue.setText("1");
                            viewHolder.heart.setImageDrawable(activity.getResources().getDrawable(R.drawable.post_heart_active));
                            LikesAndSave likesAndSave = new LikesAndSave(String.valueOf(System.currentTimeMillis()), "1", model.getPost_reference_key());
                            userDatabaseReference.child(getCurrentUser(activity)).child("likes").child(model.getPost_reference_key()).setValue(likesAndSave);
                        }
                    }
                });


                viewHolder.savePostImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewHolder.saveValue.getText().toString().equals("1")) {
                            viewHolder.saveValue.setText("");
                            viewHolder.savePostImgBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.bookmark));
                            userDatabaseReference.child(getCurrentUser(activity)).child("saved_posts").child(model.getPost_reference_key()).removeValue();

                        } else {

                            showToast(activity, "Post Saved");

                            viewHolder.saveValue.setText("1");
                            viewHolder.savePostImgBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.bookmark_active));

                            LikesAndSave likesAndSave = new LikesAndSave(String.valueOf(System.currentTimeMillis()), "1", model.getPost_reference_key());
                            userDatabaseReference.child(getCurrentUser(activity)).child("saved_posts").child(model.getPost_reference_key()).setValue(likesAndSave);
                        }
                    }
                });

            }
        };

        return feedAdapter;
    }




    public static FirebaseRecyclerAdapter<Board, BoardViewHolder> checkJoinBoardCondition
            (ProgressDialog progressDialog, Activity activity,
             int RecyclerViewId, int spanCount, String userName,
             final String UID, boolean privacy, boolean editmode) {
        progressDialog.setMessage("Loading Board Details");
        RecyclerView boardRecyclerView = (RecyclerView) activity.findViewById(RecyclerViewId);
        LinearLayoutManager recyclerViewLayoutManager = new GridLayoutManager(activity.getApplicationContext(), spanCount);
        boardRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        DATABASE_REFERENCE = setFireBaseKey(CST_JOIN_BOARD);
        FirebaseRecyclerAdapter<Board, BoardViewHolder> mFireBaseAdapter = checkBoardCondition(activity, DATABASE_REFERENCE, userName, UID, privacy, editmode);
        mFireBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });
        boardRecyclerView.setAdapter(mFireBaseAdapter);
        progressDialog.dismiss();
        return mFireBaseAdapter;
    }


    public static FirebaseRecyclerAdapter<Board, BoardViewHolder> checkBoardCondition(final Activity activity, final DatabaseReference reference, final String userName, final String UID, final boolean privacy, final boolean editMode) {
        final String CurrentUser = "_" + getCurrentUser(activity);
        FirebaseRecyclerAdapter<Board, BoardViewHolder> adapter = new FirebaseRecyclerAdapter<Board, BoardViewHolder>
                (Board.class,
                        R.layout.board_item_view,
                        BoardViewHolder.class,
                        reference.orderByChild("admin").equalTo(userName)) {
            @Override
            protected void populateViewHolder(final BoardViewHolder viewHolder, final Board model, final int position) {
                if (String.valueOf(model.getUser()).equals(CurrentUser)) {
                    showToast(activity, String.valueOf(model.getAdmin() + CurrentUser));
                } else {

                }
            }
        };
        return adapter;
    }


    public static boolean checkMember(String Board_key, final Activity activity) {
        final boolean[] check = {false};
        DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
        DATABASE_REFERENCE.child(Board_key).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (getCurrentUser(activity).equals(snapshot.getValue().toString())) {
                            Log.i("Check Member ", snapshot.getValue().toString());
                            check[0] = true;
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return check[0];
    }


    public static boolean checkUserRequested(String Board_key, final Activity activity){
        final boolean[] check = {false};
        DATABASE_REFERENCE = setFireBaseKey("boards");
        DATABASE_REFERENCE.child(Board_key).child("requested").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (getCurrentUser(activity).equals(snapshot.getValue().toString())) {
                            check[0] = true;
                            Log.i("Check User REquets ",
                                    snapshot.getValue().toString()+"= "+check[0]+" ="+getCurrentUser(activity));
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return check[0];
    }


    public static String getRelativeTimeDisplay(String pastTime) {
        Long aLong = Long.parseLong(String.valueOf(pastTime));
        Date date = new Date(aLong);
        long timeNow = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(date.getTime(), timeNow, DateUtils.MINUTE_IN_MILLIS).toString();
    }



    public static void askForPermisssion(Activity context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, android.Manifest.permission.CAMERA)) {
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        }
        ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);
    }

    public static boolean isCameraPermission(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA);
        int gallery_write = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int gallery_read = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else if (gallery_write == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else if (gallery_read == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public static String getImageFromCamera(Intent data, ImageView imageView){
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fileOutputStream;
        try {
            destination.createNewFile();
            fileOutputStream = new FileOutputStream(destination);
            fileOutputStream.write(bytes.toByteArray());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
        return destination.getAbsolutePath();
    }

    public static Uri getFileUri(Intent data){
        Uri fileuri = data.getData();
        return fileuri;
    }

    public static void enableSaveBtn(final Activity activity,final TextView save, final ImageButton common, String key) {
        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
        DATABASE_REFERENCE.child(getCurrentUser(activity)).child("saved_posts").child(key).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    save.setText("1");
                    common.setImageDrawable(activity.getResources().getDrawable(R.drawable.bookmark_active));
                } else {
                    save.setText("");
                    common.setImageDrawable(activity.getResources().getDrawable(R.drawable.bookmark));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void enableLikeBtn(final Activity activity, final TextView save, final ImageButton common, String key) {
        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
        DATABASE_REFERENCE.child(getCurrentUser(activity)).child("likes").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    save.setText("1");
                    common.setImageDrawable(activity.getResources().getDrawable(R.drawable.post_heart_active));
                } else {
                    save.setText("");
                    common.setImageDrawable(activity.getResources().getDrawable(R.drawable.heart));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void showPostDetails(final Activity activity,
                                       final String post_by, final String postMediaLink,
                                       final String postPrivacy, final String postReference,
                                       final String postText, final String getPost_type,
                                       final CircleImageView userImage, final TextView fullName,
                                       final ImageView imageMedia, final TextView post_media_link,
                                       final ImageButton play, final ImageButton pause,
                                       final Button docBtn) {
        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
        DATABASE_REFERENCE.child(post_by).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    Picasso.with(activity)
                            .load(getDataSnapShotValue(dataSnapshot, CST_USER_IMAGE))
                            .networkPolicy(NetworkPolicy.OFFLINE)

                            .into(userImage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(activity)
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
                @SuppressLint({"NewApi", "ResourceAsColor"})
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
                @SuppressLint("ResourceAsColor")
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

            Picasso.with(activity)
                    .load(postMediaLink)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(512, 334)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .error(R.drawable.ic_launcher_web)
                    .into(imageMedia);

        }
    }


    public static void goToComment(Activity activity,
                                   String post_by, String post_comments,
                                   String post_time, String post_text,
                                   String post_privacy, String post_media_link,
                                   String post_type, String post_reference_key,
                                   String like, String comments){
        Intent intent = new Intent(activity, Comment.class);
        intent.putExtra(CST_POST_BY,post_by);
        intent.putExtra(CST_POST_COMMENTS,post_comments);
        intent.putExtra(CST_POST_TIME,post_time);
        intent.putExtra(CST_POST_TEXT, post_text);
        intent.putExtra(CST_POST_PRIVACY, post_privacy);
        intent.putExtra(CST_POST_MEDIA_LINK, post_media_link);
        intent.putExtra(CST_POST_TYPE,post_type);
        intent.putExtra(CST_POST_REFERENCE_KEY, post_reference_key);
        intent.putExtra("like", like);
        intent.putExtra(CST_COMMENTS, comments);
        activity.startActivity(intent);
    }

    public static void likeSaveOnClick(final Activity activity, final TextView likeSaveValue, final ImageButton heartSave, final String postKey, final boolean id){
        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
        heartSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeSaveValue.getText().toString().equals("1")) {
                    likeSaveValue.setText("");
                    if(id){
                        heartSave.setImageDrawable(activity.getResources().getDrawable(R.drawable.heart));
                        DATABASE_REFERENCE.child(getCurrentUser(activity)).child("likes").child(postKey).removeValue();
                    }else{
                        heartSave.setImageDrawable(activity.getResources().getDrawable(R.drawable.bookmark));
                        DATABASE_REFERENCE.child(getCurrentUser(activity)).child("saved_posts").child(postKey).removeValue();
                    }
                } else {
                    likeSaveValue.setText("1");
                    if(id){
                        heartSave.setImageDrawable(activity.getResources().getDrawable(R.drawable.post_heart_active));
                        LikesAndSave likesAndSave = new LikesAndSave(String.valueOf(System.currentTimeMillis()), "1", postKey);
                        DATABASE_REFERENCE.child(getCurrentUser(activity)).child("likes").child(postKey).setValue(likesAndSave);

                    }else{
                        showSnackBar(v, "Post Saved", "");
                        heartSave.setImageDrawable(activity.getResources().getDrawable(R.drawable.bookmark_active));
                        LikesAndSave likesAndSave = new LikesAndSave(String.valueOf(System.currentTimeMillis()), "1", postKey);
                        DATABASE_REFERENCE.child(getCurrentUser(activity)).child("saved_posts").child(postKey).setValue(likesAndSave);

                    }

                }
            }
        });
    }



    public static void editBoard(final Activity activity, final String getBoard_key, final String getCreated_by, String getBoard_name, String getBoard_description, final String getBoard_privacy, final boolean archive) {
        final Dialog board_dialog = new Dialog(activity);
        board_dialog.setContentView(R.layout.edit_board);
        Button archive_board = (Button) board_dialog.findViewById(R.id.archive_board);
        final Map<String, Object> archived;
        if(archive){
            archive_board.setText("Move To Board");
            archived = setMapValue(CST_BOARD_CREATED_BY, getCurrentUser(activity));
        }else{
            archived = setMapValue(CST_BOARD_CREATED_BY, ARCHIVE_SCREEN+"_"+getCurrentUser(activity));
        }

        final EditText board_name = (EditText) board_dialog.findViewById(R.id.edit_nameOfBoardEdit);
        final EditText board_description = (EditText) board_dialog.findViewById(R.id.edit_board_description);
        final Switch swtUpdate = (Switch) board_dialog.findViewById(R.id.swtUpdate);
        Button cancel = (Button) board_dialog.findViewById(R.id.edit_cancel);
        TextView commonTitle = (TextView) board_dialog.findViewById(R.id.commonTitle);
        View viewBDA = (View) board_dialog.findViewById(R.id.viewBDA);

        board_dialog.show();

        viewBDA.setVisibility(View.VISIBLE);
        archive_board.setVisibility(View.VISIBLE);
        commonTitle.setText("Edit Board");

        board_name.setText(getBoard_name);
        board_description.setText(getBoard_description);

        if(getBoard_privacy.equals("1")){
            swtUpdate.setChecked(true);
            swtUpdate.isChecked();
        }else{

        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                board_dialog.dismiss();
            }
        });

        Button done = (Button) board_dialog.findViewById(R.id.edit_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> boardname;
                Map<String, Object> description;
                Map<String, Object> privacy;
                if(!TextUtils.isEmpty(String.valueOf(board_name.getText()))){
                    String tempPrivacy;
                    if(swtUpdate==null){
                        swtUpdate.setChecked(false);
                        tempPrivacy = "0";
                        boardname = setMapValue(CST_BOARD_NAME, String.valueOf(board_name.getText()));
                        description = setMapValue(CST_BOARD_DESCRIPTION, String.valueOf(board_description.getText()));
                        privacy = setMapValue("board_privacy", tempPrivacy);
                    }else{
                        if (swtUpdate.isChecked()) {
                            tempPrivacy = "1";
                            boardname = setMapValue(CST_BOARD_NAME, String.valueOf(board_name.getText()));
                            description = setMapValue(CST_BOARD_DESCRIPTION, String.valueOf(board_description.getText()));
                            privacy = setMapValue("board_privacy", tempPrivacy);
                        } else {
                            tempPrivacy = "0";
                            boardname = setMapValue(CST_BOARD_NAME, String.valueOf(board_name.getText()));
                            description = setMapValue(CST_BOARD_DESCRIPTION, String.valueOf(board_description.getText()));
                            privacy = setMapValue("board_privacy", tempPrivacy);
                        }
                    }
                    DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                    updateSingleValue(DATABASE_REFERENCE,getBoard_key,boardname);
                    updateSingleValue(DATABASE_REFERENCE,getBoard_key,description);
                    updateSingleValue(DATABASE_REFERENCE,getBoard_key,privacy);
                    board_dialog.dismiss();
                }else {
                    showSnackBar(board_name, "Enter Board Name", "");
                }

            }
        });


        Button delete_board = (Button) board_dialog.findViewById(R.id.delete_board);
        delete_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATABASE_REFERENCE = null;
                DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                DATABASE_REFERENCE.child(getBoard_key).removeValue();
                DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);
                DATABASE_REFERENCE.child(getCurrentUser(activity)).child("board_created").child(getBoard_key).removeValue();
                board_dialog.dismiss();
            }
        });

        archive_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DATABASE_REFERENCE = setFireBaseKey(CST_BOARD);
                updateSingleValue(DATABASE_REFERENCE,getBoard_key,archived);
                board_dialog.dismiss();
            }
        });
    }

    public static void updateSingleValue(DatabaseReference reference, String key, Map<String, Object> value) {
        reference.child(key).updateChildren(value);
    }

    public static ArrayList<String> loadLists(String key, final String refKey, final ProgressDialog progressDialog) {
        final ArrayList<String> arrayList = new ArrayList<String>();
        DATABASE_REFERENCE = setFireBaseKey(key);
        DATABASE_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0){
                    int count = 0;
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        arrayList.add(getDataSnapShotValue(snapshot,refKey));
                        progressDialog.setTitle(String.valueOf(count++));
                    }
                    progressDialog.dismiss();
                }else {

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return arrayList;
    }

    public static void setPicasso(final String url, final ImageView view, final Activity activity, final ProgressDialog progressDialog) {
        Picasso.with(activity)
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(activity)
                                .load(url)
                                .error(R.drawable.ic_launcher_web)
                                .into(view, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onError() {
                                        progressDialog.dismiss();
                                        Log.v("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });
    }

    public static void enableDisableAnalytics(Context context){
        FirebaseAnalytics mFirebaseAnalytics;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(false);
    }

    public static void DatePicker(Activity activity, TextView textView) {
        DatePickerFragment datePickerFragment = new DatePickerFragment(textView);
        DialogFragment newFragment = datePickerFragment;
        //newFragment.show(activity.getSupportFragmentManager(), "datePicker");
    }

    public static boolean checkEditText(TextView common, String err) {
        boolean b;
        if(common.getText().toString().isEmpty()&&common.getText().toString().equals("")){
            b=false;
            common.setError(err);
        }else{
            b = true;
        }
        return b;
    }

    public static void setList(ArrayList<String> list, ArrayAdapter<String> adapter, AutoCompleteTextView autoCompleteTextView, Activity activity) {
        adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, list);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
    }

    public static String getFirstLetterCaps(String yourString){
        return yourString = yourString.substring(0, 1).toUpperCase() + yourString.substring(1);
    }
}
//72 methods

//kangaroo deliver mushroom marbles save explain foam spring elevator canal opera opera