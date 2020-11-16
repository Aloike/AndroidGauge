package com.github.aloike.libgauge.attributes;

public class Angles {

    private int m_angleStart    = -135;
    private int m_angleStop     = 135;


    public Angles()
    {

    }

    public int  getRange()
    {
        return (getStop() - getStart());
    }

    public int  getStart()
    {
        return m_angleStart;
    }

    public int  getStop()
    {
        return m_angleStop;
    }

    public void set(int pStart, int pStop)
    {
        if (pStart < -360 || pStart > 360)
        {
            throw new AssertionError("Start angle must be... !");
        }

        else if (pStop < -360 || pStop > 360)
        {
            throw new AssertionError("Stop angle must be within range !");
        }

        if( ! (pStart < pStop) )
        {
            throw new AssertionError("Start angle must be smaller than stop angle!");
        }

        else if( (pStop - pStart) > 360 )
        {
            throw new AssertionError("Arc size must not exceed 360 degrees!");
        }

        // TODO test values


        this.m_angleStart   = pStart;
        this.m_angleStop    = pStop;
    }
}
