package com.sand_corporation.www.uthao;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DriverSettingsActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_FOR_EXTERNAL_STORAGE = 102;
    private static final int REQUEST_CODE_FOR_GALLERY_ACCESS = 100;
    private ImageView driverProfilePic;
    private EditText driverFullName, driverPhoneNumber, driverCarModel;
    private Button confirmdriverInfo, backToDriverMapActivity;
    private FirebaseAuth mDriverAuth;
    private DatabaseReference mDriverDatabaseRef;
    private String mDriverUID;
    private Uri resultUri;
    private String strDriverFullName, strDriverPhoneNumber, strDriverCarModel;
    private String strFirebaseStorageDriverPP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);

        checkReadExternalStoragePermission();
        driverProfilePic = findViewById(R.id.driverProfilePic);

        driverFullName = findViewById(R.id.driverFullName);
        driverPhoneNumber = findViewById(R.id.driverPhoneNumber);
        driverCarModel = findViewById(R.id.driverCarModel);

        confirmdriverInfo = findViewById(R.id.confirmdriverInfo);
        backToDriverMapActivity = findViewById(R.id.backToDriverMapActivity);

        mDriverAuth = FirebaseAuth.getInstance();
        mDriverUID = mDriverAuth.getUid();
        mDriverDatabaseRef = FirebaseDatabase.getInstance().getReference("Users/Drivers")
                            .child(mDriverUID).child("Driver_Basic_Info");
        getUerInfo();
        driverProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We will direct our driver to gallery to choose his or her profile pic from
                //gallery.
                //As usual we will use Intent to direct our driver to gallery.
                Intent intent = new Intent(Intent.ACTION_PICK);
                //'Intent.ACTION_PICK' this means this intent will help our driver to pickup something.
                //what driver will pickup, we will decide in the next line
                intent.setType("image/*");
                //User will only be allowed to pick up image.
                startActivityForResult(intent,REQUEST_CODE_FOR_GALLERY_ACCESS);
                //Instead of 'startActivity' we used 'startActivityForResult'. because 'startActivity'
                //will only open gallery.But we want to do something after opening gallery, so we used
                //'startActivityForResult'.
                //As we have used 'startActivityForResult' than we have to override one method
                //which is 'onActivityResult()' method.
                //In 'onActivityResult()' method, we will get our user image through an intent.
            }
        });
        confirmdriverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDriverInfoToDataBase();
            }
        });

        backToDriverMapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getUerInfo(){
        mDriverDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("Driver_Full_Name") != null){
                        strDriverFullName = map.get("Driver_Full_Name").toString();
                        driverFullName.setText(strDriverFullName);
                    }
                    if (map.get("Driver_Phone_Number") != null){
                        strDriverPhoneNumber = map.get("Driver_Phone_Number").toString();
                        driverPhoneNumber.setText(strDriverPhoneNumber);
                    }
                    if (map.get("Driver_Car_Model") != null){
                        strDriverCarModel = map.get("Driver_Car_Model").toString();
                        driverCarModel.setText(strDriverCarModel);
                    }
                    if (map.get("Driver_Profile_Pic") != null){
                        strFirebaseStorageDriverPP = map.get("Driver_Profile_Pic").toString();
                        Glide.with(getApplication()).load(strFirebaseStorageDriverPP).into(driverProfilePic);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void saveDriverInfoToDataBase() {
        strDriverFullName = driverFullName.getText().toString();
        strDriverPhoneNumber = driverPhoneNumber.getText().toString();
        strDriverCarModel = driverCarModel.getText().toString();

        Map driverBasicInfo = new HashMap();
        driverBasicInfo.put("Driver_Full_Name",strDriverFullName);
        driverBasicInfo.put("Driver_Phone_Number",strDriverPhoneNumber);
        driverBasicInfo.put("Driver_Car_Model",strDriverCarModel);
        mDriverDatabaseRef.updateChildren(driverBasicInfo);

        if (resultUri != null){
            //Below we will save our Driver's profile picture to firebase storage.
            StorageReference mDriverStorageRef = FirebaseStorage.getInstance().getReference()
                    .child("Driver_Profile_Pic").child(mDriverUID);
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
            byte[] data = baos.toByteArray();
            //Now we will upload this array into our firebase storage.
            UploadTask uploadTask = mDriverStorageRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //After putting our image to firebase storage. We will create a link of this image
                    //in our firebase database under Drivers 'driverUID' child under 'Driver_Basic_Info'
                    Uri profilePicDownLoadUri = taskSnapshot.getDownloadUrl();
                    Map ppUrl = new HashMap();
                    ppUrl.put("Driver_Profile_Pic",profilePicDownLoadUri.toString());
                    mDriverDatabaseRef.updateChildren(ppUrl);
                }
            });
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DriverSettingsActivity.this,"Something went wrong. Upload failed....",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //First we have to check the request code, whether the request code is image pickup
        //request or not. Than we have to check whether user select an image or not.
        if (requestCode == REQUEST_CODE_FOR_GALLERY_ACCESS && resultCode == Activity.RESULT_OK) {
            //If execution enter this line of code than we could definitely say that user has choose
            //an image for his profile pic. So now we will extract this profile pic from 'data'
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            //Now we set this image to image view. Here we only set this image to image view
            //no on the server.
            driverProfilePic.setImageURI(imageUri);
            //After this line our customer will be able to see their profile pic on the
            //image view.
            //Now we will save our customer's profile pic to database in saveDriverInfoToDataBase()
            //method
        }
    }

    private void checkReadExternalStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                },PERMISSION_REQUEST_FOR_EXTERNAL_STORAGE);
            }
        }
    }
}
