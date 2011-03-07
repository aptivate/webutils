/**
 * 
 */
package org.aptivate.web.controls;

public abstract class StringValuedControl extends FormControl
{
	private String m_ParamValue, m_DefaultValue;
    protected StringValuedControl(String name, String paramValue)
    {
        super(name);
        m_ParamValue = paramValue;
    }
    public final String getValue()
    {
        if (m_ParamValue != null)
        {
            return m_ParamValue;
        }
        else
        {
            return m_DefaultValue;
        }
    }
    public final FormControl setDefaultValue(String value)
    {
        m_DefaultValue = value;
        return this;
    }
}