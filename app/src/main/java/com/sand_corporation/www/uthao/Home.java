package com.sand_corporation.www.uthao;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.appsee.Appsee;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.firebase.perf.metrics.Trace;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.sand_corporation.www.uthao.Animation.TabAnimation;
import com.sand_corporation.www.uthao.BackGroundJob.FireBaseJobDispatcherBackGroundJob;
import com.sand_corporation.www.uthao.CustomPlaceAutoComplete.CustomLinearLayoutManager;
import com.sand_corporation.www.uthao.CustomPlaceAutoComplete.PlaceAutoComplete;
import com.sand_corporation.www.uthao.CustomPlaceAutoComplete.PlacesAutoCompleteAdapter;
import com.sand_corporation.www.uthao.CustomPlaceAutoComplete.RecyclerItemClickListener;
import com.sand_corporation.www.uthao.CustomerDummyRideHistory.CustomerDummyRideHistoryDbHelper;
import com.sand_corporation.www.uthao.DeviceToDeviceNotification.IFCMService;
import com.sand_corporation.www.uthao.DeviceToDeviceNotification.ModelClass.Data;
import com.sand_corporation.www.uthao.DeviceToDeviceNotification.ModelClass.FCMResponse;
import com.sand_corporation.www.uthao.DeviceToDeviceNotification.ModelClass.Sender;
import com.sand_corporation.www.uthao.FirebaseMessaging.ModelClass.Token;
import com.sand_corporation.www.uthao.GlobalVariable.Common;
import com.sand_corporation.www.uthao.LanguageChange.LocalHelper;
import com.sand_corporation.www.uthao.NavigationMenuRecyclerView.NavigationMenuAdapter;
import com.sand_corporation.www.uthao.NavigationMenuRecyclerView.NavigationMenuItem;
import com.sand_corporation.www.uthao.Permissions.Permissions;
import com.sand_corporation.www.uthao.Remote.IGoogleAPI;
import com.sand_corporation.www.uthao.RemoteAppUpdate.RemoteAppUpdateHelper;
import com.sand_corporation.www.uthao.SQDatabase.DbContract;
import com.sand_corporation.www.uthao.SQDatabase.DbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, RoutingListener, TextWatcher,
        RemoteAppUpdateHelper.onUpdateCheckListener,
        ComponentCallbacks2 {

    //Driver chat panel bottom sheet
    private BottomSheetBehavior driverChatAndInfoBottomSheetBehavior;
    private LinearLayout driverChatAndInfoBottomSheet;
    private ImageView ic_up_sign_driver_bottom, sendVoiceMessageToDriver, sendTextMessageToDriver;
    private EditText edtDriverChatWindow;
    private CircleImageView assignedDriverProfilePic;
    private TextView assignedDriverName,assignedDriverRating, assignedDriverCarModel,
            assignedDriverTotalLifeTimeTrips, assignedDriverVehicleRegNumber,
            paymentMethodForAssignedDriver, assignedDriverDistance, assignedDriverETA;
    private Button btnCancelCurrentRide, btnCallAssignedDriver;
    private String strAssignedDriverName, strAssignedDriverRating, strAssignedDriverPhoneNumber,
            strAssignedDriverCarModel, strAssignedDriverTotalLifeTimeTrips,
            strAssignedDriverVehicleRegNumber, strAssignedDriverDistance;

    //Check current request pickup and destination Address
    private Button editRequest, requestedPickUpAndDestinationAddressOK;
    private LinearLayout checkAddress;
    private ImageView ic_back_sign1;
    private TextView checkPickUpAddress, checkDestinationAddress, checkDistance;

    //Select your Ride
    private LinearLayout selectBikeOrCar;
    private ImageView ic_back_signFromSelectYourRide, selectMotorCycle, selectCar;

    //Select service for car
    private LinearLayout selectServiceForCar;
    private ImageView ic_back_sign_select_service_for_car, selectEconomyServiceForCar,
            selectPremiumServiceForCar;
    private TextView economyServiceCostForCar,premiumServiceCostForCar;

    //Select service for bike
    private LinearLayout selectServiceForBike;
    private ImageView ic_back_sign_select_service_for_bike, selectEconomyServiceForBike,
            selectPremiumServiceForBike;
    private TextView economyServiceCostForBike,premiumServiceCostForBike;

    //Selected vehicle and service;
    private String selectedVehicle = null;
    private String selectedService = null;

    //Select payment method
    private LinearLayout selectPaymentMethod;
    private ImageView ic_back_sign_selectPaymentMethod, selectUthaoWallet,
            selectCash;

    //Place a request to Uthao
    private LinearLayout placeArequestToUthao;
    private ImageView ic_back_signForPlacingRequestToUthao;
    private Button cancelRequest, callToUthao;
    private boolean isCustomerRequestForDriverHasTimedOut = false;
    private boolean isCustomerOrDriverForcedStopRequest = false;
    private ArrayList<String> driverWhoHaveDecline;

    //Create ring tone in driver apps
    private String driverFCMToken;
    private IFCMService ifcmService;


    private static final int CHECK_IS_LOCATION_ENABLED_IN_THIS_DEVICE = 10000;
    private static final String TAG = "CheckHome";
    private Permissions permissions;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Trace myTrace; //This is for firebase performance monitoring
    private static final int GPS_PERMISSION_REQUEST = 102;
    private GoogleMap mMap;
    private Marker pickUpMarker, destinationMarker;
    private MarkerOptions customerPickUpMarkerOptions, customerDestinationMarkerOptions;
    private boolean isPickUpMarkerFrozen = false;
    private boolean isDestinationMarkerFrozen = true;
    private boolean switchbetweenDummyDestinationPickUpBox = false;
    //switchbetweenDummyDestinationPickUpBox == false means customer selected pickup box
    private boolean isCarAnimationShowingToUser = false;

    private String customerUID;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location mLastLocation;
    private LatLng pickUpLocationLatLng, customerDestinationLatLng, mLastLocationLatLng;
    private Button customer_ride_request, btnFairEstimation;
    private int customer_request_type = 1;
    private ImageButton myMapLocationButton, navigationDrawerCustomButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Menu navigation_menu;
    private CircleImageView nav_profile_image;
    private Bitmap bmapCustomerProfilePic;
    private boolean callUthaoStatus = false;
    //Means customer did not put request for Uthao in our firebase database.
    private int rideStatus = 0;
    //By above 'rideStatus' variable we will keep track of our driver ride status
    //Example: whether he picked up the customer, whether he dropped the customer.
    //rideStatus == 0,That means, ride is not started yet
    private String customerDestinationAdresss, customerPickUpAddress;
    //Variable about driver info panel below
    private LinearLayout assignedDriverInfoPanel;
    //***********************************Bottom Sheet Variable Start**********************
    private BottomSheetFragment bottomSheetFragment;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout bottomSheetLinearLayout;
    private TextView txtCustomerRideDateDummyHisSingle, txtDriverCarModelDummyHisSingle, txtTotalCostOfTheRideDummyHisSingle,
            txtPaymentModeDummyHisSingle, txtCustomerPickUpLocationDummyHisSingle, txtCustomerDestinationLocationDummyHisSingle,
            txtCustomerRideDistance, txtDriverNameDummyHisSingle, nav_profile_name;
    //Below those textViews font will be changed
    private TextView receiptUnit, receiptFare, receiptTotal, receiptBaseFare, receiptBaseFareDistance,
            receiptBaseFarePerCosting, receiptBaseFareTotalCosting, receiptTotalDistance,
            txtCustomerRideDistanceDummyHisSingle, receiptTotalDistancePerUnitCosting,
            receiptTotalDistanceTotalCosting, receiptTotalMinutes, receiptTotalMinutesQuantity,
            receiptTotalMinutesPerUnitCosting, receiptTotalMinutesTotalCosting, receiptTotalFareWithoutDiscount,
            receiptTotalDiscountText, receiptTotalDiscountAmountInThisRide, receiptTotalText, receiptTotalFareAfterDiscount;
    private ImageView imgDriverProfilePicDummyHisSingle, ic_back_sign_from_money_receipt;
    private RatingBar rtnDriverRatingDummyHisSingle;
    private TabHost tabHost;
    private Double travelDistance, travelTime, driverDistanceToPicUpLocation,
            driverETAtoPickUpLocation;
    private ImageView imgDummyRideHistoryPolyLIneDummyHisSingle;
    private String customerDummyRideId;
    private Long customerDummyRideDate;
    private Double totalDummyFair;
    private Bitmap customerMapSnapShot;
    private boolean takenMapSnapShotForDummyReceipt = false;
    private boolean nowIsTheTimeToTakeSnapShot = false;
    //***********************************Bottom Sheet Variable End**********************
    private String strCarType = "";
    private RadioGroup customerChoiceOfCarGroup;
    private RadioButton uthaoX, uthaoPremium;
    private RatingBar assignedDriverInfoPanelRtnBar;
    private boolean googlePlaceAutoCompleteLatLngBoundset = false;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimaryLight, R.color.colorAccent, R.color.primary_dark_material_light};
    //All the variable for pick up and destination selector box
    //********************Start********************************
    private LatLngBounds bound_100KM_Radius;
    private EditText edtActualDestinationSelector, edtDummyDestinationSelector,
            edtDummyPickUpSelector,edtActualPickUpSelector;
    private ImageView imgDeleteDummyPickUp, imgDeleteActualPickUp,
            imgDeleteDummyDestination, imgDeleteActualDestination;
    private RecyclerView recyclerView_pick_up, recyclerView_Destination, navigationMenuRecyclerView;
    private CustomLinearLayoutManager mLinearLayoutManagerForPickUp, mLinearLayoutManagerForDestination;
    private PlacesAutoCompleteAdapter mPlacesAutoCompleteAdapter;
    private NavigationMenuAdapter navigationMenuAdapter;
    private LinearLayout dummyPickUpSelectorLayOut, dummyDestinationSelectorLayOut,
            actualPickUpSelectorLayOut, actualDestinationSelectorLayOut,
            crossMarkContainerForPickUp, crossMarkContainerForDestination;
    private RelativeLayout dummyPickUpRelativeLayout, dummyDestinationRelativeLayout;
    private RelativeLayout relativeLayOutpickUpDestinationActualDummyBox;
    private LinearLayout.LayoutParams linearDummyPickUpParams;
    private boolean doubleBackToExitPressedOnce = false;
    //All the variable for pick up and destination selector box
    //********************End********************************


    //************************************************************************
    //****************************CAR ANIMATION****************************************
    private ValueAnimator valueAnimator;
    private List<LatLng> polyLineListForCarAnimation;
    private List<LatLng> polyLineListForDriverDistance;
    private Marker carMarker;
    private float v;
    private double lat, lng;
    private Handler handlerForCarAnimation;
    private LatLng currentPosition, startPosition, endPosition;
    private int index, next;
    private PlaceAutocompleteFragment autocompleteFragment;
    private String destination;
    private PolylineOptions grayPolylineOptions, blackPolylineOptions;
    private Polyline blackPolyline, grayPolyline;
    private IGoogleAPI mService;

    //********************************Fair Calculation***********************************
    //************************************Starts Here************************************
    private Double premiumCarBaseFair = 80.00;
    private Double premiumCarPerKmFair = 19.00;
    private Double premiumCarWaitingTimePerMin = 5.00;

    private Double economyCarBaseFair = 40.00;
    private Double economyCarPerKmFair = 17.00;
    private Double economyCarWaitingTimePerMin = 4.00;

    private Double premiumBikeBaseFair = 30.00;
    private Double premiumBikePerKmFair = 9.00;
    private Double premiumBikeWaitingTimePerMin = 2.50;

    private Double economyBikeBaseFair = 25.00;
    private Double economyBikePerKmFair = 8.00;
    private Double economyBikeWaitingTimePerMin = 2.00;

    //********************************Fair Calculation***********************************
    //************************************Ends Here************************************


    //************************************************************************
    //****************************CAR ANIMATION***************************************

    //Firebase job dispatcher variable
    private static final String JOB_TAG = "My_Job_Tag";
    private FirebaseJobDispatcher fireBaseJobDispatcher;



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseCrash.log("Home: onStart.called");
        Crashlytics.log("Home: onStart.called");
        buildGoogleApiClient();
        edtActualPickUpSelector.addTextChangedListener(this);
        edtActualDestinationSelector.addTextChangedListener(this);
        createDefaultLanguageForThisApp();

    }

    private void createDefaultLanguageForThisApp() {
        FirebaseCrash.log("Home: createDefaultLanguageForThisApp.called");
        Crashlytics.log("Home: createDefaultLanguageForThisApp.called");
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

    public void updateDefaultLanguage(String language) {
        FirebaseCrash.log("Home: updateDefaultLanguage.called");
        Crashlytics.log("Home: updateDefaultLanguage.called");
        Context context = LocalHelper.setLocale(Home.this,language);
        Resources resources = context.getResources();
        //Below we will change all the language


        //Button string
        btnFairEstimation.setText(R.string.estimate_our_fair);
        customer_ride_request.setText(R.string.estimate_our_fair);

        //Edit text string
        edtDummyPickUpSelector.setHint(R.string.autocomplete_hint_pickUp);
        edtActualPickUpSelector.setHint(R.string.autocomplete_hint_pickUp);

        edtDummyDestinationSelector.setHint(R.string.autocomplete_hint_Destination);
        edtActualDestinationSelector.setHint(R.string.autocomplete_hint_Destination);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //Don't call below two log method here. It will create this error
        //Caused by java.lang.IllegalStateException: Must Initialize Fabric before using singleton()
//        FirebaseCrash.log("Home: attachBaseContext.called");
//        Crashlytics.log("Home: attachBaseContext.called");
        //This will help us to change default language
        super.attachBaseContext(LocalHelper.onAttach(newBase,"en"));

    }


    @Override
    @AddTrace(name = "onCreateTrace", enabled = true/*Optional*/)
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());
        Fabric.with(this);
        Appsee.start(getString(R.string.com_appsee_apikey));
        FirebaseCrash.log("Home: onCreate.called");
        Crashlytics.log("Home: onCreate.called");
        super.onCreate(savedInstanceState);
        Log.i("CustomerMapsActivity", "onCreate() called");
        setContentView(R.layout.activity_home);
        FirebaseCrash.log("Home:setContentView: Success");
        Crashlytics.log("Home:setContentView: Success");

        checkHowManyPermissionAllowed(Home.this);

        //*********************************************************
        //If only new user complete registration, than below code will be execute.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String details = bundle.getString("Details");
            if(details != null){
                String faceBookDetails = bundle.getString("Details");
                String profilePicture = bundle.getString("Customer_FaceBook_PP");
                String name = bundle.getString("Customer_Name");
                String email = bundle.getString("Customer_Email");
                String profileLink = bundle.getString("Customer_FaceBookUrlLink");
                String gender = bundle.getString("Customer_Gender");
                String birthday = bundle.getString("Customer_BirthDate");
                String ageRange = bundle.getString("Customer_Age_Range");
                //If registration is successful, than we will put our customer name in the database.
                customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference mNewUserBasicInfoDbRef= FirebaseDatabase.getInstance().
                        getReference("Users/Customers").child(customerUID).child("Customer_Basic_Info");
                HashMap<String, Object> map = new HashMap();
                if (faceBookDetails != null){
                    map.put("Customer_FaceBook_Details", faceBookDetails);
                }
                if (profilePicture != null){
                    map.put("Customer_FaceBook_PP", profilePicture);
                }
                if (name != null){
                    map.put("Customer_Name", name);
                }
                if (email != null){
                    map.put("Customer_Email", email);
                }
                if (profileLink != null){
                    map.put("Customer_FaceBookUrlLink", profileLink);
                }
                if (gender != null){
                    map.put("Customer_Gender", gender);
                }
                if (birthday != null){
                    map.put("Customer_BirthDate", birthday);
                }
                if (ageRange != null){
                    map.put("Customer_Age_Range", ageRange);
                }
                map.put("Customer_Ratings",0);
                map.put("Customer_Total_Trips",0);
                map.put("Registration_Status","Completed");
                mNewUserBasicInfoDbRef.updateChildren(map).addOnCompleteListener
                        (new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseCrash.log("Home:RealDB:NewUserBasicInfo Update: Success");
                            Crashlytics.log("Home:RealDB:NewUserBasicInfo Update: Success");
                        }else {
                            FirebaseCrash.log("Home:RealDB:NewUserBasicInfo Update: failed");
                            Crashlytics.log("Home:RealDB:NewUserBasicInfo Update: failed");
                        }
                    }
                });
                Log.i("Check","Home: customer name: " + details);

                //Now put those data in fire store by update method.
                //If we don't use update method here, previously put mobile number
                //will be deleted
                FirebaseFirestore mFireStoreCustomerBasicInfoDB =FirebaseFirestore.getInstance();
                mFireStoreCustomerBasicInfoDB.collection("Users").document("Customers").collection(customerUID)
                        .document("Customer_Basic_Info")
                        .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseCrash.log("Home:fireStore:NewUserBasicInfo Update: Success");
                            Crashlytics.log("Home:fireStore:NewUserBasicInfo Update: Success");
                        }else {
                            FirebaseCrash.log("Home:fireStore:NewUserBasicInfo Update: failed");
                            Crashlytics.log("Home:fireStore:NewUserBasicInfo Update: failed");
                        }
                    }
                });
            }

        }

        dataFoundInSharedPreference();


        //*********************************************************

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderPart = navigationView.getHeaderView(0);
        nav_profile_image = navigationHeaderPart.findViewById(R.id.nav_profile_image);
        nav_profile_name = navigationHeaderPart.findViewById(R.id.nav_profile_name);
        navigation_menu = navigationView.getMenu();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        settingUpFirebaseCrashlyticsDebugMode();
        //This poly line is the route between driver & customer
        polylines = new ArrayList<>();
        //Below poly line for car animation
        polyLineListForCarAnimation = new ArrayList<>();
        polyLineListForDriverDistance = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        driverWhoHaveDecline = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.customerMap);
        mapFragment.getMapAsync(this);
        FirebaseCrash.log("Home:fireStore:getMapAsync: Success");
        Crashlytics.log("Home:fireStore:getMapAsync: Success");
//        allCodeOfNavigationDrawyerForOnCreateMethod();
        customer_ride_request = findViewById(R.id.customer_ride_request);
        btnFairEstimation = findViewById(R.id.btnFairEstimation);



//        customerChoiceOfCarGroup = findViewById(R.id.customerChoiceOfCarGroup);
//        uthaoX = findViewById(R.id.uthaoX);
//        uthaoPremium = findViewById(R.id.uthaoPremium);
        //Make auto select of 'UthaoX' in radio group
