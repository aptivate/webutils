package org.aptivate.web.utils;

import org.apache.log4j.Logger;

public class Timer
{
    private static final Logger s_LOG = Logger.getLogger(Timer.class);
    private long m_StartTime = 0;
    private String m_Name;
    
    public Timer(String name)
    {
        m_StartTime = System.currentTimeMillis();
        m_Name = name;
        s_LOG.info("Starting " + m_Name);
    }
    
    public void stop()
    {
        long elapsed = System.currentTimeMillis() - m_StartTime;
        s_LOG.info("Stopping " + m_Name + " after " + elapsed + "ms");
    }

    public void stop(String reason)
    {
        long elapsed = System.currentTimeMillis() - m_StartTime;
        s_LOG.info("Stopping " + m_Name + " for " + reason + " after " + 
            elapsed + "ms");
    }
}
