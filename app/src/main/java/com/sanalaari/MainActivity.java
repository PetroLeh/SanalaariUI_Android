package com.sanalaari;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView resultView;
    SeekBar lengthBar;
    EditText letterInput;
    Button searchButton;

    Sanalaari sanalaari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> rawData = readFile("kotus-sanalista_v1.xml");
        sanalaari = new Sanalaari(rawData);

        lengthBar = (SeekBar) findViewById(R.id.lengthBar);
        letterInput = (EditText) findViewById(R.id.letterInput);
        searchButton = (Button) findViewById(R.id.searchButton);
        resultView = (TextView) findViewById(R.id.resultView);

        setUpViews();
    }

    private void setUpViews() {
        letterInput.setActivated(true);
        resultView.setText("Sanalistan koko: " + sanalaari.size() + " sanaa");
        lengthBar.setEnabled(false);

        searchButton.setOnClickListener(view -> {
            String letterSet = letterInput.getText().toString();
            if (letterSet.length() < 2) {
                lengthBar.setEnabled(false);
                resultView.setText("Aewan liian vähän kirjaimia");

            } else {
                lengthBar.setEnabled(true);
                lengthBar.setMax(letterSet.length());
                int length = lengthBar.getProgress();
                ArrayList<String> resultSet = sanalaari.getPlain(letterSet, length);
                String result = "ei tuloksia";
                if (resultSet.size() > 0) {
                    result = "kirjaimista '" + letterSet + "' saa\n"
                            + "muodostettua seuraavat\n"
                            + length + "-kirjaimiset sanat (" + resultSet.size() + " kpl):\n\n"
                            + toSingleString(resultSet);
                }
                resultView.setText(result);
            }
        });

        lengthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int length, boolean b) {
                if (length < 2)
                    length = 2;
                seekBar.setProgress(length);

                String letterSet = letterInput.getText().toString();
                ArrayList<String> resultSet = sanalaari.getPlain(letterSet, length);
                String result = "ei tuloksia";
                if (resultSet.size() > 0) {
                    result = "kirjaimista '" + letterSet + "' saa\n"
                            + "muodostettua seuraavat\n"
                            + length + "-kirjaimiset sanat (" + resultSet.size() + " kpl):\n\n"
                            + toSingleString(resultSet);
                }
                resultView.setText(result);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private ArrayList<String> readFile(String fName) {
        ArrayList<String> result = new ArrayList<>();
        try {
            InputStream stream = getAssets().open(fName);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream, "UTF-8"), 4096);
            String row = reader.readLine();
            while(row != null) {
                result.add(row);
                row = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading file " + fName);
            System.out.println(e.getMessage());
        }
        return result;
    }

    private String toSingleString(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder("");
        for (String s : list) {
            sb.append(s.toUpperCase());
            sb.append("\n");
        }
        return sb.toString();
    }
}
