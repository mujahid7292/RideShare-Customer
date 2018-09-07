package com.sand_corporation.www.uthao;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.appsee.Appsee;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sand_corporation.www.uthao.DatePicker.SetDate;
import com.sand_corporation.www.uthao.IntroSlider.SliderActivity;
import com.sand_corporation.www.uthao.Permissions.Permissions;
import com.sand_corporation.www.uthao.SQDatabase.DbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import io.fabric.sdk.android.Fabric;
import retrofit2.http.Url;

public class Welcome extends AppCompatActivity{

    private static final String DEFAULT = "N/A";
    private static final int GPS_PERMISSION_REQUEST = 101;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 102;
    private static final String TAG = "Welcome";
    private static final int PHONE_STATE_PERMISSION = 103;
    private String customerUID, strCustomerName, strCustomerMobileNumber,strCustomerCountryCode,
            registrationNumberWithCountryCode,strCustomerEmail, strCustomerPassword,
            strCustomerGender,strRegisterBirthDay,
            strSignInCountryCode, strSignInPhone,signInNumberWithCountryCode;
    private MaterialEditText edtRegisterGender,
            edtRegistrationPhoneNumber, edtRegistrationCountryCode, edtSignInCountryCode,
            edtSignInPhoneNumber,edtFaceBookMissedGender;
    private EditText edtSignInEnterOTP, edtRegistrationEnterOTP,edtRegisterEmail,
            edtRegisterName, edtRegisterBirthDay, edtFaceBookMissedBirthDay, edtFaceBookMissedName,
            edtFaceBookMissedEmail;
    private Button btnCustomerLogInWelcome, btnCustomerRegistrationWelcome,
            btnVerifyRegistrationPhoneNumber, btnVerifySignInPhoneNumber,btnVerifySignInOTPNumber,
            btnVerifyRegistrationOTPNumber, btnBackToWelcomePageFromSignIn,btnChooseEmailPasswordSignIn,
            btnCompleteRegistrationUsingEmailPassword,btnBackToAllAccountPage,
            btnSignInUsingPhoneNumber,btnUpdateFaceBookMissedData, button_facebook_login;
    private RelativeLayout welcome_layout;
    private LinearLayout customerRegistrationOTPLayOut, customerRegistrationPhoneVerificationLayout
            ,customerSignInPhoneVerificationLayOut,customerSignInOTPLayOut,layoutFaceBookMissedName,
            layoutFaceBookMissedEmail, layoutFaceBookMissedGender, layoutFaceBookMissedBirthDay,
            customerOtherAccountLinkLayOut;
    private ScrollView  customerRegisterUsingEmailPasswordLayOut,
            faceBookMissedDataLayOut;
    private FirebaseAuth mCustomerAuth; // Here we are declaring a variable type firebase authentication
    private FirebaseAuth.AuthStateListener mCustomerAuthListener;
    //By FirebaseAuth.AuthStateListener we will listen for fireBase Authentication.
    private FirebaseAnalytics mFirebaseAnalytics;
    private PhoneAuthProvider.ForceResendingToken mRegisterResendToken;
    private PhoneAuthProvider.ForceResendingToken mSignInResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mRegisterCallbacks;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mSignInCallbacks;
    private AuthCredential credential;
    //This below two variable for fire store
    private FirebaseFirestore mFireStoreCustomerBasicInfoDB;
    private DatabaseReference mCustomerBasicInfoDataBaseRef;
    private CallbackManager mCallbackManager; //This one is for FaceBook LogIN
    private Handler.Callback callback;
//    private GoogleSignInClient mGoogleSignInClient;//This one is for Google LogIN
    private static final int RC_SIGN_IN = 9001;//This one is for Google Lo
    private SignInButton google_sign_in_button;//This one is for Google Lo
    private String mVerificationStage = "null";
    private String userRegistrationVerificationId;
    private String userSignInVerificationId;
    private ProgressBar login_progress;
    private Trace myTrace;
    //Get this devices phone number
    private SpotsDialog waitingDialogSeverConnection, waitingDialogFaceBookSeverConnection,
            waitingDialogForPhoneVerification;
    private String receivedOTP;
    private Permissions permissions; //This object for runtime permission
    private Bundle faceBookDataBundle;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RadioGroup radioCustomerRegisterGender, radioFaceBookMissedGender;
    private RadioButton radioCustomerRegisterMale, radioCustomerRegisterFemale,
            radioFaceBookMissedMale, radioFaceBookMissedFemale;
    private DatePicker datePickerEmailPasswordRegistration,
            datePickerFaceBookMissedBirthDay;
    private ImageView ic_back_sign, ic_back_sign2, ic_back_sign3, ic_back_sign4,
            ic_back_sign6;

    @Override
    protected void onStart() {
        super.onStart();
        //If google play service is not available in the user device, our app
        //will not work. So in below method we will check if user device has google
        //play service or not. If don't have than we will redirect our user to
        //download page
        isGooglePlayServicesAvailable();
        //Below 'myTrace' code will check in app performance. We can see
        //our every user performance in firebase console.
        myTrace = FirebasePerformance.getInstance().newTrace("test_trace");
        myTrace.start();
        FirebaseApp.initializeApp(this);
        FirebaseCrash.getInstance(FirebaseApp.initializeApp(this));
        FirebaseCrash.log("Welcome onStart() called");
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //First we initialize firebase authentication variable
        //This below variable will check if our user is registered or not
        //only registered user will get access to our server / database
        mCustomerAuth = FirebaseAuth.getInstance();

        //Every time, On the start of our app we will check whether our user is new
        //user or existing user. This below code will check that. If user is existing
        //user than we will redirect our user to Home.class (By passing registration
        //page)
        if (mCustomerAuth != null) {
            if (mCustomerAuthListener != null) {
                mCustomerAuth.addAuthStateListener(mCustomerAuthListener);  //VERY IMPORTANT
                checkWhetherUserIsRegisteredOrNot();
            }
        }
        FirebaseCrash.log("Welcome: onStart() finished");

        //This below code will check whether user opened our app for the first time
        //Than it will show an intro slider page. In intro slider we will give short
        //promo of our product
        String registration_status = preferences.getString("Registration_Status",DEFAULT);
        String sliding_Status = preferences.getString("Sliding_Status",DEFAULT);
        if (registration_status.equals(DEFAULT) && sliding_Status.equals(DEFAULT)){
            FirebaseCrash.log("Welcome: User opened app for the first time");
            Intent intent = new Intent(Welcome.this, SliderActivity.class);
            startActivity(intent);
        }

        //Below we are initializing all the view from welcome activity
        welcome_layout = findViewById(R.id.welcome_layout);
        customerSignInPhoneVerificationLayOut = findViewById(R.id.customerSignInPhoneVerificationLayOut);
        customerSignInOTPLayOut = findViewById(R.id.customerSignInOTPLayOut);
        customerRegistrationPhoneVerificationLayout = findViewById(R.id.customerRegistrationPhoneVerificationLayout);
        customerRegistrationOTPLayOut = findViewById(R.id.customerRegistrationOTPLayOut);

        layoutFaceBookMissedName = findViewById(R.id.layoutFaceBookMissedName);
        layoutFaceBookMissedEmail = findViewById(R.id.layoutFaceBookMissedEmail);
        layoutFaceBookMissedGender = findViewById(R.id.layoutFaceBookMissedGender);
        layoutFaceBookMissedBirthDay = findViewById(R.id.layoutFaceBookMissedBirthDay);

        customerOtherAccountLinkLayOut =findViewById(R.id.customerOtherAccountLinkLayOut);
        customerRegisterUsingEmailPasswordLayOut = findViewById(R.id.customerRegisterUsingEmailPasswordLayOut);
        faceBookMissedDataLayOut = findViewById(R.id.faceBookMissedDataLayOut);

        //Here we will show only welcome_layout to our user and hide
        //all of the layout.
        welcome_layout.setVisibility(View.VISIBLE);
        FirebaseCrash.log("Welcome: layOut-welcome_layout");
        customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
        customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
        customerRegistrationOTPLayOut.setVisibility(View.GONE);
        customerSignInOTPLayOut.setVisibility(View.GONE);
        customerOtherAccountLinkLayOut.setVisibility(View.GONE);
        customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
        faceBookMissedDataLayOut.setVisibility(View.GONE);
        mVerificationStage = "welcome_layout";




    }

    @Override
    protected void onResume() {
        FirebaseCrash.log("Welcome onResume().called");
        //Register incoming sms auto detector. This broadcast manager will listen
        //for new message in user device.
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());
        Fabric.with(this);
        Appsee.start(getString(R.string.com_appsee_apikey));
        FirebaseCrash.log("Welcome onCreate.called");
        FirebaseCrash.log("Welcome CalligraphyConfig .build() completed");
        //Below initialize Fire Store
        mFireStoreCustomerBasicInfoDB = FirebaseFirestore.getInstance();
        // Check whether we're recreating a previously destroyed instance
        //View get destroyed when user get out of app for some reason.Exmp:
        //to see if one time password has come to msg inbox.
        if (savedInstanceState != null) {
            FirebaseCrash.log("Welcome: customer resarted from saved instance state");
            // Restore value of members from saved state
            strCustomerMobileNumber = savedInstanceState.getString("strCustomerMobileNumber");
            strCustomerCountryCode = savedInstanceState.getString("strCustomerCountryCode");
            strCustomerEmail = savedInstanceState.getString("strCustomerEmail");
            mVerificationStage = savedInstanceState.getString("mVerificationStage");
            strCustomerGender = savedInstanceState.getString("strCustomerGender");
            strSignInPhone = savedInstanceState.getString("strSignInPhone");
            if (mVerificationStage != null && mVerificationStage.equals("welcome_layout")) {
                //Bring Layout where customer leaved.
                welcome_layout.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-welcome_layout");
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);
            } else if (mVerificationStage != null && mVerificationStage.equals("customerRegistrationPhoneVerificationLayout")){
                //Bring Layout where customer leaved.
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-customerRegistrationPhoneVerificationLayout");
                edtRegistrationPhoneNumber.setText(strCustomerMobileNumber == null ? "N/A":strCustomerMobileNumber);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);

            } else if (mVerificationStage != null && mVerificationStage.equals("customerSignInPhoneVerificationLayOut")){
                //Bring Layout where customer leaved.
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-customerSignInPhoneVerificationLayOut");
                edtSignInPhoneNumber.setText(strSignInPhone == null ? "N/A":strSignInPhone);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);

            } else if (mVerificationStage != null && mVerificationStage.equals("customerRegistrationOTPLayOut")){
                //Bring Layout where customer leaved.
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-customerRegistrationOTPLayOut");
                edtRegistrationEnterOTP.setText(receivedOTP == null ? "":receivedOTP);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);

            } else if (mVerificationStage != null && mVerificationStage.equals("customerSignInOTPLayOut")){
                //Bring Layout where customer leaved.
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-customerSignInOTPLayOut");
                edtSignInEnterOTP.setText(receivedOTP == null ? "":receivedOTP);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);

            } else if (mVerificationStage != null && mVerificationStage.equals("customerOtherAccountLinkLayOut")){
                //Bring Layout where customer leaved.
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-customerOtherAccountLinkLayOut");
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);

            } else if (mVerificationStage != null && mVerificationStage.equals("customerRegisterUsingEmailPasswordLayOut")){
                //Bring Layout where customer leaved.
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-customerRegisterUsingEmailPasswordLayOut");
                edtRegisterName.setText(strCustomerMobileNumber == null ? "N/A":strCustomerMobileNumber);
                edtRegisterEmail.setText(strCustomerEmail == null ? "N/A":strCustomerEmail);
                edtRegisterBirthDay.setText(strRegisterBirthDay == null ? "N/A":strRegisterBirthDay);
                faceBookMissedDataLayOut.setVisibility(View.GONE);

            } else if (mVerificationStage != null && mVerificationStage.equals("faceBookMissedDataLayOut")){
                //Bring Layout where customer leaved.
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-faceBookMissedDataLayOut");

            }
        } else {
            // Probably initialize members with default values for a new instance
            mVerificationStage = "false";
        }

        setContentView(R.layout.activity_welcome);
        FirebaseCrash.log("Welcome: onCreate() layout created");
        checkHowManyPermissionAllowed(Welcome.this);
        //Ask for android run time permission, by creating object from Permissions.class
        permissions = new Permissions(Welcome.this);
        permissions.requestAllPermission(Welcome.this);


        faceBookDataBundle = new Bundle();
        preferences = getSharedPreferences("Customer_Basic_Info",
                Context.MODE_PRIVATE);
        //Context.MODE_PRIVATE This means MyData will be accessible by only this app
        editor = preferences.edit();

