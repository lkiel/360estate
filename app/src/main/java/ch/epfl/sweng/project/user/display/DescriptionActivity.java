package ch.epfl.sweng.project.user.display;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.project.DataMgmt;
import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.slider.SlideActivity;
import ch.epfl.sweng.project.engine3d.PanoramaActivity;

import static ch.epfl.sweng.project.DataMgmt.getImgFromUrlIntoView;


public class DescriptionActivity extends AppCompatActivity {

    public static final int cellSize = 300;
    private boolean isInitiallyInFavorite;
    private CheckBox checkBoxFavorite;
    private String idItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        idItem = getIntent().getStringExtra("idItem");
        final ArrayList<String> imagesURL = new ArrayList<>();
        StringBuilder descriptionBuilder = new StringBuilder();
        StringBuilder titleBuilder = new StringBuilder();
        DataMgmt.getDataForDescription(idItem, imagesURL, descriptionBuilder, titleBuilder);
        String description = descriptionBuilder.toString();
        String title = titleBuilder.toString();

        TextView descriptionView = (TextView) findViewById(R.id.description_text);
        descriptionView.setText(description.toCharArray(), 0, description.length());

        TextView titleView = (TextView) findViewById(R.id.description_title);
        titleView.setText(title.toCharArray(), 0, title.length());

        View.OnClickListener imgListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DescriptionActivity.this, SlideActivity.class);
                Bundle extras = new Bundle();
                extras.putString("URL", (String) view.getTag());
                extras.putStringArrayList("ArrayURL", imagesURL);
                intent.putExtras(extras);
                startActivity(intent);
            }
        };

        final LinearLayout scrollImg = (LinearLayout) findViewById(R.id.imgs);
        for (String url : imagesURL) {
            ImageView imgV = new ImageView(this);
            imgV.setTag(url);
            getImgFromUrlIntoView(this, url, imgV);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(cellSize, cellSize);
            params.setMargins(0, 0, 0, 0);
            imgV.setLayoutParams(params);
            imgV.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgV.setOnClickListener(imgListener);
            scrollImg.addView(imgV);
        }

        Button button = (Button) findViewById(R.id.action_launch_panorama);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToPanorama = new Intent(DescriptionActivity.this, PanoramaActivity.class);
                intentToPanorama.putExtra("id", idItem);
                startActivity(intentToPanorama);
            }
        });

        checkBoxFavorite = (CheckBox) findViewById(R.id.addToFavorites);

        isInitiallyInFavorite = ListActivity.favoriteContainsUrl(idItem);

        if (isInitiallyInFavorite) {
            checkBoxFavorite.setChecked(true);
        } else {
            checkBoxFavorite.setChecked(false);
        }


        checkBoxFavorite.setOnClickListener(new OnCheckedFavorite(idItem, checkBoxFavorite));
    }

    @Override
    public void onBackPressed() {
        ListActivity.synchronizeServer();
        if (getIntent().getBooleanExtra("isToggled", false)) {
            if (isInitiallyInFavorite != checkBoxFavorite.isChecked()) {
                if (isInitiallyInFavorite) {
                    ListActivity.removeItem(idItem);
                } else {
                    ListActivity.addItem(idItem);
                }
            }
        }
        ListActivity.notifyItemAdapter();
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        ListActivity.synchronizeServer();
        super.onStop();
    }
}


