package edu.orangecoastcollege.cs273.gabyers.petprotector;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Class overrides Activity Lifecycle method "onCreate" in order to receive intent and display correct user provided information
 */
public class PetDetailsActivity extends AppCompatActivity {


    private ImageView picView;
    private TextView phoneTextView;
    private TextView detailsTextView;
    private TextView nameTextView;

    /**
     * Method references correct widgets and receives intent in order to display the list item information
     * While its abstract, this is going to display the info from the item that was selected from another class's ListAdapter
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        nameTextView = (TextView) findViewById(R.id.detailsNameTextView);
        phoneTextView = (TextView) findViewById(R.id.detailsPhoneTextView);
        detailsTextView = (TextView) findViewById(R.id.detailsDetailsTextView);
        picView = (ImageView) findViewById(R.id.detailsPicImageView);

       PetProfile selectedPet = getIntent().getExtras().getParcelable("PetProfileObj");

//        String name = receivedIntent.getStringExtra("NAME");
//        String details = receivedIntent.getStringExtra("DETAILS");
//        String phoneNumber = receivedIntent.getStringExtra("PHONE");
//        Uri imagePath = Uri.parse(receivedIntent.getStringExtra("IMAGE_PATH"));

        picView.setImageURI(selectedPet.getImagePath());
        nameTextView.setText(selectedPet.getName());
        detailsTextView.setText(selectedPet.getDetails());
        phoneTextView.setText(selectedPet.getPhoneNumber());

    }
}
