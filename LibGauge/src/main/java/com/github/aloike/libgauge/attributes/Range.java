package com.github.aloike.libgauge.attributes;

public class Range {

    private float m_graduationMin = 0f;
    private float m_graduationMax = 100f;

    private float m_valueDisplayMin = -Float.MAX_VALUE;
    private float m_valueDisplayMax = Float.MAX_VALUE;


    public Range()
    {

    }


    public float getGraduationMin()
    {
        return this.m_graduationMin;
    }

    public float getGraduationMax()
    {
        return this.m_graduationMax;
    }

    public float getGraduationRange()
    {
        return (this.m_graduationMax - this.m_graduationMin);
    }

    public void setGraduationRange(float pMin, float pMax)
    {
        if( ! (pMin < pMax) )
        {
            throw new AssertionError("Min value must be smaller than max value!");
        }

        this.m_graduationMin = pMin;
        this.m_graduationMax = pMax;
    }


    public float getValueDisplayMin()
    {
        return this.m_valueDisplayMin;
    }

    public float getValueDisplayMax()
    {
        return this.m_valueDisplayMax;
    }

    public float getValueDisplayRange()
    {
        return (this.m_valueDisplayMax - this.m_valueDisplayMin);
    }

    public void setValueDisplayRange(float pMin, float pMax)
    {
        if( ! (pMin < pMax) )
        {
            throw new AssertionError("Min value must be smaller than max value!");
        }

        this.m_valueDisplayMin = pMin;
        this.m_valueDisplayMax = pMax;
    }
}
