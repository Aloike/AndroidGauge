package com.github.aloike.libgauge.parts;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.aloike.libgauge.attributes.Angles;
import com.github.aloike.libgauge.attributes.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

public class Dial
{
    private int     m_backgroundColor   = Color.BLACK;
    private Paint   m_backgroundPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap  m_bitmap            = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888);;
    protected float m_valueRangeFactor  = 1.0f;

    private boolean     m_updateRequested   = true;

    private List<Graduation>    m_graduations   = new ArrayList<Graduation>();
    private ArrayList<Label>    m_labels        = new ArrayList<Label>();
    private List<Section>       m_sections      = new ArrayList<Section>();


    public Dial()
    {
        this.init();
        this.invalidate();

        // TODO Remove section declarations! Those are here for test purpose only !
        try {
            Section lSection = new Section(0f, 20f, Color.GREEN);
            m_sections.add(lSection);

            lSection = new Section(40f, 60f, Color.YELLOW);
            m_sections.add(lSection);

            lSection = new Section(80f, 100f, Color.RED);
            m_sections.add(lSection);


        } catch (DataFormatException e) {

        }
    }


    public int  getBackgroundColor()
    {
        return this.m_backgroundColor;
    }

    public Bitmap bitmap()
    {
        return this.m_bitmap;
    }

    private void    init()
    {
        this.m_backgroundPaint.setColor(Color.BLACK);
        this.m_backgroundPaint.setStyle(Paint.Style.FILL);

        /*
         *  By default, this gauge has one graduation
         */
        Graduation lGraduation
                = new Graduation();
        this.m_graduations.add(lGraduation);

        /*
         *  By default, this gauge has two labels
         */
        Label   lLabelPtr
                = new Label();

        lLabelPtr.setPosX_pc(50f);
        lLabelPtr.setPosY_pc(25f);
        lLabelPtr.setText("Label 0");
        lLabelPtr.setTextSizeFactor(7.5f);
        this.m_labels.add(lLabelPtr);

        lLabelPtr   = new Label();
        lLabelPtr.setPosX_pc(50f);
        lLabelPtr.setPosY_pc(35f);
        lLabelPtr.setText("Label 1");
        lLabelPtr.setTextSizeFactor(7.5f);
        this.m_labels.add(lLabelPtr);
    }

    public void setBackgroundColor(int pColor)
    {
        this.m_backgroundColor  = pColor;

        this.invalidate();
    }

    /**
     *  A call to this method will ask for an update of the bitmap on the next draw.
     */
    public void invalidate()
    {
        this.m_updateRequested  = true;
    }

    /**
     * Generates a bitmap with all static elements.
     */
    private void updateBitmap(Canvas pCanvas, Range pRange, Angles pAngles)
    {
        /* Choose the bitmap side length from the smallest of canvas width and height */
        float   lCanvasSide
                = Math.min(
                        pCanvas.getWidth(),
                        pCanvas.getHeight()
                );

        /* Check that the size is not null (can happen in Android Studio's layout editor) */
        if( lCanvasSide <= 0 )
        {
            return;
        }


        this.m_bitmap
                = Bitmap.createBitmap(
                        (int)lCanvasSide,
                        (int)lCanvasSide,
                        Bitmap.Config.ARGB_8888
                );


        // Set the background to transparent
        this.m_bitmap.eraseColor(Color.TRANSPARENT);

        // Create a canvas for this bitmap
        Canvas lCanvas  = new Canvas(this.m_bitmap);

        float lCanvasCenterX  = lCanvas.getWidth() / 2f;
        float lCanvasCenterY  = lCanvas.getHeight() / 2f;

        /* Draw the dial background */
        float   lRadius
                = lCanvasSide
                / 2.0f;

        this.m_backgroundPaint.setColor(this.m_backgroundColor);
        lCanvas.drawCircle(
                lCanvasCenterX,
                lCanvasCenterY,
                lRadius,
                this.m_backgroundPaint
        );

        /* Draw sections */
        for (Section lSectionIt : m_sections) {
            lSectionIt.draw(lCanvas, pRange, pAngles);
        }


        /*
            Draw graduations
        */
        /* Take into account the dial value range factor */
        Range   lRangeWithFactor    = new Range();
        lRangeWithFactor.setValueDisplayRange(
                pRange.getValueDisplayMin(),
                pRange.getValueDisplayMax() );
        lRangeWithFactor.setGraduationRange(
                pRange.getGraduationMin() / this.getValueRangeFactor(),
                pRange.getGraduationMax() / this.getValueRangeFactor() );
        for (Graduation lGraduation : m_graduations) {
            lGraduation.draw(lCanvas, lRangeWithFactor, pAngles);
        }


        /* Draw labels */
        for (Label lLabel : m_labels) {
            lLabel.draw( lCanvas );
        }
    }

    public void draw(Canvas pCanvas, Range pRange, Angles pAngles)
    {
        /* If anything changed, regenerate the bitmap */
        if( m_updateRequested )
        {
            m_updateRequested   = false;
            this.updateBitmap(pCanvas, pRange, pAngles);
        }


        pCanvas.drawBitmap(
                this.bitmap(),
                (pCanvas.getWidth() / 2.0f) - (this.bitmap().getWidth() / 2.0f),
                (pCanvas.getHeight() / 2.0f) - (this.bitmap().getHeight() / 2.0f),
                this.m_backgroundPaint
        );
    }

    public List<Graduation> getGraduationsList()
    {
        return this.m_graduations;
    }

    public ArrayList<Label>  getLabelsList()
    {
        return this.m_labels;
    }

    public List<Section>  getSectionsList()
    {
        return this.m_sections;
    }


    public float    getValueRangeFactor()
    {
        return this.m_valueRangeFactor;
    }


    /**
     * To set a factor on the dial values. Useful in cases where you use large numbers, like engine
     * RPM, and you want to show only a shot value (like "2" on the dial while the value is 2000).
     *
     * @param pFactor The factor to apply on the range.
     *
     * @see #getLabelsList() to add a new label showing the factor (it has to be done manually).
     */
    public void setValueRangeFactor(float pFactor)
    {
        this.m_valueRangeFactor = pFactor;

        this.invalidate();
    }
}
