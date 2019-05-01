package fit.realstrength.www.real_strength;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateNameAndPhoto extends AppCompatActivity {

    private EditText etName;
    private Button btnDone;
    private String updateName;
    private ImageView updatePhoto;
    private Button btnUpload;
    private ProgressDialog progressDialog;

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");
    private Uri updatedUri = ProfileHomePage.downloadUri;

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int GET_FROM_GALLERY = 2;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference sref = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name_and_photo);

        etName = findViewById(R.id.etName);
        btnDone = findViewById(R.id.btnFinish);
        updatePhoto = findViewById(R.id.imageView);
        btnUpload = findViewById(R.id.btnUpload);
        progressDialog = new ProgressDialog(this);

        etName.setText(ProfileHomePage.displayName);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString().length() != 0) {
                    updateName = etName.getText().toString();
                    //ProfileHomePage.tvName.setText(updateName);
                    updateDatabaseName();
                    finish();
                    startActivity(new Intent(UpdateNameAndPhoto.this,ProfileHomePage.class));
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please enter a valid name",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //display previous image
        Glide.with(this).load(ProfileHomePage.downloadUri).into(updatePhoto);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }
    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    dispatchTakePictureIntent();

                } else if (items[i].equals("Gallery")) {
                    galleryImage();
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }
    private Uri photoURI;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "fit.realstrength.www.real_strength",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private void galleryImage() {
        // startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GET_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //for camera
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
          /* Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImage.setImageBitmap(imageBitmap);
            finalPhoto = imageBitmap;
            */
            //setPic();
            progressDialog.setMessage("Uploading....");
            progressDialog.show();

            Glide.with(getApplicationContext()).load(photoURI).into(updatePhoto);
            uploadFile(photoURI);

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(photoURI).build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Log.d(TAG, "User profile updated.");
                            }
                        }
                    });

        }

        //for gallery
        else if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {

            progressDialog.setMessage("Uploading....");
            progressDialog.show();

            Uri targetUri = data.getData();

            Glide.with(getApplicationContext()).load(targetUri).into(updatePhoto);
            uploadFile(targetUri);


            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(targetUri).build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Log.d(TAG, "User profile updated.");
                            }
                        }
                    });

        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void updateDatabaseName() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseMembers.child(uid).child("memberName").setValue(updateName);
    }


    public void uploadFile(Uri imagUri) {
        if (imagUri != null) {

            final StorageReference imageRef = sref.child("Profile Pictures") // folder path in firebase storage
                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            imageRef.putFile(imagUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                            // Get the download URL
                            //downloadUri = snapshot.getUploadSessionUri();
                            progressDialog.dismiss();

                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    updatedUri = uri;
                                    ProfileHomePage.downloadUri = uri;
                                }
                            });
                            // use this download url with imageview for viewing & store this link to firebase message data
                            Toast.makeText(getApplicationContext(),"Upload finished",Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // show message on failure may be network/disk ?
                            Toast.makeText(getApplicationContext(),"Upload failed",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpdateNameAndPhoto.this,ProfileHomePage.class));
        finish();
    }
}
