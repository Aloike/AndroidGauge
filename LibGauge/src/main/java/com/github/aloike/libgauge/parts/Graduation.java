package com.github.aloike.libgauge.parts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.github.aloike.libgauge.attributes.Angles;
import com.github.aloike.libgauge.attributes.Range;

public class Graduation {

    private int             m_count     = 11;
    private float           m_linesLengthFactor_pc  = 7.5f;
    private float           m_linesWidthFactor_pc   = 2.0f;
    private Paint           m_paintGrad = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint           m_paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path            m_path      = new Path();
    private float           m_textSizeFactor_pc = 7.5f;
    private String          m_valuesTextFormat  = "%.0f";


    public Graduation()
    {
        init();
    }

    public void draw(Canvas pCanvas, Range pRange, Angles pAngles)
    {

        int lCanvasCenterX  = pCanvas.getWidth() / 2;
        int lCanvasCenterY  = pCanvas.getHeight() / 2;

        /* Calculate the width of a graduation mark as a ratio from canvas size, with a minimum
         * size of 1. */
        float lGraduationWidth  = pCanvas.getHeight() * m_linesWidthFactor_pc / 100.0f;
        this.m_paintGrad.setStrokeWidth(Math.max(1, lGraduationWidth));


        /* Generate the path pattern we will use to draw all graduations */
        float lGraduationLength = pCanvas.getHeight() * m_linesLengthFactor_pc / 100.0f;

        this.m_path.reset();

        this.m_path.moveTo(
                lCanvasCenterX,
                0.0f );

        this.m_path.lineTo(
                lCanvasCenterX,
                lGraduationLength );


        m_paintText.setTextSize(pCanvas.getHeight() * m_textSizeFactor_pc / 100.0f);

        Rect lTextBounds    = new Rect();
        m_paintText.getTextBounds("X",0, 1, lTextBounds);
        float lTextPosY
                = lGraduationLength
                + lTextBounds.height() * 1.5f;
                /*= pCanvas.getHeight()
                - lGraduationLength
                - lTextBounds.height();*/


        pCanvas.save();


        /* Rotate the canvas to the first position */
        pCanvas.rotate(
                pAngles.getStart(),
                lCanvasCenterX,
                lCanvasCenterY );

        /* Calculate the spacing between each graduation, in degrees */
        float   lGraduationsSpacing_deg
                = ((float)pAngles.getRange()) / (((float)this.m_count) - 1f);

        float lCurrentAngle_deg = pAngles.getStart();
        do
        {
            /* Draw the mark */
            pCanvas.drawPath(m_path, m_paintGrad);

            /* Draw the text */
            float   lCorrespondingValue
                    = this.convertAngleToValue(
                            lCurrentAngle_deg,
                            pAngles,
                    pRange
                    );
            String lText
                    = String.format(
                            this.m_valuesTextFormat,
                            lCorrespondingValue );

            m_paintText.getTextBounds(lText,0, lText.length(), lTextBounds);
            float lTextPosX = lCanvasCenterX - lTextBounds.width()/2f;

            pCanvas.drawText(
                    lText,
                    lTextPosX,
                    lTextPosY,
                    m_paintText
            );


            /* Rotate the canvas to the next position */
            pCanvas.rotate(
                    lGraduationsSpacing_deg,
                    lCanvasCenterX,
                    lCanvasCenterY );

            lCurrentAngle_deg   += lGraduationsSpacing_deg;
        } while (lCurrentAngle_deg <= pAngles.getStop() );

        pCanvas.restore();
    }

    private void    init()
    {
        this.m_paintGrad.setColor(Color.LTGRAY);
        this.m_paintGrad.setStyle(Paint.Style.STROKE);
        this.m_paintGrad.setStrokeWidth(this.m_linesWidthFactor_pc);

        this.m_paintText.setColor(Color.LTGRAY);
        this.m_paintText.setTextSize(10);
        this.m_paintText.setTypeface(Typeface.MONOSPACE);
    }

    private float convertAngleToValue(float pAngle_deg, Angles pAngles, Range pRange)
    {
        float   retval  = (pAngle_deg - pAngles.getStart()) / pAngles.getRange();

        retval  *= pRange.getGraduationRange();
        retval  += pRange.getGraduationMin();

        return retval;
    }


    public int  getCount()
    {
        return m_count;
    }

    public void setCount(int pCount)
    {
        if (pCount < 0)
        {
            throw new AssertionError("Graduations count must be a positive number!");
        }

        this.m_count    = pCount;
    }

    public int  getGraduationColor()
    {
        return this.m_paintGrad.getColor();
    }

    public void setGraduationColor(int pColor)
    {
        this.m_paintGrad.setColor(pColor);
    }

    public float    getLinesLengthFactor_pc()
    {
        return this.m_linesLengthFactor_pc;
    }

    public void     setLinesLengthFactor(float pFactor_pc)
    {
        if(pFactor_pc < 0f || pFactor_pc > 100f)
        {
            throw new AssertionError("Factor must be in range [0;100]!");
        }

        this.m_linesLengthFactor_pc = pFactor_pc;
    }

    public float    getLinesWidthFactor_pc()
    {
        return this.m_linesWidthFactor_pc;
    }

    public void     setLinesWidthFactor_pc(float pFactor_pc)
    {
        if(pFactor_pc < 0f || pFactor_pc > 100f)
        {
            throw new AssertionError("Factor must be in range [0;100]!");
        }

        this.m_linesWidthFactor_pc  = pFactor_pc;
    }

    public int  getTextColor()
    {
        return this.m_paintText.getColor();
    }

    public void setTextColor(int pColor)
    {
        this.m_paintText.setColor(pColor);
    }

    public float    getTextSizeFactor_pc()
    {
        return this.m_textSizeFactor_pc;
    }

    public void     setTextSizeFactor_pc(float pFactor_pc)
    {
        if(pFactor_pc < 0f || pFactor_pc > 100f)
        {
            throw new AssertionError("Factor must be in range [0;100]!");
        }

        this.m_textSizeFactor_pc    = pFactor_pc;
    }

    public String   getValuesTextFormat()
    {
        return this.m_valuesTextFormat;
    }

    public void     setValuesTextFormat(String pFormat)
    {
        this.m_valuesTextFormat = pFormat;
    }
}
