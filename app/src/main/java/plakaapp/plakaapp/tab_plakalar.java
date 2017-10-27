package plakaapp.plakaapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Created by Simulakra on 28.10.2017.
 */


public class tab_plakalar extends Activity {

    SeekBar redSeekBar;
    SeekBar greenSeekBar;
    SeekBar blueSeekBar;

    ImageView picker;

    int redValue;
    int greenValue;
    int blueValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_plakalar);

        //Kaynak: http://kod5.org/android-seekbar-kullanimi/
        //Berke DÜNDAR 28.10.2017

        picker=findViewById(R.id.plakaRenkPalet);

        redSeekBar = (SeekBar) findViewById(R.id.seekBarRed);
        greenSeekBar = (SeekBar) findViewById(R.id.seekBarGreen);
        blueSeekBar = (SeekBar) findViewById(R.id.seekBarBlue);

        redValue  = redSeekBar.getProgress();
        greenValue  = greenSeekBar.getProgress();
        blueValue  = blueSeekBar.getProgress();

        redSeekBar.setMax(255);
        greenSeekBar.setMax(255);
        blueSeekBar.setMax(255);

        picker.setBackgroundColor(Color.rgb(redValue,greenValue,blueValue));

        redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //Arkaplanı yeni değere göre set ediyoruz.
                redValue = progress;
                picker.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
            }
        });

        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //Arkaplanı yeni değere göre set ediyoruz.
                greenValue = progress;
                picker.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
            }
        });

        blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //Arkaplanı yeni değere göre set ediyoruz.
                blueValue = progress;
                picker.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
            }
        });

    }
}