//        datePickerEmailPasswordRegistration = findViewById(R.id.datePickerEmailPasswordRegistration);
//        datePickerFaceBookMissedBirthDay = findViewById(R.id.datePickerFaceBookMissedBirthDay);


        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterName = findViewById(R.id.edtRegisterName);
        edtRegisterGender = findViewById(R.id.edtRegisterGender);
        edtRegisterBirthDay = findViewById(R.id.edtRegisterBirthDay);
        SetDate registerBirthDayDate = new SetDate(edtRegisterBirthDay, this);



        edtRegistrationPhoneNumber = findViewById(R.id.edtRegistrationPhoneNumber);
        edtRegistrationCountryCode = findViewById(R.id.edtRegistrationCountryCode);
        edtSignInCountryCode = findViewById(R.id.edtSignInCountryCode);
        edtSignInPhoneNumber = findViewById(R.id.edtSignInPhoneNumber);
        edtSignInEnterOTP = findViewById(R.id.edtSignInEnterOTP);
        edtRegistrationEnterOTP = findViewById(R.id.edtRegistrationEnterOTP);

        edtFaceBookMissedName = findViewById(R.id.edtFaceBookMissedName);
        edtFaceBookMissedEmail = findViewById(R.id.edtFaceBookMissedEmail);
        edtFaceBookMissedGender = findViewById(R.id.edtFaceBookMissedGender);
        edtFaceBookMissedBirthDay = findViewById(R.id.edtFaceBookMissedBirthDay);
        SetDate faceBookMissedBirthDayDate = new SetDate(edtFaceBookMissedBirthDay, this);


        waitingDialogSeverConnection = new SpotsDialog(Welcome.this,
                "Connecting To Server");
        waitingDialogForPhoneVerification = new SpotsDialog(Welcome.this,
                "Auto Verification Is Going On");
        waitingDialogFaceBookSeverConnection = new SpotsDialog(Welcome.this,
                "Verifying User With FaceBook Server");

        btnVerifyRegistrationPhoneNumber = findViewById(R.id.btnVerifyRegistrationPhoneNumber);
        btnVerifySignInPhoneNumber = findViewById(R.id.btnVerifySignInPhoneNumber);
        btnVerifySignInOTPNumber = findViewById(R.id.btnVerifySignInOTPNumber);
        btnVerifyRegistrationOTPNumber =findViewById(R.id.btnVerifyRegistrationOTPNumber);
        //This button is for sending received OTP number to fire base Server
        btnVerifyRegistrationPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnVerifyRegistrationPhoneNumber.onClick");
                if (checkGPSConnectionPermission() && checkAndRequestSMSPermissions()){
                    startCustomerRegistrationProcess();
                } else {
                    Toast.makeText(Welcome.this,"You must provide all the permission" +
                            " before proceed",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnVerifyRegistrationOTPNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnVerifyRegistrationOTPNumber.onClick");
                FirebaseCrash.log("Welcome: OTP entered manually");
                String sentCode = edtRegistrationEnterOTP.getText().toString();
                if (!sentCode.equals("")){
                    FirebaseCrash.log("Welcome: edtRegistrationEnterOTP: " + sentCode);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential
                            (userRegistrationVerificationId, sentCode);
                    // [END verify_with_code]
                    signInWithPhoneAuthCredentialForNewUser(credential);
                } else {
                    Toast.makeText(Welcome.this,"Please enter verification code",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        btnVerifySignInPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnVerifySignInPhoneNumber.onClick");
                if (checkGPSConnectionPermission() && checkAndRequestSMSPermissions()){
                    startCustomerLogInProcess();
                } else {
                    Toast.makeText(Welcome.this,"You must provide all the permission" +
                            " before proceed",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnVerifySignInOTPNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnVerifySignInOTPNumber.onClick");
                FirebaseCrash.log("Welcome: OTP entered manually");
                String sentCode = edtSignInEnterOTP.getText().toString();
                if (!sentCode.equals("")){
                    FirebaseCrash.log("Welcome: edtSignInEnterOTP: " + sentCode);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential
                            (userSignInVerificationId, sentCode);
                    // [END verify_with_code]
                    signInWithPhoneAuthCredentialForExistingUser(credential);
                } else {
                    Toast.makeText(Welcome.this,"Please enter verification code",
                            Toast.LENGTH_LONG).show();
                }



            }
        });

        //This call back is for register using PhoneAuth Verification.
        mRegisterCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                waitingDialogForPhoneVerification.dismiss();
                FirebaseCrash.log("Welcome: NewUser Registration: onVerificationCompleted().called");
                Toast.makeText(Welcome.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onVerificationCompleted:" + phoneAuthCredential);

                signInWithPhoneAuthCredentialForNewUser(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(Welcome.this, "Verification Failed", Toast.LENGTH_SHORT).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    FirebaseCrash.log("Welcome: SMS quota finished");
                    Toast.makeText(Welcome.this, "InValid Phone Number", Toast.LENGTH_SHORT).show();
                }
                waitingDialogForPhoneVerification.dismiss();
                btnCustomerRegistrationWelcome.setEnabled(true);

                // Show a message and update the UI
                // ...

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                FirebaseCrash.log("Welcome: Registration onCodeSent.called");
                Log.i(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(Welcome.this, "Verification code has been send on your number", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                userRegistrationVerificationId = verificationId;
                mRegisterResendToken = forceResendingToken;
                //Now change the layout
                //Bring 'enterOTPLayout
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-customerRegistrationOTPLayOut");
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);
                mVerificationStage = "customerRegistrationOTPLayOut";

            }
        };

        //This call back is for sign in using PhoneAuth Verification.
        mSignInCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(Welcome.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                signInWithPhoneAuthCredentialForExistingUser(phoneAuthCredential);
                FirebaseCrash.log("Welcome: ExistingUser sign in: onVerificationCompleted().called");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(Welcome.this, "Verification Failed", Toast.LENGTH_SHORT).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Toast.makeText(Welcome.this, "InValid Phone Number", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    FirebaseCrash.log("Welcome: SMS quota finished");
                }
                waitingDialogForPhoneVerification.dismiss();
                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                waitingDialogForPhoneVerification.dismiss();
                Log.i(TAG, "onCodeSent:" + userSignInVerificationId);
                Toast.makeText(Welcome.this, "Verification code has been send on your number", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                userSignInVerificationId = verificationId;
                mSignInResendToken = forceResendingToken;
                //Now change the layout
                //Bring 'enterOTPLayout
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-customerSignInOTPLayOut");
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);
                mVerificationStage = "customerSignInOTPLayOut";
            }
        };


        mCustomerAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //First find out that if this app has any customer previously registered.
                FirebaseUser mCustomer = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseCrash.log("Welcome: CustomerUid: " + (mCustomer != null ? mCustomer.getUid() : null));
            }
        };

        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        //************************************With Facebook Button*********************************
        //******************************************Start******************************************
