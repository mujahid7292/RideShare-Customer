package com.sand_corporation.www.uthao;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sand_corporation.www.uthao.LanguageChange.LocalHelper;
import com.sand_corporation.www.uthao.Permissions.Permissions;
import com.sand_corporation.www.uthao.SQDatabase.DbContract;
import com.sand_corporation.www.uthao.SQDatabase.DbHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class CustomerSettingsActivity extends AppCompatActivity {

    private static final String DEFAULT = "N/A";
    public static final int REQUEST_CODE_FOR_GALLERY_ACCESS = 100;
//    private static final int PERMISSION_REQUEST_FOR_EXTERNAL_STORAGE = 102;
    private EditText customerFullNameField, customerPhoneNumberField,
            customerEmailField, customerHomeAddressField;
    private TextView btnSignOut;
    private ImageView ic_back_sign, btnEditOfSettingsPage;
    private CircleImageView imgCustomerProfilePic;
    private Bitmap bmapCustomerProfilePic;
    private String customerName,customerEmail,customerMobile, customerHomeAddress;
    private FirebaseAuth mCustomerAuth;
    private DatabaseReference mCustomerDataBaseRef;
    private String customerUID;
    private Uri resultUri;
    private String firebaseStorageCustomerPP;
    private Permissions permissions;

    @Override
    protected void onStart() {
        super.onStart();
        createDefaultLanguageForThisApp();
    }

    private void createDefaultLanguageForThisApp() {
        FirebaseCrash.log("CustomerSettingsActivity: createDefaultLanguageForThisApp.called");
        Crashlytics.log("CustomerSettingsActivity: createDefaultLanguageForThisApp.called");
        //init paper first
        //We use paper library to write in internal storage
        Paper.init(this);

        //Default language is English
        String language = Paper.book().read("language");
        if (language == null){
            Paper.book().write("language","en");
        }
        updateDefaultLanguage((String)Paper.book().read("language"));
    }

    private void updateDefaultLanguage(String language) {
        FirebaseCrash.log("CustomerSettingsActivity: updateDefaultLanguage.called");
        Crashlytics.log("CustomerSettingsActivity: updateDefaultLanguage.called");
        Context context = LocalHelper.setLocale(this,language);
        Resources resources = context.getResources();

        //Below we will change all the language
        //Button string
        btnSignOut.setText(R.string.sign_out);

        //Edit text string
        customerFullNameField.setHint(R.string.please_enter_your_full_name);
        customerEmailField.setHint(R.string.please_enter_your_email);
        customerPhoneNumberField.setHint(R.string.please_enter_your_mobile_number);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseCrash.log("CustomerSettingsActivity:onCreate.called");
        Crashlytics.log("CustomerSettingsActivity:onCreate.called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_settings);

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Log.i("Check","CustomerSettingsActivity: Storage Permission asked before");
        }

        checkHowManyPermissionAllowed(CustomerSettingsActivity.this);

        permissions = new Permissions(CustomerSettingsActivity.this);



        customerFullNameField = findViewById(R.id.customerFullNameField);
        customerPhoneNumberField = findViewById(R.id.customerPhoneNumberField);
        customerEmailField = findViewById(R.id.customerEmailField);
        customerHomeAddressField = findViewById(R.id.customerHomeAddressField);


        btnSignOut =findViewById(R.id.btnSignOut);
        ic_back_sign = findViewById(R.id.ic_back_sign);
        ic_back_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnEditOfSettingsPage = findViewById(R.id.btnEditOfSettingsPage);
        btnEditOfSettingsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getContentDescription().equals("edit")){
                    customerEmailField.setEnabled(true);
                    customerEmailField.setCursorVisible(true);
                    customerEmailField.setClickable(true);
                    customerEmailField.setFocusable(true);
                    customerEmailField.setFocusableInTouchMode(true);
                    customerEmailField.setLongClickable(true);
                    customerHomeAddressField.setEnabled(true);
                    customerHomeAddressField.setCursorVisible(true);
                    customerHomeAddressField.setCursorVisible(true);
                    customerHomeAddressField.setFocusable(true);
                    customerHomeAddressField.setFocusableInTouchMode(true);
                    customerHomeAddressField.setLongClickable(true);
                    btnEditOfSettingsPage.setImageResource(R.drawable.ic_save);
                    btnEditOfSettingsPage.setContentDescription("save");
                } else if (view.getContentDescription().equals("save")){
                    customerEmailField.setEnabled(false);
                    customerEmailField.setCursorVisible(false);
                    customerEmailField.setClickable(false);
                    customerEmailField.setFocusable(false);
                    customerEmailField.setFocusableInTouchMode(false);
                    customerEmailField.setLongClickable(false);
                    customerHomeAddressField.setEnabled(false);
                    customerHomeAddressField.setCursorVisible(false);
                    customerHomeAddressField.setCursorVisible(false);
                    customerHomeAddressField.setFocusable(false);
                    customerHomeAddressField.setFocusableInTouchMode(false);
                    customerHomeAddressField.setLongClickable(false);
                    customerHomeAddressField.setEnabled(false);
                    saveCustomerInfoToDataBase();
                    btnEditOfSettingsPage.setImageResource(R.drawable.ic_edit);
                    btnEditOfSettingsPage.setContentDescription("edit");
                }

            }
        });


        imgCustomerProfilePic = findViewById(R.id.customerProfilePic);


        mCustomerAuth = FirebaseAuth.getInstance();
        customerUID = mCustomerAuth.getCurrentUser().getUid();
        mCustomerDataBaseRef = FirebaseDatabase.getInstance().getReference("Users/Customers")
                .child(customerUID).child("Customer_Basic_Info");
        getUserInfo();



        imgCustomerProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("CustomerSettingsActivity:imgCustomerProfilePic.onClick");
                Crashlytics.log("CustomerSettingsActivity:imgCustomerProfilePic.onClick");
                permissions.requestAllPermission(CustomerSettingsActivity.this);
                //We will direct our customer to gallery to choose his or her profile pic from
                //gallery.
                //As usual we will use Intent to direct our customer to gallery.
                Intent intent = new Intent(Intent.ACTION_PICK);
                //'Intent.ACTION_PICK' this means this intent will help our customer to pickup something.
                //what customer will pickup, we will decide in the next line
                intent.setType("image/*");
                //User will only be allowed to pick up image.
                startActivityForResult(intent, REQUEST_CODE_FOR_GALLERY_ACCESS);
                //Instead of 'startActivity' we used 'startActivityForResult'. because 'startActivity'
                //will only open gallery.But we want to do something after opening gallery, so we used
                //'startActivityForResult'.
                //As we have used 'startActivityForResult' than we have to override one method
                //which is 'onActivityResult()' method.
                //In 'onActivityResult()' method, we will get our user image through an intent.
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("CustomerSettingsActivity:btnSignOut.onClick");
                Crashlytics.log("CustomerSettingsActivity:btnSignOut.onClick");
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CustomerSettingsActivity.this, Welcome.class);
                startActivity(intent);
                finish();
            }
        });
        

    }

    private void getUserInfo() {
        FirebaseCrash.log("CustomerSettingsActivity:getUserInfo.called");
        Crashlytics.log("CustomerSettingsActivity:getUserInfo.called");
        SharedPreferences preferences = getSharedPreferences("Customer_Basic_Info",
                Context.MODE_PRIVATE);
        final String name =preferences.getString("Customer_Name",DEFAULT);//For DEFAULT see top of the code
        final String email =preferences.getString("Customer_Email",DEFAULT);//For DEFAULT see top of the code
        final String mobile =preferences.getString("Customer_Mobile",DEFAULT);//For DEFAULT see top of the code
        String home_address = preferences.getString("Customer_Home_address",DEFAULT);//For DEFAULT see top of the code
        final String Customer_FaceBook_PP = preferences.getString("Customer_FaceBook_PP",DEFAULT);//For DEFAULT see top of the code
        Log.i("Check","SharedPref FaceBook PP: " + Customer_FaceBook_PP + "\n" +
                                "Customer_Name: " + name + "\n" +
                                "Customer_Email: " + email + "\n" +
                                "mobile: " + mobile + "\n" +
        "Customer_Home_address: " + home_address);

        if (!home_address.equals(DEFAULT)){
            customerHomeAddressField.setText(home_address);
        }

        if(name.equals(DEFAULT) || email.equals(DEFAULT)
                || mobile.equals(DEFAULT)){
//            Toast.makeText(this,"There is no saved Data found",Toast.LENGTH_SHORT).show();
            //If we don't find customer info locally saved than we will retrieve info from
            //firebase.
            mCustomerDataBaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                        Map<String, Object> map =(Map<String, Object>) dataSnapshot.getValue();
                        //Now we will save those basic info to internal storage
                        SharedPreferences preferences = getSharedPreferences("Customer_Basic_Info",
                                Context.MODE_PRIVATE);
                        //Context.MODE_PRIVATE This means MyData will be accessible by only this app
                        SharedPreferences.Editor editor = preferences.edit();

                        if (map.get("Customer_Name") != null){
                            customerName = map.get("Customer_Name").toString();
                            customerFullNameField.setText(customerName);
                            if (!name.equals(DEFAULT)){
                                editor.putString("Customer_Name",customerName);
                            }
                        }
                        if (map.get("Customer_Mobile") != null) {
                            customerMobile = map.get("Customer_Mobile").toString();
                            customerPhoneNumberField.setText(customerMobile);
                            if (!mobile.equals(DEFAULT)){
                                editor.putString("Customer_Mobile",customerMobile);
                            }

                        }
                        if (map.get("Customer_Email") != null) {
                            customerEmail = map.get("Customer_Email").toString();
                            customerEmailField.setText(customerEmail);
                            if (!email.equals(DEFAULT)){
                                editor.putString("Customer_Email",customerEmail);
                            }
                        }
                        if (map.get("Customer_Password") != null) {
                            customerHomeAddress = map.get("Customer_Password").toString();
                            customerPhoneNumberField.setText(customerHomeAddress);
                            if (!mobile.equals(DEFAULT)){
                                editor.putString("Customer_Password", customerHomeAddress);
                            }
                        }
                        editor.apply();
                        //editor.apply(); we are writing those value to shared preference storage

                        //Now we will display customer's profile pic using glide.
                        if (map.get("Customer_Profile_Pic_Url") != null) {
                            firebaseStorageCustomerPP = map.get("Customer_Profile_Pic_Url").toString();
                            Glide.with(getApplication()).load(firebaseStorageCustomerPP).into(imgCustomerProfilePic);
                        } else if (!Customer_FaceBook_PP.equals(DEFAULT)){
                            Glide.with(getApplication()).load(Customer_FaceBook_PP).into(imgCustomerProfilePic);
                        }
//                        Toast.makeText(getApplicationContext(),"Data retrieved from fireBase: Successful",
//                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            customerFullNameField.setText(name);
            customerEmailField.setText(email);
            customerPhoneNumberField.setText(mobile);

            if (!Customer_FaceBook_PP.equals(DEFAULT)){
                Glide.with(getApplication()).load(Customer_FaceBook_PP).into(imgCustomerProfilePic);
                Log.i("Check", "FaceBook PP Has Been Set");
            } else {
                //Get User profile pic if exists
                getCustomerProfilePic(name);
            }

        }

    }

    private void getCustomerProfilePic(String customerFullName) {
        FirebaseCrash.log("CustomerSettingsActivity:getCustomerProfilePic.called");
        Crashlytics.log("CustomerSettingsActivity:getCustomerProfilePic.called");
        if (isExternalStorageReadable()){
            Log.i("Check","isExternalStorageReadable: true");
            //First get customer name from shared preferences
            if (!customerFullName.equals(DEFAULT)){
                Log.i("Check", "!customerFullName.equals(DEFAULT)");
                //Now we will check if we have profile pic in the SQLite db
                //Ask for storage permission
                permissions.requestAllPermission(CustomerSettingsActivity.this);
                DbHelper helper = new DbHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = helper.retrieveCustomerPicFromQLiteDatabase(db);
                if (cursor != null) {
                    Log.i("Check", "Cursor != null");
                    if (cursor.getCount() > 0) {
                        Log.i("Check", "cursor.getCount() > 0");
                        //This means we have profile pic in SQLite db
                        //Now we will retrieve this PP
                        Log.i("Check", "We have pp in sqlite");
                        cursor.moveToFirst();
                        int imgCustomerPicColumn_number = cursor.getColumnIndex(DbContract
                                .TABLE_USER_PIC_SECOND_COLUMN_NAME);
                        bmapCustomerProfilePic = getImage(cursor.getBlob(imgCustomerPicColumn_number));
                        imgCustomerProfilePic.setImageBitmap(bmapCustomerProfilePic);
//                        Toast.makeText(this,"Profile Pic retrieved from " +
//                                        "SQLiteDataBase: Successful",
//                                Toast.LENGTH_SHORT).show();
                        cursor.close();
                    } else {
                        Log.i("Check", "cursor.getCount() == 0");
                        //We will see if we have user image on cloud
                        StorageReference filePath = FirebaseStorage.getInstance().getReference().
                                child("Customer_Profile_Pic").child(customerUID);
                        final long ONE_MEGABYTE = 1024 * 1024;
                        filePath.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Log.i("Check", "PP retrieved from firebase storage successful");
                                // Data for "images/island.jpg" is returns, use this as needed
                                saveCustomerPicToSqliteDB(bytes);
                                imgCustomerProfilePic.setImageBitmap(getImage(bytes));
//                            Toast.makeText(getApplicationContext(),"Profile Pic retrieved from " +
//                                            "fireBase: Successful",
//                                    Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.i("Check", "PP retrieved from firebase storage failed");
                                // Handle any errors
//                            Toast.makeText(getApplicationContext(),"Profile Pic retrieved from " +
//                                            "fireBase: Unsuccessful",
//                                    Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Log.i("Check", "Cursor == null");

                }
            }
        } else {
            Log.i("Check","isExternalStorageReadable: false");
        }
    }

    private void saveCustomerInfoToDataBase() {
        FirebaseCrash.log("CustomerSettingsActivity:saveCustomerInfoToDataBase.called");
        Crashlytics.log("CustomerSettingsActivity:saveCustomerInfoToDataBase.called");
        customerName = customerFullNameField.getText().toString();
        customerEmail = customerEmailField.getText().toString();
        customerMobile = customerPhoneNumberField.getText().toString();
        customerHomeAddress = customerHomeAddressField.getText().toString();

        //Now we will save those basic info to internal storage
        SharedPreferences preferences = getSharedPreferences("Customer_Basic_Info",
                Context.MODE_PRIVATE);
        //Context.MODE_PRIVATE This means MyData will be accessible by only this app
        SharedPreferences.Editor editor = preferences.edit();
        Map userInfo = new HashMap();
        Map<String, Object> fireStoreMap = new HashMap<>();
        if (!customerHomeAddress.equals("") ){
            userInfo.put("Customer_Home_address", customerHomeAddress);
            fireStoreMap.put("Customer_Home_address", customerHomeAddress);
            editor.putString("Customer_Home_address",customerHomeAddress);
        }
        if (!customerEmail.equals("")){
            userInfo.put("Customer_Email_Changed",customerEmail);
            fireStoreMap.put("Customer_Email_Changed", customerEmail);
            editor.putString("Customer_Email_Changed",customerEmail);
        }
        mCustomerDataBaseRef.updateChildren(userInfo);
        editor.apply();

        mCustomerDataBaseRef = FirebaseDatabase.getInstance().getReference("Users/Customers")
                .child(customerUID).child("Customer_Basic_Info");

        FirebaseFirestore mFireStoreCustomerBasicInfoDB =FirebaseFirestore.getInstance();
        mFireStoreCustomerBasicInfoDB.collection("Users")
                .document("Customers")
                .collection(customerUID)
                .document("Customer_Basic_Info")
                .update(fireStoreMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("CustomerSettingsActivity:fireStore:saveCustomerInfoToDataBase Update: Success");
                    Crashlytics.log("CustomerSettingsActivity:fireStore:saveCustomerInfoToDataBase Update: Success");
                }else {
                    FirebaseCrash.log("CustomerSettingsActivity:fireStore:saveCustomerInfoToDataBase Update: failed");
                    Crashlytics.log("CustomerSettingsActivity:fireStore:saveCustomerInfoToDataBase Update: failed");
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FirebaseCrash.log("CustomerSettingsActivity:onActivityResult.called");
        Crashlytics.log("CustomerSettingsActivity:onActivityResult.called");
        super.onActivityResult(requestCode, resultCode, data);
        //First we have to check the request code, whether the request code is image pickup
        //request or not. Than we have to check whether user select an image or not.
        if (requestCode == REQUEST_CODE_FOR_GALLERY_ACCESS && resultCode == Activity.RESULT_OK){
            //If execution enter this line of code than we could definitely say that user has choose
            //an image for his profile pic. So now we will extract this profile pic from 'data'
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            //Now we set this image to image view. Here we only set this image to image view
            //no on the server.
            imgCustomerProfilePic.setImageURI(resultUri);
            //After this line our customer will be able to see their profile pic on the
            //image view.
            //Now we will save our customer's profile pic to database in saveCustomerInfoToDataBase()
            //method

            if (resultUri != null){
                //Below we will save our customer's profile picture to firebase storage.
                StorageReference filePath = FirebaseStorage.getInstance().getReference().
                        child("Customer_Profile_Pic").child(customerUID);
                //Now we will transfer our 'resultUri' into bitmap
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //As firebase storage has limited free space, so we will first compress our image.
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);
                //Now we will convert this bitmap image into an array
                final byte[] imgData = baos.toByteArray();
                //Now we will upload this array into our firebase storage.
                UploadTask uploadTask = filePath.putBytes(imgData);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //After putting our image to firebase storage. We will create a link of this image
                        //in our firebase database under customer 'customerUID' child
                        Uri profilePicDownloadUri = taskSnapshot.getDownloadUrl();
//                    Toast.makeText(CustomerSettingsActivity.this,"Download Url: " +
//                    profilePicDownloadUri.toString(),Toast.LENGTH_LONG).show();
                        Log.i("DownloadUrl",profilePicDownloadUri.toString());

                        Map pic_url = new HashMap();
                        pic_url.put("Customer_Profile_Pic_Url",profilePicDownloadUri.toString());
                        mCustomerDataBaseRef.updateChildren(pic_url);

                        finish();
                        saveCustomerPicToSqliteDB(imgData);
                    }
                });

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CustomerSettingsActivity.this,"Something went wrong. Upload failed....",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }else {
                finish();
            }
        }
    }

    private void saveCustomerPicToSqliteDB(byte[] imgData) {
        FirebaseCrash.log("CustomerSettingsActivity:saveCustomerPicToSqliteDB.called");
        Crashlytics.log("CustomerSettingsActivity:saveCustomerPicToSqliteDB.called");
        //First get customer name from shared preferences
        SharedPreferences preferences = getSharedPreferences("Customer_Basic_Info",
                Context.MODE_PRIVATE);
        String customerName = preferences.getString("Customer_Name",DEFAULT);//For DEFAULT see top of the code
        if (!customerName.equals(DEFAULT) && isExternalStorageWritable()){
            //Now we will check if we are updating profile pic or inserting
            //profile pic for the first time
            DbHelper helper = new DbHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = helper.retrieveCustomerPicFromQLiteDatabase(db);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    //This means we are updating profile pic
                    helper.updateCustomerPicToSQLiteDatabase(customerName, imgData, db);
//                    Toast.makeText(getApplicationContext(),"Profile Pic updated " +
//                                    "in SQLite: Successful",
//                            Toast.LENGTH_SHORT).show();
                    cursor.close();
                } else {
                    //This means we are inserting profile pic for the first time
                    helper.saveCustomerPicToSQLiteDatabase(customerName, imgData, db);
//                    Toast.makeText(getApplicationContext(),"Profile Pic save " +
//                                    "in SQLite: Successful",
//                            Toast.LENGTH_SHORT).show();
                    cursor.close();
                }
            }
        }

    }