//        customerChoiceOfCarGroup.check(R.id.uthaoX);

        myMapLocationButton = findViewById(R.id.myMapLocationButton);
        navigationDrawerCustomButton = findViewById(R.id.navigationDrawerCustomButton);

        //Create object from Permissions.class
        permissions = new Permissions(Home.this);

        //We initialize 'customerDestinationLatLng' with default 0.00 LatLng.
        customerDestinationLatLng = new LatLng(0.00, 0.00);
        bottomSheetLinearLayout = findViewById(R.id.bottomSheetLinearLayout);



        initializeAllTheViewFromPickupAndDestinationBox();
        customerDummyPickUpAndDestinationBoxListener();
        customerActualPickUpAndDestinationBoxListener();
        deleteWrittenTextInSearchBoxForActualBox();
        deleteWrittenTextInSearchBoxForDummyBox();
        customerPickUpBoxOnItemSelection();
        customerDestinationBoxOnItemSelection();
        populateNavigationMenuToRecyclerView();
        setNavigationProfilePic();

        sendFCMTokenToServer();

        //We are subscribing to topic so that we don't need token
        //to send message
        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        //This below 'RemoteAppUpdateHelper' will check if new version of this app
        //available in play store or not.
        RemoteAppUpdateHelper.with(this)
                .onUpdateCheck(this)
                .check();

        startFireBaseJobDispatcherBackGroundJob();
    }

    private boolean setUpAllTheViewsFromDriverChatAndInfoBottomSheet = false;
    private void setUpAllTheViewsFromDriverChatAndInfoBottomSheet(){
        setUpAllTheViewsFromDriverChatAndInfoBottomSheet = true;
        driverChatAndInfoBottomSheet = findViewById(R.id.driverChatAndInfoBottomSheet);
        driverChatAndInfoBottomSheet.setVisibility(View.VISIBLE);
        ic_up_sign_driver_bottom = findViewById(R.id.ic_up_sign_driver_bottom);
        edtDriverChatWindow = findViewById(R.id.edtDriverChatWindow);
        assignedDriverProfilePic = findViewById(R.id.assignedDriverProfilePic);
        assignedDriverName = findViewById(R.id.assignedDriverName);
        assignedDriverCarModel = findViewById(R.id.assignedDriverCarModel);
        sendVoiceMessageToDriver = findViewById(R.id.sendVoiceMessageToDriver);
        sendTextMessageToDriver = findViewById(R.id.sendTextMessageToDriver);
        assignedDriverProfilePic = findViewById(R.id.assignedDriverProfilePic);
        assignedDriverRating = findViewById(R.id.assignedDriverRating);
        assignedDriverTotalLifeTimeTrips = findViewById(R.id.assignedDriverTotalLifeTimeTrips);
        assignedDriverVehicleRegNumber = findViewById(R.id.assignedDriverVehicleRegNumber);
        paymentMethodForAssignedDriver = findViewById(R.id.paymentMethodForAssignedDriver);
        assignedDriverDistance = findViewById(R.id.assignedDriverDistance);
        assignedDriverETA = findViewById(R.id.assignedDriverETA);
        btnCancelCurrentRide = findViewById(R.id.btnCancelCurrentRide);
        btnCallAssignedDriver = findViewById(R.id.btnCallAssignedDriver);

        ic_up_sign_driver_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int state = driverChatAndInfoBottomSheetBehavior.getState();
                switch (state){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        ic_up_sign_driver_bottom.setImageDrawable(getResources()
                                .getDrawable(R.drawable.ic_botttom_sheet_down));
                        driverChatAndInfoBottomSheetBehavior
                                .setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        ic_up_sign_driver_bottom.setImageDrawable(getResources()
                                .getDrawable(R.drawable.ic_botttom_sheet_up));
                        driverChatAndInfoBottomSheetBehavior
                                .setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                }
            }
        });

        sendVoiceMessageToDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sendTextMessageToDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnCancelCurrentRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Home:btnCancelCurrentRide.called");
                Crashlytics.log("Home:btnCancelCurrentRide.called");
                Log.i(TAG,"Home:btnCancelCurrentRide.called");
                rideStatus = 0;
                endCurrentRideByCustomer();
            }
        });

        btnCallAssignedDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + strAssignedDriverPhoneNumber));
                startActivity(callIntent);
            }
        });

        if (driverChatAndInfoBottomSheet != null){
            driverChatAndInfoBottomSheetBehavior = BottomSheetBehavior
                    .from(driverChatAndInfoBottomSheet);
            driverChatAndInfoBottomSheetBehavior.setBottomSheetCallback
                    (new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            switch (newState){
                                case BottomSheetBehavior.STATE_COLLAPSED:{

                                    Log.d("CheckBottomSheet","collapsed") ;
                                }
                                case BottomSheetBehavior.STATE_SETTLING:{

                                    Log.d("CheckBottomSheet","settling") ;
                                }
                                case BottomSheetBehavior.STATE_EXPANDED:{

                                    Log.d("CheckBottomSheet","expanded") ;
                                }
                                case BottomSheetBehavior.STATE_HIDDEN: {
                                    Log.d("CheckBottomSheet" , "hidden") ;
                                }
                                case BottomSheetBehavior.STATE_DRAGGING: {
                                    Log.d("CheckBottomSheet","dragging") ;
                                }
                            }
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                        }
                    });
        } else {
            Log.i(TAG,"driverChatAndInfoBottomSheet = null");
        }

    }

    private void destriyAllTheViewsFromDriverChatAndInfoBottomSheet(){
        driverChatAndInfoBottomSheet = null;
        ic_up_sign_driver_bottom = null;
        edtDriverChatWindow = null;
        assignedDriverProfilePic = null;
        assignedDriverName = null;
        assignedDriverCarModel = null;
        sendVoiceMessageToDriver = null;
        sendTextMessageToDriver = null;
        assignedDriverProfilePic = null;
        assignedDriverRating = null;
        assignedDriverTotalLifeTimeTrips = null;
        assignedDriverVehicleRegNumber = null;
        paymentMethodForAssignedDriver = null;
        assignedDriverDistance = null;
        assignedDriverETA = null;
        btnCancelCurrentRide = null;
        btnCallAssignedDriver = null;
    }

    private boolean setUpAllTheViewsFromCheckAddress = false;
    private void setUpAllTheViewsFromCheckAddress(){
        setUpAllTheViewsFromCheckAddress = true;
        checkAddress = findViewById(R.id.checkAddress);
        checkAddress.setVisibility(View.VISIBLE);
        relativeLayOutpickUpDestinationActualDummyBox.setVisibility(View.GONE);
        ic_back_sign1 = findViewById(R.id.ic_back_sign1);
        checkPickUpAddress = findViewById(R.id.checkPickUpAddress);
        checkDestinationAddress = findViewById(R.id.checkDestinationAddress);
        checkDistance = findViewById(R.id.checkDistance);
        editRequest = findViewById(R.id.editRequest);
        requestedPickUpAndDestinationAddressOK = findViewById
                (R.id.requestedPickUpAndDestinationAddressOK);


        checkPickUpAddress.setText(customerPickUpAddress);
        checkDestinationAddress.setText(customerDestinationAdresss);
        checkDistance.setText(travelDistance.toString() + " k.m");

        ic_back_sign1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAddress.setVisibility(View.GONE);
                customer_ride_request.setVisibility(View.VISIBLE);
                relativeLayOutpickUpDestinationActualDummyBox.setVisibility(View.VISIBLE);
                mMap.clear();
                isCarAnimationShowingToUser = false;
                imgDeleteDummyDestination.setEnabled(true);
                imgDeleteDummyPickUp.setEnabled(true);
                edtDummyPickUpSelector.setText("");
                edtDummyDestinationSelector.setText("");
                createPickUpAndDestinationMarkerObject();
            }
        });

        editRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAddress.setVisibility(View.GONE);
                customer_ride_request.setVisibility(View.VISIBLE);
                relativeLayOutpickUpDestinationActualDummyBox.setVisibility(View.VISIBLE);
                mMap.clear();
                isCarAnimationShowingToUser = false;
                imgDeleteDummyDestination.setEnabled(true);
                imgDeleteDummyPickUp.setEnabled(true);
                edtDummyPickUpSelector.setText("");
                edtDummyDestinationSelector.setText("");
                createPickUpAndDestinationMarkerObject();
            }
        });

        requestedPickUpAndDestinationAddressOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAddress.setVisibility(View.GONE);
                setUpAllTheViewsFromSelectYourRide();
            }
        });
    }

    private boolean setUpAllTheViewsFromSelectYourRide = false;
    private void setUpAllTheViewsFromSelectYourRide(){
        setUpAllTheViewsFromSelectYourRide = true;
        selectBikeOrCar = findViewById(R.id.selectBikeOrCar);
        selectBikeOrCar.setVisibility(View.VISIBLE);
        ic_back_signFromSelectYourRide = findViewById(R.id.ic_back_signFromSelectYourRide);
        selectMotorCycle = findViewById(R.id.selectMotorCycle);
        selectCar = findViewById(R.id.selectCar);

        ic_back_signFromSelectYourRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBikeOrCar.setVisibility(View.GONE);
                checkAddress.setVisibility(View.VISIBLE);
            }
        });

        selectMotorCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBikeOrCar.setVisibility(View.GONE);
                setUpAllTheViewsFromSelectForBike();
            }
        });

        selectCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBikeOrCar.setVisibility(View.GONE);
                setUpAllTheViewsFromSelectForCar();
            }
        });

    }

    private boolean setUpAllTheViewsFromSelectForCar = false;
    private void setUpAllTheViewsFromSelectForCar(){
        setUpAllTheViewsFromSelectForCar = true;
        selectServiceForCar = findViewById(R.id.selectServiceForCar);
        selectServiceForCar.setVisibility(View.VISIBLE);
        ic_back_sign_select_service_for_car = findViewById
                (R.id.ic_back_sign_select_service_for_car);
        selectEconomyServiceForCar = findViewById(R.id.selectEconomyServiceForCar);
        selectPremiumServiceForCar = findViewById(R.id.selectPremiumServiceForCar);
        economyServiceCostForCar = findViewById(R.id.economyServiceCostForCar);
        premiumServiceCostForCar = findViewById(R.id.premiumServiceCostForCar);

        Double totalCostOfEconomyServiceForCar = (
                economyCarBaseFair +
                        (travelDistance * economyCarPerKmFair) +
                        (travelTime * economyCarWaitingTimePerMin));
        Double totalCostOfPremiumServiceForCar = (
                premiumCarBaseFair +
                        (travelDistance * premiumCarPerKmFair) +
                        (travelTime * premiumCarWaitingTimePerMin));

        Log.i("CheckFair","Economy Base Fair For Car: " + economyCarBaseFair + "\n" +
        "Distance Fair For Car: " + travelDistance + " k.m * " + economyCarPerKmFair + " tk = "
                + (travelDistance *economyCarPerKmFair) + " tk\n" +
        "Waiting Time Fair For Car: " + travelTime + " min * " + economyCarWaitingTimePerMin +
        " tk = " + (travelTime * economyCarWaitingTimePerMin) + " tk\n" +
        "Total Fair: " + totalCostOfEconomyServiceForCar + " tk");

        Log.i("CheckFair","Premium Base Fair For Car: " + premiumCarBaseFair + "\n" +
                "Distance Fair For Car: " + travelDistance + " k.m * " + premiumCarPerKmFair + " tk = "
                + (travelDistance * premiumCarPerKmFair) + " tk\n" +
                "Waiting Time Fair For Car: " + travelTime + " min * " + premiumCarWaitingTimePerMin +
                " tk = " + (travelTime * premiumCarWaitingTimePerMin) + " tk\n" +
                "Total Fair: " + totalCostOfPremiumServiceForCar + " tk");

        economyServiceCostForCar.setText(String.valueOf(new DecimalFormat("##.##").format(totalCostOfEconomyServiceForCar)));
        premiumServiceCostForCar.setText(String.valueOf(new DecimalFormat("##.##").format(totalCostOfPremiumServiceForCar)));

        ic_back_sign_select_service_for_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBikeOrCar.setVisibility(View.VISIBLE);
                selectServiceForCar.setVisibility(View.GONE);
            }
        });
        selectEconomyServiceForCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectServiceForCar.setVisibility(View.GONE);
                setUpAllTheViewsFromSelectPaymentMethod();
                selectedVehicle = "Car";
                selectedService = "Economy";
            }
        });
        selectPremiumServiceForCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectServiceForCar.setVisibility(View.GONE);
                setUpAllTheViewsFromSelectPaymentMethod();
                selectedVehicle = "Car";
                selectedService = "Premium";
            }
        });
    }


    private boolean setUpAllTheViewsFromSelectForBike = false;
    private void setUpAllTheViewsFromSelectForBike(){
        setUpAllTheViewsFromSelectForBike= true;
        selectServiceForBike = findViewById(R.id.selectServiceForBike);
        selectServiceForBike.setVisibility(View.VISIBLE);
        ic_back_sign_select_service_for_bike = findViewById
                (R.id.ic_back_sign_select_service_for_bike);
        selectEconomyServiceForBike = findViewById(R.id.selectEconomyServiceForBike);
        selectPremiumServiceForBike = findViewById(R.id.selectPremiumServiceForBike);
        economyServiceCostForBike = findViewById(R.id.economyServiceCostForBike);
        premiumServiceCostForBike = findViewById(R.id.premiumServiceCostForBike);

        Double totalCostOfEconomyServiceForBike = (
                economyBikeBaseFair +
                        (travelDistance * economyBikePerKmFair) +
                        (travelTime * economyBikeWaitingTimePerMin));
        Double totalCostOfPremiumServiceForBike = (
                premiumBikeBaseFair +
                        (travelDistance * premiumBikePerKmFair) +
                        (travelTime * premiumBikeWaitingTimePerMin));

        Log.i("CheckFair","Economy Base Fair For Bike: " + economyBikeBaseFair + "\n" +
                "Distance Fair For Bike: " + travelDistance + " k.m * " + economyBikePerKmFair + " tk = "
                + (travelDistance * economyBikePerKmFair) + " tk\n" +
                "Waiting Time Fair For Bike: " + travelTime + " min * " + economyBikeWaitingTimePerMin +
                " tk = " + (travelTime * economyBikeWaitingTimePerMin) + " tk\n" +
                "Total Fair: " + totalCostOfEconomyServiceForBike + " tk");

        Log.i("CheckFair","Premium Base Fair For Bike: " + premiumBikeBaseFair + "\n" +
                "Distance Fair For Bike: " + travelDistance + " k.m * " + premiumBikePerKmFair + " tk = "
                + (travelDistance * premiumBikePerKmFair) + " tk\n" +
                "Waiting Time Fair For Bike: " + travelTime + " min * " + premiumBikeWaitingTimePerMin +
                " tk = " + (travelTime * premiumBikeWaitingTimePerMin) + " tk\n" +
                "Total Fair: " + totalCostOfPremiumServiceForBike + " tk");

        economyServiceCostForBike.setText(String.valueOf(new DecimalFormat("##.##").format(totalCostOfEconomyServiceForBike)));
        premiumServiceCostForBike.setText(String.valueOf(new DecimalFormat("##.##").format(totalCostOfPremiumServiceForBike)));

        ic_back_sign_select_service_for_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBikeOrCar.setVisibility(View.VISIBLE);
                selectServiceForBike.setVisibility(View.GONE);
            }
        });
        selectEconomyServiceForBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectServiceForBike.setVisibility(View.GONE);
                setUpAllTheViewsFromSelectPaymentMethod();
                selectedVehicle = "Bike";
                selectedService = "Economy";
            }
        });
        selectPremiumServiceForBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectServiceForBike.setVisibility(View.GONE);
                setUpAllTheViewsFromSelectPaymentMethod();
                selectedVehicle = "Bike";
                selectedService = "Premium";
            }
        });
    }


    private void setUpAllTheViewsFromSelectPaymentMethod(){
        selectPaymentMethod = findViewById(R.id.selectPaymentMethod);
        selectPaymentMethod.setVisibility(View.VISIBLE);
        ic_back_sign_selectPaymentMethod = findViewById(R.id.ic_back_sign_selectPaymentMethod);
        selectUthaoWallet = findViewById(R.id.selectUthaoWallet);
        selectCash = findViewById(R.id.selectCash);

        ic_back_sign_selectPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPaymentMethod.setVisibility(View.GONE);
                if (selectedVehicle.equals("Car")){
                    selectServiceForCar.setVisibility(View.VISIBLE);
                } else if (selectedVehicle.equals("Bike")){
                    selectPaymentMethod.setVisibility(View.GONE);
                    selectServiceForBike.setVisibility(View.VISIBLE);
                }
            }
        });

        selectUthaoWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this,
                        "Uthao wallet is not activated", Toast.LENGTH_LONG).show();
            }
        });

        selectCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedVehicle != null && selectedService != null){
                    selectPaymentMethod.setVisibility(View.GONE);
                    setUpAllTheViewsFromPlaceArequestToUthao();
                }
            }
        });
    }

    private void setUpAllTheViewsFromPlaceArequestToUthao(){
        placeArequestToUthao = findViewById(R.id.placeArequestToUthao);
        placeArequestToUthao.setVisibility(View.VISIBLE);
        ic_back_signForPlacingRequestToUthao = findViewById
                (R.id.ic_back_signForPlacingRequestToUthao);
        cancelRequest = findViewById(R.id.cancelRequest);
        callToUthao = findViewById(R.id.callToUthao);

        ic_back_signForPlacingRequestToUthao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeArequestToUthao.setVisibility(View.GONE);
                selectPaymentMethod.setVisibility(View.VISIBLE);
            }
        });

        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeArequestToUthao.setVisibility(View.GONE);
                selectedService = null;
                selectedVehicle = null;
                customer_ride_request.setVisibility(View.VISIBLE);
                relativeLayOutpickUpDestinationActualDummyBox.setVisibility(View.VISIBLE);
                mMap.clear();
                isCarAnimationShowingToUser = false;
                imgDeleteDummyDestination.setEnabled(true);
                imgDeleteDummyPickUp.setEnabled(true);
                edtDummyPickUpSelector.setText("");
                edtDummyDestinationSelector.setText("");
                createPickUpAndDestinationMarkerObject();
            }
        });

        callToUthao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedVehicle != null && selectedService != null){
                    //This below code will keep screen on when user is in the home activity
                    //user is on call.
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    driverWhoHaveDecline.clear();
                    isCustomerRequestForDriverHasTimedOut = false;
                    isCustomerOrDriverForcedStopRequest = false;
                    placeArequestToUthao.setVisibility(View.GONE);
                    getClossestDriver(selectedVehicle,selectedService);
                    Handler cancelCustomerRequest = new Handler();

                    Runnable cancelCustomerReqRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (!driverFound && !isCustomerOrDriverForcedStopRequest){
                                isCustomerRequestForDriverHasTimedOut = true;
                                //If our customer don't have any driver than
                                //execution will enter this line of code
                                driverFound = false;
                                driverFoundID = null;
                                customerRequestForDriverHasTimedOut();
                            }

                        }
                    };

                    cancelCustomerRequest.postDelayed(cancelCustomerReqRunnable,
                            30 * 1000);

                }
            }
        });
    }

    private void customerRequestForDriverHasTimedOut() {
        endCurrentRideByCustomer();
    }


    public void startFireBaseJobDispatcherBackGroundJob() {
        fireBaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(Home.this));
        Job job = fireBaseJobDispatcher.newJobBuilder().
                // the JobService that will be called
                        setService(FireBaseJobDispatcherBackGroundJob.class)
                // uniquely identifies the job
                .setTag(JOB_TAG)
                //Same job will run again and again
                .setRecurring(true)
                // don't persist past a device reboot
                //.setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                // job will run after device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 30 and 32 minutes from now
                .setTrigger(Trigger.executionWindow(60*30, 60*32))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run on an unmetered network
                        //Constraint.ON_UNMETERED_NETWORK,
                        // only run when the device is charging
                        //Constraint.DEVICE_CHARGING
                        Constraint.ON_ANY_NETWORK)
                .build();
        fireBaseJobDispatcher.mustSchedule(job);
    }

    private void updateCustomerLastLocationAndTimeToServer() {
        customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mCustomerLastUpdateRef= FirebaseDatabase.getInstance().
                getReference("Users/Customers").child(customerUID)
                .child("Customer_Last_Update");
        HashMap<String, Object> map = new HashMap();
        map.put("Customer_Last_Latitude",mLastLocation.getLatitude());
        map.put("Customer_Last_Longitude",mLastLocation.getLongitude());
        map.put("Customer_Last_Home_Activity",getDate(getCurrentTimeStamp()));

        mCustomerLastUpdateRef.updateChildren(map).addOnCompleteListener
                (new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseCrash.log("Home:RealDB:CustomerLastLocation Update: Success");
                            Crashlytics.log("Home:RealDB:CustomerLastLocation Update: Success");
                        }else {
                            FirebaseCrash.log("Home:RealDB:CustomerLastLocation Update: failed");
                            Crashlytics.log("Home:RealDB:CustomerLastLocation Update: failed");
                        }
                    }
                });


        //Now put those data in fire store by update method.
        FirebaseFirestore mFireStoreCustomerBasicInfoDB =FirebaseFirestore.getInstance();
        mFireStoreCustomerBasicInfoDB.collection("Users").document("Customers").
                collection(customerUID)
                .document("Customer_Last_Update")
                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Home:fireStore:CustomerLastLocation Update: Success");
                    Crashlytics.log("Home:fireStore:CustomerLastLocation Update: Success");
                }else {
                    FirebaseCrash.log("Home:fireStore:CustomerLastLocation Update: failed");
                    Crashlytics.log("Home:fireStore:CustomerLastLocation Update: failed");
                }
            }
        });
    }


    public void fabricForceCrash() {
        throw new RuntimeException("This is a crash");
    }

    private void sendFCMTokenToServer() {
        FirebaseCrash.log("Home:fireStore:sendFCMTokenToServer.called");
        Crashlytics.log("Home:fireStore:sendFCMTokenToServer.called");
        SharedPreferences preferences = getSharedPreferences("FCM_TOKEN",
                Context.MODE_PRIVATE);
        String lattesToken =preferences.getString("FCM_LATEST_TOKEN",DEFAULT);//For DEFAULT see top of the code

        customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mCustomerFcmTokenRef = FirebaseDatabase.getInstance()
                .getReference("Users/Customers").child(customerUID).child("FCM_LATEST_TOKEN");
        mCustomerFcmTokenRef.setValue(lattesToken).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Home:realTimeDB:sendFCMTokenToServer: success");
                    Crashlytics.log("Home:realTimeDB:sendFCMTokenToServer: success");
                } else {
                    FirebaseCrash.log("Home:realTimeDB:sendFCMTokenToServer: failed");
                    Crashlytics.log("Home:realTimeDB:sendFCMTokenToServer: failed");
                }
            }
        });

        //Now put those data in fire store by update method.
        //If we don't use update method here, previously put Customer_UID
        //will be deleted
        Map<String, Object> map = new HashMap<>();
        map.put("FCM_LATEST_TOKEN",lattesToken);
        FirebaseFirestore mFireStoreCustomerBasicInfoDB =FirebaseFirestore.getInstance();
        mFireStoreCustomerBasicInfoDB.collection("Users").document("Customers").collection(customerUID)
                .document("FCM_LATEST_TOKEN")
                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Home:fireStore:sendFCMTokenToServer: success");
                    Crashlytics.log("Home:fireStore:sendFCMTokenToServer: success");
                } else {
                    FirebaseCrash.log("Home:fireStore:sendFCMTokenToServer: failed");
                    Crashlytics.log("Home:fireStore:sendFCMTokenToServer: failed");
                }
            }
        });
    }

    private void dataFoundInSharedPreference() {
        FirebaseCrash.log("Home:dataFoundInSharedPreference.called");
        Crashlytics.log("Home:dataFoundInSharedPreference.called");
        SharedPreferences preferences = getSharedPreferences("Customer_Basic_Info",
                Context.MODE_PRIVATE);
        String name =preferences.getString("Customer_Name",DEFAULT);//For DEFAULT see top of the code
        String email =preferences.getString("Customer_Email",DEFAULT);//For DEFAULT see top of the code
        String mobile =preferences.getString("Customer_Mobile",DEFAULT);//For DEFAULT see top of the code
        String password = preferences.getString("Customer_Password",DEFAULT);//For DEFAULT see top of the code
        String Customer_FaceBook_PP = preferences.getString("Customer_FaceBook_PP",DEFAULT);//For DEFAULT see top of the code
        Log.i("Check","Name: " + name +"\n" +
                "email: " + email +"\n" +
                "mobile: " + mobile +"\n" +
                "password: " + password +"\n" +
                "Customer_FaceBook_PP: " + Customer_FaceBook_PP +"\n");

    }

    @Override
    @AddTrace(name = "onMapReady", enabled = true/*Optional*/)
    public void onMapReady(GoogleMap googleMap) {
        FirebaseCrash.log("Home:onMapReady.called");
        Crashlytics.log("Home:onMapReady.called");
        //Load custom map like UBER
        //********START************
        try {
            boolean isSuccess = googleMap.setMapStyle
                    (MapStyleOptions.loadRawResourceStyle
                            (Home.this, R.raw.uthao_map));
        } catch (Resources.NotFoundException ex) {
            ex.printStackTrace();
        }
        //********END**************
        mMap = googleMap;
        mService = Common.getGoogleApi(); //Initialize mService
        ifcmService = Common.getFCMService(); //Initialize device to device FCM service
        Log.i("CustomerMapsActivity", "Map displayed");
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Ask for location permission
            permissions.requestAllPermission(Home.this);
            return;
        }
        Log.i("Check","Home: location permission: Granted");
        //Above permission check is for below code.
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        myMapLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Home:myMapLocationButton.clicked");
                Crashlytics.log("Home:myMapLocationButton.clicked");
                if (mLastLocationLatLng != null){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLastLocationLatLng,16.05f));
                }
            }
        });
        navigationDrawerCustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Home:navigationDrawerCustomButton.clicked");
                Crashlytics.log("Home:navigationDrawerCustomButton.clicked");
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        customer_ride_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //isCarAnimationShowingToUser == true means we can not freely put
                //pickup and destination marker on the map
                isCarAnimationShowingToUser = true;
                imgDeleteDummyDestination.setEnabled(false);
                imgDeleteDummyPickUp.setEnabled(false);
                FirebaseCrash.log("Home:customer_ride_request.clicked");
                Crashlytics.log("Home:customer_ride_request.clicked");
                if (edtDummyPickUpSelector.getText().toString().equals("")){
                    Toast.makeText(Home.this,"Please enter pickup location",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (edtDummyDestinationSelector.getText().toString().equals("")){
                    Toast.makeText(Home.this,"Please enter destination location",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                getDirectionUsingGoogleDirection(pickUpLocationLatLng,customerDestinationLatLng);
                myTrace = FirebasePerformance.getInstance().newTrace("test_trace");
                myTrace.start();
                //Execution will enter this block of code when customer will press the button to
                //call uthao.
                strCarType = getUserChoiceOfCarType();
                callUthaoStatus = true;
                customer_ride_request.setVisibility(View.GONE);

            }
        });



//        allCodeAboutGooglePlaceAutoComplete();

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onCameraMove() {
                double lat= mMap.getCameraPosition().target.latitude;
                double lng = mMap.getCameraPosition().target.longitude;
                LatLng cameraPositionLatLng = new LatLng(lat,lng);
                //This 'cameraPositionLatLng' variable will always give the center of the screen
                //LatLng. So we could set up or move our pick up or destination location keeping marker
                //center on the screen.

                if (pickUpMarker != null && !isPickUpMarkerFrozen && !isCarAnimationShowingToUser){
                    pickUpMarker.setPosition(cameraPositionLatLng);
                    //This setPosition() method will always set marker on the center of the
                    //screen
                    Bundle params = new Bundle();
                    params.putString("pickMarker_isMoving", cameraPositionLatLng.toString());
                    mFirebaseAnalytics.logEvent("pickMarker_isMoving", params);
                }

                if (destinationMarker != null && !isDestinationMarkerFrozen && !isCarAnimationShowingToUser){
                    destinationMarker.setPosition(cameraPositionLatLng);
                    Bundle params = new Bundle();
                    params.putString("destMarker_isMoving", cameraPositionLatLng.toString());
                    mFirebaseAnalytics.logEvent("destMarker_isMoving", params);
                }
            }
        });


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                double latitude= mMap.getCameraPosition().target.latitude;
                double longitude = mMap.getCameraPosition().target.longitude;
                LatLng cameraPositionLatLng = new LatLng(lat,lng);
                //This 'cameraPositionLatLng' variable will always give the center of the screen
                //LatLng. So we could set up or move our pick up or destination location keeping marker
                //center on the screen.

                if (actualPickUpSelectorLayOut.getVisibility() == View.VISIBLE ||
                        actualDestinationSelectorLayOut.getVisibility() == View.VISIBLE){

                } else {

                }

                //***************************************Reverse GeoCoding**************************
                //*********************************************START********************************

                if (pickUpMarker != null && !isPickUpMarkerFrozen && !isCarAnimationShowingToUser){
                    //isPickUpMarkerFrozen = false means customer touched
                    //edtDummyPickUpSelector edit test box
                    LatLng pickUp = pickUpMarker.getPosition();
                    getFullGeoAddressFromLatitudeAndLongitude(pickUp.latitude,pickUp.longitude);
                }
                if (destinationMarker != null && !isDestinationMarkerFrozen && !isCarAnimationShowingToUser){
                    //isDestinationMarkerFrozen = false means customer touched
                    //edtDummyDestinationSelector edit test box
                    LatLng destination = destinationMarker.getPosition();
                    getFullGeoAddressFromLatitudeAndLongitude(destination.latitude,destination.longitude);
                }
                //***************************************Reverse GeoCoding**************************
                //*********************************************END********************************

                //**************************************Take Map Snap Shot**************************
                //********************************************Start*********************************
                if (nowIsTheTimeToTakeSnapShot){
                    //Execution will enter this block of code only when above if statement is true
                    nowIsTheTimeToTakeSnapShot = false;
                    mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                        @Override
                        public void onSnapshotReady(Bitmap bitmap) {
                            customerMapSnapShot = getResizedBitmap(bitmap,
                                    (int) (getScreenWidth()*0.30),
                                    (int) (getScreenHeight()*0.30));
                            //Save customer ride time and google map screen shot into
                            //SQLite database.
                            saveCustomerDummyRideHisDataToSQLiteDatabase
                                    (customerDummyRideId,
                                            getBytes(customerMapSnapShot));
                        }
                    });

                }
                //**************************************Take Map Snap Shot**************************
                //********************************************End*********************************

            }
        });
        btnFairEstimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Home:btnFairEstimation.clicked");
                Crashlytics.log("Home:btnFairEstimation.clicked");
                switch (customer_request_type){
                    case 1:
                        if (edtDummyPickUpSelector.getText().toString().equals("")){
                            Toast.makeText(Home.this,"Please enter pickup location",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (edtDummyDestinationSelector.getText().toString().equals("")){
                            Toast.makeText(Home.this,"Please enter destination location",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        //customer_request_type == 1 means
                        //customer requested 'Get estimated fair'
                        FirebaseCrash.log("Home:btnFairEstimation.clicked GetEstimatedFair");
                        Crashlytics.log("Home:btnFairEstimation.clicked GetEstimatedFair");
                        // TODO: Use your own attributes to track content views in your app
                        Answers.getInstance().logContentView(new ContentViewEvent()
                                .putContentName("Get estimated fair")
                                .putContentType("Checked fair")
                                .putContentId("1234")
                                .putCustomAttribute("Favorites Count", 20)
                                .putCustomAttribute("Screen Orientation", "Landscape"));

                        DatabaseReference mAllCompletedDummyRideOFUthoaApp = FirebaseDatabase.getInstance().
                                getReference("All_Completed_Dummy_Ride_History");
                        customerDummyRideId  = mAllCompletedDummyRideOFUthoaApp.push().getKey();

                        customerDummyRideDate = getCurrentTimeStamp();
                        //This above will give us an unique id to save this ride.
                        //Now we will save this unique id on customer "Customer_Dummy_Ride_History" database
                        // & also "All_Completed_Dummy_Ride_History" database.

                        //Now add same data to fire store
                        Map<String, Object> dataMapForFireStore = new HashMap<>();
                        dataMapForFireStore.put("Customer_Dummy_Ride_ID", customerDummyRideId);
                        FirebaseFirestore mFireStoreCustomerBasicInfoDB =FirebaseFirestore.getInstance();
                        mFireStoreCustomerBasicInfoDB.collection("Users")
                                .document("All_Completed_Dummy_Ride_History")
                                .set(dataMapForFireStore).addOnCompleteListener
                                (new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    FirebaseCrash.log("Home:fireStore:customerDummyRideId: success");
                                    Crashlytics.log("Home:fireStore:customerDummyRideId: success");
                                } else {
                                    FirebaseCrash.log("Home:fireStore:customerDummyRideId: failed");
                                    Crashlytics.log("Home:fireStore:customerDummyRideId: failed");
                                }
                            }
                        });
                        getDirection();
                        getDummyDriverForFairEstimation();
                        break;
                    case 2:
                        //customer_request_type == 2 means
                        //customer requested 'Get money receipt'
                        FirebaseCrash.log("Home:btnFairEstimation.clicked GetMoneyReceipt");
                        Crashlytics.log("Home:btnFairEstimation.clicked GetMoneyReceipt");
                        stopCarAnimation();
                        recordDummyRideForGeneratingReceipt();
                        showMoneyReceiptForDummyRide();
                        takenMapSnapShotForDummyReceipt = false;
                        nowIsTheTimeToTakeSnapShot = false;
                        btnFairEstimation.setText(R.string.estimate_our_fair);
                        createPickUpAndDestinationMarkerObject();
                        isDestinationMarkerFrozen = true;
                        //isDestinationMarkerFrozen = true this statement is very important, if we
                        //remove this statement 'edtDummyDestinationSelector' setText will show user
                        //current location. We can not fix it by edtDummyDestinationSelector.setText("")
                        //this method.
                        break;
                }
            }
        });

        checkDeviceCurrentMemoryStatus();
        getThisDeviceResoulution();
        initializeBottomSheetBehavior();

    }

    private void getThisDeviceResoulution() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Point size = new Point();
        String resolution = "N/A";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(size);
            resolution = size.x + "x" + size.y;
        }

        Log.i("Check","width: " + width + "\n" +
                "height: " + height + "\n" +
        "resolution: " + resolution);
    }

    //**********************************************************************************
    //********************************Btn Get Estimated Fair****************************
    //****************************************START*************************************

    private void getDummyDriverForFairEstimation() {
        FirebaseCrash.log("Home:getDummyDriverForFairEstimation.called");
        Crashlytics.log("Home:getDummyDriverForFairEstimation.called");
        txtDriverCarModelDummyHisSingle = findViewById(R.id.txtDriverCarModelDummyHisSingle);
        txtDriverNameDummyHisSingle = findViewById(R.id.txtDriverNameDummyHisSingle);
        imgDriverProfilePicDummyHisSingle = findViewById(R.id.imgDriverProfilePicDummyHisSingle);
        rtnDriverRatingDummyHisSingle = findViewById(R.id.rtnDriverRatingDummyHisSingle);
        //Rating bar custom color
        //For custom color only using layer drawable to fill the star colors
        LayerDrawable stars = (LayerDrawable) rtnDriverRatingDummyHisSingle
                .getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#969696"),
                PorterDuff.Mode.SRC_ATOP); // for filled stars
        stars.getDrawable(1).setColorFilter(Color.parseColor("#969696"),
                PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.colorTransparentWhite),
                PorterDuff.Mode.SRC_ATOP); // for empty stars

        txtDriverCarModelDummyHisSingle.setText("Toyoto Premio");
        txtDriverNameDummyHisSingle.setText("Kholilur Rahman");
        imgDriverProfilePicDummyHisSingle.setImageResource(R.drawable.ic_profile_pic);
        rtnDriverRatingDummyHisSingle.setRating(4);
    }

    private void saveCustomerDummyRideHisDataToSQLiteDatabase
            (String customerDummyRideDate,
             byte[] imgDummyRideHistoryPolyLIne){
        FirebaseCrash.log("Home:saveCustomerDummyRideHisDataToSQLiteDatabase.called");
        Crashlytics.log("Home:saveCustomerDummyRideHisDataToSQLiteDatabase.called");
        Log.i("LOG", "saveDataToSQLiteDatabase From Main");
        CustomerDummyRideHistoryDbHelper helper = new CustomerDummyRideHistoryDbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        helper.saveDataToSQLiteDatabase(customerDummyRideDate,imgDummyRideHistoryPolyLIne,db);
        helper.close();
    }

    private void updateCustomerDummyRideHisDataToSQLiteDatabase
            (String customerDummyRideId,String strCustomerRideDate,String strDriverCarModel,
             String strTotalCostOfTheRide, String strPaymentMode,
             String strCustomerPickUpLocation, String strCustomerDestinationLocation,
             String strCustomerRideDistance, String strDriverName,
             String strTotalDistanceCost, String strTotalMinutes,
             String strTotalMinutesCost, String strTotalCostWithoutDiscount,
             String strTotalDiscount, String strTotalCostAfterDiscount,
             byte[] imgDriverProfilePic,int intDriverRating){
        FirebaseCrash.log("Home:updateCustomerDummyRideHisDataToSQLiteDatabase.called");
        Crashlytics.log("Home:updateCustomerDummyRideHisDataToSQLiteDatabase.called");
        CustomerDummyRideHistoryDbHelper helper = new CustomerDummyRideHistoryDbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        helper.updateSQLiteDatabase(customerDummyRideId,
                strCustomerRideDate,strDriverCarModel,
                strTotalCostOfTheRide, strPaymentMode,
                strCustomerPickUpLocation, strCustomerDestinationLocation,
                strCustomerRideDistance, strDriverName,
                strTotalDistanceCost, strTotalMinutes,
                strTotalMinutesCost, strTotalCostWithoutDiscount,
                strTotalDiscount, strTotalCostAfterDiscount,
        imgDriverProfilePic,intDriverRating, db);
        helper.close();
    }

    private void initializeBottomSheetBehavior() {
        FirebaseCrash.log("Home:initializeBottomSheetBehavior.called");
        Crashlytics.log("Home:initializeBottomSheetBehavior.called");
        txtTotalCostOfTheRideDummyHisSingle = findViewById(R.id.txtTotalCostOfTheRideDummyHisSingle);
        txtCustomerRideDateDummyHisSingle = findViewById(R.id.txtCustomerRideDateDummyHisSingle);
        txtPaymentModeDummyHisSingle = findViewById(R.id.txtPaymentModeDummyHisSingle);
        txtCustomerPickUpLocationDummyHisSingle = findViewById(R.id.txtCustomerPickUpLocationDummyHisSingle);
        txtCustomerDestinationLocationDummyHisSingle = findViewById(R.id.txtCustomerDestinationLocationDummyHisSingle);
        imgDriverProfilePicDummyHisSingle = findViewById(R.id.imgDriverProfilePic);
        imgDummyRideHistoryPolyLIneDummyHisSingle = findViewById(R.id.imgDummyRideHistoryPolyLIneDummyHisSingle);
        ic_back_sign_from_money_receipt = findViewById(R.id.ic_back_sign_from_money_receipt);
        ic_back_sign_from_money_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/Amandella.ttf");
        //Below those textViews font will be changed
        receiptUnit = findViewById(R.id.receiptUnit);
        receiptUnit.setTypeface(tf);
        receiptFare = findViewById(R.id.receiptFare);
        receiptFare.setTypeface(tf);
        receiptTotal = findViewById(R.id.receiptTotal);
        receiptTotal.setTypeface(tf);
        receiptBaseFare = findViewById(R.id.receiptBaseFare);
        receiptBaseFare.setTypeface(tf);
        receiptBaseFareDistance = findViewById(R.id.receiptBaseFareDistance);
        receiptBaseFareDistance.setTypeface(tf);
        receiptBaseFarePerCosting = findViewById(R.id.receiptBaseFarePerCosting);
        receiptBaseFarePerCosting.setTypeface(tf);
        receiptBaseFareTotalCosting = findViewById(R.id.receiptBaseFareTotalCosting);
        receiptBaseFareTotalCosting.setTypeface(tf);
        receiptTotalDistance = findViewById(R.id.receiptTotalDistance);
        receiptTotalDistance.setTypeface(tf);
        txtCustomerRideDistanceDummyHisSingle = findViewById(R.id.txtCustomerRideDistanceDummyHisSingle);
        txtCustomerRideDistanceDummyHisSingle.setTypeface(tf);
        receiptTotalDistancePerUnitCosting = findViewById(R.id.receiptTotalDistancePerUnitCosting);
        receiptTotalDistancePerUnitCosting.setTypeface(tf);
        receiptTotalDistanceTotalCosting = findViewById(R.id.receiptTotalDistanceTotalCosting);
        receiptTotalDistanceTotalCosting.setTypeface(tf);
        receiptTotalMinutes = findViewById(R.id.receiptTotalMinutes);
        receiptTotalMinutes.setTypeface(tf);
        receiptTotalMinutesQuantity = findViewById(R.id.receiptTotalMinutesQuantity);
        receiptTotalMinutesQuantity.setTypeface(tf);
        receiptTotalMinutesPerUnitCosting = findViewById(R.id.receiptTotalMinutesPerUnitCosting);
        receiptTotalMinutesPerUnitCosting.setTypeface(tf);
        receiptTotalMinutesTotalCosting = findViewById(R.id.receiptTotalMinutesTotalCosting);
        receiptTotalMinutesTotalCosting.setTypeface(tf);
        receiptTotalFareWithoutDiscount = findViewById(R.id.receiptTotalFareWithoutDiscount);
        receiptTotalFareWithoutDiscount.setTypeface(tf);
        receiptTotalDiscountText = findViewById(R.id.receiptTotalDiscountText);
        receiptTotalDiscountText.setTypeface(tf);
        receiptTotalDiscountAmountInThisRide = findViewById(R.id.receiptTotalDiscountAmountInThisRide);
        receiptTotalDiscountAmountInThisRide.setTypeface(tf);
        receiptTotalText = findViewById(R.id.receiptTotalText);
        receiptTotalText.setTypeface(tf);
        receiptTotalFareAfterDiscount = findViewById(R.id.receiptTotalFareAfterDiscount);
        receiptTotalFareAfterDiscount.setTypeface(tf);
        setUpTabLayOut();
        changeTabHostTitleColorAndUnderlineColor();
        bottomSheetBehavior = BottomSheetBehavior
                .from(findViewById(R.id.bottomSheetLinearLayout));
        bottomSheetBehavior.setBottomSheetCallback(
                new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_COLLAPSED:{

                        Log.d("Check","collapsed") ;
                    }
                    case BottomSheetBehavior.STATE_SETTLING:{

                        Log.d("Check","settling") ;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED:{

                        Log.d("Check","expanded") ;
                    }
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        Log.d("Check" , "hidden") ;
                    }
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        btnFairEstimation.setVisibility(View.VISIBLE);
                        Log.d("Check","dragging") ;
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("Check","BottomSheet Slide: " + slideOffset);
            }
        });
    }

    private void setUpTabLayOut() {
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec(getResources().getString(R.string.money_receipt));
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.money_receipt));
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec(getResources().getString(R.string.complain));
        spec.setContent(R.id.tab2);
        spec.setIndicator(getResources().getString(R.string.complain));
        tabHost.addTab(spec);

        //Event
        tabHost.setOnTabChangedListener(new TabAnimation(this,tabHost));
    }

    private void changeTabHostTitleColorAndUnderlineColor() {
        //Change TabHost title color and underline color
        TabHost tabhost = getTabHost();
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        {
            //Change Title color
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
            //Change Title underline color
            View v = tabhost.getTabWidget().getChildAt(i);
            v.setBackgroundResource(R.drawable.tab_widget_bottom_indicator_by_xml);
        }
    }

    private TabHost getTabHost() {
        return tabHost = findViewById(R.id.tabHost);
    }

    private void showMoneyReceiptForDummyRide() {
        FirebaseCrash.log("Home:showMoneyReceiptForDummyRide.called");
        Crashlytics.log("Home:showMoneyReceiptForDummyRide.called");
//        bottomSheetFragment = new BottomSheetFragment();
//        bottomSheetFragment.show(getSupportFragmentManager(),
//                "BottomSheet Fragment");
        String customerDummyPickupLocation,customerDummyDestinationLocation;
        customerDummyPickupLocation = customerPickUpAddress;
        customerDummyDestinationLocation  = customerDestinationAdresss;
        Double distanceFair = travelDistance * economyCarPerKmFair;
        Double waitingTimeFair = travelTime * economyCarWaitingTimePerMin;
        totalDummyFair = economyCarBaseFair + distanceFair + waitingTimeFair;

        if (customerMapSnapShot != null){
            imgDummyRideHistoryPolyLIneDummyHisSingle.setImageBitmap(customerMapSnapShot);
        }

        updateCustomerDummyRideHisDataToSQLiteDatabase(customerDummyRideId,
                getDate(customerDummyRideDate), "Toyota Allion",
                String.valueOf(totalDummyFair),"CASH",customerDummyPickupLocation,
                customerDummyDestinationLocation,
                String.format("%s%s",new DecimalFormat("##.##").format(travelDistance), getString(R.string.Km)),
                "Kholilur Rahman",
                String.format("%s%s",new DecimalFormat("##.##").format(distanceFair),getString(R.string.Taka)),
                String.format("%s%s",new DecimalFormat("##.##").format(travelTime),getString(R.string.miuntes)),
                String.format("%s%s",new DecimalFormat("##.##").format(waitingTimeFair),getString(R.string.Taka)),
                String.format("%s%s", new DecimalFormat("##.##").format(totalDummyFair), getString(R.string.Taka)),
                String.format("%s%s",new DecimalFormat("##.##").format(totalDummyFair * 0.20), getString(R.string.Taka)),
                String.format("%s%s",new DecimalFormat("##.##").format(totalDummyFair * 0.80), getString(R.string.Taka)),
                null,3);

        txtCustomerRideDateDummyHisSingle.setText(getDate(customerDummyRideDate));
        txtPaymentModeDummyHisSingle.setText(R.string.Cash);
        txtCustomerPickUpLocationDummyHisSingle.setText(customerDummyPickupLocation);
        txtCustomerDestinationLocationDummyHisSingle.setText(customerDummyDestinationLocation);
        txtCustomerRideDistanceDummyHisSingle.setText(String.format("%s%s", new DecimalFormat("##.##").format(travelDistance), getString(R.string.Km)));
        txtTotalCostOfTheRideDummyHisSingle.setText(String.format("%s%s", new DecimalFormat("##.##").format(totalDummyFair *0.80), getString(R.string.Taka)));
        receiptTotalDistanceTotalCosting.setText(String.format("%s%s",new DecimalFormat("##.##").format(distanceFair), getString(R.string.Taka)));
        receiptTotalMinutesQuantity.setText(String.format("%s%s",new DecimalFormat("##.##").format(travelTime), getString(R.string.miuntes)));
        receiptTotalMinutesTotalCosting.setText(String.format("%s%s",new DecimalFormat("##.##").format(waitingTimeFair), getString(R.string.Taka)));
        receiptTotalFareWithoutDiscount.setText(String.format("%s%s", new DecimalFormat("##.##").format(totalDummyFair), getString(R.string.Taka)));
        receiptTotalDiscountAmountInThisRide.setText(String.format("%s%s",new DecimalFormat("##.##").format(totalDummyFair * 0.20), getString(R.string.Taka)));
        receiptTotalFareAfterDiscount.setText(String.format("%s%s",new DecimalFormat("##.##").format(totalDummyFair * 0.80), getString(R.string.Taka)));
        if (bottomSheetLinearLayout.getVisibility() == View.GONE){
            bottomSheetLinearLayout.setVisibility(View.VISIBLE);
        }
        bottomSheetBehavior.setPeekHeight(500);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        btnFairEstimation.setVisibility(View.GONE);
    }

    private void recordDummyRideForGeneratingReceipt() {
        FirebaseCrash.log("Home:recordDummyRideForGeneratingReceipt.called");
        Crashlytics.log("Home:recordDummyRideForGeneratingReceipt.called");
        String customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mCustomerDummyRideHistory = FirebaseDatabase.getInstance()
                .getReference("Users/Customers")
                .child(customerUID)
                .child("Customer_Dummy_Ride_History");
        DatabaseReference mAllCompletedDummyRideOFUthoaApp = FirebaseDatabase.getInstance().
                getReference("All_Completed_Dummy_Ride_History");

        mCustomerDummyRideHistory.child(customerDummyRideId).setValue(true);
//        DatabaseReference mDriverDummyRideHistory = FirebaseDatabase.getInstance().
//                getReference("Users/Drivers")
//                .child(dummyDriverUIDForDummyRide).child("Driver_Dummy_Driving_History");
//        mDriverDummyRideHistory.child(requestUniqueID).setValue(true);

        //Now we will save all the required info of this ride on "All_Completed_Ride_History"
        //database.
        HashMap map = new HashMap();
        map.put("CustomerUID",customerUID);
        map.put("Driver_Rating", 0);
        map.put("Ride_Start_Time", customerDummyRideDate);
//        Log.i("Check","totalDummyFair: " +totalDummyFair);
//        map.put("Total_Payment",totalDummyFair);
//        Log.i("Check","totalDummyFair: " +totalDummyFair);
        map.put("Payment_Method_Used","Cash");
        map.put("PickUp_Location_Address",customerPickUpAddress);
        map.put("Destination_Location_Address",customerDestinationAdresss);
        map.put("Distance",travelDistance);
        map.put("PickUpLatLng",pickUpLocationLatLng);
        map.put("DestinationLatLng",customerDestinationLatLng);
        //map.put("","");
        mAllCompletedDummyRideOFUthoaApp.child(customerDummyRideId).updateChildren(map).
                addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            FirebaseCrash.log("Home:RealTimeDB:DummyRide update: success");
                            Crashlytics.log("Home:RealTimeDB:DummyRide update: success");
                        } else {
                            FirebaseCrash.log("Home:RealTimeDB:DummyRide update: failed");
                            Crashlytics.log("Home:RealTimeDB:DummyRide update: failed");
                        }
                    }
                });

        //Now add same data to fire store
        HashMap<String, Object> dataMapForFireStore = new HashMap();
        dataMapForFireStore.put("CustomerUID",customerUID);
        dataMapForFireStore.put("Driver_Rating", "0");
        dataMapForFireStore.put("Ride_Start_Time", customerDummyRideDate.toString());