//        LoginButton loginButton = findViewById(R.id.button_facebook_login);
//        loginButton.setReadPermissions("email", "public_profile","user_birthday");
//        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.i("Check", "facebook:onSuccess:" + loginResult);
//                FirebaseCrash.log("Welcome: facebook:onSuccess:" + loginResult.toString());
//                attachFacebookLogin(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.i("Check", "facebook:onCancel");
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.i("Check", "facebook:onError"+ error.toString());
//                FirebaseCrash.log("Welcome: facebook:onError:" + error.toString());
//            }
//        });
        //******************************************End*********************************************

        //*********************************Facebook Custom Button***********************************
        //*****************************************Start********************************************
        button_facebook_login = findViewById(R.id.button_facebook_login);
        button_facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Welcome.this,
                        Arrays.asList("email", "public_profile","user_birthday"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i("Check", "facebook:onSuccess:" + loginResult.getAccessToken() + "\n" +
                        loginResult.getRecentlyDeniedPermissions().toString() + "\n" +
                        loginResult.getRecentlyGrantedPermissions().toString());
                        FirebaseCrash.log("Welcome: facebook:onSuccess:" + loginResult.toString());
                        attachFacebookLogin(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.i("Check", "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.i("Check", "facebook:onError"+ error.toString() + "\n"
                        +error.getLocalizedMessage() + "\n"
                                +error.getMessage());
                        FirebaseCrash.log("Welcome: facebook:onError:" + error.toString());
                    }
                });
            }
        });

        //************************************************End***************************************
        // [END initialize_fblogin]

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.uthao_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        // Build a GoogleSignInClient with the options specified by gso.
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        google_sign_in_button = findViewById(R.id.google_sign_in_button);
//        google_sign_in_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
////                startActivityForResult(signInIntent, RC_SIGN_IN);
//            }
//        });

        //Sign in with Email and password
        btnChooseEmailPasswordSignIn = findViewById(R.id.btnEmailPasswordSignIn);
        btnChooseEmailPasswordSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnChooseEmailPasswordSignIn.onClick");
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-customerRegisterUsingEmailPasswordLayOut");
                faceBookMissedDataLayOut.setVisibility(View.GONE);
                mVerificationStage = "customerRegisterUsingEmailPasswordLayOut";
            }
        });

        //OnClick of this below button we will attach Email and password with phone Auth.
        btnCompleteRegistrationUsingEmailPassword = findViewById(R.id.btnRegistrationUsingEmailPassword);
        btnCompleteRegistrationUsingEmailPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnCompleteRegistrationUsingEmailPassword.onClick");
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-customerRegisterUsingEmailPasswordLayOut");
                faceBookMissedDataLayOut.setVisibility(View.GONE);
                mVerificationStage = "customerRegisterUsingEmailPasswordLayOut";
                attachEmailPasswordSignIn();
            }
        });

        btnCustomerRegistrationWelcome = (Button) findViewById(R.id.btnCustomerRegistration);
        btnCustomerLogInWelcome = (Button) findViewById(R.id.btnCustomerLogInWelcome);

        btnCustomerRegistrationWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnCustomerRegistrationWelcome.onClick");
                permissions.requestAllPermission(Welcome.this);
                if (checkGPSConnectionPermission() && checkAndRequestSMSPermissions()){
                    welcome_layout.setVisibility(View.GONE);
                    customerRegistrationPhoneVerificationLayout.setVisibility(View.VISIBLE);
                    FirebaseCrash.log("Welcome: layOut-customerRegistrationPhoneVerificationLayout");
                    customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                    customerRegistrationOTPLayOut.setVisibility(View.GONE);
                    customerSignInOTPLayOut.setVisibility(View.GONE);
                    customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                    customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                    faceBookMissedDataLayOut.setVisibility(View.GONE);
                    mVerificationStage = "customerRegistrationPhoneVerificationLayout";

                } else {
                    Toast.makeText(Welcome.this,"You must provide all the permission" +
                            " before proceed",Toast.LENGTH_LONG).show();
                }

            }
        });




        btnCustomerLogInWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnCustomerLogInWelcome.onClick");
                //Show Customer Log In Lay Out
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.VISIBLE);
                FirebaseCrash.log("Welcome: layOut-customerSignInPhoneVerificationLayOut");
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);
                mVerificationStage = "customerSignInPhoneVerificationLayOut";

                permissions.requestAllPermission(Welcome.this);


            }
        });

        btnUpdateFaceBookMissedData = findViewById(R.id.btnUpdateFaceBookMissedData);
        btnUpdateFaceBookMissedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnUpdateFaceBookMissedData.onClick");
                if (layoutFaceBookMissedGender.getVisibility() == View.VISIBLE){
                    String gender = getCustomerGender(radioFaceBookMissedGender);
                    FirebaseCrash.log("Welcome: getCustomerGender: " + gender);
                    if (gender != null){
                        editor.putString("Customer_Gender", gender);
                        faceBookDataBundle.putString("Customer_Gender",gender);
                    } else {
                        Toast.makeText(Welcome.this,"Please provide Gender",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                if (layoutFaceBookMissedBirthDay.getVisibility() == View.VISIBLE){
                    String birthday = edtFaceBookMissedBirthDay.getText().toString();
                    FirebaseCrash.log("Welcome: edtFaceBookMissedBirthDay: " + birthday);
                    if (!birthday.equals("")){
                        editor.putString("Customer_BirthDate", birthday);
                        faceBookDataBundle.putString("Customer_BirthDate",birthday );
                    } else {
                        Toast.makeText(Welcome.this,"Please provide BirthDay",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                if (layoutFaceBookMissedName.getVisibility() == View.VISIBLE){
                    String name = edtFaceBookMissedName.getText().toString();
                    FirebaseCrash.log("Welcome: edtFaceBookMissedName: " + name);
                    if (!name.equals("")){
                        editor.putString("Customer_Name", name);
                        faceBookDataBundle.putString("Customer_Name",name );
                    } else {
                        Toast.makeText(Welcome.this,"Please provide Name",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                if (layoutFaceBookMissedEmail.getVisibility() == View.VISIBLE){
                    String email = edtFaceBookMissedEmail.getText().toString();
                    FirebaseCrash.log("Welcome: edtFaceBookMissedEmail: " + email);
                    if (!email.equals("") && isEmailValid(email)){
                        editor.putString("Customer_Email", email);
                        faceBookDataBundle.putString("Customer_Email",email );
                    } else {
                        Toast.makeText(Welcome.this,"Please provide email",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                editor.putString("Registration_Status","OK");
                editor.commit();
                getDeviceConfiguration();
                Intent intent = new Intent(Welcome.this,Home.class);
                intent.putExtras(faceBookDataBundle);
                startActivity(intent);
                finish();
            }
        });

        btnBackToAllAccountPage = findViewById(R.id.btnBackToAllAccountPage);
        btnBackToAllAccountPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.VISIBLE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);
            }
        });

        radioCustomerRegisterGender = findViewById(R.id.radioCustomerRegisterGender);
        radioCustomerRegisterMale =findViewById(R.id.radioCustomerRegisterMale);
        radioCustomerRegisterFemale =findViewById(R.id.radioCustomerRegisterFemale);

        radioFaceBookMissedGender = findViewById(R.id.radioFaceBookMissedGender);
        radioFaceBookMissedMale = findViewById(R.id.radioFaceBookMissedMale);
        radioFaceBookMissedFemale = findViewById(R.id.radioFaceBookMissedFemale);
        initializeAllTheBackSign();
    }

    private void initializeAllTheBackSign() {

        //Customer Sign in phone Verification Layout
        ic_back_sign = findViewById(R.id.ic_back_sign);
        //Customer Sign in OTP Layout
        ic_back_sign2 = findViewById(R.id.ic_back_sign2);
        //Customer Registration Phone Verification Layout
        ic_back_sign3 = findViewById(R.id.ic_back_sign3);
        //Customer Registration OTP Layout
        ic_back_sign4 = findViewById(R.id.ic_back_sign4);
        //Customer Email Password Registration Layout
        ic_back_sign6 = findViewById(R.id.ic_back_sign6);

        ic_back_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Customer Sign in phone Verification Layout
                welcome_layout.setVisibility(View.VISIBLE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);
            }
        });
        ic_back_sign2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Customer Sign in OTP Layout
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.VISIBLE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);
            }
        });
        ic_back_sign3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Customer Registration Phone Verification Layout
                welcome_layout.setVisibility(View.VISIBLE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);
            }
        });
        ic_back_sign4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Customer Registration OTP Layout
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.VISIBLE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);
            }
        });
        ic_back_sign6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Customer Email Password Registration Layout
                welcome_layout.setVisibility(View.GONE);
                customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                customerRegistrationOTPLayOut.setVisibility(View.GONE);
                customerSignInOTPLayOut.setVisibility(View.GONE);
                customerOtherAccountLinkLayOut.setVisibility(View.VISIBLE);
                customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                faceBookMissedDataLayOut.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (customerSignInPhoneVerificationLayOut.getVisibility() == View.VISIBLE){
            welcome_layout.setVisibility(View.VISIBLE);
            customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
            customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
            customerRegistrationOTPLayOut.setVisibility(View.GONE);
            customerSignInOTPLayOut.setVisibility(View.GONE);
            customerOtherAccountLinkLayOut.setVisibility(View.GONE);
            customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
            faceBookMissedDataLayOut.setVisibility(View.GONE);
        } else if (customerSignInOTPLayOut.getVisibility() == View.VISIBLE){
            welcome_layout.setVisibility(View.GONE);
            customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
            customerSignInPhoneVerificationLayOut.setVisibility(View.VISIBLE);
            customerRegistrationOTPLayOut.setVisibility(View.GONE);
            customerSignInOTPLayOut.setVisibility(View.GONE);
            customerOtherAccountLinkLayOut.setVisibility(View.GONE);
            customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
            faceBookMissedDataLayOut.setVisibility(View.GONE);
        } else if(customerRegistrationPhoneVerificationLayout.getVisibility() == View.VISIBLE){
            welcome_layout.setVisibility(View.VISIBLE);
            customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
            customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
            customerRegistrationOTPLayOut.setVisibility(View.GONE);
            customerSignInOTPLayOut.setVisibility(View.GONE);
            customerOtherAccountLinkLayOut.setVisibility(View.GONE);
            customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
            faceBookMissedDataLayOut.setVisibility(View.GONE);
        } else if (customerRegistrationOTPLayOut.getVisibility() == View.VISIBLE){
            welcome_layout.setVisibility(View.GONE);
            customerRegistrationPhoneVerificationLayout.setVisibility(View.VISIBLE);
            customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
            customerRegistrationOTPLayOut.setVisibility(View.GONE);
            customerSignInOTPLayOut.setVisibility(View.GONE);
            customerOtherAccountLinkLayOut.setVisibility(View.GONE);
            customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
            faceBookMissedDataLayOut.setVisibility(View.GONE);
        } else if (customerRegisterUsingEmailPasswordLayOut.getVisibility() == View.VISIBLE){
            welcome_layout.setVisibility(View.GONE);
            customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
            customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
            customerRegistrationOTPLayOut.setVisibility(View.GONE);
            customerSignInOTPLayOut.setVisibility(View.GONE);
            customerOtherAccountLinkLayOut.setVisibility(View.VISIBLE);
            customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
            faceBookMissedDataLayOut.setVisibility(View.GONE);
        } else if (faceBookMissedDataLayOut.getVisibility() == View.VISIBLE){
            Toast.makeText(this, "Do not press back button", Toast.LENGTH_SHORT).show();
        } else if (customerOtherAccountLinkLayOut.getVisibility() == View.VISIBLE){
            Toast.makeText(this, "Do not press back button", Toast.LENGTH_SHORT).show();
        }else {
            super.onBackPressed();
        }
    }

    private String getCustomerGender(RadioGroup radioGroup){
        FirebaseCrash.log("Welcome: getCustomerGender.called");
        int selectedRadioButton = radioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = findViewById(selectedRadioButton);
        //If customer selected gender than return the selected gender
        if (radioButton != null){
            if (radioButton.getText() != null){
                return radioButton.getText().toString();
            }
        }

        //Other wise return null
        return null;
    }

    private void startCustomerRegistrationProcess() {
        FirebaseCrash.log("Welcome: startCustomerRegistrationProcess.called");
        strCustomerMobileNumber = edtRegistrationPhoneNumber.getText().toString();
        FirebaseCrash.log("Welcome: edtRegistrationPhoneNumber: " + strCustomerMobileNumber);
        strCustomerCountryCode = edtRegistrationCountryCode.getText().toString();
        FirebaseCrash.log("Welcome: edtRegistrationCountryCode: " + strCustomerCountryCode);

        registrationNumberWithCountryCode = strCustomerCountryCode + strCustomerMobileNumber ;
        Log.i("Check","registrationNumberWithCountryCode: " + registrationNumberWithCountryCode);

        if (registrationNumberWithCountryCode.equals("")){
            Toast.makeText(Welcome.this,"Please enter your phone number",Toast.LENGTH_LONG).show();
            return;
        }else if (!isPhoneNumberValid(registrationNumberWithCountryCode)){
            Toast.makeText(Welcome.this,"Please insert phone number with Country Code",Toast.LENGTH_LONG).show();
            return;
        }

        //Disable register button until registration failed.
        btnCustomerRegistrationWelcome.setEnabled(false);
        //Register new user
        //Now we will send customer phone number to firebase database
        FirebaseCrash.log("Welcome: registrationNumberWithCountryCode: " +registrationNumberWithCountryCode);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                registrationNumberWithCountryCode,
                60,
                TimeUnit.SECONDS,
                Welcome.this,
                mRegisterCallbacks
        );
    }

    private void signInWithPhoneAuthCredentialForNewUser(PhoneAuthCredential phoneAuthCredential) {
        FirebaseCrash.log("Welcome: signInWithPhoneAuthCredentialForNewUser.called");
        //Show waiting animation.
        waitingDialogForPhoneVerification.show();
        mCustomerAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener
                (Welcome.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.i(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(Welcome.this, "Invalid Verification", Toast.LENGTH_SHORT).show();
                                waitingDialogForPhoneVerification.dismiss();
                                FirebaseCrash.log("Welcome: signInWithCredential:failed");
                            }
                        } else {
                            Log.i(TAG, "signInWithCredential:success");
                            FirebaseCrash.log("Welcome: signInWithCredential:success");
                            //Now we will verify whether our user is registered user or not.
                            //In some case user may forget that he has already done registration
                            // in uthoa. So we will check this for saving our database corruption.
                            customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference mExistingUserDbRef= FirebaseDatabase.getInstance().
                                    getReference("Users/Customers")
                                    .child(customerUID)
                                    .child("Customer_Basic_Info");
                            mExistingUserDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("Registration_Status")) {
                                        Log.i("Check","User is Registered");
                                        FirebaseCrash.log("Welcome: existingUser: UserHasPrevious Data");
                                        //Now we will fetch user basic info from firebase and save those to internal storage
                                        SharedPreferences preferences = getSharedPreferences("Customer_Basic_Info",
                                                Context.MODE_PRIVATE);
                                        //Context.MODE_PRIVATE This means MyData will be accessible by only this app
                                        final SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("Customer_Mobile",signInNumberWithCountryCode);
//                                        If sign in is successful, than we will retrieve our customer info from the
//                                        database.
//                                        *******************************REAL TIME DB*********************************
//                                        **********************************START*************************************
//                                        ****************************************************************************
                                        DatabaseReference retrieveCustomerBasicInfo = FirebaseDatabase.getInstance()
                                                .getReference("Users/Customers")
                                                .child(customerUID).child("Customer_Basic_Info");
                                        retrieveCustomerBasicInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                                                    FirebaseCrash.log("Welcome: existingUser: previous data fetching: success");
//                                                    Bundle bundle = new Bundle();
//                                                    bundle.putString("Details", "HasResponse");
                                                    Map<String, Object> map =(Map<String, Object>) dataSnapshot.getValue();
                                                    if (map.get("Customer_Name") != null){
                                                        String customerName = map.get("Customer_Name").toString();
                                                        editor.putString("Customer_Name",customerName);
//                                                        bundle.putString("Customer_Name", customerName);
                                                    }
                                                    if (map.get("Customer_Email") != null) {
                                                        String customerEmail = map.get("Customer_Email").toString();
                                                        editor.putString("Customer_Email",customerEmail);
//                                                        bundle.putString("Customer_Email", customerEmail);
                                                    }
                                                    if (map.get("Customer_Password") != null) {
                                                        String customerPassword = map.get("Customer_Password").toString();
                                                        editor.putString("Customer_Password",customerPassword);
//                                                        bundle.putString("Customer_Password", customerPassword);
                                                    }

                                                    //Now we will display customer's profile pic using glide.
                                                    if (map.get("Customer_Profile_Pic_Url") != null) {
                                                        String firebaseStorageCustomerPP = map.get("Customer_Profile_Pic_Url").toString();
                                                        editor.putString("Customer_Profile_Pic_Url",firebaseStorageCustomerPP);
//                                                        bundle.putString("Customer_Profile_Pic_Url", firebaseStorageCustomerPP);
                                                    }

                                                    if (map.get("Customer_FaceBook_PP") != null){
                                                        String Customer_FaceBook_PP = map.get("Customer_FaceBook_PP").toString();
                                                        editor.putString("Customer_FaceBook_PP",Customer_FaceBook_PP);
//                                                        bundle.putString("Customer_FaceBook_PP", Customer_FaceBook_PP);
                                                    }
                                                    editor.putString("Registration_Status","OK");
                                                    editor.apply();
                                                    waitingDialogForPhoneVerification.dismiss();
                                                    getDeviceConfiguration();
                                                    Intent intent = new Intent(Welcome.this,Home.class);
//                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                    finish();
                                                    //editor.apply(); we are writing those value to shared preference storage
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
//                                        *******************************REAL TIME DB*********************************
//                                        ************************************END*************************************
//                                        ****************************************************************************

                                    } else {
                                        Log.i("Check","User is UnRegistered");
                                        //Now we will save those basic info to internal storage
                                        SharedPreferences preferences = getSharedPreferences("Customer_Basic_Info",
                                                Context.MODE_PRIVATE);
                                        //Context.MODE_PRIVATE This means MyData will be accessible by only this app
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("Customer_Mobile",registrationNumberWithCountryCode);
                                        editor.apply();
                                        //If sign in is successful, than we will save our customer info to the database.
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("Customer_Mobile",registrationNumberWithCountryCode);

                                        customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        DatabaseReference mNewUserDbRef= FirebaseDatabase.getInstance().
                                                getReference("Users/Customers")
                                                .child(customerUID)
                                                .child("Customer_Basic_Info");
                                        mNewUserDbRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    FirebaseCrash.log("Welcome: realTimeDb: customerUID & Mobile : success");
                                                } else {
                                                    FirebaseCrash.log("Welcome: realTimeDb: customerUID & Mobile : failed");
                                                }
                                            }
                                        });
                                        //Now add same data to fire store
                                        Map<String, Object> fireStoreMap = new HashMap<>();
                                        fireStoreMap.put("Customer_Mobile", registrationNumberWithCountryCode);
                                        mFireStoreCustomerBasicInfoDB.
                                                collection("Users")
                                                .document("Customers")
                                                .collection(customerUID)
                                                .document("Customer_Basic_Info")
                                                .set(fireStoreMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            FirebaseCrash.log("Welcome: fireStore: customerUID & Mobile : success");
                                                        } else {
                                                            FirebaseCrash.log("Welcome: fireStore: customerUID & Mobile : failed");
                                                        }
                                                    }
                                                });

                                        waitingDialogForPhoneVerification.dismiss();


                                        welcome_layout.setVisibility(View.GONE);
                                        customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                                        customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                                        customerRegistrationOTPLayOut.setVisibility(View.GONE);
                                        customerSignInOTPLayOut.setVisibility(View.GONE);
                                        customerOtherAccountLinkLayOut.setVisibility(View.VISIBLE);
                                        FirebaseCrash.log("Welcome: layOut-customerOtherAccountLinkLayOut");
                                        customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                                        faceBookMissedDataLayOut.setVisibility(View.GONE);
                                        mVerificationStage = "customerOtherAccountLinkLayOut";
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                });
    }

    private void signInWithPhoneAuthCredentialForExistingUser(PhoneAuthCredential phoneAuthCredential) {
        //Show waiting animation.
        waitingDialogForPhoneVerification.show();
        mCustomerAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener
                (Welcome.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.i(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(Welcome.this, "Invalid Verification", Toast.LENGTH_SHORT).show();
                                waitingDialogForPhoneVerification.dismiss();
                                FirebaseCrash.log("Welcome: existingUser: signInWithCredential: failed");
                            }
                        } else {
                            Log.i(TAG, "signInWithCredential:success");
                            FirebaseCrash.log("Welcome: existingUser: signInWithCredential: success");
                            //Now we will verify whether our user is registered user or not.
                            //If sign in is successful, than we will save our customer info to the database.
                            customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference mExistingUserDbRef= FirebaseDatabase.getInstance().
                                    getReference("Users/Customers")
                                    .child(customerUID)
                                    .child("Customer_Basic_Info");
                            mExistingUserDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("Registration_Status")) {
                                        Log.i("Check","User is Registered");
                                          FirebaseCrash.log("Welcome: existingUser: UserHasPrevious Data");
                                        //Now we will fetch user basic info from firebase and save those to internal storage
                                        SharedPreferences preferences = getSharedPreferences("Customer_Basic_Info",
                                                Context.MODE_PRIVATE);
                                        //Context.MODE_PRIVATE This means MyData will be accessible by only this app
                                        final SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("Customer_Mobile",signInNumberWithCountryCode);
//                                        If sign in is successful, than we will retrieve our customer info from the
//                                        database.
//                                        *******************************REAL TIME DB*********************************
//                                        **********************************START*************************************
//                                        ****************************************************************************
                                        DatabaseReference retrieveCustomerBasicInfo = FirebaseDatabase.getInstance()
                                                .getReference("Users/Customers")
                                                .child(customerUID).child("Customer_Basic_Info");
                                        retrieveCustomerBasicInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                                                      FirebaseCrash.log("Welcome: existingUser: previous data fetching: success");
//                                                    Bundle bundle = new Bundle();
//                                                    bundle.putString("Details", "HasResponse");
                                                    Map<String, Object> map =(Map<String, Object>) dataSnapshot.getValue();
                                                    if (map.get("Customer_Name") != null){
                                                        String customerName = map.get("Customer_Name").toString();
                                                        editor.putString("Customer_Name",customerName);
//                                                        bundle.putString("Customer_Name", customerName);
                                                    }
                                                    if (map.get("Customer_Email") != null) {
                                                        String customerEmail = map.get("Customer_Email").toString();
                                                        editor.putString("Customer_Email",customerEmail);
//                                                        bundle.putString("Customer_Email", customerEmail);
                                                    }
                                                    if (map.get("Customer_Password") != null) {
                                                        String customerPassword = map.get("Customer_Password").toString();
                                                        editor.putString("Customer_Password",customerPassword);
//                                                        bundle.putString("Customer_Password", customerPassword);
                                                    }

                                                    //Now we will display customer's profile pic using glide.
                                                    if (map.get("Customer_Profile_Pic_Url") != null) {
                                                        String firebaseStorageCustomerPP = map.get("Customer_Profile_Pic_Url").toString();
                                                        editor.putString("Customer_Profile_Pic_Url",firebaseStorageCustomerPP);
//                                                        bundle.putString("Customer_Profile_Pic_Url", firebaseStorageCustomerPP);
                                                    }

                                                    if (map.get("Customer_FaceBook_PP") != null){
                                                        String Customer_FaceBook_PP = map.get("Customer_FaceBook_PP").toString();
                                                        editor.putString("Customer_FaceBook_PP",Customer_FaceBook_PP);
//                                                        bundle.putString("Customer_FaceBook_PP", Customer_FaceBook_PP);
                                                    }
                                                    editor.putString("Registration_Status","OK");
                                                    editor.apply();
                                                    waitingDialogForPhoneVerification.dismiss();
                                                    getDeviceConfiguration();
                                                    Intent intent = new Intent(Welcome.this,Home.class);
//                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                    finish();
                                                    //editor.apply(); we are writing those value to shared preference storage
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
//                                        *******************************REAL TIME DB*********************************
//                                        ************************************END*************************************
//                                        ****************************************************************************

                                    } else {
                                        Log.i("Check","User is UnRegistered");
                                          FirebaseCrash.log("Welcome: existingUser: previous data fetching: failed");
                                        //Show New User Registration Layout
                                        waitingDialogForPhoneVerification.dismiss();

                                        welcome_layout.setVisibility(View.GONE);
                                        customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                                        customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                                        customerRegistrationOTPLayOut.setVisibility(View.GONE);
                                        customerSignInOTPLayOut.setVisibility(View.GONE);
                                        customerOtherAccountLinkLayOut.setVisibility(View.VISIBLE);
                                        customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                                        faceBookMissedDataLayOut.setVisibility(View.GONE);
                                        mVerificationStage = "customerOtherAccountLinkLayOut";
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                            //****************************CLOUD FIRE STORE********************************
                            //**********************************START*************************************
                            //****************************************************************************
//                            Log.i(TAG, "signInWithCredential:success");
//                            //Now we will verify whether our user is registered user or not.
//                            //If sign in is successful, than we will save our customer info to the database.
//                            customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                            mFireStoreCustomerBasicInfoDB.collection("Users").document("Customers")
//                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    DocumentSnapshot documentSnapshot = task.getResult();
//                                    if (documentSnapshot.exists()){
//                                        String customerUIDFromServer = documentSnapshot.getString("Customer_UID");
//                                        if (customerUIDFromServer.equals(customerUID)){
//                                            //Our user is registered.
//                                            FirebaseCrash.log("Welcome: newly signed is user UID matched: success");
//                                            Log.i("Check","User is Registered\n"+
//                                            "customerUIDFromServer: " + customerUIDFromServer +"\n" +
//                                            "customerUID: " + customerUID);
//                                            //Now we will fetch user basic info from firebase and save those to internal storage
//                                            SharedPreferences preferences = getSharedPreferences("Customer_Basic_Info",
//                                                    Context.MODE_PRIVATE);
//                                            //Context.MODE_PRIVATE This means MyData will be accessible by only this app
//                                            final SharedPreferences.Editor editor = preferences.edit();
//                                            editor.putString("Customer_Mobile",signInNumberWithCountryCode);
//
//                                            mFireStoreCustomerBasicInfoDB.collection("Users").document("Customers").collection(customerUID)
//                                                    .document("Customer_Basic_Info").get().
//                                                    addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                            if (task.isSuccessful()){
//                                                                DocumentSnapshot documentSnapshot = task.getResult();
//                                                                if (documentSnapshot.exists()){
//                                                                    FirebaseCrash.log("Welcome: fetchedUser previously stored info: success");
////                                                                    Bundle bundle = new Bundle();
////                                                                    bundle.putString("Details", "HasResponse");
//                                                                    CustomerBasicInfo customer = documentSnapshot.toObject(CustomerBasicInfo.class);
//
//                                                                    if (customer.getCustomer_Name() != null){
//                                                                        String customerName = customer.getCustomer_Name();
//                                                                        editor.putString("Customer_Name",customerName);
////                                                                        bundle.putString("Customer_Name", customerName);
//                                                                    }
//                                                                    if (customer.getCustomer_Email() != null) {
//                                                                        String customerEmail = customer.getCustomer_Email();
//                                                                        editor.putString("Customer_Email",customerEmail);
////                                                                        bundle.putString("Customer_Email", customerEmail);
//                                                                    }
//
//                                                                    //Now we will display customer's profile pic using glide.
//                                                                    if (customer.getCustomer_Profile_Pic_Url() != null) {
//                                                                        String firebaseStorageCustomerPP = customer.getCustomer_Profile_Pic_Url();
//                                                                        editor.putString("Customer_Profile_Pic_Url",firebaseStorageCustomerPP);
////                                                                        bundle.putString("Customer_Profile_Pic_Url", firebaseStorageCustomerPP);
//                                                                    }
//
//                                                                    if (customer.getCustomer_FaceBook_PP() != null){
//                                                                        String Customer_FaceBook_PP = customer.getCustomer_FaceBook_PP();
//                                                                        editor.putString("Customer_FaceBook_PP",Customer_FaceBook_PP);
////                                                                        bundle.putString("Customer_FaceBook_PP", Customer_FaceBook_PP);
//                                                                    }
//                                                                    editor.putString("Registration_Status","OK");
//                                                                    editor.apply();
//                                                                    waitingDialogForPhoneVerification.dismiss();
//                                                                    Intent intent = new Intent(Welcome.this,Home.class);
////                                                                    intent.putExtras(bundle);
//                                                                    startActivity(intent);
//                                                                    finish();
//                                                                    //editor.apply(); we are writing those value to shared preference storage
//                                                                    Log.i("Check","Read Contact: ");
//                                                                }
//
//                                                            }
//                                                        }
//                                                    });
//
//                                        } else {
//                                            Log.i("Check","User is UnRegistered");
//                                            FirebaseCrash.log("Welcome: signing in user not found in DB");
//                                            //Show New User Registration Layout
//                                            waitingDialogForPhoneVerification.dismiss();
//
//                                            welcome_layout.setVisibility(View.GONE);
//                                            customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
//                                            customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
//                                            customerRegistrationOTPLayOut.setVisibility(View.GONE);
//                                            customerSignInOTPLayOut.setVisibility(View.GONE);
//                                            customerOtherAccountLinkLayOut.setVisibility(View.VISIBLE);
//                                            FirebaseCrash.log("Welcome: layOut-customerOtherAccountLinkLayOut");
//                                            customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
//                                            faceBookMissedDataLayOut.setVisibility(View.GONE);
//                                            mVerificationStage = "customerOtherAccountLinkLayOut";
//                                        }
//                                    }
//                                }
//                            });

                            //****************************CLOUD FIRE STORE********************************
                            //**********************************END***************************************
                            //****************************************************************************











                        }
                    }
                });
    }

    private void attachGoogleSignIn(GoogleSignInAccount account) {
        credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mCustomerAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(Welcome.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            user.sendEmailVerification();
                            Log.d(TAG, "linkWithCredential:success\nUser: " + user);
                            getDeviceConfiguration();
                            Toast.makeText(Welcome.this, "Verification Done", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Welcome.this, Home.class));
                            finish();
                            // ...
                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(Welcome.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void attachFacebookLogin(final AccessToken accessToken) {
        FirebaseCrash.log("Welcome: attachFacebookLogin.called");
        waitingDialogFaceBookSeverConnection.show();
        credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mCustomerAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(Welcome.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            user.sendEmailVerification();
                            Log.d(TAG, "linkWithCredential:success\nUser: " + user);
                            //Save Profile pic, Name, Email to fireBase and SQLite
                            getAndSaveInfoFromCustomerFaceBookPublicProfile(accessToken);
                            waitingDialogFaceBookSeverConnection.dismiss();
                            Toast.makeText(Welcome.this, "Verification Done", Toast.LENGTH_SHORT).show();
                            FirebaseCrash.log("Welcome: facebook linked with phoneAuth");
                            // ...
                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            FirebaseCrash.log("Welcome: facebook linked with phoneAuth : unsuccessful" +task.toString());
                            Toast.makeText(Welcome.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            waitingDialogFaceBookSeverConnection.dismiss();

                        }
                    }
                });
    }

    private void getAndSaveInfoFromCustomerFaceBookPublicProfile(AccessToken accessToken) {

        //Use FaceBook Graph Api to Access gender and age range of customer
        //*************************Graph Api Start*************************
        //Now we will save those basic info to internal storage
        editor.putString("Customer_Mobile",registrationNumberWithCountryCode);




        if(AccessToken.getCurrentAccessToken()!=null) {
            System.out.println(AccessToken.getCurrentAccessToken().getToken());
            Log.i("Check", "AccessToken: " +AccessToken.getCurrentAccessToken().getToken() );

            FirebaseCrash.log("Welcome: facebook graphApi.called");
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, final GraphResponse response) {

                            if (object != null && response != null){
                                try {
                                    FirebaseCrash.log("Welcome:got response from facebook graphApi");
                                    faceBookDataBundle.putString("Details", response.toString());
                                    Log.i("Check", "FB details are: " +
                                            response.toString());

                                    Log.i("Check", "FB response.getRawResponse(): " +
                                            response.getRawResponse());

                                    Log.i("Check", "FB response.getJSONObject(): " +
                                            response.getJSONObject());

                                    Log.i("Check", "FB object.toString(): " +
                                            object.toString());

                                    Log.i("Check", "Customer UID: " +customerUID);
                                    String userId = object.getString("id");
                                    URL profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=100&height=100");
                                    faceBookDataBundle.putString("Customer_FaceBook_PP", profilePicture.toString());
                                    editor.putString("Customer_FaceBook_PP", profilePicture.toString());
                                    Log.i("Check","profilePicture: " + profilePicture);
                                    //Create the variable which will be retrieved from face book
                                    String name,email, profileLink,gender,birthday,ageRange;
                                    if(object.has("name")){
                                        name = object.getString("name");
                                        editor.putString("Customer_Name", name);
                                        faceBookDataBundle.putString("Customer_Name",name );
                                        Log.i("Check","name: " + name);
                                    } else {
                                        name = null;
                                    }
                                    if (object.has("email")){
                                        email = object.getString("email");
                                        editor.putString("Customer_Email", email);
                                        faceBookDataBundle.putString("Customer_Email",email );
                                        Log.i("Check","email: " + email);
                                    } else {
                                        email = null;
                                    }
                                    if(object.has("link")){
                                        profileLink = object.getString("link");
                                        editor.putString("Customer_FaceBookUrlLink", profileLink);
                                        faceBookDataBundle.putString("Customer_FaceBookUrlLink",profileLink );
                                        Log.i("Check","Customer_FaceBookUrlLink: " + profileLink);
                                    } else {
                                        profileLink = null;
                                    }
                                    if (object.has("gender")){
                                        gender = object.getString("gender");
                                        editor.putString("Customer_Gender", gender);
                                        faceBookDataBundle.putString("Customer_Gender",gender );
                                        Log.i("Check","Gender: " + gender);
                                    } else {
                                        gender = null;
                                    }
                                    if (object.has("birthday")){
                                        birthday = object.getString("birthday");
                                        editor.putString("Customer_BirthDate", birthday);
                                        faceBookDataBundle.putString("Customer_BirthDate",birthday );
                                        Log.i("Check","birthday: " + birthday);
                                    } else {
                                        birthday = null;
                                    }
                                    if (object.has("age_range")){
                                        ageRange = object.getString("age_range");
                                        editor.putString("Customer_Age_Range", ageRange);
                                        faceBookDataBundle.putString("Customer_Age_Range",ageRange );
                                        Log.i("Check","Customer_Age_Range: " + ageRange);
                                    } else {
                                        ageRange = null;
                                    }

                                    if (name == null || email == null ||
                                            gender == null || birthday == null){
                                        welcome_layout.setVisibility(View.GONE);
                                        customerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                                        customerSignInPhoneVerificationLayOut.setVisibility(View.GONE);
                                        customerRegistrationOTPLayOut.setVisibility(View.GONE);
                                        customerSignInOTPLayOut.setVisibility(View.GONE);
                                        customerOtherAccountLinkLayOut.setVisibility(View.GONE);
                                        customerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                                        faceBookMissedDataLayOut.setVisibility(View.VISIBLE);
                                        FirebaseCrash.log("Welcome: layOut-faceBookMissedDataLayOut");
                                        mVerificationStage = "faceBookMissedDataLayOut";

                                        if (name == null){
                                            layoutFaceBookMissedName.setVisibility(View.VISIBLE);
                                        }
                                        if (email == null){
                                            layoutFaceBookMissedEmail.setVisibility(View.VISIBLE);
                                        }
                                        if (gender == null){
                                            layoutFaceBookMissedGender.setVisibility(View.VISIBLE);
                                        }
                                        if (birthday == null){
                                            layoutFaceBookMissedBirthDay.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        editor.putString("Registration_Status","OK");
                                        editor.commit();
                                        getDeviceConfiguration();
                                        Intent intent = new Intent(Welcome.this,Home.class);
                                        intent.putExtras(faceBookDataBundle);
                                        startActivity(intent);
                                        finish();
                                    }


//
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }catch(Exception e2){
                                    e2.printStackTrace();
                                }
//
                            }

                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday,link,age_range"); //
            request.setParameters(parameters);
            request.executeAsync();
        } else {
            System.out.println("Access Token NULL");
            Log.i("Check", "AccessToken: NULL");
        }
        //*************************Graph Api End*************************
        //***************************************************************


    }


    private void saveFaceBookProfilePicToFireBase(Url profilePicture) {
        //Now We will save Customer Profile pic to fireBase and SQLite database
        if (profilePicture != null){
            //Below we will save our customer's profile picture to firebase storage.
            StorageReference filePath = FirebaseStorage.getInstance().getReference().
                    child("Customer_Profile_Pic").child(customerUID);
            //Now we will transfer our 'resultUri' into bitmap
            Bitmap bitmap = getBitmapFromURL(String.valueOf(profilePicture));
            //As firebase storage has limited free space, so we will first compress our image.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
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

                    Map<String, Object> pic_url = new HashMap();
                    pic_url.put("Customer_Profile_Pic_Url",profilePicDownloadUri.toString());
                    mCustomerBasicInfoDataBaseRef.updateChildren(pic_url);
                    mFireStoreCustomerBasicInfoDB.collection("Users/Customers").document(customerUID)
                            .set(pic_url);
                    saveCustomerPicToSqliteDB(imgData);
                }
            });

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Welcome.this,"Something went wrong. Upload failed....",
                            Toast.LENGTH_LONG).show();

                }
            });
        }else {
            //Go To Home Activity
        }
    }


    private void saveCustomerPicToSqliteDB(byte[] imgData) {
        FirebaseCrash.log("Welcome: saveCustomerPicToSqliteDB.called");
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
                    FirebaseCrash.log("Welcome: updating PP");
                    helper.updateCustomerPicToSQLiteDatabase(customerName, imgData, db);
                    Toast.makeText(getApplicationContext(),"Profile Pic updated " +
                                    "in SQLite: Successful",
                            Toast.LENGTH_SHORT).show();
                    cursor.close();
                } else {
                    //This means we are inserting profile pic for the first time
                    FirebaseCrash.log("Welcome: inserting PP for 1st time");
                    helper.saveCustomerPicToSQLiteDatabase(customerName, imgData, db);
                    Toast.makeText(getApplicationContext(),"Profile Pic save " +
                                    "in SQLite: Successful",
                            Toast.LENGTH_SHORT).show();
                    cursor.close();
                }
            }
        }

    }

    private void attachEmailPasswordSignIn() {
        FirebaseCrash.log("Welcome: attachEmailPasswordSignIn.called");
        strCustomerName = edtRegisterName.getText().toString();
        FirebaseCrash.log("Welcome: edtRegisterName: " + strCustomerName);
        strCustomerEmail = edtRegisterEmail.getText().toString();
        FirebaseCrash.log("Welcome: edtRegisterEmail: " + strCustomerEmail);
        strCustomerGender = getCustomerGender(radioCustomerRegisterGender);
        FirebaseCrash.log("Welcome: getCustomerGender: " + strCustomerGender);
        strRegisterBirthDay = edtRegisterBirthDay.getText().toString();
        FirebaseCrash.log("Welcome: edtRegisterBirthDay: " + strRegisterBirthDay);
        strCustomerPassword = "123456789";

        if (strCustomerName.equals("")){
            Toast.makeText(Welcome.this,"Please enter your name",Toast.LENGTH_LONG).show();
            return;
        }

        if (strCustomerGender == null){
            Toast.makeText(Welcome.this,"Please enter your Gender",Toast.LENGTH_LONG).show();
            return;
        }

        if (strRegisterBirthDay.equals("")){
            Toast.makeText(Welcome.this,"Please enter your BirthDay",Toast.LENGTH_LONG).show();
            return;
        }

        if (strCustomerEmail.equals("")){
            Toast.makeText(Welcome.this,"Please enter your email",Toast.LENGTH_LONG).show();
            return;
        } else if (!isEmailValid(strCustomerEmail)){
            Toast.makeText(Welcome.this,"Please provide valid email address",Toast.LENGTH_LONG).show();
            return;
        }

        //Now we will attach EmailAuthProvider with PhoneAuthProvide.
        credential = EmailAuthProvider.getCredential(strCustomerEmail, strCustomerPassword);
        mCustomerAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(Welcome.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseCrash.log("Welcome: EmailPassword linked with phoneAuth: success");
                            FirebaseUser user = task.getResult().getUser();
                            user.sendEmailVerification();
                            Log.d(TAG, "linkWithCredential:success\nUser: " + user);
                            Toast.makeText(Welcome.this, "Verification Done", Toast.LENGTH_SHORT).show();


                            //Now we will upload customer basic info to server
                            DatabaseReference mCustomerEmailPutRef = FirebaseDatabase.getInstance().getReference("Users/Customers")
                                    .child(customerUID).child("Customer_Basic_Info");
                            HashMap <String, Object> map = new HashMap();
                            map.put("Customer_Email", strCustomerEmail);
                            map.put("Customer_Gender", strCustomerGender);
                            map.put("Customer_Name", strCustomerName);
                            map.put("Customer_BirthDate", strRegisterBirthDay);
                            map.put("Customer_Ratings",0);
                            map.put("Customer_Total_Trips",0);
                            map.put("Registration_Status","Completed");
                            mCustomerEmailPutRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        FirebaseCrash.log("Welcome: realTimeDB for emailPass update: success");
                                    }else {
                                        FirebaseCrash.log("Welcome: realTimeDB for emailPass update: failed");
                                    }
                                }
                            });

                            mFireStoreCustomerBasicInfoDB.collection("Users").document("Customers").
                                    collection(customerUID).document("Customer_Basic_Info")
                                    .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        FirebaseCrash.log("Welcome: fireStore for emailPass update: success");
                                    }else {
                                        FirebaseCrash.log("Welcome: fireStore for emailPass update: failed");
                                    }
                                }
                            });
                            //Now we will save those basic info to internal storage
                            SharedPreferences preferences = getSharedPreferences("Customer_Basic_Info",
                                    Context.MODE_PRIVATE);
                            //Context.MODE_PRIVATE This means MyData will be accessible by only this app
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Customer_Name",strCustomerName);
                            editor.putString("Customer_Email",strCustomerEmail);
                            editor.putString("Customer_Gender", strCustomerGender);
                            editor.putString("Registration_Status","OK");
                            editor.apply();
                            getDeviceConfiguration();

                            startActivity(new Intent(Welcome.this, Home.class));
                            finish();
                            // ...
                        } else {
                            FirebaseCrash.log("Welcome: EmailPassword linked with phoneAuth: failed");
                            Log.w("Check", "linkWithCredential:failure", task.getException());
                            Toast.makeText(Welcome.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState, PersistableBundle outPersistentState) {
        FirebaseCrash.log("Welcome: onSaveInstanceState.called");
        //Save customer's current registration process
        savedInstanceState.putString("mVerificationStage", "true");
        if (strCustomerName != null && !strCustomerName.equals("")) {
            savedInstanceState.putString("strCustomerName", strCustomerName);
        }
        if (strCustomerMobileNumber != null && !strCustomerMobileNumber.equals("")) {
            savedInstanceState.putString("strCustomerMobileNumber", strCustomerMobileNumber);
        }
        if (strSignInPhone != null && !strSignInPhone.equals("")) {
            savedInstanceState.putString("strSignInPhone", strSignInPhone);
        }
        if (strCustomerEmail != null && !strCustomerEmail.equals("")) {
            savedInstanceState.putString("strCustomerEmail", strCustomerEmail);
        }
        if (strCustomerGender != null && !strCustomerGender.equals("")) {
            savedInstanceState.putString("strCustomerGender", strCustomerGender);
        }
        if (mVerificationStage != null) {
            savedInstanceState.putString("mVerificationStage", mVerificationStage);
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    private void startCustomerLogInProcess() {
        FirebaseCrash.log("Welcome: startCustomerLogInProcess.called");
        strSignInCountryCode = edtSignInCountryCode.getText().toString();
        FirebaseCrash.log("Welcome: edtSignInCountryCode: " + strSignInCountryCode);
        strSignInPhone = edtSignInPhoneNumber.getText().toString();
        FirebaseCrash.log("Welcome: edtSignInPhoneNumber: " + strSignInPhone);

        signInNumberWithCountryCode = strSignInCountryCode+strSignInPhone;

        if (strSignInCountryCode.equals("")){
            Toast.makeText(Welcome.this,"Please enter your country code",Toast.LENGTH_LONG).show();
            return;
        }
        if (strSignInPhone.equals("")){
            Toast.makeText(Welcome.this,"Please enter your phone number",Toast.LENGTH_LONG).show();
            return;
        } else if (!isPhoneNumberValid(signInNumberWithCountryCode)){
            Toast.makeText(Welcome.this,"Please provide valid phone number",Toast.LENGTH_LONG).show();
            return;
        }

        //Show waiting animation.
        waitingDialogForPhoneVerification.show();
        //Sign In user
        //Now we will send customer phone number to firebase database
        FirebaseCrash.log("Welcome: signInNumberWithCountryCode: " + signInNumberWithCountryCode);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                signInNumberWithCountryCode,
                60,
                TimeUnit.SECONDS,
                Welcome.this,
                mSignInCallbacks
        );


    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            FirebaseCrash.log("Welcome: getBitmapFromURL.called");
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    public void isGooglePlayServicesAvailable() {
        FirebaseCrash.log("Welcome: isGooglePlayServicesAvailable.called");
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(Welcome.this, status, 2404).show();
            }
            final String GooglePlayServicesURL = "https://play.google.com/store/apps/details?id=com.google.android.gms";
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Download Google Play Service")
                    .setMessage("Please download google play service from app store")
                    .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            openWebPage(GooglePlayServicesURL);
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            dialog.show();
        }

    }

    public void openWebPage(String url) {
        FirebaseCrash.log("Home:AppVersion:openWebPage.called");
        Crashlytics.log("Home:AppVersion:openWebPage.called");

        Uri webpage = Uri.parse(url);

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://" + url);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private boolean checkGPSConnectionPermission() {
        FirebaseCrash.log("Welcome: checkGPSConnectionPermission.called");
        boolean gpsStatus = false;
        if (ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, GPS_PERMISSION_REQUEST);

            }
            //That means we don't have gps permission
            gpsStatus = false;
        } else {
            //That means we have gps permission
            gpsStatus =true;
        }
        return gpsStatus;
    }

    private  boolean checkAndRequestSMSPermissions() {
        FirebaseCrash.log("Welcome: checkAndRequestSMSPermissions.called");
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_SMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void checkWhetherUserIsRegisteredOrNot() {
        FirebaseCrash.log("Welcome: checkWhetherUserIsRegisteredOrNot.called");
        if (mCustomerAuth != null  && mCustomerAuth.getCurrentUser() != null){
            customerUID = mCustomerAuth.getCurrentUser().getUid();
            FirebaseCrash.log("MainActivity: Customer is registered, ID: " + customerUID);
            //Now we will check whether customer is registering for the first time.
            //If first time registration & activity get destroyed than user get
            //access to Home.class without facebook or email sign up.
            //so below we will check whether user done either facebook or email
            //sign up or not
            String registration_status = preferences.getString("Registration_Status",DEFAULT);
            if (!registration_status.equals(DEFAULT)){
                Log.i("Check","Customer Registration process fully completed");
                Intent intent = new Intent(Welcome.this, Home.class);
                startActivity(intent);
                finish();
            } else {
                Log.i("Check","Customer Registration in progress");
            }

        }
    }


    private void checkAndRequestPhoneStatePermissions() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ android.Manifest.permission.READ_PHONE_STATE},
                    PHONE_STATE_PERMISSION);
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        FirebaseCrash.log("Welcome: isExternalStorageWritable.called");
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
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private String smsReceivedPhoneNumber;
    private String smsReceivedsenderNum;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            FirebaseCrash.log("Welcome: BroadcastReceiver.onReceive.called");
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                final String phoneNumber = intent.getStringExtra("phoneNumber");
                final String senderNum = intent.getStringExtra("senderNum");
                //Do whatever you want with the code here
                receivedOTP = message;
                smsReceivedPhoneNumber = phoneNumber;
                smsReceivedsenderNum = senderNum;

                edtRegistrationEnterOTP.setText(receivedOTP);
                edtSignInEnterOTP.setText(receivedOTP);
                FirebaseCrash.log("Received OTP: " + receivedOTP + "\n" +
                                    "phoneNumber: " + smsReceivedPhoneNumber + "\n" +
                                    "senderNum: " + smsReceivedsenderNum + "\n");
                Log.i("Check","Received OTP: " + receivedOTP + "\n" +
                                      "phoneNumber: " + smsReceivedPhoneNumber + "\n" +
                                      "senderNum: " + smsReceivedsenderNum + "\n");
                Toast.makeText(getApplicationContext(),"Received OTP: " + receivedOTP + "\n" +
                                                           "phoneNumber: " + phoneNumber + "\n" +
                                                           "senderNum: " + senderNum + "\n"
                        ,Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        mCustomerAuth.removeAuthStateListener(mCustomerAuthListener);  //VERY IMPORTANT
        FirebaseCrash.log("Welcome: onStop() called");
        myTrace.stop();
    }

    private void getDeviceConfiguration(){
        String _OSVERSION = System.getProperty("os.version");
        String AndroidVersion = android.os.Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        String _DEVICE = android.os.Build.DEVICE;
        String Device_Model = android.os.Build.MODEL;
        String _PRODUCT = android.os.Build.PRODUCT;
        String Device_Brand = android.os.Build.BRAND;
        String _DISPLAY = android.os.Build.DISPLAY;
        String _CPU_ABI = android.os.Build.CPU_ABI;
        String _CPU_ABI2 = android.os.Build.CPU_ABI2;
        String _UNKNOWN = android.os.Build.UNKNOWN;
        String _HARDWARE = android.os.Build.HARDWARE;
        String _ID = android.os.Build.ID;
        String _MANUFACTURER = android.os.Build.MANUFACTURER;
        String _SERIAL = android.os.Build.SERIAL;
        String _USER = android.os.Build.USER;
        String _HOST = android.os.Build.HOST;
        int _NUM_OF_CPU_CORES = getNumCores();
        String TotalRAM = getTotalRAM();

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int device_available_width = metrics.widthPixels;
        int device_available_height = metrics.heightPixels;

        Point size = new Point();
        int device_actual_width = 0;
        int device_actual_height = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(size);
            device_actual_width = size.x;
            device_actual_height =size.y;
        }

        Log.i("Check",
                "_OSVERSION: " + _OSVERSION + "\n" +
                "AndroidVersion: " + AndroidVersion + "\n" +
                "sdkVersion: " + sdkVersion + "\n" +
                "_DEVICE: " + _DEVICE + "\n" +
                "Device_Model: " + Device_Model + "\n" +
                "_PRODUCT: " + _PRODUCT + "\n" +
                "Device_Brand: " + Device_Brand + "\n" +
                "_DISPLAY: " + _DISPLAY + "\n" +
                "_CPU_ABI: " + _CPU_ABI + "\n" +
                "_CPU_ABI2: " + _CPU_ABI2 + "\n" +
                "_UNKNOWN: " + _UNKNOWN + "\n" +
                "_HARDWARE: " + _HARDWARE + "\n" +
                "_ID: " + _ID + "\n" +
                "_MANUFACTURER: " + _MANUFACTURER + "\n" +
                "_SERIAL: " + _SERIAL + "\n" +
                "_USER: " + _USER + "\n" +
                "_HOST: " + _HOST + "\n" +
                "_NUM_OF_CPU_CORES: " + _NUM_OF_CPU_CORES + "\n" +
                "TotalRAM: " + TotalRAM + "\n" );
        Log.i("Check","device_available_width: " + device_available_width + "\n" +
                "device_available_height: " + device_available_height + "\n" +
                "device_actual_width: " + device_actual_width + "\n" +
                "device_actual_height: " + device_actual_height);


        HashMap map = new HashMap();
        map.put("AndroidVersion",AndroidVersion);
        map.put("sdkVersion",sdkVersion);
        map.put("Device_Model",Device_Model);
        map.put("Device_Brand",Device_Brand);
        map.put("TotalRAM",TotalRAM);
        map.put("_NUM_OF_CPU_CORES",_NUM_OF_CPU_CORES);
        map.put("device_available_width",device_available_width);
        map.put("device_available_height",device_available_height);
        map.put("device_actual_width",device_actual_width);
        map.put("device_actual_height",device_actual_height);
        DatabaseReference mCustomerDeviceRef = FirebaseDatabase.getInstance().getReference("Users/Customers")
                .child(customerUID).child("Device_Specification");
        mCustomerDeviceRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Welcome:RealTime:Device_Specification: success");
                    Crashlytics.log("Welcome:RealTime:Device_Specification: success");
                } else {
                    FirebaseCrash.log("Welcome:RealTime:Device_Specification: failed");
                    Crashlytics.log("Welcome:RealTime:Device_Specification: failed");
                }
            }
        });

        HashMap<String, Object> fireMap = new HashMap();
        fireMap.put("AndroidVersion",AndroidVersion);
        fireMap.put("sdkVersion",sdkVersion);
        fireMap.put("Device_Model",Device_Model);
        fireMap.put("Device_Brand",Device_Brand);
        fireMap.put("TotalRAM",TotalRAM);
        fireMap.put("_NUM_OF_CPU_CORES",_NUM_OF_CPU_CORES);
        fireMap.put("device_available_width",device_available_width);
        fireMap.put("device_available_height",device_available_height);
        fireMap.put("device_actual_width",device_actual_width);
        fireMap.put("device_actual_height",device_actual_height);

        FirebaseFirestore mFireCustomerDeviceRef = FirebaseFirestore.getInstance();
        mFireCustomerDeviceRef.collection("Users").document("Customers")
                .collection(customerUID).document("Device_Specification")
                .set(fireMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Welcome:fireStore:Device_Specification: success");
                    Crashlytics.log("Welcome:fireStore:Device_Specification: success");
                } else {
                    FirebaseCrash.log("Welcome:fireStore:Device_Specification: failed");
                    Crashlytics.log("Welcome:fireStore:Device_Specification: failed");
                }
            }
        });
    }

    private void getThisDeviceResoulution() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Point size = new Point();
        String real_resolution = "N/A";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(size);
            real_resolution = size.x + "x" + size.y;
        }

        Log.i("Check","width: " + width + "\n" +
                "height: " + height + "\n" +
                "resolution: " + real_resolution);
    }

    private int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Default to return 1 core
            return 1;
        }
    }

    public String getTotalRAM() {
        RandomAccessFile reader = null;
        String load = null;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }
        return load;
    }

    //Current Android version data
    public String currentVersion(){
        double release;
        try {
            release = Double.parseDouble(android.os.Build.VERSION.RELEASE);
            Log.i("Check","release: " +release);
        } catch (NumberFormatException e) {
            release = 0; // your default value
        }
        String codeName="Unsupported";//below Jelly bean OR above Oreo
        if(release>=4.1 && release<4.4)codeName="Jelly Bean";
        else if(release<5)codeName="Kit Kat";
        else if(release<6)codeName="Lollipop";
        else if(release<7)codeName="Marshmallow";
        else if(release<8)codeName="Nougat";
        else if(release<9)codeName="Oreo";
        return codeName+" v"+release+", API Level: "+Build.VERSION.SDK_INT;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.contains("+");
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
        permissionsAvailable.add(android.Manifest.permission.INTERNET);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                attachGoogleSignIn(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                // ...
//            }
//        }

        if (requestCode == 603) {
            Bundle bundle = data.getBundleExtra("Details");
            String customerNameFromBundle = bundle.getString("name");
            Log.i("Check","customerNameFromBundle" + customerNameFromBundle);

        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }



    //********************************Run Time Permission Result*************************
    //*************************************START*****************************************
    //Below those are the integer value we need at the time of requesting permission.
    private static final int Request_READ_PHONE_STATE = 1001;
//    private static final int Request_WRITE_EXTERNAL_STORAGE = 1002;
    private static final int Request_READ_EXTERNAL_STORAGE = 1003;
//    private static final int Request_CONTACTS = 1004;
    private static final int Request_FINE_LOCATION = 1005;
    private static final int Request_SEND_SMS = 1007;
    private static final int Request_READ_SMS = 1008;
    private static final int Request_RECEIVE_SMS = 1009;

    private static final int Request_READ_PHONE_STATE_2ND_TIME = 2001;
    private static final int Request_WRITE_EXTERNAL_STORAGE_2ND_TIME = 2002;
    private static final int Request_READ_EXTERNAL_STORAGE_2ND_TIME = 2003;
    private static final int Request_CONTACTS_2ND_TIME = 2004;
    private static final int Request_FINE_LOCATION_2ND_TIME = 2005;
    private static final int Request_SEND_SMS_2ND_TIME = 2007;
    private static final int Request_READ_SMS_2ND_TIME = 2008;
    private static final int Request_RECEIVE_SMS_2ND_TIME = 2009;

    private static final int Request_GROUP_PERMISSION = 1425;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case Request_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have READ_PHONE_STATE Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,Phone_State : Success");
                } else {
                    //Here we we may put a counter, to calculate how may times a request is denied
//                    Toast.makeText(mContext,"You Don't Have READ_PHONE_STATE Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,Phone_State : Failed");
                }
                break;

//            case Request_WRITE_EXTERNAL_STORAGE:
//                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
////                    Toast.makeText(mContext,"You Have WRITE_EXTERNAL_STORAGE Permission",Toast.LENGTH_LONG).show();
//                    Log.i("Check","onRequestPermissionsResult,WRITE_EXTERNAL_STORAGE : Success");
//                } else {
//                    //Here we we may put a counter, to calculate how may times a request is denied
////                    Toast.makeText(mContext,"You Don't Have WRITE_EXTERNAL_STORAGE Permission",Toast.LENGTH_LONG).show();
//
//                    Log.i("Check","onRequestPermissionsResult,WRITE_EXTERNAL_STORAGE : Failed");
//                }
//                break;

            case Request_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have WRITE_EXTERNAL_STORAGE Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,WRITE_EXTERNAL_STORAGE : Success");
                } else {
                    //Here we we may put a counter, to calculate how may times a request is denied
//                    Toast.makeText(mContext,"You Don't Have WRITE_EXTERNAL_STORAGE Permission",Toast.LENGTH_LONG).show();

                    Log.i("Check","onRequestPermissionsResult,WRITE_EXTERNAL_STORAGE : Failed");
                }
                break;


