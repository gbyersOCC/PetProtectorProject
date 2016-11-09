package edu.orangecoastcollege.cs273.gabyers.petprotector;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author Grant Byers
 * 11/3/2016    Class provides Controller from PetProfile(Model) to activity_pet_list.xml(View)
 * This class provides methods to retrieve user-provided information (as saved data) and both acess and write(saved data) to database
 */
public class PetListActivity extends AppCompatActivity {

    private Uri imageURI;
    private static final int CONSTANT = 100;


    private ImageView picView;
    private EditText nameEditText;
    private EditText detailsEditText;
    private EditText phoneEditText;

    private DBHelper mDb;
    private ProfileAdapter mProfileAdapter;
    private ArrayList<PetProfile> mProfileList;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        //link up to layout imageViews
        picView = (ImageView) findViewById(R.id.profileImageView);
        nameEditText = (EditText) findViewById(R.id.listNameEditText);
        detailsEditText = (EditText) findViewById(R.id.listDetailsEditText);
        phoneEditText = (EditText) findViewById(R.id.listPhoneEditText);

        //list view reference
        mListView = (ListView) findViewById(R.id.profileListView);

        //get the DBHelper object
        mDb = new DBHelper(this);

        //for testing: this can be removed if you want database to keep Pet records
        mDb.deleteAllProfiles();


        imageURI = getUriToResource(this, R.drawable.none);

//        mDb.addProfile(new PetProfile("Grant", "Lots of Details", "740-438-0612", imageURI));
//        mDb.addProfile(new PetProfile("Muggsy", "Lots of Details", "740-438-0612", getUriToResource(this, R.drawable.none)));
//        mDb.addProfile(new PetProfile("Knuckles", "Lots of Details", "740-438-0612", getUriToResource(this, R.drawable.none)));

        //create arrayList for Database dump, and adapter requires array list
        mProfileList = new ArrayList<>();
        mProfileList = mDb.getAllProfiles();

        //create addapter (while hooking it up to arraylist and layout)
        mProfileAdapter = new ProfileAdapter(this, R.layout.profile_list_item, mProfileList);

        //list view needs hooked up to adapter
        mListView.setAdapter(mProfileAdapter);
    }

    /**
     * Method is attached to ImageView "onClick"
     * Request permissions for Read/Write to external storage and Access to Camera
     * and sets instance variable Uri to the image selected by user
     *
     * @param view
     */
    public void addProfilePic(View view){
        ArrayList<String> permList = new ArrayList<>();

        //hook up each one to manifest (also checks to see current state of permission)
        int cameraPerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int readPerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        //check to see if permission is granted for each one
        //if its not equal to granted add it to ArrayList
        if(cameraPerm != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.CAMERA);
        if(readPerm != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if(writePerm != PackageManager.PERMISSION_GRANTED)
            permList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //Act.Compat needs String array not arrayList, used array list because because of its varying size
        if(permList.size() > 0)
        {
            String[] permArray = new String[permList.size()];
            ActivityCompat.requestPermissions(this, permList.toArray(permArray), CONSTANT);
        }
        //open imageGallery (by checking that all permissions equal granted)
        if(cameraPerm == PackageManager.PERMISSION_GRANTED && readPerm ==PackageManager.PERMISSION_GRANTED&& writePerm == PackageManager.PERMISSION_GRANTED)
        {
            //use an intent to launch gallery and take pics
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, CONSTANT);
        }else
            imageURI = getUriToResource(this, R.drawable.none);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CONSTANT && resultCode == RESULT_OK)
        {
            imageURI = data.getData();
            picView.setImageURI(imageURI);

            //make the imageView setImageUri()
        }else
            imageURI = getUriToResource(this, R.drawable.none);
    }

    /**
     * Method is attached to list layout button "onClick"
     * Method extracts data from the layouts EditText and creates a new PetProfile and adds it to database and to ListAdapter
     * @param view
     */
    public void addProfile(View view)
    {
        String name = nameEditText.getText().toString();
        String details = detailsEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        if(name.equals("")||details.equals("")||phone.equals(""))
            Toast.makeText(this, "Must enter correct data in all fields.", Toast.LENGTH_SHORT).show();
        else{
        //create new PetProfile out of recieved info, make Uri from uri instance
        PetProfile newProfile = new PetProfile(name, details, phone, imageURI);
            //add obj to database
            mDb.addProfile(newProfile);

            //add obj to List Adapter
            mProfileAdapter.add(newProfile);

            nameEditText.setText("");
            detailsEditText.setText("");
            phoneEditText.setText("");
            Uri resetUri = getUriToResource(this, R.drawable.none);
            imageURI = resetUri;
            picView.setImageURI(resetUri);
        }
    }
    /**
     * Method is attached to list items linear layout "onClick"
     * to update activity with a new layout showing bigger selected image along with a few other saved fields
     * @param view
     */
    public void viewProfileDetails(View view)
    {
        LinearLayout selectedLayout = (LinearLayout) view;

        PetProfile selectedProfile = (PetProfile) selectedLayout.getTag();

        Intent sendIntent = new Intent(this, PetDetailsActivity.class);
        sendIntent.putExtra("PetProfileObj",selectedProfile);

//        sendIntent.putExtra("IMAGE_PATH", selectedProfile.getImagePath().toString());
//        sendIntent.putExtra("NAME", selectedProfile.getName());
//        sendIntent.putExtra("DETAILS", selectedProfile.getDetails());
//        sendIntent.putExtra("PHONE", selectedProfile.getPhoneNumber());

        startActivity(sendIntent);
    }

    /**
     * NON-WORKING code. Method is ready to have button attached to layout
     * Method clears Database, Arraylist and Adapter of all saved objects
     * @param view
     */
    public void clearAllPets(View view)
    {
        mDb.deleteAllProfiles();
        mProfileList.clear();
        mProfileAdapter.clear();

    }

    /**
     * Method builds a correct Uri type image path to picture
     * @param context
     * @param resId
     * @return
     * @throws Resources.NotFoundException
     */
    public static Uri getUriToResource(@NonNull Context context, @AnyRes int resId)throws Resources.NotFoundException{
        Resources res = context.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                "://" + res.getResourcePackageName(resId)+
                '/'+res.getResourceTypeName(resId)+
                '/'+res.getResourceEntryName(resId));
    }
}
