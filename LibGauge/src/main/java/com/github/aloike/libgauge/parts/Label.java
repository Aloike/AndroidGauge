package com.github.aloike.libgauge.parts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class Label {

    Paint   m_paint             = new Paint(Paint.ANTI_ALIAS_FLAG);
    float   m_posX_percent      = 50f;
    float   m_posY_percent      = 50f;
    String  m_text              = new String();
    float   m_textSizeFactor_pc = 10f;


    /**
     * Default constructor.
     */
    public Label()
    {
        init();
    }

    /**
     * Method to process initialization.
     */
    private void init()
    {
        this.m_paint.setColor(Color.WHITE);
        this.m_paint.setTypeface(Typeface.MONOSPACE);
    }

    /**
     * Draws this label on #pCanvas.
     *
     * @param pCanvas The canvas on which the label will be drawn.
     */
    public void draw(Canvas pCanvas)
    {
        /* Set text size */
        float lTextSize
                = m_textSizeFactor_pc / 100f
                * Math.min(
                        pCanvas.getWidth(),
                        pCanvas.getHeight() );
        this.m_paint.setTextSize( lTextSize );

        /* Retrieve the text area's size */
        Rect lTextBounds   = new Rect();
        this.m_paint.getTextBounds(
                this.getText(),
                0,
                this.getText().length(),
                lTextBounds );

        /* Calculate the text center position and compensate for text size */
        float lPosX
                = pCanvas.getWidth() * this.m_posX_percent / 100.0f
                - (lTextBounds.width() / 2f);
        float lPosY
                = pCanvas.getHeight() * this.m_posY_percent / 100.0f
                - (this.m_paint.descent() + this.m_paint.ascent()) / 2.0f;
//                + (lTextSize / 2f);
//                + (lTextBounds.height() / 2f);

        pCanvas.drawText(
                this.getText(),
                lPosX,
                lPosY,
                this.m_paint
        );
    }

    /**
     * Returns the color to be used when drawing the label.
     * @return The color to be used when drawing the label.
     */
    public int
            getColor()
    {
        return this.m_paint.getColor();
    }

    /**
     * Sets the color to be used when drawing the label.
     * @param pColor  The color.
     */
    public void
            setColor(int pColor)
    {
        this.m_paint.setColor(pColor);
    }

    /**
     * Set the horizontal position of the label's center on the canvas, as a percentage of the
     * canvas width.
     *
     * @param   pValue_pc   The horizontal position of the label as a percentage of the destination
     *                      canvas width.
     */
    public void
            setPosX_pc(float pValue_pc)
    {
        this.m_posX_percent = pValue_pc;
    }

    /**
     * Set the vertical position of the label's center on the canvas, as a percentage of the
     * canvas height.
     *
     * @param   pValue_pc   The vertical position of the label as a percentage of the destination
     *                      canvas height.
     */
    public void
            setPosY_pc(float pValue_pc)
    {
        this.m_posY_percent = pValue_pc;
    }

    /**
     * Returns a string containing the text to be displayed by the label.
     *
     * @return  A string containing the text to be displayed by the label.
     */
    public String
            getText()
    {
        return this.m_text;
    }

    /**
     * Sets the text to be displayed by the label.
     *
     * @param pText The text to be displayed by the label.
     */
    public void
            setText(String pText)
    {
        this.m_text = pText;
    }

    /**
     * Set the label's text size, as a percentage of the destination canvas width.
     *
     * @param   pValue_pc   The size factor of the label's text.
     */
    public void
            setTextSizeFactor(float pValue_pc)
    {
        this.m_textSizeFactor_pc    = pValue_pc;
    }

    /**
     * Accessor to get label's typeface.
     *
     * @return The typeface.
     *
     * @see #setTypeface(Typeface)
     */
    public Typeface
            getTypeface()
    {
        return this.m_paint.getTypeface();
    }

    /**
     * Sets the label's typeface.
     *
     * @param pTypeface The typeface to use when drawing the label.
     *
     * @see #getTypeface()
     *
     */
    public void
            setTypeface(Typeface pTypeface)
    {
        this.m_paint.setTypeface(pTypeface);
    }
}
