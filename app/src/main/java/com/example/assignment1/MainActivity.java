package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void openWebsite(View view) {
        // Always use https:// for better security
        Uri webpage = Uri.parse("https://www.android.com");

        // Create an intent with ACTION_VIEW and the URL
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        // Verify there's an activity to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Add flags to open in a new task and clear any existing instances
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            // Provide a fallback option
            Toast.makeText(this, "Please install a web browser", Toast.LENGTH_SHORT).show();

            // Optionally open Play Store to install a browser
            Uri marketUri = Uri.parse("market://details?id=com.android.chrome");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            if (marketIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(marketIntent);
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void sendEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // Only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"example@example.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "Message body");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No email app installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void makePhoneCall(View view) {
        String phoneNumber = "tel:1234567890";

        // Check for CALL_PHONE permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PHONE);
        } else {
            startCall(phoneNumber);
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void startCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(phoneNumber));

        if (null != intent.resolveActivity(getPackageManager())) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No phone app installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(null); // Call again now that permission is granted
            } else {
                Toast.makeText(this, "Permission denied to make phone calls", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void viewLocation(View view) {
        // Geo location for Google HQ
        Uri location = Uri.parse("geo:37.4221,-122.0841?z=14");
        Intent intent = new Intent(Intent.ACTION_VIEW, location);

        if (null != intent.resolveActivity(getPackageManager())) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No map app installed", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void shareContent(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome app!");

        // Create chooser to explicitly show sharing options
        Intent chooser = Intent.createChooser(intent, "Share using");

        if (null != intent.resolveActivity(getPackageManager())) {
            startActivity(chooser);
        } else {
            Toast.makeText(this, "No apps available to share", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void openSettings(View view) {
        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Unable to open settings", Toast.LENGTH_SHORT).show();
        }
    }
}