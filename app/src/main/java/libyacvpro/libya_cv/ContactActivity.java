package libyacvpro.libya_cv;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;

public class ContactActivity extends AppCompatActivity {

    @BindView(R.id.btnContact)
    ImageView btnContact;
    @BindView(R.id.btnRate)
    ImageView btnRate;

    @BindView(R.id.btnURL)
    ImageView btnURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        btnURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                String wwwUrl =  "https://www.libyacv.com" ;
                websiteIntent.setData(Uri.parse(wwwUrl));
                startActivity(websiteIntent);
            }});

        btnRate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" +  getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" +  getPackageName())));
                }
            }});

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent testIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + "موضوع الرسالة" + "&body=" + "محتوي الرسالة" + "&to=" + "info@libyacv.com");
                testIntent.setData(data);
                startActivity(testIntent);
            }});
    }
}