//            case Request_CONTACTS:
//                if (grantResults.length > 0 && grantResults[3] == PackageManager.PERMISSION_GRANTED){
////                    Toast.makeText(mContext,"You Have Permission to read Phone Contacts",Toast.LENGTH_LONG).show();
//                    Log.i("Check","onRequestPermissionsResult,CONTACTS : Success");
//                } else {
////                    Toast.makeText(mContext,"Contacts Permission is denied.Turn off Contacts module of the app",
////                            Toast.LENGTH_LONG).show();
//
//                    Log.i("Check","onRequestPermissionsResult,CONTACTS : Failed");
//                }
//                break;

            case Request_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[2] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have FINE_LOCATION Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,FINE_LOCATION : Success");
                } else {

                    Log.i("Check","onRequestPermissionsResult,FINE_LOCATION : Failed");
                }
                break;


            case Request_SEND_SMS:
                if (grantResults.length > 0 && grantResults[3] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have SEND_SMS Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,SEND_SMS : Success");
                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setMessage("To run this app in your device we need access device sms. Without access to device sms" +
//                            " this app will not run!!!!\nPlease allow access to device sms permission in the next screen");
//                    builder.setTitle("Allow sms permission");
//                    builder.setPositiveButton("Allow sms permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            requestPermission(TXT_READ_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("read sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_RECEIVE_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("receive sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_SEND_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("send sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                        }
//                    });
//                    builder.setNegativeButton("Deny sms Permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            finish();
//                        }
//                    });
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                    Toast.makeText(mContext,"You Don't Have SEND_SMS Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,SEND_SMS : Failed");
                }
                break;

            case Request_READ_SMS:
                if (grantResults.length > 0 && grantResults[4] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have READ_SMS Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,READ_SMS : Success");
                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setMessage("To run this app in your device we need access device sms. Without access to device sms" +
//                            " this app will not run!!!!\nPlease allow access to device sms permission in the next screen");
//                    builder.setTitle("Allow sms permission");
//                    builder.setPositiveButton("Allow sms permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            requestPermission(TXT_READ_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("read sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_RECEIVE_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("receive sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_SEND_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("send sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                        }
//                    });
//                    builder.setNegativeButton("Deny sms Permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            finish();
//                        }
//                    });
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                    Toast.makeText(mContext,"You Don't Have READ_SMS Permission",Toast.LENGTH_LONG).show();

                    Log.i("Check","onRequestPermissionsResult,READ_SMS : Failed");
                }
                break;

            case Request_RECEIVE_SMS:
                if (grantResults.length > 0 && grantResults[5] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have RECEIVE_SMS Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,RECEIVE_SMS : Success");
                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setMessage("To run this app in your device we need access device sms. Without access to device sms" +
//                            " this app will not run!!!!\nPlease allow access to device sms permission in the next screen");
//                    builder.setTitle("Allow sms permission");
//                    builder.setPositiveButton("Allow sms permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            requestPermission(TXT_READ_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("read sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_RECEIVE_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("receive sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_SEND_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("send sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                        }
//                    });
//                    builder.setNegativeButton("Deny sms Permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            finish();
//                        }
//                    });
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                    Toast.makeText(mContext,"You Don't Have RECEIVE_SMS Permission",Toast.LENGTH_LONG).show();

                    Log.i("Check","onRequestPermissionsResult,RECEIVE_SMS : Failed");
                }
                break;

            case Request_GROUP_PERMISSION:
                String result = "";
                int i = 0;

                for (String perm : permissions){
                    String status = "";
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        switch (i){
                            case 0:
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Welcome.this);
                                builder.setMessage("Without Device Phone State Permission This App Will Shut Down");
                                builder.setTitle("Allow Phone State Permission");
                                builder.setPositiveButton("Allow Phone State Permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
                                                        {android.Manifest.permission.READ_PHONE_STATE},
                                                Request_READ_PHONE_STATE_2ND_TIME);
                                    }
                                });
                                builder.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                                android.app.AlertDialog alertDialog = builder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                                break;

//                            case 1:
//                                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(Welcome.this);
//                                builder1.setMessage("Without Storage Write Permission This App Will Shut Down");
//                                builder1.setTitle("Allow Storage Write Permission");
//                                builder1.setPositiveButton("Allow Storage Write Permission", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
//                                                        {Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                                Request_WRITE_EXTERNAL_STORAGE_2ND_TIME);
//                                    }
//                                });
//                                builder1.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialogInterface.dismiss();
//                                        finish();
//                                    }
//                                });
//
//                                android.app.AlertDialog alertDialog1 = builder1.create();
//                                alertDialog1.show();
//                                break;

                            case 1:
                                android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(Welcome.this);
                                builder2.setMessage("Without Storage Read Permission This App Will Shut Down");
                                builder2.setTitle("Allow Storage Read Permission");
                                builder2.setPositiveButton("Allow Storage Read Permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                            ActivityCompat.requestPermissions(Welcome.this, new String[]
                                                            {android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                                    Request_READ_EXTERNAL_STORAGE_2ND_TIME);
                                        }
                                    }
                                });
                                builder2.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                                android.app.AlertDialog alertDialog2 = builder2.create();
                                alertDialog2.setCancelable(false);
                                alertDialog2.setCanceledOnTouchOutside(false);
                                alertDialog2.show();
                                break;

