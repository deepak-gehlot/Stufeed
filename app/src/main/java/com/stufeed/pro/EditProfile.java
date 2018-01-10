package com.stufeed.pro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stufeed.R;
import com.stufeed.frag.DatePickerFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.stufeed.utility.LocVari.*;
import static com.stufeed.utility.SowmitrasMethod.*;
import static com.stufeed.utility.SowmitrasStrings.*;


/**
 * Created by sowmitras on 21/11/17.
 */

public class EditProfile extends AppCompatActivity implements
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private static final int REQUEST_PERMISSIONS = 20;
    private static final int GALLARY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 10;
    // private Bundle bundle;
    private static int identifyIMG;
    private SharedPreferences sharedPref;
    private String fullname, program, branch, gender, mobile, birthday,
            email, college, username, role, mImage, year, about, name, graduationYear, aboutStatus, defaultImag, accountKey;
    private EditText userNameEdt, /*autoComplete1,*/ aboutStatusEdt, mobileEdt,
            old_password, new_password, confirm_password, graduationYearEdit, selectGender, birthdayEdit;
    private TextView collegeText, changePasswordText, take_photo, gallery,
            cancel_image, removeImg, /*autoComplete2,*/ emailEdt;
    private CircleImageView studentImg;
    private ProgressDialog progressDialog;
    private FloatingActionButton saveBtn;
    private boolean main = true;
    private Bitmap bitmap;
    private AutoCompleteTextView autoComplete1, autoComplete2;
    private File destination;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private SharedPreferences.Editor editor;
    private String mImagePath = "value null mil rahi hai ";
    private boolean gall = false;
    private String forGallary = "1", forCamera = "2", imageIdentify = "0";
    private Uri filePath;
    private ArrayList<String> commonArrayList;
    private ArrayAdapter<String> arrayAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Toolbar toolbar;

    private int pos;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDisableAnalytics(this);
        setContentView(R.layout.edit_user_details);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReferenceFromUrl("gs://stufeed-dev.appspot.com");

        studentImg = (CircleImageView) findViewById(R.id.studentImg);
        studentImg.setOnClickListener(this);
        progressDialog = loadProgressDialog(this, LOADING_IMAGE, CENTER_DISPLAY);


        //bundle = getIntent().getExtras();
        sharedPref = EditProfile.this.getSharedPreferences(CST_SHARED_REFERENCE, Activity.MODE_PRIVATE);


        accountKey = sharedPref.getString(CST_ACCOUNT_KEY,"");
        username = sharedPref.getString(CST_USER_NAME, "");
        fullname = sharedPref.getString(CST_USER_FULL_NAME, "");
        program = sharedPref.getString(CST_PROGRAM, "");
        college = sharedPref.getString(CST_COLLEGE, "");
        branch = sharedPref.getString(CST_BRANCH, "");
        role = sharedPref.getString(CST_ROLE, "");
        mImage = sharedPref.getString(CST_USER_IMAGE, "");
        year = sharedPref.getString(CST_GRADUATION_YEAR, "");
        about = sharedPref.getString(CST_ABOUT_STATUS, "");
        gender = sharedPref.getString(CST_GENDER, "");
        mobile = sharedPref.getString(CST_CONTACT_NUMBER, "");
        email = sharedPref.getString(CST_EMAIL_ID, "");
        birthday = sharedPref.getString(CST_BIRTHDAY, "");

        userNameEdt = (EditText) findViewById(R.id.userNameEdt);
        userNameEdt.setText(fullname);
        graduationYearEdit = (EditText) findViewById(R.id.graduationYearText);
        graduationYearEdit.setText(year);
        graduationYearEdit.setOnClickListener(this);
        aboutStatusEdt = (EditText) findViewById(R.id.aboutStatusEdt);
        aboutStatusEdt.setText(about);
        selectGender = (EditText) findViewById(R.id.selectGender);
        selectGender.setText(gender);
        selectGender.setOnClickListener(this);
        mobileEdt = (EditText) findViewById(R.id.mobileEdt);

        mobileEdt.setText(mobile);
        birthdayEdit = (EditText) findViewById(R.id.birthdayTxt);
        birthdayEdit.setText(birthday);
        birthdayEdit.setOnClickListener(this);
        autoComplete2 = (AutoCompleteTextView) findViewById(R.id.autoComplete2);
        autoComplete1 = (AutoCompleteTextView) findViewById(R.id.autoComplete1);
        collegeText = (TextView) findViewById(R.id.collegeText);
        collegeText.setText(college);
        emailEdt = (TextView) findViewById(R.id.emailEdt);
        emailEdt.setText(email);
        changePasswordText = (TextView) findViewById(R.id.changePasswordText);
        changePasswordText.setOnClickListener(this);

        setTitle(username);
        setPicasso(mImage,studentImg,this,progressDialog);
        progressDialog.show();
        toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        switch (role){
            case CST_STUDENT:
                autoComplete1.setHint(CST_PROGRAM);
                autoComplete2.setHint(CST_BRANCH);
                autoComplete1.setText(getFromLocalDataBase(sharedPref,CST_PROGRAM));
                autoComplete2.setText(getFromLocalDataBase(sharedPref,CST_BRANCH));
                autoComplete1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.program_edit_icon, 0, 0, 0);
                autoComplete2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.branch_edit_icon, 0, 0, 0);
                setList(commonArrayList = loadLists(CST_DEGREES, CST_DEGREE,progressDialog),arrayAdapter, autoComplete1,this);
                setList(commonArrayList = loadLists(CST_BRANCHES, CST_BRANCH,progressDialog),arrayAdapter, autoComplete2,this);
                break;
            case CST_DEPARTMENT:
                progressDialog.dismiss();
                autoComplete1.setHint(CST_DEPARTMENT);
                autoComplete1.setText(getFromLocalDataBase(sharedPref,CST_DEPARTMENT));
                autoComplete1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.branch_edit_icon, 0, 0, 0);
                setList(commonArrayList = loadLists(CST_DEPARTMENTS, CST_DEPARTMENT,progressDialog),arrayAdapter, autoComplete1,this);

                autoComplete2.setVisibility(View.GONE);
                break;
            case CST_FACULTY:
                autoComplete1.setHint(CST_DESIGNATION);
                autoComplete2.setHint(CST_DEPARTMENT);
                autoComplete1.setText(getFromLocalDataBase(sharedPref,CST_DESIGNATION));
                autoComplete2.setText(getFromLocalDataBase(sharedPref,CST_DEPARTMENT));
                autoComplete1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.program_edit_icon,0, 0, 0);
                autoComplete2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.branch_edit_icon,0, 0, 0);
                setList(commonArrayList = loadLists(CST_DESIGNATIONS, CST_DESIGNATION,progressDialog),arrayAdapter, autoComplete1,this);
                setList(commonArrayList = loadLists(CST_DEPARTMENTS, CST_DEPARTMENT,progressDialog),arrayAdapter, autoComplete2,this);
                break;
            case CST_INSTITUTE:
                showToast(EditProfile.this, CST_INSTITUTE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectGender:
                final CharSequence[] gender = {"Male","Female"};
                final AlertDialog.Builder alert = new AlertDialog.Builder(EditProfile.this);
                //alert.setCancelable(false);
                alert.setTitle("Select Gender");
                alert.setSingleChoiceItems(gender,-1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(gender[which]=="Male") {
                            selectGender.setText("Male");
                            dialog.dismiss();
                        } else if (gender[which]=="Female") {
                            selectGender.setText("Female");
                            dialog.dismiss();
                        }
                    }
                });
                alert.show();
                break;
            case R.id.changePasswordText:
                final Dialog ialog = new Dialog(EditProfile.this);
                ialog.setContentView(R.layout.change_password_layout);
                ialog.setCancelable(false);
                old_password = (EditText) ialog.findViewById(R.id.old_password);
                new_password = (EditText) ialog.findViewById(R.id.new_password);
                confirm_password = (EditText) ialog.findViewById(R.id.confirm_password);

                final Button dialogok = (Button) ialog.findViewById(R.id.ok);
                final Button dialogCancel = (Button) ialog.findViewById(R.id.dialog_cancel_btn);

                dialogok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(new_password.getText().toString().trim().equals(confirm_password.getText().toString().trim())){
                            if ( firebaseUser!= null && !new_password.getText().toString().trim().equals("")) {
                                if (new_password.getText().toString().trim().length() < 6) {
                                    new_password.setError("atleast 6 characters");
                                    showSnackBar(new_password,"atleast 6 characters","");
                                } else {
                                    progressDialog = loadProgressDialog(EditProfile.this,"Changing Password",BOTTOM_DISPLAY);
                                    progressDialog.show();
                                    firebaseUser.updatePassword(new_password.getText().toString().trim())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        showToast(EditProfile.this, "Password is updated, sign in with new password!");
                                                        logOut(editor,"","", 1);
                                                        firebaseAuth.signOut();
                                                        progressDialog.dismiss();
                                                        goToNext(EditProfile.this, SPLASH_SCREEN, true);
                                                    } else {
                                                        progressDialog.dismiss();
                                                    }
                                                }
                                            });
                                }
                            } else if (new_password.getText().toString().trim().equals("")) {
                                new_password.setError("Enter password");
                            }
                        }else{
                            showSnackBar(confirm_password,"Password Mismatch, Please enter same password","");
                        }
                    }
                });
                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ialog.dismiss();
                    }
                });
                ialog.show();
                break;
            case R.id.birthdayTxt:
                DatePicker(birthdayEdit);
                break;
            case R.id.graduationYearText:
                final NumberPicker numberPicker;
                final Dialog graduation_dialog = new Dialog(EditProfile.this);
                graduation_dialog.setContentView(R.layout.graduation_year_layout);
                graduation_dialog.setTitle("Select Year");
                graduation_dialog.setCancelable(false);
                numberPicker = (NumberPicker) graduation_dialog.findViewById(R.id.numberPicker);
                numberPicker.setMinValue(1960);
                numberPicker.setMaxValue(2027);

                final Button done = (Button) graduation_dialog.findViewById(R.id.done);
                final Button cancel = (Button) graduation_dialog.findViewById(R.id.dialog_cancel_btn);

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        graduationYearEdit.setText(Integer.toString(numberPicker.getValue()));
                        graduation_dialog.dismiss();

                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        graduation_dialog.dismiss();
                    }
                });
                graduation_dialog.show();
                break;
            case R.id.studentImg:
                final Dialog student_dialog = new Dialog(EditProfile.this);
                student_dialog.setContentView(R.layout.student_image_layout);

                take_photo = (TextView) student_dialog.findViewById(R.id.take_photo);
                gallery = (TextView) student_dialog.findViewById(R.id.gallery);
                cancel_image = (TextView) student_dialog.findViewById(R.id.img_cancel);
                removeImg = (TextView) student_dialog.findViewById(R.id.removeImg);


                if (studentImg.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.ic_launcher_web).getConstantState()) {

                }
                else {
                    if(studentImg.getDrawable() == null){
                        removeImg.setVisibility(View.GONE);
                    }else {
                        removeImg.setVisibility(View.VISIBLE);
                    }
                }
                take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (android.os.Build.VERSION.SDK_INT >= 22) {
                            if (isCameraPermission(EditProfile.this)) {
                                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, CAMERA_REQUEST);
                                student_dialog.dismiss();
                                return;
                            }
                            askForPermisssion(EditProfile.this);
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
                        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, GALLARY_REQUEST);
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
                removeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        studentImg.setImageDrawable(null);
                        studentImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_web));
                        student_dialog.dismiss();
                    }
                });
                break;
        }
    }


    private void sendValuesOnServer(String imgPath) {


        Log.i(LOGCAT, ""+userNameEdt.getText()+"\n"+ autoComplete1.getText()+"\n"+ autoComplete2.getText()+"\n"
                +graduationYearEdit.getText()+"\n"+collegeText.getText()+"\n"+aboutStatusEdt.getText()+"\n"
                +selectGender.getText()+"\n"+mobileEdt.getText()+"\n"+ emailEdt.getText()+"\n"+birthdayEdit.getText()+"\n"
                +changePasswordText.getText()
                +imgPath);

        /*User userProfileDetails = new User(accountKey+"",
                imgPath,
                String.valueOf(userNameEdt.getText().toString()),
                String.valueOf(autoComplete1.getText().toString()+""),
                String.valueOf(autoComplete2.getText().toString()+""),
                String.valueOf(graduationYearEdit.getText().toString()),
                String.valueOf(collegeText.getText().toString()),
                String.valueOf(aboutStatusEdt.getText().toString()),
                String.valueOf(selectGender.getText().toString()),
                String.valueOf(mobileEdt.getText().toString()),
                String.valueOf(emailEdt.getText().toString()),
                String.valueOf(birthdayEdit.getText().toString()),
                String.valueOf(role),
                "0",
                "degree",
                "designation",
                "department",
                "faculty",
                "request",0);*/
        DATABASE_REFERENCE = setFireBaseKey(CST_USER_LISTS);

        updateSingleValue(DATABASE_REFERENCE,username,setMapValue(CST_USER_NAME,String.valueOf(userNameEdt.getText())));

        updateSingleValue(DATABASE_REFERENCE,username,setMapValue(CST_GRADUATION_YEAR,String.valueOf(graduationYearEdit.getText())));
        updateSingleValue(DATABASE_REFERENCE,username,setMapValue(CST_ABOUT_STATUS,String.valueOf(aboutStatusEdt.getText())));
        updateSingleValue(DATABASE_REFERENCE,username,setMapValue(CST_GENDER,String.valueOf(selectGender.getText())));

        updateSingleValue(DATABASE_REFERENCE,username,setMapValue(CST_BIRTHDAY,String.valueOf(birthdayEdit.getText())));
        updateSingleValue(DATABASE_REFERENCE,username,setMapValue(CST_CONTACT_NUMBER,String.valueOf(mobileEdt.getText())));
        updateSingleValue(DATABASE_REFERENCE,username,setMapValue(CST_USER_IMAGE,imgPath));


        SharedPreferences.Editor editor = sharedPref.edit();
        setToLocalDataBase(editor, sharedPref, CST_USER_FULL_NAME,  String.valueOf(userNameEdt.getText().toString()));
        setToLocalDataBase(editor, sharedPref, CST_GRADUATION_YEAR,  String.valueOf(graduationYearEdit.getText().toString()));
        setToLocalDataBase(editor, sharedPref, CST_COLLEGE, String.valueOf(collegeText.getText().toString()));
        setToLocalDataBase(editor, sharedPref, CST_ROLE, role);
        setToLocalDataBase(editor, sharedPref, CST_USER_IMAGE, imgPath);
        setToLocalDataBase(editor, sharedPref, CST_GENDER, String.valueOf(selectGender.getText().toString()));
        setToLocalDataBase(editor, sharedPref, CST_ABOUT_STATUS, String.valueOf(aboutStatusEdt.getText().toString()));
        setToLocalDataBase(editor, sharedPref, CST_BIRTHDAY, String.valueOf(birthdayEdit.getText().toString()));


        switch (role){
            case CST_STUDENT:
                setToLocalDataBase(editor, sharedPref, CST_BRANCH, String.valueOf(autoComplete2.getText().toString()));
                setToLocalDataBase(editor, sharedPref, CST_PROGRAM, String.valueOf(autoComplete1.getText().toString()));

                updateSingleValue(DATABASE_REFERENCE,username,setMapValue(CST_PROGRAM,String.valueOf(autoComplete1.getText())));
                updateSingleValue(DATABASE_REFERENCE,username,setMapValue(CST_BRANCH,String.valueOf(autoComplete2.getText())));
                break;
            case CST_DEPARTMENT:
                setToLocalDataBase(editor, sharedPref, CST_DEPARTMENT, String.valueOf(autoComplete1.getText().toString()));
                updateSingleValue(DATABASE_REFERENCE,username,setMapValue(CST_DEPARTMENT,String.valueOf(autoComplete1.getText())));
                break;
            case CST_FACULTY:
                setToLocalDataBase(editor, sharedPref, CST_DESIGNATION, String.valueOf(autoComplete1.getText().toString()));
                setToLocalDataBase(editor, sharedPref, CST_DEPARTMENT, String.valueOf(autoComplete2.getText().toString()));

                updateSingleValue(DATABASE_REFERENCE,username,setMapValue(CST_DESIGNATION,String.valueOf(autoComplete1.getText())));
                updateSingleValue(DATABASE_REFERENCE,username,setMapValue(CST_DEPARTMENT,String.valueOf(autoComplete2.getText())));
                break;
            case CST_INSTITUTE:

                break;
            default:
                break;
        }


        boolean b = setToLocalDataBase(editor, sharedPref, COMMON_FLAG, MY_PROFILE_FLAG);
        if(b) {
            progressDialog.dismiss();
            goToNext(EditProfile.this,BOARD_SCREEN,true);
        }
    }

    //TODO on Create Opetion Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    //TODO Edit, Notification and Setting
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save) {
            //Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
            if(imageIdentify.equals("1")){
                // Toast.makeText(this, "For Gallery ", Toast.LENGTH_SHORT).show();
                progressDialog.setMessage("Saving Your Data");
                progressDialog.show();
                StorageReference childRef = storageReference.child("users/"+username + ".jpg");
                UploadTask uploadTask = childRef.putFile(filePath);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Toast.makeText(EditUserDetails.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mImagePath = String.valueOf(downloadUrl);
                        sendValuesOnServer(mImagePath);
                        progressDialog.dismiss();
                        gall = false;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Toast.makeText(EditUserDetails.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }else if(imageIdentify.equals("2")){
                // Toast.makeText(this, "For Camera ", Toast.LENGTH_SHORT).show();
                progressDialog.setMessage("Saving Your Data");
                progressDialog.show();
                Uri file = Uri.fromFile(destination);
                StorageReference childRef = storageReference.child("users/" + username + ".jpg");
                UploadTask uploadTask = childRef.putFile(file);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Toast.makeText(EditUserDetails.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mImagePath = String.valueOf(downloadUrl);
                        sendValuesOnServer(mImagePath);
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Toast.makeText(EditUserDetails.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }else if(studentImg.getDrawable().getConstantState() == getResources().getDrawable( R.mipmap.ic_launcher).getConstantState()){
                sendValuesOnServer(mImage);
            }else{
                mImage = sharedPref.getString(CST_USER_IMAGE, "");
                sendValuesOnServer(mImage);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void DatePicker(TextView textView) {
        DatePickerFragment datePickerFragment = new DatePickerFragment(textView);
        DialogFragment newFragment = datePickerFragment;
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLARY_REQUEST) {
                try {
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                    filePath = data.getData();
                    stream = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    studentImg.setImageBitmap(bitmap);
                    imageIdentify = "1";
                    identifyIMG = 1;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    if (stream != null)
                        try {
                            stream.close();
                        } catch (IOException e1) {
                            e.printStackTrace();
                        }
                }
            } else if (requestCode == CAMERA_REQUEST) {
                //Toast.makeText(this, "Camera Image", Toast.LENGTH_SHORT).show();

                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpeg");
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
                studentImg.setImageBitmap(bitmap);
                //String Imagepath = (getImageFromCamera(data,studentImg));
                imageIdentify = "2";
                identifyIMG = 1;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            } else {
                // Toast.makeText(this, "You deny Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
