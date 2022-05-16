package gg.cann1neof.mdev.swipenews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddSourceActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    final String SAVED_TEXT = "gg.cann1neof.mdev.swipenews.ClientSources2";
    ArrayList<String> sources;
    ChipGroup chipGroup;
    MaterialToolbar topAppBar;
    EditText newSource;
    ImageButton addNewSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_source);

        topAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);
        chipGroup = (ChipGroup) findViewById(R.id.chipGroup);
        newSource = (EditText) findViewById(R.id.newSourceText);
        addNewSource = (ImageButton) findViewById(R.id.btnAddSource);

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSourceActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        fetchSources();

        showSources();

        addNewSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newText = newSource.getText().toString();
                if(newText.indexOf('.') != -1){
                    newText = newText.replace(" ", "");
                    sources.add(newText);
                    setSources(SAVED_TEXT, sources, AddSourceActivity.this);
                    newSource.setText("");

                    addNewChip(newText);
                }
            }
        });

        setSources(SAVED_TEXT, sources, this);
    }

    private void showSources() {
        if(sources != null){
            chipGroup.removeAllViews();
            for(String source : sources){
                addNewChip(source);
            }
        }
    }

    private void addNewChip(String source) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_template, null);
        chip.setText(source);
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = ((Chip) view).getText().toString();
                sources.remove(text);
                chipGroup.removeView(view);
                setSources(SAVED_TEXT, sources, AddSourceActivity.this);
                showSources();
            }
        });
        chipGroup.addView(chip);
    }

    private void fetchSources() {
        String savedText = getSources(SAVED_TEXT, this);
        if(savedText != null) {
            sources = new ArrayList<String>(Arrays.asList(savedText.split(",")));
        } else {
            sources = new ArrayList<String>();
        }
    }

    public static String getSources(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static void setSources(String key, List<String> value, Context context) {
        String result = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if(value != null){
                result = value.stream().collect(Collectors.joining(","));
            }
        }

        if(result == null || result.length() == 0){
            result = null;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, result);
        editor.apply(); // or editor.commit() in case you want to write data instantly
    }
}
