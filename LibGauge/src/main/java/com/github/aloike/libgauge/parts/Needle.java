package com.github.aloike.libgauge.parts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.github.aloike.libgauge.attributes.Angles;
import com.github.aloike.libgauge.attributes.Range;

public class Needle {

    private Paint   m_paintContour  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint   m_paintFill     = new Paint(Paint.ANTI_ALIAS_FLAG);


    public Needle()
    {
        init();
    }

    private void init()
    {
        this.m_paintContour.setColor(Color.BLACK);
        this.m_paintContour.setStrokeWidth(10);
        this.m_paintContour.setStyle(Paint.Style.STROKE);

        this.m_paintFill.setStrokeCap(Paint.Cap.BUTT);
        this.m_paintFill.setColor(Color.WHITE);
        this.m_paintFill.setStrokeWidth(5);
        this.m_paintFill.setStyle(Paint.Style.FILL);

    }

    public void draw(Canvas pCanvas, Range pRange, Angles pAngles, float pValue)
    {
        float lAngle    = convertValueToAngle(pValue, pRange, pAngles);

        float lCanvasCenterX  = pCanvas.getWidth() / 2f;
        float lCanvasCenterY  = pCanvas.getHeight() / 2f;

        float lCanvasSideLength
                = Math.min(
                        pCanvas.getWidth(),
                        pCanvas.getHeight() );

        /* Create the needle path */
        PointF lPointA   = new PointF(
                lCanvasCenterX - lCanvasSideLength * 0.05f,
                lCanvasCenterY
        );
        PointF lPointB   = new PointF(
                lCanvasCenterX,
                0
        );
        PointF lPointC   = new PointF(
                lCanvasCenterX + lCanvasSideLength * 0.05f,
                lCanvasCenterY
        );

        Path lPath  = new Path();
        lPath.setFillType(Path.FillType.EVEN_ODD);
        lPath.moveTo(lPointA.x, lPointA.y);
        lPath.lineTo(lPointB.x, lPointB.y);
        lPath.lineTo(lPointC.x, lPointC.y);
        lPath.lineTo(lPointA.x, lPointA.y);
        lPath.close();

        this.m_paintContour.setStrokeWidth(lCanvasSideLength * 0.005f);


        /* Save canvas' original position */
        pCanvas.save();

        /* Rotate the canvas to the needle position */
        pCanvas.rotate(
                lAngle,
                lCanvasCenterX,
                lCanvasCenterY
        );

        pCanvas.drawPath(
                lPath,
                this.m_paintFill
        );
        pCanvas.drawPath(
                lPath,
                this.m_paintContour
        );


        /* Restore the canvas to its original position */
        pCanvas.restore();
    }

    private float convertValueToAngle(float pValue, Range pRange, Angles pAngles)
    {
        float retval;


        if( pValue <= pRange.getGraduationMin() )
        {
            retval  = pAngles.getStart();
        }
        else if( pValue >= pRange.getGraduationMax() )
        {
            retval  = pAngles.getStop();
        }
        else
        {
            /* Calculate a percentage of the range */
            retval  = pValue - pRange.getGraduationMin();
            retval  /= pRange.getGraduationRange();

            /* Get the corresponding angle */
            retval  *= pAngles.getRange();
            retval  += pAngles.getStart();
        }

        return retval;
    }
}
