package org.aptivate.web;

import junit.framework.AssertionFailedError;
import junit.framework.ComparisonFailure;
import junit.framework.TestCase;

import org.aptivate.web.controls.CheckBox;
import org.aptivate.web.utils.HtmlIterator;

public class HtmlIteratorTest extends TestCase
{

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(HtmlIteratorTest.class);
    }

    public HtmlIteratorTest(String arg0)
    {
        super(arg0);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.HtmlIterator(String)'
     */
    public void testHtmlIterator()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertXHTML()'
     */
    public void testAssertXHTML()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertDTD(String, String, String)'
     */
    public void testAssertDTD()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertStart(String)'
     */
    public void testAssertStartString()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertStart(String, String[][])'
     */
    public void testAssertStartStringStringArrayArray()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertStart(String, Attributes)'
     */
    public void testAssertStartStringAttributes()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertStart(String, Attributes, String)'
     */
    public void testAssertStartStringAttributesString()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertStart(String, Map)'
     */
    public void testAssertStartStringMap()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertEnd(String)'
     */
    public void testAssertEndString()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertEnd(String, String[][])'
     */
    public void testAssertEndStringStringArrayArray()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertEnd(String, Attributes)'
     */
    public void testAssertEndStringAttributes()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertText(String)'
     */
    public void testAssertText()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertRawText(String)'
     */
    public void testAssertRawText()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertEndDocument()'
     */
    public void testAssertEndDocument()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertSimple(String, String)'
     */
    public void testAssertSimpleStringString()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertSimple(String, String, String[][])'
     */
    public void testAssertSimpleStringStringStringArrayArray()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertSimple(String, String, Attributes)'
     */
    public void testAssertSimpleStringStringAttributes()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertSimple(String, String, Attributes, String)'
     */
    public void testAssertSimpleStringStringAttributesString()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertElementRaw(String, String)'
     */
    public void testAssertElementRawStringString()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertElementRaw(String, String, String[][])'
     */
    public void testAssertElementRawStringStringStringArrayArray()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertElementRaw(String, String, Attributes)'
     */
    public void testAssertElementRawStringStringAttributes()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertElementRaw(String, String, Map)'
     */
    public void testAssertElementRawStringStringMap()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertEmpty(String)'
     */
    public void testAssertEmptyString()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertEmpty(String, String[][])'
     */
    public void testAssertEmptyStringStringArrayArray()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertEmpty(String, Attributes)'
     */
    public void testAssertEmptyStringAttributes()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertSubmit(String, String)'
     */
    public void testAssertSubmitStringString()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertSubmit(String, String, Attributes)'
     */
    public void testAssertSubmitStringStringAttributes()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertSelectMulti(String, List, List)'
     */
    public void testAssertSelectMulti()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertSelectTable(String, List, List)'
     */
    public void testAssertSelectTable()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertSelectFromList(String, Map, List, boolean, int)'
     */
    public void testAssertSelectFromListStringMapListBooleanInt()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.getRowValue(Object)'
     */
    public void testGetRowValue()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.getRowLabel(Object)'
     */
    public void testGetRowLabel()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertSelectFromList(String, String, List, boolean, int)'
     */
    public void testAssertSelectFromListStringStringListBooleanInt()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertYesNoUndef(String, Map)'
     */
    public void testAssertYesNoUndefStringMap()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertYesNoUndef(String, String)'
     */
    public void testAssertYesNoUndefStringString()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertYesNo(String, String)'
     */
    public void testAssertYesNo()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertInput(String, String)'
     */
    public void testAssertInputStringString()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertInput(String, Map)'
     */
    public void testAssertInputStringMap()
    {
        // TODO Auto-generated method stub

    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertHtml(String)'
     */
    public void testAssertHtml()
    {
        // TODO Auto-generated method stub

    }
    
    abstract class AssertThrows
    {
        private Class m_Expected;
        public AssertThrows(Class expected)
        {
            m_Expected = expected;
        }
        protected abstract void task() throws Exception;
        public void execute() throws Throwable
        {
            try
            {
                task();
            }
            catch (Throwable t)
            {
            	if (m_Expected == null)
            	{
                    throw new ComparisonFailure(t.toString(),
                            null, t.getClass().toString());
            	}
            	else 
            	{
            		if (!m_Expected.isAssignableFrom(t.getClass()))	            		
	                {
            			/*
	                    throw new ComparisonFailure(t.toString(),
	                        m_Expected.toString(), t.getClass().toString());
	                       */
            			throw t;
	                }
	                assertTrue(m_Expected.isAssignableFrom(t.getClass()));
            	}
            }
        }
    }

    class AssertHtmlThrows extends AssertThrows
    {
        private String html;
        protected final HtmlIterator hi;
        
        AssertHtmlThrows(String html) throws Throwable
        {
            this(html, AssertionFailedError.class);
        }

        AssertHtmlThrows(String html, Class expected) throws Throwable
        {
            super(expected);
            this.html = html;
            this.hi = new HtmlIterator(html);
            execute();
        }
        
        public void task() throws Exception
        {
            hi.assertFormSubmit("foo", "bar");
        }                
    }

    String SUBMIT   = "<input type='submit' name='foo' value='bar' />";
    String TEXT     = "<input type='text'   name='foo' value='bar' />";
    String HIDDEN   = "<input type='hidden' name='foo' value='bar' />";
    String TEXTAREA = "<textarea name='foo'>bar</textarea>";

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertFormSubmit(String, String)'
     */
    public void testAssertFormSubmit() throws Throwable
    {
        new HtmlIterator(SUBMIT).assertFormSubmit("foo", "bar");
        for (String html : new String[]{TEXT, HIDDEN, TEXTAREA})
        {
            new AssertHtmlThrows(html).execute();
        }
    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertFormTextField(String, String)'
     */
    public void testAssertFormTextField() throws Throwable
    {
        new HtmlIterator(TEXT).assertFormTextField("foo", "bar");
        for (String html : new String[]{SUBMIT, HIDDEN, TEXTAREA})
        {
            new AssertHtmlThrows(html).execute();
        }
    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertFormHidden(String, String)'
     */
    public void testAssertFormHidden() throws Throwable
    {
        new HtmlIterator(HIDDEN).assertFormHidden("foo", "bar");
        for (String html : new String[]{SUBMIT, TEXT, TEXTAREA})
        {
            new AssertHtmlThrows(html);
        }
    }

    /*
     * Test method for 'org.aptivate.web.utils.HtmlIterator.assertFormTextArea(String, String)'
     */
    public void testAssertFormTextArea() throws Throwable
    {
        new HtmlIterator(TEXTAREA).assertFormTextArea("foo", "bar");
        for (String html : new String[]{SUBMIT, TEXT, HIDDEN})
        {
            new AssertHtmlThrows(html);
        }
    }

    public void testAssertCheckBox() throws Throwable
    {
    	String checked = "<input type=\"checkbox\" name=\"foo\" " +
			"value=\"bar\" checked=\"checked\" />";
    	String unchecked = "<input type=\"checkbox\" name=\"foo\" " +
    		"value=\"bar\" />";

    	new AssertHtmlThrows(checked)
    	{
    		public void task()
    		{ new CheckBox("foo", "bar", false).assertField(hi, null); }
    	};

    	new AssertHtmlThrows(checked, null)
    	{
    		public void task()
    		{ new CheckBox("foo", "bar", true).assertField(hi, null); }
    	};

    	new AssertHtmlThrows(unchecked, null)
		{
    		public void task()
    		{ new CheckBox("foo", "bar", false).assertField(hi, null); }
		};

    	new AssertHtmlThrows(unchecked)
		{
    		public void task()
    		{ new CheckBox("foo", "bar", true).assertField(hi, null); }
		};
    }
}