//        Log.i("Check","totalDummyFair: fire store " +totalDummyFair);
////        dataMapForFireStore.put("Total_Payment",totalDummyFair.toString());
        dataMapForFireStore.put("Payment_Method_Used","Cash");
        dataMapForFireStore.put("PickUp_Location_Address",customerPickUpAddress);
        dataMapForFireStore.put("Destination_Location_Address",customerDestinationAdresss);
        dataMapForFireStore.put("Distance",travelDistance.toString());
        dataMapForFireStore.put("PickUpLatLng",pickUpLocationLatLng.toString());
        dataMapForFireStore.put("DestinationLatLng",customerDestinationLatLng.toString());
        FirebaseFirestore mFireStoreCustomerBasicInfoDB =FirebaseFirestore.getInstance();
        mFireStoreCustomerBasicInfoDB.collection("Users").document("Customers")
                .collection(customerUID).document("Customer_Dummy_Ride_History")
                .set(dataMapForFireStore)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Home:fireStore:DummyRide update: success");
                    Crashlytics.log("Home:fireStore:DummyRide update: success");
                } else {
                    FirebaseCrash.log("Home:fireStore:DummyRide update: failed");
                    Crashlytics.log("Home:fireStore:DummyRide update: failed");
                }
            }
        });
    }




    //*************************All Code About Image Processing*****************************
    //*************************************Start*******************************************
    // convert from bitmap to byte array
    private byte[] getBytes(Bitmap bitmap) {
        FirebaseCrash.log("Home:getBytes.called");
        Crashlytics.log("Home:getBytes.called");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    private Bitmap getImage(byte[] image) {
        FirebaseCrash.log("Home:getImage.called");
        Crashlytics.log("Home:getImage.called");
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        FirebaseCrash.log("Home:getResizedBitmap.called");
        Crashlytics.log("Home:getResizedBitmap.called");
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private Long getCurrentTimeStamp() {
        FirebaseCrash.log("Home:getCurrentTimeStamp.called");
        Crashlytics.log("Home:getCurrentTimeStamp.called");
        Long time = System.currentTimeMillis()/1000;
        return time;
    }

    private String getDate(Long ride_end_time) {
        FirebaseCrash.log("Home:getDate.called");
        Crashlytics.log("Home:getDate.called");
        //First we will create an object from Calender.class As we are considering
        //Different time zone. This will give us customers local time zone.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(ride_end_time * 1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm",calendar).toString();
        return date;

    }

    private int getScreenWidth() {
        FirebaseCrash.log("Home:getScreenWidth.called");
        Crashlytics.log("Home:getScreenWidth.called");
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight() {
        FirebaseCrash.log("Home:getScreenHeight.called");
        Crashlytics.log("Home:getScreenHeight.called");
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void getMaximumAMountOfVM(){
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int freeMemory = (int) (Runtime.getRuntime().freeMemory() / 1024);
        Log.i("Check","maxMemory: " + maxMemory + "\n" +
                "freeMemory: " + freeMemory);
    }
    //*************************All Code About Image Processing*****************************
    //*************************************End*******************************************


    @AddTrace(name = "createPickUpAndDestinationMarkerObject()", enabled = true/*Optional*/)
    private void createPickUpAndDestinationMarkerObject() {
        FirebaseCrash.log("Home:createPickUpAndDestinationMarkerObject.called");
        Crashlytics.log("Home:createPickUpAndDestinationMarkerObject.called");
        if (mLastLocationLatLng != null){
            customerPickUpMarkerOptions = new MarkerOptions()
                    .position(pickUpLocationLatLng)
                    .title("Pick Up Here")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker));
            pickUpMarker = mMap.addMarker(customerPickUpMarkerOptions);

            customerDestinationMarkerOptions = new MarkerOptions()
                    .position(customerDestinationLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker))
//                    .draggable(true)
                    .title("Destination");
            destinationMarker = mMap.addMarker(customerDestinationMarkerOptions);
        }
    }



    private String getUserChoiceOfCarType() {
        FirebaseCrash.log("Home:getUserChoiceOfCarType.called");
        Crashlytics.log("Home:getUserChoiceOfCarType.called");
//        int selectedRadioButton = customerChoiceOfCarGroup.getCheckedRadioButtonId();
//        final RadioButton radioButton = findViewById(selectedRadioButton);
//        //If customer select a car than return the selected car
//        if (radioButton.getText() != null){
//            return radioButton.getText().toString();
//        }
//        //Other wise return "UthaoX"
        strCarType = "Premium";
        return strCarType;
    }

    private int radius = 1;
    private boolean driverFound = false;
    private String driverFoundID;
    private GeoQuery geoQuery;


    private void getClossestDriver(final String selectedVehicle,
                                   final String selectedService) {
        FirebaseCrash.log("Home:getClossestDriver.called");
        Crashlytics.log("Home:getClossestDriver.called");


        if (!selectedVehicle.equals("") && !selectedService.equals("")
                && !isCustomerRequestForDriverHasTimedOut){
            Log.i(TAG,"selectedVehicle: " + selectedVehicle + "\n" +
            "selectedService: " + selectedService);
            setCustomerRideStatusInDatabase(4);
            //rideStatus = 4; that means our customer is looking for driver

            //Now we will send this customer's status and customer id to our firebase database.
            //this activity open means customer is looking for a ride.
            //GeoFire uses the Firebase database for data storage, allowing query results to be updated
            //in realtime as they change. GeoFire selectively loads only the data near certain locations,
            //keeping your applications light and responsive, even with extremely large datasets.
            DatabaseReference mCustomerDatabaseRef = FirebaseDatabase.getInstance()
                    .getReference("Customer_request");
            GeoFire geoFireForCustomer = new GeoFire(mCustomerDatabaseRef);
            geoFireForCustomer.setLocation(customerUID,
                    new GeoLocation(pickUpLocationLatLng.latitude,
                            pickUpLocationLatLng.longitude), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            Log.i(TAG,"Successfully put customer Geo Query location");
                        }
                    });

            if (geoQuery != null){
                geoQuery.removeAllListeners();
                Log.i(TAG,"getClossestDriver: geoQuery: listener removed");
                //As there is chance of recalling this method again, we have to remove any listener before adding
                //new listener.
            }

            String toBeSearchedDB = null;
            String toBeSearchedServiceType = null;
            String driverOrBiker = null;
            if (selectedVehicle.equals("Car")){
                toBeSearchedDB = "DriversAvailAble";
                Common.driverOrBiker = "Driver";
            } else if (selectedVehicle.equals("Bike")){
                toBeSearchedDB = "BikersAvailAble";
                Common.driverOrBiker = "Biker";
            }

            if (selectedService.equals("Economy")){
                toBeSearchedServiceType = "Economy";
            } else if (selectedService.equals("Premium")){
                toBeSearchedServiceType = "Premium";
            }
            if (toBeSearchedDB == null || toBeSearchedServiceType == null){
                Log.i(TAG,"Null Pointer");
                return;
            }
            DatabaseReference mDriversAvailable = FirebaseDatabase.getInstance()
                    .getReference(toBeSearchedDB)
                    .child(toBeSearchedServiceType);
            //We will find our driver in "DriversAvailAble" child.
            GeoFire geoFireForDriver = new GeoFire(mDriversAvailable);

            geoQuery = geoFireForDriver.queryAtLocation
                    (new GeoLocation(pickUpLocationLatLng.latitude,
                    pickUpLocationLatLng.longitude
            ),radius);
            //We will create center of the customer pick up location.

            final String finalDriverOrBiker = driverOrBiker;
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                    Log.i(TAG,"List of Driver: " + key);
                    //This method will be called whenever geoQuery will match our query requirement. In this
                    //case found driver in certain radius.
                    if (!driverWhoHaveDecline.isEmpty()){
                        //This means we have previously found driver & he has declined
                        for (int i = 0; i <driverWhoHaveDecline.size(); i++){
                            if(key.equals(driverWhoHaveDecline.get(i))){
                                Log.i(TAG,"This driver already declined: " +
                                        driverWhoHaveDecline.get(i));
                            } else {
                                processFoundDriver(key);
                            }
                        }
                    } else {
                        //This means we are finding driver for the first time
                        processFoundDriver(key);
                    }


                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {

                }

                @Override
                public void onGeoQueryReady() {
                    //This method will be called whenever geoQuery will match our query requirement and will collect
                    //all the data or geoQuery could not match our requirement.
                    //In this case if geoQuery find 5 driver in one kilometers radius than it will call onKeyEntered()
                    //method 5 times. After fetching all the driver id it will call this method or other case if
                    //geoQuery could not find any driver in one kilometers radius than after search finish it will
                    //call this method
                    if (!driverFound && ! isCustomerRequestForDriverHasTimedOut){
                        //If driverFound == false than, it will become true for this "!" sign and code execution will
                        //enter this block of code.
                        //This means geoQuery did not find any driver in one kilometers radius. That means we have to
                        //increase our radius.
                        radius++;
                        Log.i(TAG,"Radius: " + radius);
                        getClossestDriver(selectedVehicle,selectedService);
                        //Than we again call this getClossestDriver() method.
                    } else {
//                        Toast.makeText(Home.this,"We have found your driver\nDriver ID: " + driverFoundID,
//                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    Log.i(TAG,"onGeoQueryError: " + error.getMessage());
                }
            });
        }

    }

    private void processFoundDriver(String key) {
        if (!driverFound && !isCustomerRequestForDriverHasTimedOut){
            //If driverFound == false than, it will become true for this "!" sign and code execution will
            //enter this block of code. we are doing this because we want to collect only
            //one driver id from this 1 kilometers radius. So after finding first driver driverFound
            //boolean variable will turn true & code will never enter this block again.
            driverFound = true;
            driverFoundID = key;
            driverWhoHaveDecline.add(driverFoundID);
            FirebaseCrash.log("Home:FoundDriverRadius: " +radius);
            Crashlytics.log("Home:FoundDriverRadius: " + radius);
            Log.i(TAG,"FoundDriverRadius: " + radius + "\n" +
                    "FoundDriverID: " + driverFoundID);
            //Now we will remove this driver from driver available database
            DatabaseReference mDriverIsAvailable = FirebaseDatabase.getInstance()
                    .getReference(Common.driverOrBiker + "sAvailAble")
                    .child(selectedService);
            GeoFire geoFireIsAvailable = new GeoFire(mDriverIsAvailable);
            geoFireIsAvailable.removeLocation(driverFoundID);

            createRingToneInDriverApp(Common.driverOrBiker,driverFoundID);

            setCustomerRideStatusInDatabase(5);
            //rideStatus = 5; that means our customer has found temporary driver

            //Now we will get update from our customers "Current_Ride_Info_Panel", about
            // ride status
            Listen_To_Current_Ride_Info_Panel(Common.driverOrBiker);
        }
    }

    private void letKnowTheDriverDetailSOFOurCustomer() {
        //Now we have to let our driver know that we have found one customer for him. So we
        //will put this customerID inside this driver's child.
        DatabaseReference mDriverDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Common.driverOrBiker + "s")
                .child(driverFoundID)
                .child("Customer_Request");
        String customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HashMap map = new HashMap();
        map.put("CustomerRequestID",customerUID);
        map.put("CustomerDestinationAddress",customerDestinationAdresss);
        map.put("CustomerDestinationLat",customerDestinationLatLng.latitude);
        map.put("CustomerDestinationLng",customerDestinationLatLng.longitude);
        map.put("CustomerPickUpAddress",customerPickUpAddress);
        map.put("CustomerPickUpLat",pickUpLocationLatLng.latitude);
        map.put("CustomerPickUpLng",pickUpLocationLatLng.longitude);

        mDriverDatabaseRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Home:RealDB:CurrentRideDetailsSentToDriver: success");
                    Crashlytics.log("Home:RealDB:CurrentRideDetailsSentToDriver: success");
                    Log.i(TAG,"Home:RealDB:CurrentRideDetailsSentToDriver: success");
                } else {
                    FirebaseCrash.log("Home:RealDB:CurrentRideDetailsSentToDriver: failed");
                    Crashlytics.log("Home:RealDB:CurrentRideDetailsSentToDriver: failed");
                    Log.i(TAG,"Home:RealDB:CurrentRideDetailsSentToDriver: failed");
                }
            }
        });
        //Now put the same data to fire store
        HashMap<String, Object> fireStoreMap = new HashMap();
        fireStoreMap.put("CustomerRequestID",customerUID);
        fireStoreMap.put("CustomerDestination",customerDestinationAdresss);
        fireStoreMap.put("CustomerDestinationLat",customerDestinationLatLng.latitude);
        fireStoreMap.put("CustomerDestinationLng",customerDestinationLatLng.longitude);
        fireStoreMap.put("CustomerPickUpAddress",customerPickUpAddress);
        fireStoreMap.put("CustomerPickUpLat",pickUpLocationLatLng.latitude);
        fireStoreMap.put("CustomerPickUpLng",pickUpLocationLatLng.longitude);

        FirebaseFirestore mFireStoreCustomerBasicInfoDB =FirebaseFirestore.getInstance();
        mFireStoreCustomerBasicInfoDB.collection("Users")
                .document(Common.driverOrBiker + "s")
                .collection(driverFoundID)
                .document("Customer_Request")
                .set(fireStoreMap) //Here will be set() not update()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseCrash.log("Home:fireStore:CurrentRideDetailsSentToDriver: success");
                            Crashlytics.log("Home:fireStore:CurrentRideDetailsSentToDriver: success");
                            Log.i(TAG,"Home:fireStore:CurrentRideDetailsSentToDriver: failed");
                        } else {
                            FirebaseCrash.log("Home:fireStore:CurrentRideDetailsSentToDriver: failed");
                            Crashlytics.log("Home:fireStore:CurrentRideDetailsSentToDriver: failed");
                            Log.i(TAG,"Home:fireStore:CurrentRideDetailsSentToDriver: failed + \n +" +
                                    "Failed Message: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void setCustomerRideStatusInDatabase(final int rideStatus) {
        DatabaseReference setRideStatus = FirebaseDatabase.getInstance()
                .getReference("Users/Customers")
                .child(customerUID)
                .child("Current_Ride_Info_Panel");
        HashMap<String, Object> lookingForDriver = new HashMap<>();
        lookingForDriver.put("Ride_Status",rideStatus);
        setRideStatus.setValue(lookingForDriver)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i(TAG,"Ride_Status: " + rideStatus);
                        }
                    }
                });

    }

    private void createRingToneInDriverApp(String finalDriverOrBiker, String driverFoundID) {
        DatabaseReference mDriverTokenDatabseReference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(finalDriverOrBiker + 's')
                .child(driverFoundID)
                .child("FCM_LATEST_TOKEN");
        mDriverTokenDatabseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    driverFCMToken = dataSnapshot.getValue().toString();
                    Token token = dataSnapshot.getValue(Token.class);
                    Log.i(TAG,"Driver FCMToken: " + token.getToken());

                    //Make raw payload - Convert Latlng to json
                    String json_lat_lng = new Gson().toJson(pickUpLocationLatLng);
                    Log.i(TAG,"lat: " + pickUpLocationLatLng.latitude + "\n" +
                    "lng: " + pickUpLocationLatLng.longitude);
                    Data data = new Data(
                            "customer_call",
                            "Call from customer",
                            customerUID,
                            json_lat_lng,
                            "N/A",
                            "N/A");
                    //Send it to driver app. We will deserialize it there
                    Sender content = new Sender(data, token.getToken() );
                    ifcmService.sendMessage(content)
                            .enqueue(new Callback<FCMResponse>() {
                                @Override
                                public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                    Log.i(TAG,"response: "  + response.body().toString());
                                    if (response.body().success == 1){
                                        Log.i(TAG,"Created Ringtone In Driver App");
                                    } else {
                                        Log.i(TAG,"Failed TO Create Ringtone In Driver App");
                                    }
                                }

                                @Override
                                public void onFailure(Call<FCMResponse> call, Throwable t) {
                                    Log.i(TAG,"Failed TO Create Ringtone In Driver App: " + t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private DatabaseReference mCustomerCurrentRideInfoPnaelRef;
    private void Listen_To_Current_Ride_Info_Panel(final String driverOrBiker) {
        FirebaseCrash.log("Home:Listen_To_Current_Ride_Info_Panel.called");
        Crashlytics.log("Home:Listen_To_Current_Ride_Info_Panel.called");

        String customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mCustomerCurrentRideInfoPnaelRef = FirebaseDatabase.getInstance()
                .getReference("Users/Customers")
                .child(customerUID)
                .child("Current_Ride_Info_Panel");
        mCustomerCurrentRideInfoPnaelRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("Ride_Status") != null){
                        rideStatus = Integer.parseInt(map.get("Ride_Status").toString());
                        switch (rideStatus){
                            case  0:
                                //rideStatus == 0, That means our user did not call for ride yet.
                                if (driverChatAndInfoBottomSheet != null){
                                    if (driverChatAndInfoBottomSheet.getVisibility() == View.VISIBLE){
                                        Log.i(TAG,"Ride request cancelled by driver");
                                        Toast.makeText(Home.this, "Driver Cancelled The Ride",
                                                Toast.LENGTH_SHORT).show();
                                        endCurrentRideByDriver();
                                    }
                                }

                                break;

                            case  1:
                                //rideStatus == 1,That means our driver is on his way to pick up customer.
                                //Now we will get our driver location for this customer.
                                if (Common.driverOrBiker != null){
                                    letKnowTheDriverDetailSOFOurCustomer();
                                    getDriverLocation(Common.driverOrBiker,selectedService);
                                    getDriverInfo(Common.driverOrBiker,selectedService);
                                }

                                break;

                            case  2:
                                //rideStatus == 2,That means our driver is on his way to drop our customer

                                break;

                            case 3:
                                //rideStatus == 3,That means our driver has dropped our customer
                                if (driverChatAndInfoBottomSheet != null){
                                    if (driverChatAndInfoBottomSheet.getVisibility() == View.VISIBLE){
                                        driverChatAndInfoBottomSheet.setVisibility(View.GONE);
                                        destriyAllTheViewsFromDriverChatAndInfoBottomSheet();
                                    }
                                }
                                break;

                            case 4:
                                //rideStatus == 4,That means our customer is looking for driver

                                break;

                            case 5:
                                //rideStatus == 5,That means our customer has found some temporary driver

                                break;

                            case 6:
                                //rideStatus == 6,That means temporary driver has declined to go
                                driverFound = false;
                                driverFoundID = null;
                                getClossestDriver(selectedVehicle,selectedService);
                                break;

                        }

                    }

                    if (map.get(driverOrBiker +"_Distance") != null){
                        driverDistanceToPicUpLocation
                                = Double.parseDouble(map.get(driverOrBiker +"_Distance").toString());
                    }

                    if (map.get(driverOrBiker + "_ETA") != null){
                        driverETAtoPickUpLocation = Double.parseDouble(map.get(driverOrBiker + "_ETA").toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getDriverInfo(final String finalDriverOrBiker2, String selectedService) {
        FirebaseCrash.log("Home:getDriverInfo.called");
        Crashlytics.log("Home:getDriverInfo.called");

        String vehicle_type = null;
        if(finalDriverOrBiker2.equals("Driver")){
            vehicle_type = "Car";
        } else if (finalDriverOrBiker2.equals("Biker")){
            vehicle_type = "Bike";
        }

        DatabaseReference mDriverDataBaseRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(finalDriverOrBiker2 + "s")
                .child(driverFoundID)
                .child(finalDriverOrBiker2 + "_Basic_Info");
        final String finalVehicle_type = vehicle_type;
        mDriverDataBaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() >0){
                    //After confirming that our driver has info on database than we make
                    //visible 'setUpAllTheViewsFromDriverChatAndInfoBottomSheet'
                    //So before fetching data we have to show loading sign.
                    setUpAllTheViewsFromDriverChatAndInfoBottomSheet();
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get(finalDriverOrBiker2 + "_Full_Name") != null){
                        strAssignedDriverName = map.get(finalDriverOrBiker2 + "_Full_Name").toString();
                        assignedDriverName.setText(strAssignedDriverName);
                    }
                    if (map.get(finalDriverOrBiker2 + "_Mobile") != null){
                        strAssignedDriverPhoneNumber = map.get(finalDriverOrBiker2 + "_Mobile").toString();
                    }
                    if (map.get(finalDriverOrBiker2 + "_Ratings") != null){
                        strAssignedDriverRating = map.get(finalDriverOrBiker2 + "_Ratings").toString();
                        assignedDriverRating.setText(strAssignedDriverRating);
                    }
                    if (map.get(finalDriverOrBiker2 + "_Total_Trips") != null){
                        strAssignedDriverTotalLifeTimeTrips = map.get(finalDriverOrBiker2 + "_Total_Trips").toString();
                        assignedDriverTotalLifeTimeTrips.setText(strAssignedDriverTotalLifeTimeTrips);
                    }

                    if (map.get(finalDriverOrBiker2 +"_"+ finalVehicle_type + "_Registration") != null){
                        strAssignedDriverVehicleRegNumber = map.get
                                (finalDriverOrBiker2 +"_"+ finalVehicle_type + "_Registration").toString();
                        assignedDriverVehicleRegNumber.setText(strAssignedDriverVehicleRegNumber);
                    }
                    if (map.get(finalVehicle_type + "_Model") != null){
                        strAssignedDriverCarModel = map.get(finalVehicle_type + "_Model").toString();
                        assignedDriverCarModel.setText(strAssignedDriverCarModel);
                    }

                    getAssignedDriverProfilePic(finalDriverOrBiker2);
//                    getDriverAverageRating();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAssignedDriverProfilePic(final String driverOrBiker) {
        DatabaseReference getAssignedDriverPP = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(driverOrBiker + "s")
                .child(driverFoundID)
                .child(driverOrBiker + "_Profile_Pic");
        getAssignedDriverPP.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("Profile_Pic_Url") != null){
                        String strAssignedDriverProfilePic = map.get("Profile_Pic_Url").toString();
                        Log.i(TAG,"Assigned Driver Profile Pic: " + strAssignedDriverProfilePic);
                        Glide.with(getApplication()).load(strAssignedDriverProfilePic).into(assignedDriverProfilePic);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getDriverAverageRating() {
        //Do not delete
        FirebaseCrash.log("Home:getDriverAverageRating.called");
        Crashlytics.log("Home:getDriverAverageRating.called");

        DatabaseReference mDriverRatingDB = FirebaseDatabase.getInstance()
                .getReference("Users/Drivers")
                .child(driverFoundID)
                .child("Driver_Driving_History");
        mDriverRatingDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int sumOfTotalRating = 0;
                    int numberOfRating = 0;
                    float averageRating = 0.0f;
                    for (DataSnapshot pastServiceID : dataSnapshot.getChildren()){
                        if (pastServiceID.getChildrenCount() > 0){
                            //That means there is rating inside
                            Map<String, Object> map = (Map<String, Object>) pastServiceID.getValue();
                            if (map.get("Driver_Rating") != null){
                                int rating = Integer.valueOf(map.get("Driver_Rating").toString());
                                sumOfTotalRating = sumOfTotalRating + rating;
                                Log.i(TAG,"Rating: " + rating
                                        +"\nsumOfTotalRating: " + sumOfTotalRating);
                                numberOfRating++;
                            }
                        }
                    }
                    if (sumOfTotalRating != 0){
                        averageRating = sumOfTotalRating /numberOfRating;
//                        assignedDriverInfoPanelRtnBar.setRating(averageRating);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void endCurrentRideByCustomer() {
        //Now we will remove Current_Ride_Info_panel
        DatabaseReference mCurrentRideInfoPanelDb = FirebaseDatabase.getInstance()
                .getReference("Users/Customers")
                .child(customerUID)
                .child("Current_Ride_Info_Panel");
        HashMap<String, Object> rideStatus = new HashMap<>();
        rideStatus.put("Ride_Status",0);
        mCurrentRideInfoPanelDb.setValue(rideStatus);
        //isCarAnimationShowingToUser == false means we can freely put
        //pickup and destination marker on the map
        relativeLayOutpickUpDestinationActualDummyBox.setVisibility(View.VISIBLE);
        selectedService = null;
        selectedVehicle = null;
        customer_ride_request.setVisibility(View.VISIBLE);
        mMap.clear();
        isCarAnimationShowingToUser = false;
        imgDeleteDummyDestination.setEnabled(true);
        imgDeleteDummyPickUp.setEnabled(true);
        edtDummyPickUpSelector.setText("");
        edtDummyDestinationSelector.setText("");
        createPickUpAndDestinationMarkerObject();
        isCarAnimationShowingToUser = false;
        imgDeleteDummyDestination.setEnabled(true);
        imgDeleteDummyPickUp.setEnabled(true);
        FirebaseCrash.log("Home:endCurrentRideByCustomer.called");
        Crashlytics.log("Home:endCurrentRideByCustomer.called");
        if (driverChatAndInfoBottomSheet != null &&
                driverChatAndInfoBottomSheet.getVisibility() == View.VISIBLE){
            driverChatAndInfoBottomSheet.setVisibility(View.GONE);
        }
        callUthaoStatus = false;
        if (geoQuery != null){
            geoQuery.removeAllListeners();
            Log.i(TAG,"endCurrentRideByCustomer: geoQuery: listener removed");
        }
        if (mDriverWorkingLocationRefListener != null){
            mDriverWorkingLocationRef.removeEventListener(mDriverWorkingLocationRefListener);
        }



        //Now we will remove customer uthao request data from fireBase Database.
        String customerUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mCustomerDatabaseRef = FirebaseDatabase.getInstance()
                .getReference("Customer_request");
        GeoFire geoFire = new GeoFire(mCustomerDatabaseRef);
        geoFire.removeLocation(customerUID);
        ////Now we will remove customer ID from 'driverFoundID' child (CustomerRequestID)
        //Now we have to let our driver know that customer has cancel ride. So we
        //will remove customerID from inside this driver's child.
        //So for precaution we will first check if this customer has found some driver.
        //than we remove this customer id from that driver's database
        if (driverFoundID != null){
            DatabaseReference mDriverDatabaseRef = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(Common.driverOrBiker + "s")
                    .child(driverFoundID)
                    .child("Customer_Request");
            mDriverDatabaseRef.setValue(true).addOnCompleteListener
                    (new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                FirebaseCrash.log("Home:RealTimeDB:customerUID removed: success");
                                Crashlytics.log("Home:RealTimeDB:customerUID removed: success");
                            } else {
                                FirebaseCrash.log("Home:RealTimeDB:customerUID removed: failed");
                                Crashlytics.log("Home:RealTimeDB:customerUID removed: failed");
                            }
                        }
                    });

            DatabaseReference mDriverDatabaseRef1 = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(Common.driverOrBiker + "s")
                    .child(driverFoundID)
                    .child("Driver_Status");
            mDriverDatabaseRef1.setValue(0).addOnCompleteListener
                    (new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                FirebaseCrash.log("Home:RealTimeDB:customerUID removed: success");
                                Crashlytics.log("Home:RealTimeDB:customerUID removed: success");
                            } else {
                                FirebaseCrash.log("Home:RealTimeDB:customerUID removed: failed");
                                Crashlytics.log("Home:RealTimeDB:customerUID removed: failed");
                            }
                        }
                    });

            HashMap<String , Object> fireStoreMap = new HashMap<>();
            fireStoreMap.put("Customer_Request",null);
            FirebaseFirestore mFireStoreDriverDatabaseRef =FirebaseFirestore.getInstance();
            mFireStoreDriverDatabaseRef.collection("Users")
                    .document(Common.driverOrBiker + "s")
                    .collection(driverFoundID)
                    .document("Customer_Request")
                    .update(fireStoreMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        FirebaseCrash.log("Home:fireStore:customerUID removed: success");
                        Crashlytics.log("Home:fireStore:customerUID removed: success");
                    } else {
                        FirebaseCrash.log("Home:fireStore:customerUID removed: failed");
                        Crashlytics.log("Home:fireStore:customerUID removed: failed");
                    }
                }
            });
        }
        //So now go back to previous state
        erasePolyLine();
        driverFound = false;
        driverFoundID = null;
        isCustomerOrDriverForcedStopRequest = true;
        radius = 1;
        if (pickUpMarker != null){
            pickUpMarker.remove();
        }
        if (assignedDriverName != null){
            assignedDriverName.setText("");
            assignedDriverCarModel.setText("");
            assignedDriverProfilePic.setImageResource(R.mipmap.ic_blank_profile_pic);
        }

        customer_ride_request.setText(R.string.estimate_our_fair);
        customer_ride_request.setClickable(true);
        myTrace.stop();
        mMap.clear();
        customer_ride_request.setVisibility(View.VISIBLE);
        edtDummyDestinationSelector.setText("");
        edtDummyPickUpSelector.setText("");
        createPickUpAndDestinationMarkerObject();
    }

    private void endCurrentRideByDriver() {
        //isCarAnimationShowingToUser == false means we can freely put
        //pickup and destination marker on the map
        relativeLayOutpickUpDestinationActualDummyBox.setVisibility(View.VISIBLE);
        isCarAnimationShowingToUser = false;
        imgDeleteDummyDestination.setEnabled(true);
        imgDeleteDummyPickUp.setEnabled(true);
        FirebaseCrash.log("Home:endCurrentRideByCustomer.called");
        Crashlytics.log("Home:endCurrentRideByCustomer.called");
        if (driverChatAndInfoBottomSheet.getVisibility() == View.VISIBLE){
            driverChatAndInfoBottomSheet.setVisibility(View.GONE);
        }
        callUthaoStatus = false;
        if (geoQuery != null){
            geoQuery.removeAllListeners();
            Log.i(TAG,"endCurrentRideByCustomer: geoQuery: listener removed");
        }
        if (mDriverWorkingLocationRefListener != null){
            mDriverWorkingLocationRef.removeEventListener(mDriverWorkingLocationRefListener);
        }

        DatabaseReference mCustomerDatabaseRef = FirebaseDatabase.getInstance()
                .getReference("Customer_request");
        GeoFire geoFireForCustomer = new GeoFire(mCustomerDatabaseRef);
        geoFireForCustomer.removeLocation(customerUID);

        //So now go back to previous state
        erasePolyLine();
        driverFound = false;
        driverFoundID = null;
        isCustomerOrDriverForcedStopRequest = true;
        radius = 1;
        if (pickUpMarker != null){
            pickUpMarker.remove();
        }
        assignedDriverName.setText("");
        assignedDriverCarModel.setText("");
        assignedDriverProfilePic.setImageResource(R.mipmap.ic_blank_profile_pic);
        customer_ride_request.setText(R.string.call_uthao);
        customer_ride_request.setClickable(true);
        //Now we will restore customers Ride_Status == 0
        DatabaseReference mCustomerCurrentRideInfoPnaelReffinish = FirebaseDatabase.getInstance()
                .getReference("Users/Customers")
                .child(customerUID)
                .child("Current_Ride_Info_Panel");
        HashMap<String, Object> mapfinish = new HashMap();
        mapfinish.put("Ride_Status",0);
        //rideStatus == 0,That means our customer is ready for new ride
        mCustomerCurrentRideInfoPnaelReffinish.setValue(mapfinish)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i(TAG,"Home:realTime:Ride_Status Update: success");
                            FirebaseCrash.log("Home:realTime:Ride_Status Update: success");
                            Crashlytics.log("Home:realTime:Ride_Status Update: success");
                        } else {
                            Log.i(TAG,"Home:realTime:Ride_Status Update: failed");
                            FirebaseCrash.log("Home:realTime:Ride_Status Update: failed");
                            Crashlytics.log("Home:realTime:Ride_Status Update: failed");
                        }
                    }
                });

        HashMap<String, Object> fireMapfinish = new HashMap();
        fireMapfinish.put("Ride_Status",rideStatus);
        FirebaseFirestore mFireCustomerRideInfo = FirebaseFirestore.getInstance();
        mFireCustomerRideInfo.collection("Users").document("Customers").collection(customerUID)
                .document("Current_Ride_Info_Panel").set(fireMapfinish)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Home:fireStore:Ride_Status Update: success");
                    Crashlytics.log("Home:fireStore:Ride_Status Update: success");
                } else {
                    FirebaseCrash.log("Home:fireStore:Ride_Status Update: failed");
                    Crashlytics.log("Home:fireStore:Ride_Status Update: failed");
                }
            }
        });
        myTrace.stop();
        mMap.clear();
        customer_ride_request.setVisibility(View.VISIBLE);
        edtDummyDestinationSelector.setText("");
        edtDummyPickUpSelector.setText("");
        createPickUpAndDestinationMarkerObject();
    }

    private void ShowMoneyReceipt(){
        initializeBottomSheetBehavior();
    }

    private Marker mDriverMarker;
    private DatabaseReference mDriverWorkingLocationRef;
    private ValueEventListener mDriverWorkingLocationRefListener;

    private void getDriverLocation(String finalDriverOrBiker2, String selectedService) {
        FirebaseCrash.log("Home:getDriverLocation.called");
        Crashlytics.log("Home:getDriverLocation.called");
        //We have to find this driver location in the "DriversWorking" child from there we will
        //get this drivers lat lng.
        mDriverWorkingLocationRef = FirebaseDatabase.getInstance()
                .getReference(finalDriverOrBiker2 + "s_Working")
                .child(selectedService)
                .child(driverFoundID).child("l");
        mDriverWorkingLocationRefListener = mDriverWorkingLocationRef
                .addValueEventListener(new ValueEventListener()
                {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //From this datasnapshot we will get drivers latlng
                if (dataSnapshot.exists()){
                    FirebaseCrash.log("Home: driver location found");
                    Crashlytics.log("Home:driver location found");
                    Log.i(TAG,"Home: driver location found");
                    List<Object> driverLocation = (List<Object>) dataSnapshot.getValue();
                    double driverLat = 0;
                    double driverLng = 0;
                    if (driverLocation.get(0) != null){
                        driverLat = Double.parseDouble(driverLocation.get(0).toString());
                    }
                    if (driverLocation.get(1) != null){
                        driverLng = Double.parseDouble(driverLocation.get(1).toString());
                    }
                    //Now we will show our driver's location on customer's map activity.
                    LatLng mDriverLatLng = new LatLng(driverLat,driverLng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(mDriverLatLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    getRouteOfDriverToPickUpLocation(mDriverLatLng,pickUpLocationLatLng);
                    //We will check if driver marker is being added for the second time.
                    //So that we could remove previous marker from customer map.
                    if (mDriverMarker != null){
                        //That means we have previously added our driver location's marker on the
                        //the customer maps. So we have to remove previous marker before adding new
                        //marker.
                        mDriverMarker.remove();
                    }

                    //Now we will get the distance between Our customer & Driver
                    //Distance start here
                    Location customerLoc = new Location("");
                    customerLoc.setLatitude(pickUpLocationLatLng.latitude);
                    customerLoc.setLongitude(pickUpLocationLatLng.longitude);

                    Location driverLoc = new Location("");
                    driverLoc.setLatitude(mDriverLatLng.latitude);
                    driverLoc.setLongitude(mDriverLatLng.longitude);

                    float distanceBetweenCustomerAndDriver = customerLoc.distanceTo(driverLoc);

                    if (distanceBetweenCustomerAndDriver < 100){

                    } else {
                        if (rideStatus == 1 || rideStatus == 2){
                            //rideStatus == 1,That means our driver is on his way to pick up customer.
                            //rideStatus == 2,That means our driver is on his way to drop our customer
                            //on his destination.
                            //#########################################################
                            //***********************To Be Changed*********************
                            //**************************Start**************************
//                            customer_ride_request.setClickable(false);
//                            customer_ride_request.setText("Now Only Driver Can Cancel Ride");


                            //#########################################################
                            //***********************To Be Changed*********************
                            //**************************End**************************
                        } else {

                        }

                    }

                    //Distance end here
                    mDriverMarker = mMap.addMarker(new MarkerOptions().position(mDriverLatLng).title("Your Driver")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void stopListeningToDriverLocation(){
        FirebaseCrash.log("Home:stopListeningToDriverLocation.called");
        Crashlytics.log("Home:stopListeningToDriverLocation.called");
        if (rideStatus == 2 && rideStatus == 3){
            //rideStatus == 1,That means our driver is on his way to pick up customer.
            //rideStatus == 2,That means our driver is on his way to drop our customer
            //on his destination. That means our driver picked up the customer
            //rideStatus == 3,That means our driver has dropped our customer
            //on his destination.
            if (mDriverWorkingLocationRefListener != null){
                mDriverWorkingLocationRef.removeEventListener(mDriverWorkingLocationRefListener);
            }
        }

    }


    private Circle circle;
    @Override
    public void onLocationChanged(Location location) {
        if (getApplicationContext() != null) {
            mLastLocation = location;
            mLastLocationLatLng = new LatLng(location.getLatitude(),location.getLongitude());
            if (!googlePlaceAutoCompleteLatLngBoundset){
                //This will make sure that inside this block of code only execute once
                pickUpLocationLatLng = mLastLocationLatLng;
                Log.i("Caution","pickUpLocationLatLng: " + pickUpLocationLatLng);
                customerDestinationLatLng = mLastLocationLatLng;
                googlePlaceAutoCompleteLatLngBoundset = true;
                getLatLngBoundForGooglePlace();
                createPickUpAndDestinationMarkerObject();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mLastLocationLatLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                updateCustomerLastLocationAndTimeToServer();
            }


        }
    }

    //Code about routing between driver & customer starts here
//    private void getRouteOfDriverToPickUpLocation(LatLng mDriverLatLng) {
//        FirebaseCrash.log("Home:getRouteOfDriverToPickUpLocation.called");
//        Crashlytics.log("Home:getRouteOfDriverToPickUpLocation.called");
//        Routing routing = new Routing.Builder()
//                .travelMode(AbstractRouting.TravelMode.DRIVING)
//                .withListener(this)
//                .alternativeRoutes(false)  //Disable alternativeRoutes
//                .waypoints(new LatLng(pickUpLocationLatLng.latitude,
//                                pickUpLocationLatLng.longitude),
//                        mDriverLatLng)
//                .build();
//        routing.execute();
//    }

    @Override
    public void onRoutingFailure(RouteException e) {
        FirebaseCrash.log("Home:onRoutingFailure.called");
        Crashlytics.log("Home:onRoutingFailure.called");
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        FirebaseCrash.log("Home:onRoutingSuccess.called");
        Crashlytics.log("Home:onRoutingSuccess.called");
        //First we remove any polylines which we previously have
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        //Now we will create new polylines
        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);


//            drivingDistanceBetweenDriverCurrentLocationAndPickUpLocation = route.get(i).getDistanceValue();
//            driverETAtoReachPickUpLocation = route.get(i).getDurationValue();
//            assignedDriverDistance.setText(String.valueOf(drivingDistanceBetweenDriverCurrentLocationAndPickUpLocation / 1000) + " km");
//            assignedDriverETA.setText(" ETA: " +String.valueOf(driverETAtoReachPickUpLocation / 60) + " min ");
//            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "
//                    + route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue()
//                    ,Toast.LENGTH_SHORT).show();
//            Log.i(TAG,"drivingDistanceBetweenDriverCurrentLocationAndPickUpLocation: " + drivingDistanceBetweenDriverCurrentLocationAndPickUpLocation + "\n" +
//            "driverETAtoReachPickUpLocation: " + driverETAtoReachPickUpLocation);
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void erasePolyLine(){
        FirebaseCrash.log("Home:erasePolyLine.called");
        Crashlytics.log("Home:erasePolyLine.called");
        for (Polyline line : polylines){
            line.remove();
            //Above we are removing one poly line at a time
        }
        //Now we have to clear polylines array list
        polylines.clear();
    }
    //Code about routing between driver & customer ends here

    protected synchronized void buildGoogleApiClient() {
        FirebaseCrash.log("Home:buildGoogleApiClient.called");
        Crashlytics.log("Home:buildGoogleApiClient.called");
        googleApiClient = new GoogleApiClient.Builder(Home.this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)  //Very important // PlaceAutoComplete will not work
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        FirebaseCrash.log("Home:GoogleApiClient:onConnected.called");
        Crashlytics.log("Home:GoogleApiClient:onConnected.called");
        //This method will be called when googleApiClient will successfully connected.
        //After googleApiClient is connected, than we can call for location request.
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true); //this is the key ingredient

        checkIsLocationIsEnabled(builder,googleApiClient,locationRequest);

        Log.i(TAG,"GPS: Enabled");

    }

    private void checkIsLocationIsEnabled(LocationSettingsRequest.Builder builder,
                                          final GoogleApiClient googleApiClient,
                                          final LocationRequest locationRequest) {
        FirebaseCrash.log("Home:checkIsLocationIsEnabled.called");
        Crashlytics.log("Home:checkIsLocationIsEnabled.called");
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.
                checkLocationSettings(this.googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        checkGPSConnectionPermission();
                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,Home.this);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
//                            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
//                            builder.setTitle(R.string.network_not_enabled)
//                                    .setMessage(R.string.open_location_settings)
//                                    .setPositiveButton(R.string.go_to_location_settings,
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                                                }
//                                            })
//                                    .setNegativeButton(R.string.cancel,
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.cancel();
//                                                }
//                                            });
//                            AlertDialog alert = builder.create();
//                            alert.show();
                            status.startResolutionForResult(Home.this, CHECK_IS_LOCATION_ENABLED_IN_THIS_DEVICE);
                        } catch (IntentSender.SendIntentException ignored) {}
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setTitle(R.string.network_not_enabled)
                                .setMessage(R.string.go_to_location_settings_manually)
                                .setPositiveButton(R.string.shut_down_app,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                }
            }
        });
    }


    @Override
    public void onConnectionSuspended(int i) {
        FirebaseCrash.log("Home:GoogleApiClient:onConnectionSuspended.called");
        Crashlytics.log("Home:GoogleApiClient:onConnectionSuspended.called");
        Log.i(TAG,"GPS: Disabled");
        buildGoogleApiClient();
        //This method will be called when googleApiClient connection will suspend.

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        FirebaseCrash.log("Home:GoogleApiClient:onConnectionFailed.called");
        Crashlytics.log("Home:GoogleApiClient:onConnectionFailed.called");
        Log.i(TAG,"GPS: Disabled");
        buildGoogleApiClient();
        //This method will be called when googleApiClient will fail to connect.

    }


    private void checkGPSConnectionPermission() {
        FirebaseCrash.log("Home:checkGPSConnectionPermission.called");
        Crashlytics.log("Home:checkGPSConnectionPermission.called");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION},GPS_PERMISSION_REQUEST);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        googlePlaceAutoCompleteLatLngBoundset = false;
        if (googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
        mMap.clear();
        edtActualPickUpSelector.
                removeTextChangedListener(this);
        edtActualDestinationSelector
                .removeTextChangedListener(this);
    }

    //All the method related with pick up and destination search box
    //***************************Start******************************
    private void initializeAllTheViewFromPickupAndDestinationBox() {
        FirebaseCrash.log("Home:initializeAllTheViewFromPickupAndDestinationBox.called");
        Crashlytics.log("Home:initializeAllTheViewFromPickupAndDestinationBox.called");
        imgDeleteActualPickUp = findViewById(R.id.imgDeleteActualPickUp);
        imgDeleteActualDestination =findViewById(R.id.imgDeleteActualDestination);
        imgDeleteDummyPickUp = findViewById(R.id.imgDeleteDummyPickUp);
        imgDeleteDummyDestination = findViewById(R.id.imgDeleteDummyDestination);

        recyclerView_pick_up = findViewById(R.id.recyclerView_pick_up);
        recyclerView_Destination = findViewById(R.id.recyclerView_Destination);

        mLinearLayoutManagerForPickUp=new CustomLinearLayoutManager(this);
        recyclerView_pick_up.setLayoutManager(mLinearLayoutManagerForPickUp);

        mLinearLayoutManagerForDestination = new CustomLinearLayoutManager(this);
        recyclerView_Destination.setLayoutManager(mLinearLayoutManagerForDestination);

        actualPickUpSelectorLayOut = findViewById(R.id.actualPickUpSelectorLayOut);
        dummyPickUpSelectorLayOut = findViewById(R.id.dummyPickUpSelectorLayOut);
        dummyDestinationSelectorLayOut = findViewById(R.id.dummyDestinationSelectorLayOut);
        actualDestinationSelectorLayOut = findViewById(R.id.actualDestinationSelectorLayOut);
        crossMarkContainerForPickUp = findViewById(R.id.crossMarkContainerForPickUp);
        crossMarkContainerForDestination = findViewById(R.id.crossMarkContainerForDestination);

        relativeLayOutpickUpDestinationActualDummyBox = findViewById(R.id
                    .relativeLayOutpickUpDestinationActualDummyBox);

        dummyPickUpRelativeLayout =findViewById(R.id.dummyPickUpRelativeLayout);
        dummyDestinationRelativeLayout = findViewById(R.id.dummyDestinationRelativeLayout);

        edtDummyPickUpSelector = findViewById(R.id.edtDummyPickUpSelector);
        edtActualPickUpSelector = findViewById(R.id.edtActualPickUpSelector);
        edtActualDestinationSelector =findViewById(R.id.edtAutocomplete_places_Destination);
        edtDummyDestinationSelector = findViewById(R.id.edtDummyDestinationSelector);

    }


    private void customerDestinationBoxOnItemSelection() {
        FirebaseCrash.log("Home:customerDestinationBoxOnItemSelection.called");
        Crashlytics.log("Home:customerDestinationBoxOnItemSelection.called");
        recyclerView_Destination.addOnItemTouchListener(new RecyclerItemClickListener(Home.this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlaceAutoComplete item = mPlacesAutoCompleteAdapter.getItem(position);
                        final String placeID = String.valueOf(item.placeID);
                        Log.i("MainActivity", "Autocomplete item selected: " + item.description);
                        //Issue a request to the Places Geo Data API to retrieve a Place object with additional
                        // details about the place.
                        final PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(googleApiClient,placeID);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceBuffer places) {
                                if (places.getStatus().isSuccess()) {
                                    final Place mPlace = places.get(0);
                                    LatLng qLoc = mPlace.getLatLng();
                                    customerDestinationLatLng = qLoc;
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(qLoc));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                                    customerDestinationAdresss = mPlace.getName().toString() + ", "
                                            + mPlace.getAddress().toString()+ ".";
                                    edtDummyDestinationSelector.setText(customerDestinationAdresss);
                                    //you can use lat with qLoc.latitude;
                                    //and long with qLoc.longitude;
                                    destinationMarker.setPosition(qLoc);
                                    Bundle params = new Bundle();
                                    params.putString("destinationMarker_LatLng", qLoc.toString());
                                    params.putString("cusDestAdresss", customerDestinationAdresss);
                                    mFirebaseAnalytics.logEvent("cusDestAdresss", params);
                                }
                                places.release();

                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeID);
                        actualDestinationSelectorLayOut.setVisibility(View.GONE);
                        btnFairEstimation.setVisibility(View.VISIBLE);
                        dummyDestinationSelectorLayOut.setVisibility(View.VISIBLE);
                        dummyPickUpSelectorLayOut.setVisibility(View.VISIBLE);

                        //Clear recycler view adapter
                        recyclerView_Destination.getRecycledViewPool().clear();
                        mLinearLayoutManagerForDestination.removeAllViews(); //May be the problem.
                        if (mPlacesAutoCompleteAdapter != null){
                            mPlacesAutoCompleteAdapter.mResultList.clear();
                            mPlacesAutoCompleteAdapter.notifyDataSetChanged();
                        }

//                        Toast.makeText(CustomerMapsActivity.this,"dummyPickUpSelectorLayOut VISIBLE",Toast.LENGTH_SHORT).show();

                    }
                }));
    }

    private void customerPickUpBoxOnItemSelection() {
        FirebaseCrash.log("Home:customerPickUpBoxOnItemSelection.called");
        Crashlytics.log("Home:customerPickUpBoxOnItemSelection.called");
        recyclerView_pick_up.addOnItemTouchListener(new RecyclerItemClickListener(Home.this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlaceAutoComplete item = mPlacesAutoCompleteAdapter.getItem(position);
                        final String placeID = String.valueOf(item.placeID);
                        Log.i("MainActivity", "Autocomplete item selected: " + item.description);
                        //Issue a request to the Places Geo Data API to retrieve a Place object with additional
                        // details about the place.
                        final PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(googleApiClient,placeID);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceBuffer places) {
                                if (places.getStatus().isSuccess()) {
                                    final Place mPlace = places.get(0);
                                    LatLng qLoc = mPlace.getLatLng();
                                    pickUpLocationLatLng = qLoc;
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(qLoc));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                                    customerPickUpAddress = mPlace.getName().toString() + ", "
                                            + mPlace.getAddress().toString()+ ".";
                                    edtDummyPickUpSelector.setText(customerPickUpAddress);
                                    pickUpMarker.setPosition(qLoc);
                                    Bundle params = new Bundle();
                                    params.putString("pickUpMarker_LatLng", qLoc.toString());
                                    params.putString("customerPickUpAddress", customerPickUpAddress);
                                    mFirebaseAnalytics.logEvent("customer_pick_up_location_Select", params);
                                    //you can use lat with qLoc.latitude;
                                    //and long with qLoc.longitude;
                                }
                                places.release();

                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeID);
                        actualPickUpSelectorLayOut.setVisibility(View.GONE);
                        btnFairEstimation.setVisibility(View.VISIBLE);
                        dummyPickUpSelectorLayOut.setVisibility(View.VISIBLE);
                        dummyDestinationSelectorLayOut.setVisibility(View.VISIBLE);

                        //Clear recycler view adapter
                        recyclerView_pick_up.getRecycledViewPool().clear();
                        mLinearLayoutManagerForPickUp.removeAllViews(); //May be the problem.
                        if (mPlacesAutoCompleteAdapter != null){
                            mPlacesAutoCompleteAdapter.mResultList.clear();
                            mPlacesAutoCompleteAdapter.notifyDataSetChanged();
                        }


//                        Toast.makeText(CustomerMapsActivity.this,"dummyPickUpSelectorLayOut VISIBLE",Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void deleteWrittenTextInSearchBoxForActualBox() {
        FirebaseCrash.log("Home:deleteWrittenTextInSearchBoxForActualBox.called");
        Crashlytics.log("Home:deleteWrittenTextInSearchBoxForActualBox.called");
        imgDeleteActualPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtActualPickUpSelector.getText().equals("")){
                    edtActualPickUpSelector.setText("");
                    recyclerView_pick_up.getRecycledViewPool().clear();
                    mLinearLayoutManagerForPickUp.removeAllViews(); //May be the problem.
                    if (mPlacesAutoCompleteAdapter != null && mPlacesAutoCompleteAdapter.mResultList != null){
                        mPlacesAutoCompleteAdapter.mResultList.clear();
                        mPlacesAutoCompleteAdapter.notifyDataSetChanged();
                    }

                }

            }
        });
        imgDeleteActualDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtActualDestinationSelector.getText().equals("")){
                    edtActualDestinationSelector.setText("");
                    recyclerView_Destination.getRecycledViewPool().clear();
                    mLinearLayoutManagerForDestination.removeAllViews(); //May be the problem.
                    if (mPlacesAutoCompleteAdapter != null && mPlacesAutoCompleteAdapter.mResultList != null){
                        mPlacesAutoCompleteAdapter.mResultList.clear();
                        mPlacesAutoCompleteAdapter.notifyDataSetChanged();
                    }

                }
            }
        });
    }

    private void deleteWrittenTextInSearchBoxForDummyBox() {
        FirebaseCrash.log("Home:deleteWrittenTextInSearchBoxForDummyBox.called");
        Crashlytics.log("Home:deleteWrittenTextInSearchBoxForDummyBox.called");
        imgDeleteDummyPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtDummyPickUpSelector.setText("");
                if (mPlacesAutoCompleteAdapter != null && mPlacesAutoCompleteAdapter.mResultList != null){
                    mPlacesAutoCompleteAdapter.mResultList.clear();
                    mPlacesAutoCompleteAdapter.notifyDataSetChanged();
                }

            }
        });
        imgDeleteDummyDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtDummyDestinationSelector.setText("");
                if (mPlacesAutoCompleteAdapter != null && mPlacesAutoCompleteAdapter.mResultList != null){
                    mPlacesAutoCompleteAdapter.mResultList.clear();
                    mPlacesAutoCompleteAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void customerActualPickUpAndDestinationBoxListener() {
        FirebaseCrash.log("Home:customerActualPickUpAndDestinationBoxListener.called");
        Crashlytics.log("Home:customerActualPickUpAndDestinationBoxListener.called");
        edtActualPickUpSelector.addTextChangedListener(this);

        edtActualDestinationSelector.addTextChangedListener(this);
    }

    private void customerDummyPickUpAndDestinationBoxListener() {
        FirebaseCrash.log("Home:customerDummyPickUpAndDestinationBoxListener.called");
        Crashlytics.log("Home:customerDummyPickUpAndDestinationBoxListener.called");
        edtDummyPickUpSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Home:edtDummyPickUpSelector.OnClick");
                Crashlytics.log("Home:edtDummyPickUpSelector.OnClick");
                if (mLastLocationLatLng != null && edtDummyPickUpSelector.getText().toString().equals("")){
                    //If customer click this dummy pick up selector than this pick up box will invisible
                    //and new pick up box will appear where customer can search for pick up location
                    dummyPickUpSelectorLayOut.setVisibility(View.GONE);
//                    Toast.makeText(CustomerMapsActivity.this,"dummyPickUpSelectorLayOut GONE",Toast.LENGTH_SHORT).show();
                    dummyDestinationSelectorLayOut.setVisibility(View.GONE);
                    btnFairEstimation.setVisibility(View.GONE);
                    actualPickUpSelectorLayOut.setVisibility(View.VISIBLE);

                } else if (edtDummyPickUpSelector.isSelected() && !edtDummyPickUpSelector.getText().toString().equals("")){
                    //If customer click this dummy pick up selector than this pick up box will invisible
                    //and new pick up box will appear where customer can search for pick up location
                    dummyPickUpSelectorLayOut.setVisibility(View.GONE);
//                    Toast.makeText(CustomerMapsActivity.this,"dummyPickUpSelectorLayOut GONE",Toast.LENGTH_SHORT).show();
                    dummyDestinationSelectorLayOut.setVisibility(View.GONE);
                    btnFairEstimation.setVisibility(View.GONE);
                    actualPickUpSelectorLayOut.setVisibility(View.VISIBLE);
                }else if (!switchbetweenDummyDestinationPickUpBox && !edtDummyPickUpSelector.getText().toString().equals("")){
//                    isDestinationMarkerFrozen = true;
//                    isPickUpMarkerFrozen = false;
                    Crashlytics.log("customerDummyPickUpAndDestinationBoxListener(): CustomerDummyRideHistory taking time");
                    Toast.makeText(Home.this,"Use Cross marker to clear",Toast.LENGTH_LONG).show();
                }
                if (edtDummyPickUpSelector.isSelected()){
                    Log.i("Check","edtDummyPickUpSelector:from OnCLick selected" );
                }

            }
        });
        edtDummyPickUpSelector.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                FirebaseCrash.log("Home:edtDummyPickUpSelector.onTouch");
                Crashlytics.log("Home:edtDummyPickUpSelector.onTouch");
                if (pickUpLocationLatLng != null){
//                    edtDummyPickUpSelector.getBackground().setColorFilter(getResources().getColor(R.color.textBoxSelected), PorterDuff.Mode.SRC_ATOP);
//                    edtDummyDestinationSelector.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    dummyPickUpSelectorLayOut.setBackgroundColor(getResources().getColor(R.color.textBoxSelected));
                    dummyDestinationSelectorLayOut.setBackgroundColor(Color.WHITE);
                    switchbetweenDummyDestinationPickUpBox = false;
                    isDestinationMarkerFrozen = true;
                    isPickUpMarkerFrozen = false;
                    if (!edtDummyPickUpSelector.getText().equals("")){
                        crossMarkContainerForPickUp.removeView(imgDeleteDummyPickUp);
                        crossMarkContainerForPickUp.addView(imgDeleteDummyPickUp);
                        imgDeleteDummyDestination.setImageResource(R.drawable.ic_cross);
//                        crossMarkContainerForDestination.removeView(imgDeleteDummyDestination);
                    } else {
                        crossMarkContainerForPickUp.removeView(imgDeleteDummyPickUp);
                        crossMarkContainerForPickUp.addView(imgDeleteDummyPickUp);
                        imgDeleteDummyDestination.setImageResource(R.drawable.ic_search);
//                        crossMarkContainerForDestination.removeView(imgDeleteDummyDestination);
                    }

                    if (pickUpMarker != null){
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(pickUpMarker.getPosition()));
                        //Above we will get our 'pickUpMarker' current position, where we left our marker.
                    }
                    Log.i("Check","pickUpLocationLatLng: " + pickUpLocationLatLng);
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    Crashlytics.log("edtDummyPickUpSelector.setOnTouchListener(): Camera has moved to pick up location");
                }
                return false;
            }
        });
        edtDummyDestinationSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Home:edtDummyDestinationSelector.onClick");
                Crashlytics.log("Home:edtDummyDestinationSelector.onClick");
                if (mLastLocationLatLng != null && edtDummyDestinationSelector.getText().toString().equals("")){
                    //If customer click this dummy destination selector than this destination box will invisible
                    //and new destination box will appear where customer can search for destination location
                    dummyPickUpSelectorLayOut.setVisibility(View.GONE);
//                    Toast.makeText(CustomerMapsActivity.this,"dummyPickUpSelectorLayOut GONE",Toast.LENGTH_SHORT).show();
                    dummyDestinationSelectorLayOut.setVisibility(View.GONE);
                    btnFairEstimation.setVisibility(View.GONE);
                    actualDestinationSelectorLayOut.setVisibility(View.VISIBLE);
//                    isPickUpMarkerFrozen = true;
//                    isDestinationMarkerFrozen = false;
                } else {
                    Toast.makeText(Home.this,"Use Cross marker to clear",Toast.LENGTH_LONG).show();
                }
            }
        });
        edtDummyDestinationSelector.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                FirebaseCrash.log("Home:edtDummyDestinationSelector.onTouch");
                Crashlytics.log("Home:edtDummyDestinationSelector.onTouch");
                if (customerDestinationLatLng != null){
//                    edtDummyPickUpSelector.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
//                    edtDummyDestinationSelector.getBackground().setColorFilter(getResources().getColor(R.color.textBoxSelected), PorterDuff.Mode.SRC_ATOP);
                    dummyPickUpSelectorLayOut.setBackgroundColor(Color.WHITE);
                    dummyDestinationSelectorLayOut.setBackgroundColor(getResources().getColor(R.color.textBoxSelected));
                    switchbetweenDummyDestinationPickUpBox = true;
                    if (destinationMarker != null){
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(destinationMarker.getPosition()));
                        //Above we will get our 'destinationMarker' current position, where we left our marker.
                    }
