package com.sand_corporation.www.uthao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverLogInActivity extends AppCompatActivity {

   EditText edtDriverEmail, edtDriverPassword;
   Button btnDriverLogIn, btnDriverRegistration;
   FirebaseAuth mDriverAuth;
   FirebaseAuth.AuthStateListener mDriverAuthListener;
    //By FirebaseAuth.AuthStateListener we will listen for fireBase Authentication.

    @Override
    protected void onStart() {
        super.onStart();
        mDriverAuth.addAuthStateListener(mDriverAuthListener);  //VERY IMPORTANT
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_log_in);

        mDriverAuth = FirebaseAuth.getInstance();
        mDriverAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //First find out that if this app has any driver previously registered.
                FirebaseUser mDriver = FirebaseAuth.getInstance().getCurrentUser();
                if (mDriver != null){
                    Intent intent = new Intent(DriverLogInActivity.this, DriverMapsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        edtDriverEmail = (EditText)findViewById(R.id.edtDriverEmail);
        edtDriverPassword = (EditText)findViewById(R.id.edtDriverPassword);

        btnDriverLogIn = (Button) findViewById(R.id.btnDriverLogIn);
        btnDriverRegistration = (Button) findViewById(R.id.btnDriverRegistration);
        btnDriverRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String driverEmail = edtDriverEmail.getText().toString();
                final String driverPassword = edtDriverPassword.getText().toString();
                //Create driver using email & password
                mDriverAuth.createUserWithEmailAndPassword(driverEmail, driverPassword).
                        addOnCompleteListener(DriverLogInActivity.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    //If task is not successful
                                    Toast.makeText(DriverLogInActivity.this,"Something went wrong....",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    //Driver registration is successful
                                    //Now we will put the driver ID in our fireBase Real time database
                                    String driverUID = mDriverAuth.getCurrentUser().getUid();
                                    DatabaseReference mDriverDatabase = FirebaseDatabase.getInstance().
                                            getReference("Users/Drivers").child(driverUID);
                                    mDriverDatabase.setValue(true);

                                    //We don't have to write code to move driver from this activity
                                    // to DriverMapActivity.class. Because onAuthStateChanged() method
                                    //always listening for Auth state change. So if user registration
                                    // or login is successful than code inside onAuthStateChanged()
                                    //method will automatically Execute.
                                }
                            }
                        });
            }
        });

        btnDriverLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String driverEmail = edtDriverEmail.getText().toString();
                final String driverPassword = edtDriverPassword.getText().toString();
                //Sign in existing driver with email & password.
                mDriverAuth.signInWithEmailAndPassword(driverEmail, driverPassword).
                        addOnCompleteListener(DriverLogInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    //If task is not successful
                                    Toast.makeText(DriverLogInActivity.this,"Sign In Error....",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    //First we check if our driver id is available in Users/Drivers
                                    String driverUID = mDriverAuth.getCurrentUser().getUid();
                                    final DatabaseReference mDriverDatabase = FirebaseDatabase.getInstance().
                                            getReference("Users/Drivers").child(driverUID);
                                    mDriverDatabase.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                Toast.makeText(DriverLogInActivity.this,"Driver has already userId",
                                                        Toast.LENGTH_LONG).show();
                                            }else {
                                                mDriverDatabase.setValue(true);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                                //We don't have to write code to move driver from this activity
                                // to DriverMapActivity.class. Because onAuthStateChanged() method
                                //always listening for Auth state change. So if user registration
                                // or login is successful than code inside onAuthStateChanged()
                                //method will automatically Execute.
                            }
                        });

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDriverAuth.removeAuthStateListener(mDriverAuthListener);  //VERY IMPORTANT
    }
}
