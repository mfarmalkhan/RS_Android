package fit.realstrength.www.real_strength;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.login.Login;
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
import com.squareup.picasso.Picasso;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class UploadPhoto extends AppCompatActivity {


    private static final String TAG = "Hello";
    private ImageView profileImage;
    private static final int REQUEST_TAKE_PHOTO = 1;
    public static final int GET_FROM_GALLERY = 2;
    private static final int PIC_CROP = 3;
    private Button btnUpload;
    private ProgressBar progressPhoto;
    private Button btnFinish;
    private EditText etName;
    private String enteredName = "Your Name";
    private static Bitmap finalPhoto;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String mCurrentPhotoPath;
    private ProgressDialog progressDialog;

    private Uri photoURI;
    private StorageReference sref = FirebaseStorage.getInstance().getReference(); // please go to above link and setup firebase storage for android
    public static Uri downloadUri;
    public static Uri finalUri;
    private boolean infoInitialized = false;

    protected DatabaseReference databaseMembers = FirebaseDatabase.getInstance().getReference("members");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_pic);


        btnUpload = findViewById(R.id.btnUpload);
        profileImage = findViewById(R.id.imageView);
        progressPhoto = findViewById(R.id.progressPhoto);
        btnFinish = findViewById(R.id.btnFinish);
        etName = findViewById(R.id.etName);
        progressDialog = new ProgressDialog(this);

        progressPhoto.setProgress(100);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Finish profile questions
                //Start news feed with sidebar
                if (etName.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_SHORT).show();

                } else {

                    enteredName = etName.getText().toString();
                    updateDatabase();
                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        infoInitialized = true;


                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enteredName = etName.getText().toString();
                selectImage();
            }
        });


    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPhoto.this);
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

            Glide.with(getApplicationContext()).load(photoURI).into(profileImage);
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

            Glide.with(getApplicationContext()).load(targetUri).into(profileImage);
            uploadFile(targetUri);
            finalUri = targetUri;

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

            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));


            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static Bitmap getFinalPhoto() {
        return finalPhoto;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = profileImage.getWidth();
        int targetH = profileImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        //imageOrientationValidator(bitmap,mCurrentPhotoPath);
        profileImage.setImageBitmap(imageOrientationValidator(bitmap,mCurrentPhotoPath));
        finalPhoto = imageOrientationValidator(bitmap,mCurrentPhotoPath);

    }

    private Bitmap imageOrientationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }


    public void uploadFile(Uri imagUri) {
        if (imagUri != null) {

            final StorageReference imageRef = sref.child("Profile Pictures") // folder path in firebase storage
                    .child(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            //imagUri.getLastPathSegment()
            Log.d(TAG,"Uploaded");
            //Toast.makeText(getApplicationContext(),FirebaseAuth.getInstance().getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();

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
                                    downloadUri = uri;
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
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

    private void updateDatabase() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseMembers.child(uid).child("memberName").setValue(enteredName);
    }

}