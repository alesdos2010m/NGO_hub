package com.example.ngohub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class NGO_DashboardActivity extends AppCompatActivity {
    private Button button_choose, button_post;
    private ImageView image_preview;
    private EditText editText_caption, editText_title, editText_venue;

    StorageReference storageReference;                              //FirebaseStorage Reference
    private StorageTask uploadTask;
    private Uri imguri;
    private String downloadUri;
    private String current_NGO_UUID;
    private String EventPost_UUID;

    private FirebaseDatabase firebaseDatabase;                      //FirebaseDatabase declaration
    private DatabaseReference ngo_EventPosts_dbRef;                 //FirebaseDatabase Reference declaration
    private DatabaseReference ngo_NgoInformation_dbRef;             //FirebaseDatabase Reference declaration

    private FirebaseAuth mauth;                                     //FirebaseDatabase Authentication Declaration

    NGO_EventPosts ngo_eventPosts;                                  //FirebaseDatabase Object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo__dashboard);

        mauth = FirebaseAuth.getInstance();
        if (mauth.getCurrentUser() == null) {
            Toast.makeText(NGO_DashboardActivity.this, "Please Login as VO/NGO first...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(NGO_DashboardActivity.this, NGOSigninActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        current_NGO_UUID = mauth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference("NGO_Event_Post");    //storage reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        ngo_EventPosts_dbRef = firebaseDatabase.getReference("NGO_EventPosts");               //database reference
        if (firebaseDatabase.getReference("NgoInformation").child(current_NGO_UUID) != null) {
            ngo_NgoInformation_dbRef = firebaseDatabase.getReference("NgoInformation").child(current_NGO_UUID).child("NGO_EventPosts");
        }
        button_choose = (Button) findViewById(R.id.choose_image_Event_Post_NGO_Dashboard_btn);
        button_post = (Button) findViewById(R.id.post_Event_Post_NGO_Dashboard_btn);
        image_preview = (ImageView) findViewById(R.id.image_preview_Event_Post_NGO_Dashboard);
        editText_caption = (EditText) findViewById(R.id.caption_Event_Post_NGO_Dashboard);
        editText_title = (EditText) findViewById(R.id.title_Event_Post_NGO_Dashboard);
        editText_venue = (EditText) findViewById(R.id.venue_Event_Post_NGO_Dashboard);

        ngo_eventPosts = new NGO_EventPosts();

        //*********************************Set EventPost Image Values*********************************

        button_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileChooser();
            }
        });

        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadTask != null && uploadTask.isInProgress())
                {
                    Toast.makeText(NGO_DashboardActivity.this,"Please wait...", Toast.LENGTH_LONG).show();
                }
                else {
                    fileUploader();
                }
            }
        });
    }

    private String getExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void fileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data.getData() != null)
        {
            imguri = data.getData();
            image_preview.setImageURI(imguri);
        }
    }

    private void fileUploader()
    {
        if(imguri != null) {
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getExtension(imguri));

            uploadTask = reference.putFile(imguri);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(NGO_DashboardActivity.this, "Event Successfully Created...", Toast.LENGTH_LONG).show();
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUri = String.valueOf(uri);
                            btnPost();
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(this, "No Image Selected selected", Toast.LENGTH_SHORT).show();
        }
    }
    //***********************************Set Other Post Values***********************************
    private void getValues()
    {
        ngo_eventPosts.setTitle(editText_title.getText().toString());
        ngo_eventPosts.setCaption(editText_caption.getText().toString());
        ngo_eventPosts.setVenue(editText_venue.getText().toString());
        ngo_eventPosts.setImageAddress(downloadUri);
        ngo_eventPosts.setNgoInformation_UUID(current_NGO_UUID);
    }
    public void btnPost() {
        getValues();
        EventPost_UUID = UUID.randomUUID().toString();
        ngo_EventPosts_dbRef.child(EventPost_UUID).setValue(ngo_eventPosts);
        DatabaseReference pushedEventPost_UUID_dbRef = ngo_NgoInformation_dbRef.push();
        pushedEventPost_UUID_dbRef.setValue(EventPost_UUID);

        Toast.makeText(NGO_DashboardActivity.this, "Event Created Successfully...", Toast.LENGTH_LONG).show();
    }
}