//                            case 2:
//                                android.app.AlertDialog.Builder builder3 = new android.app.AlertDialog.Builder(Welcome.this);
//                                builder3.setMessage("We will verify this device by accessing received sms phone number." +
//                                        "\nWithout Read Contact Permission This App Will Shut Down");
//                                builder3.setTitle("Allow Read Contact Permission");
//                                builder3.setPositiveButton("Allow Read Contact Permission", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
//                                                        {Manifest.permission.READ_CONTACTS},
//                                                Request_CONTACTS_2ND_TIME);
//                                    }
//                                });
//                                builder3.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialogInterface.dismiss();
//                                        finish();
//                                    }
//                                });
//
//                                android.app.AlertDialog alertDialog3 = builder3.create();
//                                alertDialog3.show();
//                                break;

                            case 2:
                                android.app.AlertDialog.Builder builder4 = new android.app.AlertDialog.Builder(Welcome.this);
                                builder4.setMessage("Without Device Location Permission This App Will Shut Down");
                                builder4.setTitle("Allow Device Location Permission");
                                builder4.setPositiveButton("Allow Device Location Permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
                                                        {android.Manifest.permission.ACCESS_FINE_LOCATION},
                                                Request_FINE_LOCATION_2ND_TIME);
                                    }
                                });
                                builder4.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                                android.app.AlertDialog alertDialog4 = builder4.create();
                                alertDialog4.setCancelable(false);
                                alertDialog4.setCanceledOnTouchOutside(false);
                                alertDialog4.show();

                            case 3:
                                android.app.AlertDialog.Builder builder5 = new android.app.AlertDialog.Builder(Welcome.this);
                                builder5.setMessage("Without SMS Send Permission This App Will Shut Down");
                                builder5.setTitle("Allow SMS Send Permission");
                                builder5.setPositiveButton("Allow SMS Send Permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
                                                        {android.Manifest.permission.SEND_SMS},
                                                Request_SEND_SMS_2ND_TIME);
                                    }
                                });
                                builder5.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                                android.app.AlertDialog alertDialog5 = builder5.create();
                                alertDialog5.setCancelable(false);
                                alertDialog5.setCanceledOnTouchOutside(false);
                                alertDialog5.show();
                                break;

                            case 4:
                                android.app.AlertDialog.Builder builder6 = new android.app.AlertDialog.Builder(Welcome.this);
                                builder6.setMessage("Without Read Sms Permission This App Will Shut Down");
                                builder6.setTitle("Allow Read Sms Permission");
                                builder6.setPositiveButton("Allow Read Sms Permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
                                                        {android.Manifest.permission.READ_SMS},
                                                Request_READ_SMS_2ND_TIME);
                                    }
                                });
                                builder6.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                                android.app.AlertDialog alertDialog6 = builder6.create();
                                alertDialog6.setCancelable(false);
                                alertDialog6.setCanceledOnTouchOutside(false);
                                alertDialog6.show();
                                break;

                            case 5:
                                android.app.AlertDialog.Builder builder7 = new android.app.AlertDialog.Builder(Welcome.this);
                                builder7.setMessage("Without SMS View Permission This App Will Shut Down");
                                builder7.setTitle("Allow SMS View Permission");
                                builder7.setPositiveButton("Allow SMS View Permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
                                                        {android.Manifest.permission.RECEIVE_SMS},
                                                Request_RECEIVE_SMS_2ND_TIME);
                                    }
                                });
                                builder7.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                                android.app.AlertDialog alertDialog7 = builder7.create();
                                alertDialog7.setCancelable(false);
                                alertDialog7.setCanceledOnTouchOutside(false);
                                alertDialog7.show();
                                break;
                        }
                        status = "Denied";
                    } else {
                        status = "Granted";
                    }
                    result = result + "\n" + perm + " : " + status;

                    i++;
                }

                Log.i("Check", result);
                break;


                //***************************************2ND TIME********************************
            //**************************************REQUEST CODE*********************************
            case Request_READ_PHONE_STATE_2ND_TIME:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have READ_PHONE_STATE Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,Phone_State : Success_2ND_TIME");
                } else {
                    //Here we we may put a counter, to calculate how may times a request is denied
//                    Toast.makeText(mContext,"You Don't Have READ_PHONE_STATE Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,Phone_State : Failed_2ND_TIME");
//                    finish();
                }
                break;

            case Request_WRITE_EXTERNAL_STORAGE_2ND_TIME:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have WRITE_EXTERNAL_STORAGE Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,WRITE_EXTERNAL_STORAGE : Success_2ND_TIME");
                } else {
                    //Here we we may put a counter, to calculate how may times a request is denied
//                    Toast.makeText(mContext,"You Don't Have WRITE_EXTERNAL_STORAGE Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,WRITE_EXTERNAL_STORAGE : Failed_2ND_TIME");
//                    finish();
                }
                break;

            case Request_READ_EXTERNAL_STORAGE_2ND_TIME:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have WRITE_EXTERNAL_STORAGE Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,WRITE_EXTERNAL_STORAGE : Success_2ND_TIME");
                } else {
                    //Here we we may put a counter, to calculate how may times a request is denied
//                    Toast.makeText(mContext,"You Don't Have WRITE_EXTERNAL_STORAGE Permission",Toast.LENGTH_LONG).show();

                    Log.i("Check","onRequestPermissionsResult,WRITE_EXTERNAL_STORAGE : Failed_2ND_TIME");
//                    finish();
                }
                break;


            case Request_CONTACTS_2ND_TIME:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have Permission to read Phone Contacts",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,CONTACTS : Success_2ND_TIME");
                } else {
//                    Toast.makeText(mContext,"Contacts Permission is denied.Turn off Contacts module of the app",
//                            Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,CONTACTS : Failed_2ND_TIME");
//                    finish();
                }
                break;

            case Request_FINE_LOCATION_2ND_TIME:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have FINE_LOCATION Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,FINE_LOCATION : Success_2ND_TIME");
                } else {

                    Log.i("Check","onRequestPermissionsResult,FINE_LOCATION : Failed_2ND_TIME");
//                    finish();
                }
                break;


            case Request_SEND_SMS_2ND_TIME:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have SEND_SMS Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,SEND_SMS : Success_2ND_TIME");
                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setMessage("To run this app in your device we need access device sms. Without access to device sms" +
//                            " this app will not run!!!!\nPlease allow access to device sms permission in the next screen");
//                    builder.setTitle("Allow sms permission");
//                    builder.setPositiveButton("Allow sms permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            requestPermission(TXT_READ_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("read sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_RECEIVE_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("receive sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_SEND_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("send sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                        }
//                    });
//                    builder.setNegativeButton("Deny sms Permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            finish();
//                        }
//                    });
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                    Toast.makeText(mContext,"You Don't Have SEND_SMS Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,SEND_SMS : Failed_2ND_TIME");
//                    finish();
                }
                break;

            case Request_READ_SMS_2ND_TIME:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have READ_SMS Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,READ_SMS : Success_2ND_TIME");
                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setMessage("To run this app in your device we need access device sms. Without access to device sms" +
//                            " this app will not run!!!!\nPlease allow access to device sms permission in the next screen");
//                    builder.setTitle("Allow sms permission");
//                    builder.setPositiveButton("Allow sms permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            requestPermission(TXT_READ_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("read sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_RECEIVE_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("receive sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_SEND_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("send sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                        }
//                    });
//                    builder.setNegativeButton("Deny sms Permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            finish();
//                        }
//                    });
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                    Toast.makeText(mContext,"You Don't Have READ_SMS Permission",Toast.LENGTH_LONG).show();

                    Log.i("Check","onRequestPermissionsResult,READ_SMS : Failed_2ND_TIME");
//                    finish();
                }
                break;

            case Request_RECEIVE_SMS_2ND_TIME:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(mContext,"You Have RECEIVE_SMS Permission",Toast.LENGTH_LONG).show();
                    Log.i("Check","onRequestPermissionsResult,RECEIVE_SMS : Success_2ND_TIME");
                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setMessage("To run this app in your device we need access device sms. Without access to device sms" +
//                            " this app will not run!!!!\nPlease allow access to device sms permission in the next screen");
//                    builder.setTitle("Allow sms permission");
//                    builder.setPositiveButton("Allow sms permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            requestPermission(TXT_READ_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("read sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_RECEIVE_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("receive sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                            requestPermission(TXT_SEND_SMS);
//                            //Than update the PermissionUtil class about this recent permission.
//                            permissionUtil.updatePermissionPreference("send sms");
//                            //So after this update we will be able to know that this permission was asked before.
//                        }
//                    });
//                    builder.setNegativeButton("Deny sms Permission", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                            finish();
//                        }
//                    });
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                    Toast.makeText(mContext,"You Don't Have RECEIVE_SMS Permission",Toast.LENGTH_LONG).show();

                    Log.i("Check","onRequestPermissionsResult,RECEIVE_SMS : Failed_2ND_TIME");
//                    finish();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    //********************************Run Time Permission Result*************************
    //*************************************END*****************************************

}