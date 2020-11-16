package com.github.aloike.libgauge.parts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.github.aloike.libgauge.attributes.Range;

import static java.lang.Math.max;

public class ValueDisplay {

    String  m_displayFormatStr  = new String("%5.01f");
    boolean m_enabled           = true;
    float   m_positionHeightFactor  = 0.75f;
    float   m_textSpacing           = 0.02f;
    float   m_textRectMarginFactor  = 0.04f;
    String  m_unitText          = new String("[Unit]");
    float   m_unitTextSizeFactor_pc = 7.5f;
    float   m_valueTextSizeFactor_pc    = 10.0f;

    Paint   m_paintContour      = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint   m_paintTextValue    = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint   m_paintTextUnit     = new Paint(Paint.ANTI_ALIAS_FLAG);


    public ValueDisplay()
    {
        init();
    }

    private void init()
    {
        this.m_paintContour.setColor(Color.WHITE);
        this.m_paintContour.setStyle(Paint.Style.STROKE);

        this.m_paintTextValue.setColor(Color.WHITE);
        this.m_paintTextValue.setTextAlign(Paint.Align.CENTER);
        this.m_paintTextValue.setTextSize(10);
        this.m_paintTextValue.setTypeface(Typeface.MONOSPACE);

        this.m_paintTextUnit.setColor(Color.WHITE);
        this.m_paintTextUnit.setTextAlign(Paint.Align.CENTER);
        this.m_paintTextUnit.setTextSize(10);
        this.m_paintTextUnit.setTypeface(Typeface.MONOSPACE);
    }

    public void draw(Canvas pCanvas, Range pRange, float pValue)
    {
        float   lValueTextSize
                = pCanvas.getHeight() * this.m_valueTextSizeFactor_pc / 100.0f;

        float   lUnitTextSize
                = pCanvas.getHeight() * this.m_unitTextSizeFactor_pc / 100.0f;

        String lTextValue   = this.getDisplayedString(pRange, pValue);


        this.m_paintContour.setStrokeWidth(pCanvas.getWidth() * 0.01f);
        this.m_paintTextUnit.setTextSize(lUnitTextSize);
        this.m_paintTextValue.setTextSize(lValueTextSize);

        RectF   lTextContour
                = this.getTextRect(pCanvas, lTextValue);


        this.draw_contour(pCanvas, lTextContour);
        this.draw_text(pCanvas, lTextValue, lTextContour);
    }

    private void    draw_contour(Canvas pCanvas, RectF pTextContour)
    {
        pCanvas.drawRect(
                pTextContour,
                this.m_paintContour
        );
    }

    private void    draw_text(Canvas pCanvas, String pValueText, RectF pTextContour)
    {
        float   lPosX
                = pTextContour.centerX();

        float   lPosYValue
                = pTextContour.top
                + this.m_paintTextValue.getTextSize() - this.m_paintTextValue.descent()
                + this.m_paintContour.getStrokeWidth()
                + pCanvas.getWidth() * this.m_textRectMarginFactor;

        float   lPosYUnit
                = lPosYValue
                + pCanvas.getHeight() * m_textSpacing
                + this.m_paintTextUnit.getTextSize();

        pCanvas.drawText(
                pValueText,
                lPosX,
                lPosYValue,
                this.m_paintTextValue);

        pCanvas.drawText(
                this.m_unitText,
                lPosX,
                lPosYUnit,
                this.m_paintTextUnit );
    }

    private String   getDisplayedString(Range pRange, float pValue)
    {
        String  retval  = new String();

        if( ! this.isEnabled() )
        {
            retval  = String.format(
                    m_displayFormatStr,
                    pRange.getGraduationMax() );

            retval  = retval.replaceAll(
                    "[0-9]",
                    "-" );
        }
        else if(pValue < pRange.getValueDisplayMin())
        {
            retval  = String.format(m_displayFormatStr, pRange.getValueDisplayMin());

            retval  = retval.replaceAll(
                    "[0-9]",
                    "_");
        }
        else if(pValue > pRange.getValueDisplayMax())
        {
            retval  = String.format(m_displayFormatStr, pRange.getValueDisplayMax());

            retval  = retval.replaceAll(
                    "[0-9]",
                    "¯");
        }
        else
        {
            retval = String.format(m_displayFormatStr, pValue);
        }

        return retval;
    }

    private RectF   getTextRect(Canvas pCanvas, String pValueText)
    {
        /* Text width */
        float lTextWidth
//                = this.m_paintTextValue.measureText(pValueText);
                = max(  this.m_paintTextValue.measureText(pValueText),
                        this.m_paintTextUnit.measureText(m_unitText) );

        /* Text height */
        float lTextHeight
                = this.m_paintTextValue.getTextSize()
                + this.m_paintTextUnit.getTextSize();


        float   lWidth
                = lTextWidth
//                + this.m_paintTextValue.measureText("X") * 2
                + pCanvas.getWidth() * this.m_textRectMarginFactor * 2.0f;

        float   lHeight
                = lTextHeight
                + pCanvas.getHeight() * m_textSpacing /* * 3 */ /*< The multiplier is somewhat estimated... */
                + pCanvas.getHeight() * this.m_textRectMarginFactor * 2.0f;

        float   lPosX
                = pCanvas.getWidth() / 2.0f
                - lWidth / 2.0f;

        float   lPosY
                = pCanvas.getHeight() * m_positionHeightFactor
                - lHeight / 2.0f;


        RectF   retval
                = new RectF(
                        lPosX,
                        lPosY,
                        lPosX + lWidth,
                        lPosY + lHeight
                );


        return retval;
    }

    public String   getDisplayFormat()
    {
        return this.m_displayFormatStr;
    }

    public void     setDisplayFormat(String pFormatStr)
    {
        this.m_displayFormatStr = pFormatStr;
    }

    public boolean  isEnabled()
    {
        return this.m_enabled;
    }

    public void     setEnabled(boolean pEnabled)
    {
        this.m_enabled  = pEnabled;
    }

    public String   getUnitText()
    {
        return this.m_unitText;
    }

    public void     setUnitText(String pUnitText)
    {
        this.m_unitText = pUnitText;
    }

    public int      getUnitTextColor()
    {
        return this.m_paintTextUnit.getColor();
    }

    public void     setUnitTextColor(int pColor)
    {
        this.m_paintTextUnit.setColor(pColor);
    }

    public float    getUnitTextSizeFactor_pc()
    {
        return this.m_unitTextSizeFactor_pc;
    }

    public void     setUnitTextSizeFactor_pc(float pFactor_pc)
    {
        if(pFactor_pc < 0f || pFactor_pc > 100f)
        {
            throw new AssertionError("Factor must be in range [0;100]!");
        }

        this.m_unitTextSizeFactor_pc    = pFactor_pc;
    }

    public int      getValueTextColor()
    {
        return this.m_paintTextValue.getColor();
    }

    public void     setValueTextColor(int pColor)
    {
        this.m_paintTextValue.setColor(pColor);
    }

    public float    getValueTextSizeFactor_pc()
    {
        return this.m_valueTextSizeFactor_pc;
    }

    public void     setValueTextSizeFactor_pc(float pFactor_pc)
    {
        if(pFactor_pc < 0f || pFactor_pc > 100f)
        {
            throw new AssertionError("Factor must be in range [0;100]!");
        }

        this.m_valueTextSizeFactor_pc   = pFactor_pc;
    }
}
