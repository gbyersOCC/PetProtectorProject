package edu.orangecoastcollege.cs273.gabyers.petprotector;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Grant Byers
 * CS273    10/25/2016
 * Class applies Model layer to PetProtector project. Basic logic is done hear to ensure the id, name, details , phone number and
 * image path name a secured in a PetProfile object.
 */

public class PetProfile implements Parcelable {
    private int mId;
    private String mName;
    private String mDetails;
    private String mPhoneNumber;
    private Uri mImagePath;
    //private String mImagePath;

    public PetProfile()
    {
        this(-1, "","","",null);
    }
    public PetProfile(int id, String name, String details, String phoneNumber, Uri imagePath)
    {
        mId = id;
        mName = name;
        mDetails = details;
        mPhoneNumber = phoneNumber;
        mImagePath = imagePath;
    }
    public PetProfile(String name, String details, String phoneNumber, Uri imagePath)
    {
        this(-1, name, details, phoneNumber, imagePath);
    }

    public PetProfile(Parcel source)
    {
        mId = source.readInt();
        mName = source.readString();
        mDetails = source.readString();
        mPhoneNumber = source.readString();
        mImagePath = Uri.parse(source.readString());
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public Uri getImagePath() {

        return mImagePath;
    }

    public void setImagePath(Uri imagePath) {
        mImagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mDetails);
        dest.writeString(mPhoneNumber);
        dest.writeString(mImagePath.toString());
    }

    public static final Parcelable.Creator<PetProfile> CREATOR = new Parcelable.Creator<PetProfile>(){
        @Override
        public PetProfile createFromParcel(Parcel source) {
            return new PetProfile(source);
        }

        @Override
        public PetProfile[] newArray(int size) {
            return new PetProfile[size];
        }
    };
}