//    private void checkReadExternalStoragePermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
//                        },PERMISSION_REQUEST_FOR_EXTERNAL_STORAGE);
//            }
//        }
//    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void checkHowManyPermissionAllowed(Context mContext) {
        //In permissionNeeded Arraylist we will put the permission for which we did not get user
        //approval yet.
        ArrayList<String> permissionNeeded = new ArrayList<>();
        //In permissionsAvailable Arraylist we will put all the permissions which is required by this
        //application
        ArrayList<String> permissionsAvailable = new ArrayList<>();
        ArrayList<String> permissionsAllowed = new ArrayList<>();
        permissionsAvailable.add(android.Manifest.permission.READ_PHONE_STATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissionsAvailable.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        permissionsAvailable.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsAvailable.add(android.Manifest.permission.SEND_SMS);
        permissionsAvailable.add(android.Manifest.permission.READ_SMS);
        permissionsAvailable.add(android.Manifest.permission.RECEIVE_SMS);
        permissionsAvailable.add(Manifest.permission.INTERNET);
        //Above we did not added camera permission. We will ask for camera permission
        //to user when we truly need it. Otherwise user will get angry.

        for (String permission : permissionsAvailable){
            if (ContextCompat.checkSelfPermission(mContext,permission) !=
                    PackageManager.PERMISSION_GRANTED){
                //We will check each permission in permissionsAvailable Arraylist, whether user already
                //approved the permission or not.
                //Execution will come to this block if user did not approved the permission. So we will
                //add this specefic permission in our permissionNeeded Arraylist.
                permissionNeeded.add(permission);
            } else {
                permissionsAllowed.add(permission);
            }
        }
        Log.i("Check", "Permission Needed: " + permissionNeeded.toString() +
                "\nPermission Allowed: " + permissionsAllowed.toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