//                    Log.i("Check","customerDestinationLatLng: " + customerDestinationLatLng);
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    //Change the view color and image
                    if (!edtDummyDestinationSelector.getText().equals("")){
                        crossMarkContainerForDestination.removeView(imgDeleteDummyDestination);
                        crossMarkContainerForDestination.addView(imgDeleteDummyDestination);
                        imgDeleteDummyDestination.setImageResource(R.drawable.ic_cross);
//                        crossMarkContainerForPickUp.removeView(imgDeleteDummyPickUp);
                    } else {
                        crossMarkContainerForDestination.removeView(imgDeleteDummyDestination);
                        crossMarkContainerForDestination.addView(imgDeleteDummyDestination);
                        imgDeleteDummyDestination.setImageResource(R.drawable.ic_search);
//                        crossMarkContainerForPickUp.removeView(imgDeleteDummyPickUp);
                    }


                    isPickUpMarkerFrozen = true;
                    isDestinationMarkerFrozen = false;
                    Crashlytics.log("edtDummyDestinationSelector.setOnTouchListener(): Camera has moved to destination location");
                }
                return false;
            }
        });
    }

    private synchronized void getFullGeoAddressFromLatitudeAndLongitude(final double latitude, final double longitude){
        final String[] address = {"N/A"};
        String requestApi = null;

        requestApi = "https://maps.googleapis.com/maps/api/geocode" +
                "/json?latlng="+latitude+","+longitude+"&sensor=false";
        mService.getPath(requestApi).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                FirebaseCrash.log("Home:getFullGeoAddressFromLatitudeAndLongitude: response: success");
                Crashlytics.log("Home:getFullGeoAddressFromLatitudeAndLongitude: response: success");
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    String address = jsonArray.getJSONObject(0).get("formatted_address").toString();
                    if (!isPickUpMarkerFrozen){
                        customerPickUpAddress = address;
                        pickUpLocationLatLng = new LatLng(latitude,longitude);
                        edtDummyPickUpSelector.setText(address);
//                        Log.i("Check","PickUpAddress: " + address);
                    }
                    if (!isDestinationMarkerFrozen){
                        customerDestinationAdresss = address;
                        customerDestinationLatLng = new LatLng(latitude,longitude);
                        edtDummyDestinationSelector.setText(address);
//                        Log.i("Check","DestinationAddress: " + address);
                    }
//                    Log.i("Check","Reverse GeoCoding Address: " + address + "\n" +
//                    "response.body().toString()" + response.body().toString() + "\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(Home.this,"" + t.getMessage(),Toast.LENGTH_LONG).show();
                FirebaseCrash.log("Home:getFullGeoAddressFromLatitudeAndLongitude: response: failed");
                Crashlytics.log("Home:getFullGeoAddressFromLatitudeAndLongitude: response: failed");
            }
        });

    }

    private void getLatLngBoundForGooglePlace() {
        FirebaseCrash.log("Home:getLatLngBoundForGooglePlace.called");
        Crashlytics.log("Home:getLatLngBoundForGooglePlace.called");
        //Now we will filter our search result to only show place between 100 kilometer
        //of our user current location.
        if (mLastLocation != null){
            LatLng center = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            double radiusInMeter = 100 *1000; //100 kilometers
            LatLng targetNorthEast = SphericalUtil.computeOffset(center, radiusInMeter * Math.sqrt(2), 45);
            LatLng targetSouthWest = SphericalUtil.computeOffset(center, radiusInMeter * Math.sqrt(2), 225);
            bound_100KM_Radius = new LatLngBounds(targetSouthWest,targetNorthEast);

//            Toast.makeText(CustomerMapsActivity.this,"LatLngBound is set",Toast.LENGTH_LONG).show();
            mPlacesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(Home.this,R.layout.single_search_view_for_place_picker
                    ,googleApiClient,bound_100KM_Radius,null);
            recyclerView_pick_up.setAdapter(mPlacesAutoCompleteAdapter);
            recyclerView_Destination.setAdapter(mPlacesAutoCompleteAdapter);
            Crashlytics.log("getLatLngBoundForGooglePlace(): Success");
        } else {
            Toast.makeText(Home.this,"LatLngBound is not set",Toast.LENGTH_LONG).show();
            Crashlytics.log("getLatLngBoundForGooglePlace(): Failed");
        }
    }
    //All the method related with pick up and destination search box
    //***************************Start******************************

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (actualPickUpSelectorLayOut.getVisibility() == View.VISIBLE){
            actualPickUpSelectorLayOut.setVisibility(View.GONE);
            btnFairEstimation.setVisibility(View.VISIBLE);
            dummyPickUpSelectorLayOut.setVisibility(View.VISIBLE);
            dummyDestinationSelectorLayOut.setVisibility(View.VISIBLE);
            Crashlytics.log("onBackPressed(): actualPickUpSelectorLayOut.setVisibility(View.GONE);");
        } else if (actualDestinationSelectorLayOut.getVisibility() == View.VISIBLE){
            actualDestinationSelectorLayOut.setVisibility(View.GONE);
            btnFairEstimation.setVisibility(View.VISIBLE);
            dummyDestinationSelectorLayOut.setVisibility(View.VISIBLE);
            dummyPickUpSelectorLayOut.setVisibility(View.VISIBLE);
            Crashlytics.log("onBackPressed(): actualDestinationSelectorLayOut.setVisibility(View.GONE);");
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        } else if (bottomSheetLinearLayout.getVisibility() == View.VISIBLE){
            bottomSheetLinearLayout.setVisibility(View.GONE);
            bottomSheetBehavior.setPeekHeight(500);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (isCarAnimationShowingToUser){
            //isCarAnimationShowingToUser == true means there is a call in progress
            //so all the view from call uthao is visible
            if (driverChatAndInfoBottomSheet != null
                    && driverChatAndInfoBottomSheet.getVisibility() == View.VISIBLE){
                Toast.makeText(this, "Back press will not work",
                        Toast.LENGTH_SHORT).show();
            }
            if (checkAddress != null
                    && checkAddress.getVisibility() == View.VISIBLE){
                checkAddress.setVisibility(View.GONE);
                customer_ride_request.setVisibility(View.VISIBLE);
                relativeLayOutpickUpDestinationActualDummyBox.setVisibility(View.VISIBLE);
                mMap.clear();
                isCarAnimationShowingToUser = false;
                imgDeleteDummyDestination.setEnabled(true);
                imgDeleteDummyPickUp.setEnabled(true);
                edtDummyPickUpSelector.setText("");
                edtDummyDestinationSelector.setText("");
                createPickUpAndDestinationMarkerObject();
            }
            if (selectBikeOrCar != null
                    && selectBikeOrCar.getVisibility() == View.VISIBLE){
                checkAddress.setVisibility(View.VISIBLE);
                selectBikeOrCar.setVisibility(View.GONE);
            }
            if (selectServiceForCar != null
                    && selectServiceForCar.getVisibility() == View.VISIBLE){
                selectBikeOrCar.setVisibility(View.VISIBLE);
                selectServiceForCar.setVisibility(View.GONE);
            }

            if (selectServiceForBike != null
                    && selectServiceForBike.getVisibility() == View.VISIBLE){
                selectBikeOrCar.setVisibility(View.VISIBLE);
                selectServiceForBike.setVisibility(View.GONE);
            }

            if (selectPaymentMethod != null){
                selectPaymentMethod.setVisibility(View.GONE);
                if (selectedVehicle.equals("Car")){
                    selectServiceForCar.setVisibility(View.VISIBLE);
                } else if (selectedVehicle.equals("Bike")){
                    selectServiceForBike.setVisibility(View.VISIBLE);
                }
            }

            if (placeArequestToUthao != null){
                placeArequestToUthao.setVisibility(View.GONE);
                selectPaymentMethod.setVisibility(View.VISIBLE);
            }

        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click 2 times at a time to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 3000);
    }



    private void populateNavigationMenuToRecyclerView(){
        navigationMenuRecyclerView = (RecyclerView) findViewById(R.id.navigationMenuRecyclerView);
        navigationMenuRecyclerView.setHasFixedSize(true);
        navigationMenuRecyclerView.setNestedScrollingEnabled(false);
        //This setNestedScrollingEnabled() method will keep scrolling smooth.
        navigationMenuAdapter = new NavigationMenuAdapter(Home.this,getNavigationMenuData());
        navigationMenuRecyclerView.setAdapter(navigationMenuAdapter);
        navigationMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<NavigationMenuItem> getNavigationMenuData(){
        List<NavigationMenuItem> data = new ArrayList<>();
        int[] icon = {R.drawable.ic_history,
                R.drawable.ic_birthday,
                R.drawable.ic_notification,
                R.drawable.ic_payment,
                R.drawable.ic_help,
                R.drawable.ic_settings,
                R.drawable.ic_language};

        int[]titles = {
                       R.string.fair_estimation_history,
                       R.string.promotions,
                       R.string.notification,
                        R.string.payment,
                       R.string.help,
                       R.string.settings,
                       R.string.language,};

        for (int i = 0; i < titles.length && i < icon.length; i++){
            NavigationMenuItem currentItem = new NavigationMenuItem();
            currentItem.navigation_icon_id = icon[i];
            currentItem.navigation_title = titles[i];
            data.add(currentItem);
        }
        return data;
    }

    private static final String DEFAULT = "N/A";
    private void setNavigationProfilePic(){
        FirebaseCrash.log("Home:setNavigationProfilePic.called");
        Crashlytics.log("Home:setNavigationProfilePic.called");
        SharedPreferences preferences = getSharedPreferences("Customer_Basic_Info",
                Context.MODE_PRIVATE);
        String name =preferences.getString("Customer_Name",DEFAULT);//For DEFAULT see above of this method
        String Customer_FaceBook_PP = preferences.getString("Customer_FaceBook_PP",DEFAULT);//For DEFAULT see top of the code

        if (!Customer_FaceBook_PP.equals(DEFAULT)){
            Glide.with(getApplication()).load(Customer_FaceBook_PP).into(nav_profile_image);
            Log.i("Check","Home nav image set");
        }else {
            Log.i("Check","Home nav image set null");
        }
        if (!name.equals(DEFAULT)){
            nav_profile_name.setText(name);
            Log.i("Check","Home nav customer name set");
        } else{
            Log.i("Check","Home nav customer name null");
        }

        if (isExternalStorageReadable()) {
            //First get customer name from shared preferences
            if (!name.equals(DEFAULT) && Customer_FaceBook_PP.equals(DEFAULT)) {
                //Now we will check if we have profile pic in the SQLite db
//               Ask for storage permission
                nav_profile_name.setText(name);
                permissions.requestAllPermission(Home.this);
                DbHelper helper = new DbHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = helper.retrieveCustomerPicFromQLiteDatabase(db);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        //This means we have profile pic in SQLite db
                        //Now we will retrieve this PP
                        cursor.moveToFirst();
                        int imgCustomerPicColumn_number = cursor.getColumnIndex(DbContract
                                .TABLE_USER_PIC_SECOND_COLUMN_NAME);
                        bmapCustomerProfilePic = getImage(cursor.getBlob(imgCustomerPicColumn_number));
                        if (nav_profile_image != null){
                            nav_profile_image.setImageBitmap(bmapCustomerProfilePic);
                        }

                        Toast.makeText(this, "Profile Pic retrieved from " +
                                        "SQLiteDataBase: Successful",
                                Toast.LENGTH_SHORT).show();
                        cursor.close();
                    }
                }
            }
        }
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        FirebaseCrash.log("Home:isExternalStorageReadable.called");
        Crashlytics.log("Home:isExternalStorageReadable.called");
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private static final int PERMISSION_REQUEST_FOR_EXTERNAL_STORAGE = 102;
    private void checkReadExternalStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{android.Manifest.permission.
                        READ_EXTERNAL_STORAGE
                },PERMISSION_REQUEST_FOR_EXTERNAL_STORAGE);
            }
        }
    }

    //************************************************************************
    //****************************CAR ANIMATION START***************************************
    private void getDirectionUsingGoogleDirection(LatLng startPosition, LatLng endPosition){
        FirebaseCrash.log("Home:getDirectionUsingGoogleDirection.called");
        Crashlytics.log("Home:getDirectionUsingGoogleDirection.called");
        startPosition = new LatLng(pickUpLocationLatLng.latitude,pickUpLocationLatLng.longitude);
        endPosition = new LatLng(customerDestinationLatLng.latitude,customerDestinationLatLng.longitude);
        String requestApi = null;

        try{
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&"+
                    "transit_routing_preference=less_driving&"+
                    "origin=" + startPosition.latitude +"," +startPosition.longitude+"&"+
                    "destination="+endPosition.latitude+","+endPosition.longitude
                    +"&" + "key="+getResources().getString(R.string.browser_key_1);
            Log.i("Welcome","requestApi:" + requestApi);//Print url for debug
            final LatLng finalStartPosition = startPosition;
            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                FirebaseCrash.log("Home:getDirection: response: success");
                                Crashlytics.log("Home:getDirection: response: success");
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("routes");

                                //Get distance and travel time
                                //Start Here
                                JSONObject routes = jsonArray.getJSONObject(0);
                                JSONArray legs = routes.getJSONArray("legs");
                                JSONObject steps = legs.getJSONObject(0);
                                JSONObject distance = steps.getJSONObject("distance");
                                JSONObject duration = steps.getJSONObject("duration");
                                Log.i(TAG, "Google Distance: " + distance.toString() +"\n"
                                        +"Google Duration: " + duration.toString());
                                travelDistance = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]","") );
                                travelTime = Double.parseDouble(duration.getString("text").replaceAll("[^\\.0123456789]","") );
                                setUpAllTheViewsFromCheckAddress();
                                //Get distance and travel time
                                //End Here



                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject route = jsonArray.getJSONObject(i);
                                    JSONObject poly = route.getJSONObject("overview_polyline");
                                    String polyline = poly.getString("points");
                                    polyLineListForCarAnimation = decodePoly(polyline);
                                }
                                //Adjusting bounds
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (LatLng latLng: polyLineListForCarAnimation){
                                    builder.include(latLng);
                                }
                                LatLngBounds bounds = builder.build();
                                //Now we will make boundary for our camera
                                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,10);
                                mMap.animateCamera(mCameraUpdate);

                                grayPolylineOptions = new PolylineOptions();
                                grayPolylineOptions.color(Color.GRAY);
                                grayPolylineOptions.width(5);
                                grayPolylineOptions.startCap(new SquareCap());
                                grayPolylineOptions.endCap(new SquareCap());
                                grayPolylineOptions.jointType(JointType.ROUND);
                                grayPolylineOptions.addAll(polyLineListForCarAnimation);
                                grayPolyline = mMap.addPolyline(grayPolylineOptions);


                                blackPolylineOptions = new PolylineOptions();
                                blackPolylineOptions.color(Color.BLACK);
                                blackPolylineOptions.width(5);
                                blackPolylineOptions.startCap(new SquareCap());
                                blackPolylineOptions.endCap(new SquareCap());
                                blackPolylineOptions.jointType(JointType.ROUND);
                                blackPolylineOptions.addAll(polyLineListForCarAnimation);
                                blackPolyline = mMap.addPolyline(blackPolylineOptions);

                                getDistanceFromPolylinePath(blackPolylineOptions);


