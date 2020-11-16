package com.github.aloike.libgauge.demogauge;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import com.github.aloike.libgauge.demogauge.databinding.ActivityControlsBinding;
import com.github.aloike.libgauge.demogauge.databinding.ContentScrollingBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;

public class ActivityControls extends AppCompatActivity {

    private ActivityControlsBinding m_binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.m_binding  = ActivityControlsBinding.inflate(this.getLayoutInflater());
        View lView   = this.m_binding.getRoot();
        setContentView( lView );

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(this.m_binding.toolbar);
        //CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        this.m_binding.toolbarLayout.setTitle(getTitle());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
        this.m_binding.fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        this.m_binding.includedContentScrolling.bDialBackgroundColor.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        dialBackgroundColorPicker();
                    }
                }
        );


        this.m_binding.includedContentScrolling.cbEnabled.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        updateGauge();
                    }
                }
        );

        /* Create a listener for every SeekBar to trigger a gauge update - Quick and dirty */
        SeekBar.OnSeekBarChangeListener lListenerSeekBar_UpdateGauge
                = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                updateGauge();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };


        this.m_binding.includedContentScrolling.sbAngleBegin.setOnSeekBarChangeListener(
                lListenerSeekBar_UpdateGauge
        );

        this.m_binding.includedContentScrolling.sbAngleEnd.setOnSeekBarChangeListener(
                lListenerSeekBar_UpdateGauge
        );

        this.m_binding.includedContentScrolling.sbGraduationRangeMin.setOnSeekBarChangeListener(
                lListenerSeekBar_UpdateGauge
        );

        this.m_binding.includedContentScrolling.sbGraduationRangeMax.setOnSeekBarChangeListener(
                lListenerSeekBar_UpdateGauge
        );

        this.m_binding.includedContentScrolling.sbValue.setOnSeekBarChangeListener(
                lListenerSeekBar_UpdateGauge
        );

        this.m_binding.includedContentScrolling.sbValueRangeMin.setOnSeekBarChangeListener(
                lListenerSeekBar_UpdateGauge
        );

        this.m_binding.includedContentScrolling.sbValueRangeMax.setOnSeekBarChangeListener(
                lListenerSeekBar_UpdateGauge
        );



    }

    private void    dialBackgroundColorPicker()
    {
        int lCurrentColor
                = m_binding.includedContentScrolling.gaugeControls.getDial().getBackgroundColor();

        final ColorPicker cp
                = new ColorPicker(
                        this,
                        Color.red(lCurrentColor),
                        Color.green(lCurrentColor),
                        Color.blue(lCurrentColor) );


        /* Show color picker dialog */
        cp.show();

        /* On Click listener for the dialog, when the user select the color */
        Button okColor = (Button)cp.findViewById(R.id.okColorButton);

        okColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* You can get single channel (value 0-255) */
//                selectedColorR = cp.getRed();
//                selectedColorG = cp.getGreen();
//                selectedColorB = cp.getBlue();
                String hex
                        = String.format(
                                "#%02X%02X%02X",
                                cp.getRed(),
                                cp.getGreen(),
                                cp.getBlue() );
                m_binding.includedContentScrolling.bDialBackgroundColor.setText(hex);

                /* Or the android RGB Color (see the android Color class reference) */
                m_binding.includedContentScrolling.gaugeControls.getDial().setBackgroundColor(
                        cp.getColor()
                );
//                cp.getColor();


                cp.dismiss();
            }
        });
    }

    static AlertDialog s_dialog = null;
    private void    showPopup(String pTitle, String pMessage)
    {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(pMessage)
                .setTitle(pTitle);

        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        dialog.dismiss();
                    }
                });

        // 3. Get the AlertDialog from create()
        if( s_dialog == null) {
            s_dialog    = builder.create();
            s_dialog.show();
        }
        else if(! s_dialog.isShowing())
        {
            s_dialog.show();
        }
    }

    private void    updateGauge()
    {
        updateGauge_angles();
        updateGauge_enabled();
        updateGauge_range();
        updateGauge_value();
    }

    private void    updateGauge_angles()
    {
        int lAngleBegin
                = m_binding.includedContentScrolling.sbAngleBegin.getProgress();
        int lAngleEnd
                = m_binding.includedContentScrolling.sbAngleEnd.getProgress();

        try {
            m_binding.includedContentScrolling.gaugeControls
                    .setAngles(
                            lAngleBegin,
                            lAngleEnd);
        }
        catch(AssertionError e)
        {
            showPopup(
                    "Assertion while setting angles",
                    e.getMessage() );

            /* Reset to current used values */
            m_binding.includedContentScrolling.sbAngleBegin.setProgress(
                    m_binding.includedContentScrolling.gaugeControls.getAngles().getStart()
            );

            m_binding.includedContentScrolling.sbAngleEnd.setProgress(
                    m_binding.includedContentScrolling.gaugeControls.getAngles().getStop()
            );
        }

    }

    private void    updateGauge_enabled()
    {
        boolean lEnabled
                = m_binding.includedContentScrolling.cbEnabled.isChecked();

        m_binding.includedContentScrolling.gaugeControls
                .setEnabled( lEnabled );
    }

    private void    updateGauge_range()
    {
        int lValueRangeMin
                = m_binding.includedContentScrolling.sbValueRangeMin.getProgress();

        int lValueRangeMax
                = m_binding.includedContentScrolling.sbValueRangeMax.getProgress();

        int lGraduationRangeMin
                = m_binding.includedContentScrolling.sbGraduationRangeMin.getProgress();

        int lGraduationRangeMax
                = m_binding.includedContentScrolling.sbGraduationRangeMax.getProgress();


        try {
            m_binding.includedContentScrolling.gaugeControls
                    .setValueDisplayRange(
                            lValueRangeMin,
                            lValueRangeMax );

            m_binding.includedContentScrolling.gaugeControls
                    .setGraduationRange(
                            lGraduationRangeMin,
                            lGraduationRangeMax );
        }
        catch(AssertionError e)
        {
            showPopup(
                    "Assertion while setting ranges",
                    e.getMessage() );

            /* Reset to current used values */
            m_binding.includedContentScrolling.sbValueRangeMin.setProgress(
                    (int)m_binding.includedContentScrolling.gaugeControls.getRange()
                            .getValueDisplayMin()
            );

            m_binding.includedContentScrolling.sbValueRangeMax.setProgress(
                    (int)m_binding.includedContentScrolling.gaugeControls.getRange()
                            .getValueDisplayMax()
            );

            m_binding.includedContentScrolling.sbGraduationRangeMin.setProgress(
                    (int)m_binding.includedContentScrolling.gaugeControls.getRange()
                            .getGraduationMin()
            );

            m_binding.includedContentScrolling.sbGraduationRangeMax.setProgress(
                    (int)m_binding.includedContentScrolling.gaugeControls.getRange()
                            .getGraduationMax()
            );
        }
    }

    private void    updateGauge_value()
    {
        int lValue
                = m_binding.includedContentScrolling.sbValue.getProgress();

        try {
            m_binding.includedContentScrolling.gaugeControls
                    .setValue(lValue);
        }
        catch(AssertionError e)
        {
            showPopup(
                    "Assertion while setting value",
                    e.getMessage() );

            /* Reset to current used values */
            m_binding.includedContentScrolling.sbValue.setProgress(
                    (int)m_binding.includedContentScrolling.gaugeControls.getValue()
            );
        }

    }
}