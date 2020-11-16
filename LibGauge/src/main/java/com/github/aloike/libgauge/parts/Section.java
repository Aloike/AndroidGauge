package com.github.aloike.libgauge.parts;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.aloike.libgauge.attributes.Angles;
import com.github.aloike.libgauge.attributes.Range;

import java.util.zip.DataFormatException;

public class Section {

    //private int     m_color;
    private Paint   m_paint     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float   m_valueBegin;
    private float   m_valueEnd;
    private float   m_widthFactor_pc    = 5.0f;


    public Section(float pValueBegin, float pValueEnd, int pColor) throws DataFormatException
    {
        if( pValueBegin > pValueEnd )
        {
            throw new DataFormatException("Beginning value can't be after end value!");
        }

        this.m_valueBegin   = pValueBegin;
        this.m_valueEnd     = pValueEnd;

        this.m_paint.setColor(pColor);
        this.m_paint.setStrokeCap(Paint.Cap.BUTT);
        this.m_paint.setStyle(Paint.Style.STROKE);
    }


    public void draw(Canvas pCanvas, Range pRange, Angles pAngles)
    {
        /* First, check if we can draw this section... */
        // TODO check if we can draw this section... or maybe not? as it will use the max range...


        /*
         *  Process all calculations
         */
        /* Check if the begin value is within the gauge range */
        float   lBeginValue = 0f;
        if( this.m_valueBegin > pRange.getGraduationMax() )
        {
            /* The section starts at a higher value than the gauge max value - so don't draw it. */
            return;
        }
        else if(this.m_valueBegin < pRange.getGraduationMin())
        {
            lBeginValue = pRange.getGraduationMin();
        }
        else
        {
            lBeginValue = this.m_valueBegin;
        }

        /* Check if the end value is within the gauge range */
        float   lEndValue   = 0f;
        if( this.m_valueEnd < pRange.getGraduationMin() )
        {
            /* The section ends at a smaller value than the gauge min value - so don't draw it. */
            return;
        }
        else if(pRange.getGraduationMax() < this.m_valueEnd )
        {
            lEndValue   = pRange.getGraduationMax();
        }
        else
        {
            lEndValue   = this.m_valueEnd;
        }

        /* Translate the begin/end values as angles */
        float lBeginAngle
                = ((lBeginValue - pRange.getGraduationMin()) / (pRange.getGraduationRange()))
                * pAngles.getRange();
        lBeginAngle += pAngles.getStart() - 90f;

        float lEndAngle
                = ((lEndValue - pRange.getGraduationMin()) / (pRange.getGraduationRange()))
                * pAngles.getRange();
        lEndAngle += pAngles.getStart() - 90f;

        float lSweepAngle   = lEndAngle - lBeginAngle;


        /* Calculate the width of the Section path as a ratio from canvas size, with a minimum
         * size of 1. */
        float lStrokeWidth  = Math.max(1.0f, pCanvas.getHeight() * this.m_widthFactor_pc / 100.0f );
        this.m_paint.setStrokeWidth(lStrokeWidth);



        /* Draw the arc representing the section */
        float lOvalWidth    = pCanvas.getWidth() - (lStrokeWidth / 2f);
        float lOvalHeight   = pCanvas.getHeight() - (lStrokeWidth / 2f);
        pCanvas.drawArc(
                0 + (lStrokeWidth / 2f),
                0 + (lStrokeWidth / 2f),
                lOvalWidth,
                lOvalHeight,
                lBeginAngle,
                lSweepAngle,
                false,
                this.m_paint );
    }

    public float    getWidthFactor_pc()
    {
        return this.m_widthFactor_pc;
    }

    public void     setWidthFactor_pc(float pFactor_pc)
    {
        if(pFactor_pc < 0f || pFactor_pc > 100f)
        {
            throw new AssertionError("Factor must be in range [0;100]!");
        }

        this.m_widthFactor_pc   = pFactor_pc;
    }
}
