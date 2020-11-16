package com.github.aloike.libgauge.demogauge;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.github.aloike.libgauge.demogauge.databinding.ActivityMainBinding;
import com.github.aloike.libgauge.parts.Graduation;
import com.github.aloike.libgauge.parts.Label;
import com.github.aloike.libgauge.parts.Section;

import java.util.zip.DataFormatException;

public class MainActivityDesign extends AppCompatActivity {

    private ActivityMainBinding m_binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        m_binding   = ActivityMainBinding.inflate(getLayoutInflater());
        View view   = m_binding.getRoot();
        setContentView(view);

        m_binding.gauge.setValue( 50f );


        m_binding.gauge21.setAngles(-135, 90);
        m_binding.gauge21.setGraduationRange(-0.5f, 2.0f);
        m_binding.gauge21.setValueDisplayRange(-1f, 3.0f);


        Graduation lGraduationMajor = new Graduation();
        lGraduationMajor.setCount(6);
        lGraduationMajor.setLinesLengthFactor(10f);
        lGraduationMajor.setValuesTextFormat("%.01f");
        lGraduationMajor.setGraduationColor(Color.WHITE);

        Graduation lGraduationMinor = new Graduation();
        lGraduationMinor.setCount(11);
        lGraduationMinor.setLinesLengthFactor(5f);
        lGraduationMinor.setValuesTextFormat("");
        lGraduationMinor.setGraduationColor(Color.DKGRAY);

        m_binding.gauge21.getDial().getGraduationsList().clear();
        m_binding.gauge21.getDial().getGraduationsList().add(lGraduationMinor);
        m_binding.gauge21.getDial().getGraduationsList().add(lGraduationMajor);


        /*
            Dirty testing of labels config
         */
        m_binding.gauge21.getDial().getLabelsList().get(0).setText("Gauge 21");
        m_binding.gauge21.getDial().getLabelsList().remove(1);


        m_binding.gauge22.getDial().getLabelsList().get(0).setText("Gauge 22");
        Label   lLabel22Left    = new Label();
        lLabel22Left.setPosX_pc(25f);
        lLabel22Left.setPosY_pc(50f);
        lLabel22Left.setText("Left");
        m_binding.gauge22.getDial().getLabelsList().add(lLabel22Left);
        Label   lLabel22Right   = new Label();
        lLabel22Right.setPosX_pc(75f);
        lLabel22Right.setPosY_pc(50f);
        lLabel22Right.setText("Right");
        m_binding.gauge22.getDial().getLabelsList().add(lLabel22Right);


        /*
            Dirty testing of sections
         */
        try {
            Section lSectionA = new Section(
                    -2f,
                    0f,
                    Color.BLUE
            );

            m_binding.gauge21.getDial().getSectionsList().clear();
            m_binding.gauge21.getDial().getSectionsList().add(lSectionA);

            m_binding.gauge21.getDial().getSectionsList().add(
                    new Section(
                            1.0f,
                            2.0f,
                            Color.YELLOW
                    )
            );

            m_binding.gauge21.getDial().getSectionsList().add(
                    new Section(
                            1.5f,
                            2.0f,
                            Color.RED
                    )
            );
        }
        catch (DataFormatException e)
        {
            /* Don't really care, actually. */
        }


        /*
            Dirty testing of some value display functionality
         */
        m_binding.gauge21.getValueDisplay().setValueDisplayFormat("% 1.01f");
        m_binding.gauge21.getValueDisplay().setUnitText("bar");


        /*
            Dirty testing of the "disable" functionality
         */
        m_binding.gauge22.setEnabled(false);


        /* Configure the progress bar for testing */
        m_binding.seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        m_binding.seekBar.setMin(-20);
        m_binding.seekBar.setMax(100);
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            m_binding.gauge.setValue(progress);
            m_binding.gauge21.setValue(progress / 10.0f);
            m_binding.gauge22.setValue(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };
}