//                                carMarker = mMap.addMarker(new MarkerOptions()
//                                        .position(finalStartPosition)
//                                        .flat(true)
//                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(Home.this,"" + t.getMessage(),Toast.LENGTH_LONG).show();
                            FirebaseCrash.log("Home:getDirection: response: failed");
                            Crashlytics.log("Home:getDirection: response: failed");
                        }
                    });

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private void getRouteOfDriverToPickUpLocation(LatLng driverLocation,
                                                  LatLng customerPickUpLocation){
        FirebaseCrash.log("Home:getRouteOfDriverToPickUpLocation.called");
        Crashlytics.log("Home:getRouteOfDriverToPickUpLocation.called");
        startPosition = new LatLng(driverLocation.latitude,driverLocation.longitude);
        endPosition = new LatLng(customerPickUpLocation.latitude,customerPickUpLocation.longitude);
        String requestApi = null;

        try{
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&"+
                    "transit_routing_preference=less_driving&"+
                    "origin=" + startPosition.latitude +"," +startPosition.longitude+"&"+
                    "destination="+endPosition.latitude+","+endPosition.longitude
                    +"&" + "key="+getResources().getString(R.string.browser_key_1);
            Log.i("Welcome","requestApi:" + requestApi);//Print url for debug
            final LatLng finalStartPosition = startPosition;
            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                FirebaseCrash.log("Home:getRouteOfDriverToPickUpLocation: response: success");
                                Crashlytics.log("Home:getRouteOfDriverToPickUpLocation: response: success");
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("routes");

                                //Get distance and travel time
                                //Start Here
                                JSONObject routes = jsonArray.getJSONObject(0);
                                JSONArray legs = routes.getJSONArray("legs");
                                JSONObject steps = legs.getJSONObject(0);
                                JSONObject distance = steps.getJSONObject("distance");
                                JSONObject duration = steps.getJSONObject("duration");
                                Log.i(TAG, "Google Distance: " + distance.toString() +"\n"
                                        +"Google Duration: " + duration.toString());
                                driverDistanceToPicUpLocation = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]","") );
                                driverETAtoPickUpLocation = Double.parseDouble(duration.getString("text").replaceAll("[^\\.0123456789]","") );
                                setUpAllTheViewsFromDriverChatAndInfoBottomSheet();
                                assignedDriverDistance.setText(String.valueOf(driverDistanceToPicUpLocation) + " km");
                                assignedDriverETA.setText(" ETA: " +String.valueOf(driverETAtoPickUpLocation) + " min ");
                                Log.i(TAG,"driverDistanceToPicUpLocation: " + driverDistanceToPicUpLocation + "\n" +
                                        "driverETAtoPickUpLocation: " + driverETAtoPickUpLocation);
                                //Get distance and travel time
                                //End Here



                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject route = jsonArray.getJSONObject(i);
                                    JSONObject poly = route.getJSONObject("overview_polyline");
                                    String polyline = poly.getString("points");
                                    polyLineListForDriverDistance = decodePoly(polyline);
                                }
                                //Adjusting bounds
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (LatLng latLng: polyLineListForDriverDistance){
                                    builder.include(latLng);
                                }
                                LatLngBounds bounds = builder.build();
                                //Now we will make boundary for our camera
                                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,10);
                                mMap.animateCamera(mCameraUpdate);

                                grayPolylineOptions = new PolylineOptions();
                                grayPolylineOptions.color(Color.GRAY);
                                grayPolylineOptions.width(5);
                                grayPolylineOptions.startCap(new SquareCap());
                                grayPolylineOptions.endCap(new SquareCap());
                                grayPolylineOptions.jointType(JointType.ROUND);
                                grayPolylineOptions.addAll(polyLineListForDriverDistance);
                                grayPolyline = mMap.addPolyline(grayPolylineOptions);


                                blackPolylineOptions = new PolylineOptions();
                                blackPolylineOptions.color(Color.BLACK);
                                blackPolylineOptions.width(5);
                                blackPolylineOptions.startCap(new SquareCap());
                                blackPolylineOptions.endCap(new SquareCap());
                                blackPolylineOptions.jointType(JointType.ROUND);
                                blackPolylineOptions.addAll(polyLineListForDriverDistance);
                                blackPolyline = mMap.addPolyline(blackPolylineOptions);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(Home.this,"" + t.getMessage(),Toast.LENGTH_LONG).show();
                            FirebaseCrash.log("Home:getDirection: response: failed");
                            Crashlytics.log("Home:getDirection: response: failed");
                        }
                    });

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getDirection(){
        FirebaseCrash.log("Home:getDirection.called");
        Crashlytics.log("Home:getDirection.called");
        if (customerDestinationLatLng == null){
            Toast.makeText(Home.this,"Please insert your destination",
                    Toast.LENGTH_LONG).show();
            return;
        }
        currentPosition = new LatLng(pickUpLocationLatLng.latitude,pickUpLocationLatLng.longitude);
        //This above currentPosition is important for car start animation position.
        startPosition = new LatLng(pickUpLocationLatLng.latitude,pickUpLocationLatLng.longitude);
        endPosition = new LatLng(customerDestinationLatLng.latitude,customerDestinationLatLng.longitude);
        String requestApi = null;

        try{
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&"+
                    "transit_routing_preference=less_driving&"+
                    "origin=" + pickUpLocationLatLng.latitude +"," +pickUpLocationLatLng.longitude+"&"+
                    "destination="+customerDestinationLatLng.latitude+","+customerDestinationLatLng.longitude
                    +"&" + "key="+getResources().getString(R.string.browser_key_1);
            Log.i("Welcome","requestApi:" + requestApi);//Print url for debug
            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                FirebaseCrash.log("Home:getDirection: response: success");
                                Crashlytics.log("Home:getDirection: response: success");
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("routes");

                                //Get distance and travel time
                                //Start Here
                                JSONObject routes = jsonArray.getJSONObject(0);
                                JSONArray legs = routes.getJSONArray("legs");
                                JSONObject steps = legs.getJSONObject(0);
                                JSONObject distance = steps.getJSONObject("distance");
                                JSONObject duration = steps.getJSONObject("duration");
                                Log.i(TAG, "Distance: " + distance.toString() +"\n"
                                                    +"Duration: " + duration.toString());
                                travelDistance = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]","") );
                                travelTime = Double.parseDouble(duration.getString("text").replaceAll("[^\\.0123456789]","") );
                                //Get distance and travel time
                                //End Here



                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject route = jsonArray.getJSONObject(i);
                                    JSONObject poly = route.getJSONObject("overview_polyline");
                                    String polyline = poly.getString("points");
                                    polyLineListForCarAnimation = decodePoly(polyline);
                                }
                                //Adjusting bounds
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (LatLng latLng: polyLineListForCarAnimation){
                                    builder.include(latLng);
                                }
                                LatLngBounds bounds = builder.build();
                                //Now we will make boundary for our camera
                                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,10);
                                mMap.animateCamera(mCameraUpdate);

                                grayPolylineOptions = new PolylineOptions();
                                grayPolylineOptions.color(Color.GRAY);
                                grayPolylineOptions.width(5);
                                grayPolylineOptions.startCap(new SquareCap());
                                grayPolylineOptions.endCap(new SquareCap());
                                grayPolylineOptions.jointType(JointType.ROUND);
                                grayPolylineOptions.addAll(polyLineListForCarAnimation);
                                grayPolyline = mMap.addPolyline(grayPolylineOptions);


                                blackPolylineOptions = new PolylineOptions();
                                blackPolylineOptions.color(Color.BLACK);
                                blackPolylineOptions.width(5);
                                blackPolylineOptions.startCap(new SquareCap());
                                blackPolylineOptions.endCap(new SquareCap());
                                blackPolylineOptions.jointType(JointType.ROUND);
                                blackPolylineOptions.addAll(polyLineListForCarAnimation);
                                blackPolyline = mMap.addPolyline(blackPolylineOptions);

                                getDistanceFromPolylinePath(blackPolylineOptions);
