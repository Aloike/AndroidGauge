package com.github.aloike.libgauge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.github.aloike.libgauge.attributes.Angles;
import com.github.aloike.libgauge.attributes.Range;
import com.github.aloike.libgauge.parts.Dial;
import com.github.aloike.libgauge.base.FixedAspectRatioFrameLayout;
import com.github.aloike.libgauge.parts.Needle;
import com.github.aloike.libgauge.parts.NeedleAxis;
import com.github.aloike.libgauge.parts.ValueDisplay;


public class Gauge extends FixedAspectRatioFrameLayout {

    private Angles          m_angles                = new Angles();
    private PointF          m_center                = new PointF();
    private Range           m_range                 = new Range();
    private float           m_valueCurrent          = 0f;

    protected Dial          m_dial          = new Dial();
    protected Needle        m_needle        = new Needle();
    protected NeedleAxis    m_needleAxis    = new NeedleAxis();
    protected ValueDisplay  m_valueDisplay  = new ValueDisplay();


    public Gauge(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.init();
        this.init_attributes(context, attrs);
    }

    private void init()
    {
        this.setWillNotDraw(false);

        this.setBackgroundColor( Color.TRANSPARENT );

        this.setAspectRatioHeight(1);
        this.setAspectRatioWidth(1);
        this.requestLayout();


        this.setAngles(-135, 135);
    }

    private void init_attributes(Context context, AttributeSet attrs)
    {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Gauge,
                0,
                0);

        try {
            this.m_dial.setBackgroundColor(a.getColor(R.styleable.Gauge_backgroundColor, Color.BLACK));
        }
        finally {
            a.recycle();
        }
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);


        /* Draw the gauge dial */
        this.m_dial.draw(
                canvas,
                this.m_range,
                this.m_angles
        );

        /* Draw the value display */
        this.m_valueDisplay.draw(
                canvas,
                this.m_range,
                this.m_valueCurrent
        );

        /* Draw the needle */
        if(this.isEnabled()) {
            this.m_needle.draw(
                    canvas,
                    this.m_range,
                    this.m_angles,
                    this.m_valueCurrent
            );
        }

        /* Draw the needle axis */
        this.m_needleAxis.draw(
                canvas
        );
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        // Ask for a dial update on next redraw
        this.m_dial.invalidate();

        // Calculate the position of the center
        m_center.x  = (w / 2.0f);
        m_center.y  = (h / 2.0f);

        this.invalidate();
    }



    public Angles getAngles()
    {
        return this.m_angles;
    }

    public void setAngles(int pBegin_deg, int pEnd_deg)
    {
        this.m_angles.set(pBegin_deg, pEnd_deg);

        this.m_dial.invalidate();
        this.invalidate();
    }

    public void setAngleBegin(int pBegin_deg)
    {
        this.m_angles.set(
                pBegin_deg,
                this.m_angles.getStop() );

        this.m_dial.invalidate();
        this.invalidate();
    }

    public void setAngleEnd(int pEnd_deg)
    {
        this.m_angles.set(
                this.m_angles.getStart(),
                pEnd_deg );

        this.m_dial.invalidate();
        this.invalidate();
    }


    public Range    getRange()
    {
        return this.m_range;
    }

    public void setGraduationRange(float pGraduationMin, float pGraduationMax)
    {
        this.m_range.setGraduationRange(pGraduationMin, pGraduationMax);

        this.m_dial.invalidate();
        this.invalidate();
    }

    public void setValueDisplayRange(float pValueMin, float pValueMax)
    {
        this.m_range.setValueDisplayRange(pValueMin, pValueMax);

        this.m_dial.invalidate();
        this.invalidate();
    }

    public Dial getDial()
    {
        this.invalidate(); // Dirty fixup to check whether my background color update problem comes from here...

        return this.m_dial;
    }

    public ValueDisplay getValueDisplay()
    {
        return this.m_valueDisplay;
    }

    public float    getValue()
    {
        return this.m_valueCurrent;
    }

    public void setValue(float pValue)
    {
        // TODO check limits

        this.m_valueCurrent = pValue;

        this.invalidate();
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);

        this.m_valueDisplay.setEnabled(enabled);

        this.invalidate();
    }
}
