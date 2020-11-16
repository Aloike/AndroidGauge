package com.github.aloike.libgauge.parts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class NeedleAxis {

    private float   m_diameterFactor    = 0.1f;
    private Paint   m_paint             = new Paint(Paint.ANTI_ALIAS_FLAG);


    public NeedleAxis()
    {
        init();
    }

    private void init()
    {
        this.m_paint.setColor(Color.DKGRAY);
    }

    public void draw(Canvas pCanvas)
    {
        float lCanvasCenterX  = pCanvas.getWidth() / 2f;
        float lCanvasCenterY  = pCanvas.getHeight() / 2f;

        float lDiameter   = pCanvas.getWidth() * this.m_diameterFactor;

        pCanvas.drawCircle(
                lCanvasCenterX,
                lCanvasCenterY,
                lDiameter / 2.0f,
                this.m_paint );
    }
}
