package edu.orangecoastcollege.cs273.gabyers.petprotector;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grant Byers
 * 10/26/2016   Class builds a custom ArrayAdapter to which saved PetProfiles will be displayed and saved in tag locker for
 * later display
 */

public class ProfileAdapter extends ArrayAdapter<PetProfile>
{

    private Context mContext;
    private int mResId;
    private List<PetProfile> mProfileList = new ArrayList<>();

    private ImageView  listItemImageView;
    private TextView    listItemNameTextView;
    private TextView listItemDetailsTextView;
    private LinearLayout listLinearLayout;


    public ProfileAdapter(Context context, int resource, List<PetProfile> objects) {
        super(context, resource, objects);

        mContext = context;
        mResId = resource;
        mProfileList = objects;
    }

    /**
     * method inflates individual views for the List Adapter by creating obj and showing its details via accessor methods
     * @param pos
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        //inflate view
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(mResId, null);

        //reference to layout widgets
        listItemNameTextView = (TextView) v.findViewById(R.id.listItemNameTextView);
        listItemImageView = (ImageView) v.findViewById(R.id.listItemImageView);
        listItemDetailsTextView = (TextView) v.findViewById(R.id.listItemDetailsTextView);
        listLinearLayout = (LinearLayout) v.findViewById(R.id.listLinearLayout);

        //create PetProfile object from (provided position)
        PetProfile displayObj = mProfileList.get(pos);

        //use created obj accessors to update the text of TexView Widgets
        listItemNameTextView.setText(displayObj.getName());
        listItemDetailsTextView.setText(displayObj.getDetails());
        listItemImageView.setImageURI(displayObj.getImagePath());

        //put obj in locker
        listLinearLayout.setTag(displayObj);
        return v;
    }
}