//                                mMap.addMarker(new MarkerOptions()
//                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker))
//                                        .position(polyLineListForCarAnimation.get(polyLineListForCarAnimation.size() - 1))
//                                        .title("PickUp Location"));

                                //Our Custom Animation
                                //isCarAnimationShowingToUser == true means we can not freely put
                                //pickup and destination marker on the map
                                isCarAnimationShowingToUser = true;
                                imgDeleteDummyDestination.setEnabled(false);
                                imgDeleteDummyPickUp.setEnabled(false);
                                ValueAnimator polyLineAnimator = ValueAnimator.ofInt(0,100);
                                polyLineAnimator.setDuration(2000);
                                polyLineAnimator.setInterpolator(new LinearInterpolator());
                                polyLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        List<LatLng> points = grayPolyline.getPoints();
                                        int percentValue = (int) valueAnimator.getAnimatedValue();
                                        int size = points.size();
                                        int newPoints = (int) (size * (percentValue /100.0f));
                                        List<LatLng> p = points.subList(0,newPoints);
                                        blackPolyline.setPoints(p);
                                    }
                                });
                                polyLineAnimator.start();


                                carMarker = mMap.addMarker(new MarkerOptions()
                                        .position(currentPosition)
                                        .flat(true)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));

                                nowIsTheTimeToTakeSnapShot = true;
                                handlerForCarAnimation = new Handler();
                                index = -1;
                                next = 1;
                                if (startPosition != null && endPosition != null){
                                    handlerForCarAnimation.postDelayed(drawPathRunnable,
                                            3000);
                                } else {
                                    stopCarAnimation();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(Home.this,"" + t.getMessage(),Toast.LENGTH_LONG).show();
                            FirebaseCrash.log("Home:getDirection: response: failed");
                            Crashlytics.log("Home:getDirection: response: failed");
                        }
                    });

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private List decodePoly(String encoded) {
        FirebaseCrash.log("Home:decodePoly.called");
        Crashlytics.log("Home:decodePoly.called");
        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


    private float getBearing(LatLng startPosition, LatLng endPosition) {
        FirebaseCrash.log("Home:getBearing.called");
        Crashlytics.log("Home:getBearing.called");
        //Method for finding bearing between two points.
        //This method will move car right left on the road.
        double lat = Math.abs(startPosition.latitude - endPosition.latitude);
        double lng = Math.abs(startPosition.longitude - endPosition.longitude);

        if (startPosition.latitude < endPosition.latitude && startPosition.longitude < endPosition.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (startPosition.latitude >= endPosition.latitude && startPosition.longitude < endPosition.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (startPosition.latitude >= endPosition.latitude && startPosition.longitude >= endPosition.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (startPosition.latitude < endPosition.latitude && startPosition.longitude >= endPosition.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    private float getDistanceFromPolylinePath(PolylineOptions points){
        float totalDistance = 0;

//        Log.i(TAG,"Size of polyline points: " + points.getPoints().size());

        for(int i = 1; i < points.getPoints().size(); i++) {
//            Log.i(TAG,"polyline points: " + points.getPoints().get(i));
            Location currLocation = new Location("this");
            currLocation.setLatitude(points.getPoints().get(i).latitude);
            currLocation.setLongitude(points.getPoints().get(i).longitude);

            Location lastLocation = new Location("this");
            lastLocation.setLatitude(points.getPoints().get(i-1).latitude);
            lastLocation.setLongitude(points.getPoints().get(i-1).longitude);

            totalDistance += (lastLocation.distanceTo(currLocation));
            Log.i(TAG,"Iteration Distance By Method1: " + totalDistance + " meter");
        }

        Log.i(TAG,"Distance Found From Polyline By Method1: " + totalDistance + " meter");

        return totalDistance;
    }

    private void stopCarAnimation() {
        //In this method there is null pointer exception
        //Attempt to read from field 'double com.google.android.gms.maps.model.LatLng.longitude'
        //on a null object reference
        FirebaseCrash.log("Home:stopCarAnimation.called");
        Crashlytics.log("Home:stopCarAnimation.called");
        if (carMarker != null){
            carMarker.remove();
        }
        mMap.clear();
        if (handlerForCarAnimation != null){
            handlerForCarAnimation.removeCallbacks(drawPathRunnable);
        }
        if (valueAnimator != null){
            valueAnimator.cancel();
        }
        edtDummyPickUpSelector.setText("");
        edtDummyDestinationSelector.setText("");
        customer_request_type = 1;
        isCarAnimationShowingToUser = false;
        imgDeleteDummyDestination.setEnabled(true);
        imgDeleteDummyPickUp.setEnabled(true);
    }

    private Runnable drawPathRunnable = new Runnable() {
        @Override
        public void run() {
            FirebaseCrash.log("Home:drawPathRunnable.called");
            Crashlytics.log("Home:drawPathRunnable.called");
            if (index < polyLineListForCarAnimation.size() - 1){
                index++;
                next = index + 1;
            }

            if (index < polyLineListForCarAnimation.size() - 1){
                startPosition = polyLineListForCarAnimation.get(index);
                endPosition = polyLineListForCarAnimation.get(next);
            }


            valueAnimator = ValueAnimator.ofFloat(0,1);
            valueAnimator.setDuration(3000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            //This LinearInterpolator() method will calculate how fast
            //animation will run.
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    v = valueAnimator.getAnimatedFraction();
                    lng = v * endPosition.longitude + (1-v) * startPosition.longitude;
                    lat = v * endPosition.latitude + (1-v) * startPosition.latitude;
                    //where v is animation fraction and startposition and endPostion refer to each pair
                    //of LatLng from the decoded list from polyline for eg (0,1) then (1,2) then(2,3)
                    // and so on.
                    LatLng newPos = new LatLng(lat,lng);
                    if (newPos != null && carMarker != null){
                        carMarker.setPosition(newPos);
                    } else {
                        stopCarAnimation();
                    }

                    //Finally set position of marker to the new position, also evaluating the bearing
                    //between the consecutive points so that it seems car is turning literally and finally
                    //update camera as:
                    carMarker.setAnchor(0.5f, 0.5f);
                    carMarker.setRotation(getBearing(startPosition, newPos));
                    mMap.moveCamera(CameraUpdateFactory
                            .newCameraPosition(new CameraPosition.Builder()
                                    .target(newPos)
                                    .zoom(15.05f)
                                    .build()
                            ));
                }
            });
            valueAnimator.start();
            handlerForCarAnimation.postDelayed(this,3000);
            customer_request_type = 2;
            //customer_request_type == 2 means
            //customer requested 'Get money receipt'
            btnFairEstimation.setText(R.string.print_money_receipt);
        }
    };

    //************************************************************************
    //****************************CAR ANIMATION END***************************************

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        FirebaseCrash.log("Home:onTextChanged.called");
        Crashlytics.log("Home:onTextChanged.called");
//        if (!charSequence.toString().equals("") && googleApiClient.isConnected()){
//            mPlacesAutoCompleteAdapter.getFilter().filter(charSequence.toString());
//        } else if (!googleApiClient.isConnected()){
//            Toast.makeText(getApplicationContext(), "Google API is not connected",Toast.LENGTH_SHORT).show();
//            Log.e("MainActivity", "Google API is not connected");
//            Crashlytics.log("Google API is not connected");
//        }

        //This below code is suggested for removing index out of bound exception
        //Link: https://stackoverflow.com/questions/42266150/android-google-places-api-autocomplete-with-recycler-view

        if (!charSequence.equals("")) {
            if(googleApiClient.isConnected()){
                mPlacesAutoCompleteAdapter.getFilter().filter(charSequence.toString());
            }else{
                Log.e("Check", "API  NOT CONNECTED");
            }

        } else {
            mPlacesAutoCompleteAdapter.mResultList.clear();//make mResultList as public
            mPlacesAutoCompleteAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void afterTextChanged(Editable editable) {
        FirebaseCrash.log("Home:afterTextChanged.called");
        Crashlytics.log("Home:afterTextChanged.called");
        recyclerView_pick_up.getRecycledViewPool().clear();
        recyclerView_Destination.getRecycledViewPool().clear();
        if (mPlacesAutoCompleteAdapter != null){
            mPlacesAutoCompleteAdapter.notifyDataSetChanged();
        }

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


    //If new version of this app is available in google play store than
    //this method will be called
    @Override
    public void onUpdateCheckListener(final String googlePlayUrl) {
        FirebaseCrash.log("Home:AppVersion:onUpdateCheckListener.called");
        Crashlytics.log("Home:AppVersion:onUpdateCheckListener.called");
        //If new version of this app is available in google play store than
        //this method will be called
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version of this app available")
                .setMessage("Please update new version of this app from google play store")
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openWebPage(googlePlayUrl);
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        dialog.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FirebaseCrash.log("Home:onActivityResult.called");
        Crashlytics.log("Home:onActivityResult.called");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHECK_IS_LOCATION_ENABLED_IN_THIS_DEVICE && resultCode == RESULT_OK){
            checkGPSConnectionPermission();
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,Home.this);
        } else if (requestCode == CHECK_IS_LOCATION_ENABLED_IN_THIS_DEVICE && resultCode == RESULT_CANCELED){
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle(R.string.network_not_enabled)
                    .setMessage(R.string.Customer_Denied_Location)
                    .setPositiveButton(R.string.SHUT_DOWN_THIS_APP,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }
    }


    //***********************************Out Of Memory*************************************
    //***********************************Solution******************************************
    //*************************************Start*******************************************

    /**
     * Release memory when the UI becomes hidden or when system resources become low.
     * @param level the memory-related event that was raised.
     */
    @Override
    public void onTrimMemory(int level) {
        // Determine which lifecycle or system event was raised.
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:

                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */
//                Notice that your app receives the onTrimMemory() callback with
//                TRIM_MEMORY_UI_HIDDEN only when all the UI components of your app
//                process become hidden from the user. This is distinct from the onStop()
//                callback, which is called when an Activity instance becomes hidden,
//                which occurs even when the user moves to another activity in your app.
//                    So although you should implement onStop() to release activity resources
//                such as a network connection or to unregister broadcast receivers, you usually
//                should not release your UI resources until you receive onTrimMemory
//                (TRIM_MEMORY_UI_HIDDEN). This ensures that if the user navigates back
//                from another activity in your app, your UI resources are still available
//                to resume the activity quickly.

                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:

                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:

                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */

                break;

            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }


    public void checkDeviceCurrentMemoryStatus() {

        // Before doing something that requires a lot of memory,
        // check to see whether the device is in a low memory state.
        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();
        float availableMemory = memoryInfo.availMem;
        float threshold = memoryInfo.threshold;
        float totalMemory = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            totalMemory = memoryInfo.totalMem;
        }
        Log.i("Check","availableMemory: " + availableMemory /1000000000 + " G.B\n" +
                "threshold: " + threshold /1000000000 + " G.B\n" +
                "totalMemory: " + totalMemory /1000000000 + " G.B");

        if (!memoryInfo.lowMemory) {
            // Do memory intensive work ...
        }
    }

    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(
                ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        if (activityManager != null) {
            activityManager.getMemoryInfo(memoryInfo);
        }
        return memoryInfo;
    }

    //*************************************End*********************************************
}
