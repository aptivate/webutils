/**
 * 
 */
package org.aptivate.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.ComparisonFailure;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.aptivate.web.controls.SelectBox;
import org.aptivate.web.utils.HtmlIterator.StartElementNode.LowMemoryMap;
import org.htmlparser.util.Translate;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;

public class HtmlIterator extends TestCase
{
	private List m_Tags = new ArrayList();
	static final Logger s_LOG = Logger.getLogger(HtmlIterator.class);
	
	interface Node
	{
	    int getLine();
	    int getColumn();
	    String toShortString();
	    boolean skip();
        void lazyAssertTrue(boolean expression);
        void lazyAssertEquals(String expected, String actual);
        void lazyAssertEquals(String message, String expected, String actual);
	}
	
	static abstract class NodeBase implements Node
	{
	    private int m_Line, m_Column;
	    private String m_Document;
	    
	    protected NodeBase(String document, Locator locator)
	    {
	        m_Line   = locator.getLineNumber();
	        m_Column = locator.getColumnNumber();
	        m_Document = document;
	    }
	    
	    public int getLine()   { return m_Line; }
	    public int getColumn() { return m_Column; }
	    
        public void lazyAssertTrue(boolean expression)
        {
            if (!expression)
            {
                TestCase.assertTrue("At " + toStringInternal(), expression);
            }
        }

	    public void lazyAssertEquals(String expected, String actual)
	    {
	        if ((expected == null && actual != null) ||
		        (expected != null && !expected.equals(actual)))
	        {
                throw new ComparisonFailure("At " + toStringInternal(),
	                expected, actual);
	        }
	    }

	    public void lazyAssertEquals(String message, String expected,
	        String actual)
	    {
	        if ((expected == null && actual != null) ||
	            (expected != null && !expected.equals(actual)))
	        {
	            TestCase.assertEquals(message + " at " + toStringInternal(),
	                expected, actual == null ? "null" : actual);
	        }
	    }

	    public String toString()
	    {
	        return toShortString() + " at " + toStringInternal();
	    }

	    public String toStringInternal()
	    {
	        BufferedReader br = new BufferedReader(new StringReader(m_Document));

	        String line = null;

	        for (int i = 1; i <= m_Line; i++)
	        {
	            try
	            {
	                line = br.readLine();
	            }
	            catch (IOException e)
	            {
	                throw new RuntimeException(e);
	            }

	            if (line == null) break;
	        }

	        return "line " + m_Line + " (" + line + ")";
	    }

	    public boolean skip() { return false; }
	}
	
	public static class StartElementNode extends NodeBase
	{
        public static class LowMemoryMap
        {
            private String [] m_Store;
            public LowMemoryMap(org.xml.sax.Attributes source)
            {
                m_Store = new String [source.getLength() * 2];
                for (int i = 0; i < source.getLength(); i++)
                {
                    String attName = source.getQName(i);
                    HtmlIterator.assertTrue(attName.length() > 0);
                    m_Store[(i<<1)] = attName;
                    m_Store[(i<<1)+1] = source.getValue(i);
                }
            }
            public String get(String key)
            {
                for (int i = 0; i < m_Store.length; i += 2)
                {
                    if (m_Store[i].equals(key))
                    {
                        return m_Store[i+1];
                    }

                }
                return null;
            }
            public String [] getAll()
            {
            	String [] out = new String[m_Store.length];
            	System.arraycopy(m_Store, 0, out, 0, m_Store.length);
            	return out;
            }
        }
        
	    private String m_Name;
	    private LowMemoryMap m_Attribs;
	    public StartElementNode(String name, LowMemoryMap attribs,
            String document, Locator locator)
	    {
	        super(document, locator);
	        m_Name = name;
	        m_Attribs = attribs;
	    }
	    public String name() { return m_Name; }
	    public String toShortString()
	    {
	        return "<" + m_Name + ">";
	    }
	    public String toShortString(String name, String value)
	    {
	        return "<" + m_Name + " " + name + "=\"" + value + "\">";
	    }
	    public String toString()
	    {
	    	StringBuffer out = new StringBuffer();
	    	out.append("<" + m_Name);
	    	String [] attribs = m_Attribs.getAll();
	    	
	    	for (int i = 0; i < attribs.length; i += 2)
	    	{
	    		out.append(" ");
	    		out.append(attribs[i]);
	    		out.append("='");
	    		out.append(attribs[i+1]);
	    		out.append("'");
	    	}
	    	out.append("> ");
	    	out.append(super.toString());
	    	return out.toString();
	    }
	    private Map arrayToMap(String [][] array)
	    {
	        Map map = new HashMap();
	        for (int i = 0; i < array.length; i++)
	        {
	            String [] name_value = array[i];
	            map.put(name_value[0], name_value[1]);
	        }
	        return map;
	    }
	    public void assertAttributes(String [][] expected)
	    {
	        assertAttributes(arrayToMap(expected));
	    }
	    public void assertAttributes(Map expected)
	    {
	        for (Iterator i = expected.keySet().iterator(); i.hasNext(); )
	        {
	            String name = (String)i.next();
	            String value = (String)expected.get(name);
	            assertAttribute(name, value);
	        }
	    }
	    public void assertAttributes(Attributes expected)
	    {
	        for (Iterator i = expected.getMap().keySet().iterator();
	        	i.hasNext();)
	        {
	            String name  = (String)i.next();
	            String value = (String)expected.getMap().get(name);
	            assertAttribute(name, value);
	        }
	    }
	    
	    public String getAttribute(String name)
	    {
	    	return m_Attribs.get(name);
	    }
	    
        public StartElementNode assertAttribute(String name, int expected)
        {
            return assertAttribute(name, "" + expected);
        }

        public StartElementNode assertAttribute(String name, String expected)
        {
            String actual = null;
            
            if (m_Attribs != null)
            {
                actual = m_Attribs.get(name);
            }
            
            if (expected == null)
            {
                lazyAssertEquals(toShortString() + " " + name + 
                    " was not null but " + actual, expected, actual);
            }
            else
            {
                lazyAssertEquals("expected " + toShortString(name,expected), 
                    expected, actual);
            }
            
            return this;
        }

        public void assertAttribute(String name, String expected,
        		String message)
        {
            String actual = null;
            
            if (m_Attribs != null)
            {
                actual = m_Attribs.get(name);
            }
            
            if (expected == null)
            {
                lazyAssertEquals(message + ": " + toShortString() + " " +
                		name + " was not null but " + actual, 
                		expected, actual);
            }
            else
            {
                lazyAssertEquals(toShortString() + " " + name, 
                    expected, actual);
            }
        }
	}

	class EndElementNode extends NodeBase
	{
	    private StartElementNode m_Start;
	    public EndElementNode(StartElementNode start, String document,
            Locator locator)
        {
            super(document, locator);
            m_Start = start;
	    }
	    public StartElementNode getMatchingStartTag() { return m_Start; }
	    public String name() { return m_Start.name(); }
        public String toShortString()
        {
            return "</" + m_Start.name() + ">";
        }
	}

	interface Appendable
	{
	    String rawText();
	    void append(Appendable next);
	}
	
    static class TextNode extends NodeBase implements Appendable
    {
        private String m_Text;
        public TextNode(String text, String document,
            Locator locator)
        {
            super(document, locator);
            m_Text = text;
        }
        /**
         * Raw text is (supposed to be) exactly what you would see in
         * View Source. In practice we have to reconstruct it, so it might
         * be different, but that would be a bug.
         */
        public String rawText() { return m_Text; }
        /**
         * Text has entities replaced and white space trimmed for ease of
         * processing, in other words it's what would appear on the page
         * in a browser, unlike the raw text with entities that you see in
         * View Source.
         * @return
         */
        public String text()
        {
            return Translate.decode(m_Text.replaceAll("\\s+", " ").trim());
        }
        public String toShortString()
        {
            return "Text: " + text();
        }
        public void append(Appendable next)
        {
            m_Text += next.rawText();
        }
        private static Pattern m_AllSpace = Pattern.compile("\\s+");
        public boolean skip()
        {
            return m_AllSpace.matcher(m_Text).matches();
        }
    }
    
    class EndDocumentNode extends NodeBase
    {
        public EndDocumentNode(String document,
            Locator locator)
        {
            super(document, locator);
        }
        public String toShortString()
        {
            return "End of document";
        }
    }
    
    class DTDNode extends NodeBase
    {
    	private String m_Name;
    	private String m_PublicId;
    	private String m_URL;
    	public DTDNode(String name, String publicId, String url,
    			String document, Locator locator)
    	{
    		super(document, locator);
    		m_Name = name;
    		m_PublicId = publicId;
    		m_URL = url;
    	}

    	public String toShortString()
        {
            return "<!DOCTYPE " + m_Name + " PUBLIC \"" + m_PublicId + 
            	"\" \"" + m_URL + "\">";
        }
    }

	class ParseHandler extends DefaultHandler2
	{
	    private List m_Tags;
	    private Stack m_Stack = new Stack();
	    private Locator m_Locator = null;
	    private String m_Document;
	    private String m_EntityTextToIgnore = null;
        
	    public ParseHandler(List tags, String document)
	    {
	        m_Tags = tags;
	        m_Document = document;
	    }

	    public void setDocumentLocator(Locator locator)
	    {
	        m_Locator = locator;
	    }
	    
	    private Node lastNode;
	    
	    private void append(Node node)
	    {
	        if (node instanceof Appendable && lastNode instanceof Appendable)
	        {
	            Appendable oldNode = (Appendable)lastNode;
	            Appendable newNode = (Appendable)node;
	            oldNode.append(newNode);
	        }
	        else
	        {
	            m_Tags.add(node);
	            lastNode = node;
	        }
	    }

	    public void characters(char[] ch, int start, int length)
	    {
	        String text = new String(ch, start, length);
            
            if (m_EntityTextToIgnore != null &&
                !m_EntityTextToIgnore.equals(text))
            {
                s_LOG.warn("Expected decoded entity " + m_EntityTextToIgnore +
                    " but received something else, assuming it was replaced " +
                    "by: " + text);
                m_EntityWasReplaced = true;
            }
            
            if (m_EntityTextToIgnore != null && !m_EntityWasReplaced)
            {
                assertEquals("Entity was unexpectedly replaced",
                    m_EntityTextToIgnore, text);
                s_LOG.info("Skipping duplicate text for entity: " +
                    m_EntityTextToIgnore);
            }
            else
            {
                append(new TextNode(text, m_Document, m_Locator));
            }
	    }
	    public void endDocument()
	    {
	        append(new EndDocumentNode(m_Document, m_Locator));
	    }
	    public void startElement(String uri, String localName, String qName,
	        org.xml.sax.Attributes attributes)
	    {
            LowMemoryMap attribMap = null;   

            if (attributes.getLength() > 0)
            {
    	        attribMap = new LowMemoryMap(attributes);
            }
	        
	        HtmlIterator.assertTrue(qName.length() > 0);
	        StartElementNode start = new StartElementNode(qName, attribMap, 
	            m_Document, m_Locator);
	        append(start);
	        m_Stack.push(start);
	    }
	    public void endElement(String uri, String localName, String qName)
	    {
            HtmlIterator.assertTrue(qName.length() > 0);
            StartElementNode start = (StartElementNode)m_Stack.pop();
            assertEquals(start.name(), qName);
            append(new EndElementNode(start, m_Document, m_Locator));
	    }
	    public void skippedEntity(String name)
	    {
	        s_LOG.info("Skipped entity: " + name);
	        append(new TextNode("&" + name + ";", m_Document, m_Locator));
	    }
	    public void unparsedEntityDecl(String name,
                String publicId,
                String systemId,
                String notationName)
	    {
	        s_LOG.info("Unparsed entity: " + name);
	    }
	    public void warning(SAXParseException e)
	    {
	        s_LOG.warn("Parser warning", e);
	    }
        public void error(SAXParseException e)
        {
            s_LOG.error("Parser error", e);
        }
        @Override public InputSource resolveEntity(String publicId,
            String systemId)
        {
            s_LOG.info("Resolve entity: " + publicId + " / " + systemId);
            return null;
        }
        @Override public InputSource resolveEntity(String name,
            String publicId, String baseURI, String systemId)
        {
            s_LOG.info("Resolve entity (2): " + publicId + " / " + systemId);
            return null;
        }
        public void startDTD(String name, String publicId,
        		String systemId)
        {
        	s_LOG.info("Start DTD: " + name + " " + publicId + " " + systemId);
        	append(new DTDNode(name, publicId, systemId, m_Document,
        			m_Locator));
        }
        boolean m_EntityWasReplaced = false;
        public void startEntity(String name)
        {
        	s_LOG.info("Start entity: " + name);
            
            assertNull(m_EntityTextToIgnore);
            String entity = "&" + name + ";";
            m_EntityTextToIgnore = Translate.decode(entity);
            
            // we don't know if the entity will be replaced by something else
            // yet, so we can't add it to the stack
            m_EntityWasReplaced = false;
        }
        public void endEntity(String name)
        {
            s_LOG.info("End entity: " + name);
            String entity = "&" + name + ";";            
            assertEquals(m_EntityTextToIgnore, Translate.decode(entity));
            m_EntityTextToIgnore = null;

            if (m_EntityWasReplaced)
            {
                // replacement text already appended
            }
            else
            {
                append(new TextNode(entity, m_Document, m_Locator));
            }
        }
        public void internalEntityDecl(String name, String value)
        {
        	s_LOG.info("Internal entity declaration: " + name + " = " + value);
        }
        public void externalEntityDecl(String name, String publicId,
        		String systemId)
        {
        	s_LOG.info("External entity declaration: " + name + " " + publicId +
        			" " + systemId);
        }
        
	}

	class AssertingHandler extends DefaultHandler
	{
		private boolean gotHtml;
		private boolean gotBody;
        public Map getMap(org.xml.sax.Attributes source)
        {
        	Map results = new HashMap();
            for (int i = 0; i < source.getLength(); i++)
            {
                results.put(source.getQName(i), source.getValue(i));
            }
            return results;
        }
        
        private StringBuffer buffer = new StringBuffer();
        private void append(String string)
        {
        	buffer.append(string);
        }
        private void assertBufferedText()
        {
	        String text = buffer.toString()
	        	.replaceAll("^\\s+", "")
	        	.replaceAll("\\s+$", "")
	            .replaceAll("\\s+", " ");
	        if (text.length() > 0)
	        {
	        	assertText(text);
	        }
	        buffer.setLength(0);
        }
        
	    public void characters(char[] ch, int start, int length)
	    {
	    	assertTrue(gotBody && gotHtml);
	        String text = new String(ch, start, length);
	        append(text);
	    }
	    public void endDocument()
	    {
	    	assertBufferedText();
	    	assertFalse(gotBody && gotHtml);
	    }
	    public void startElement(String uri, String localName, String qName,
	        org.xml.sax.Attributes attributes)
	    {
	    	assertBufferedText();
	    	if (qName.equals("html") && !gotHtml)
	    	{
	    		gotHtml = true;
	    		return;
	    	}
	    	if (qName.equals("body") && !gotBody)
	    	{
	    		gotBody = true;
	    		return;
	    	}
	    	assertTrue(gotBody && gotHtml);
            assertStart(qName, getMap(attributes));
	    }
	    public void endElement(String uri, String localName, String qName)
	    {
	    	assertBufferedText();
	    	if (qName.equals("body") && gotBody)
	    	{
	    		gotBody = false;
	    		return;
	    	}
	    	if (qName.equals("html") && gotHtml)
	    	{
	    		gotHtml = false;
	    		return;
	    	}
	    	assertTrue(gotBody && gotHtml);
	    	assertEnd(qName);
	    }
	    public void skippedEntity(String name)
	    {
	        s_LOG.info("Skipped entity: " + name);
	    }
	    public void warning(SAXParseException e)
	    {
	        s_LOG.warn("Parser warning", e);
	    }
        public void error(SAXParseException e)
        {
            s_LOG.error("Parser error", e);
        }
        public InputSource resolveEntity(String publicId,
            String systemId)
        {
            s_LOG.info("Resolve entity: " + publicId + " / " + systemId);
            return null;
        }
	}

	public HtmlIterator(String document)
	throws Exception
	{
	    try
	    {
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        spf.setValidating(false);
	        SAXParser parser = spf.newSAXParser();
	        parser.getXMLReader().setFeature("http://apache.org/xml/" +
	        		"features/nonvalidating/load-external-dtd", false);
	        ParseHandler handler = new ParseHandler(m_Tags, document);
	        parser.getXMLReader().setProperty("http://xml.org/sax/properties/" +
	        		"lexical-handler", handler);
    	    parser.parse(new InputSource(new StringReader(document)),
    	        handler);
	    }
	    catch (SAXParseException e)
	    {
	        BufferedReader br = new BufferedReader(new StringReader(document));
	        
	        String line = null;
	        
	        for (int i = 1; i <= e.getLineNumber(); i++)
	        {
	            String temp = br.readLine();
	            if (temp == null)
	            {
	            	break;
	            }
	            else 
	            {
	            	line = temp;
	            }
	        }

	        int col = e.getColumnNumber();
	        if (col > 0)
	        {
		        if (col > 2) col -= 3;
		        int end = col + 12;
		        if (end > line.length()) end = line.length();
		        String substring = line.substring(col, end);
		        
		        throw new Exception(e.toString() + " near '" + substring + "' " +
		        		"at line " + e.getLineNumber() + " (" + line + ")", e);
	        }
	        else
	        {
		        throw new Exception(e.toString() + " at line " +
		        		e.getLineNumber() + " (" + line + ")", e);	        	
	        }
	    }
	}
	
	int m_NextTagIndex = 0;
	
	private Node next()
	{
	    Node node;
	    
	    do
	    {
	        node = (Node)m_Tags.get(m_NextTagIndex++);
	    }
	    while (node.skip());
	    
	    return node;
	}
	
	/*
	public void assertInstanceOf(String message, Class expected, Object object)
	{
	    assertTrue(message + ": expected "+expected+" but was "+
	        object.getClass().toString() + " " + object.toString(),
	        expected.isInstance(object));
	}
	
	public void assertInstanceOf(Class expected, Object object)
	{
	    assertTrue("expected "+expected+" but was "+
	        object.getClass().toString() + " " + object.toString(),
	        expected.isInstance(object));
	}
    */
    
    public static class Attributes
    {
        private Map m_Attribs = new HashMap();
        public Attributes() { }
        public Attributes(String name, String value)
        {
        	m_Attribs.put(name, value);
        }
        public Attributes(String [][] array)
        {
            for (int i = 0; i < array.length; i++)
            {
                String [] name_value = array[i];
                m_Attribs.put(name_value[0], name_value[1]);
            } 
        }
        public Attributes(Attributes toCopy)
        {
        	m_Attribs = new HashMap(toCopy.m_Attribs);
        }
        public Attributes add(String name, String value)
        {
            assertNull(m_Attribs.get(name));
            m_Attribs.put(name, value);
            return this;
        }
        public Attributes name  (String name)   { return add("name",   name); }
        public Attributes type  (String type)   { return add("type",   type); }
        public Attributes href  (String link)   { return add("href",   link); }
        public Attributes value (String value)  { return add("value",  value); }
        public Attributes style (String style)  { return add("style",  style); }
        public Attributes clazz (String clazz)  { return add("class",  clazz); }
        public Attributes method(String method) { return add("method", method); }
        public Attributes action(String action) { return add("action", action); }
        Map getMap()
        {
            return m_Attribs;
        }
    }
    
    public void assertXHTML()
    {
    	assertDTD("html", "-//W3C//DTD XHTML 1.0 Strict//EN",
    			"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
    }
    
    /**
	 * asserts that the next node is a DTD node
	 * matching expected name and attributes.
	 */
	public void assertDTD(String name, String publicId, String systemId)
	{
	    Node node = next();
	    node.lazyAssertEquals("<!DOCTYPE " + name + " PUBLIC \"" + 
	    		publicId + "\" \"" + systemId + "\">", node.toShortString());
	}
	
	public StartElementNode assertStart(String tag)
	{
	    return assertStart(tag, s_NoAttributes);
	}

    /**
     * Asserts that the next node is a start element node
     * matching expected name and attributes.
     * @deprecated pass an Attributes object rather than a String array
     * @param tag the expected name of the start element node
     * @param attributes the expected attributes of the start
     * element node
     * @return validated start element node
     */
    public StartElementNode assertStart(String tag, String [][] attributes)
    {
        return assertStart(tag, new Attributes(attributes));
    }

    /**
	 * asserts that the next node is a start element node
	 * matching expected name and attributes.
	 * @param tag the expected name of the start element node
	 * @param attributes the expected attributes of the start
	 * element node
	 * @return validated start element node
	 */
	public StartElementNode assertStart(String tag, Attributes attributes)
	{
	    Node node = next();
	    node.lazyAssertEquals("<" + tag + ">", node.toShortString());
	    StartElementNode sen = (StartElementNode)node;
	    node.lazyAssertEquals(tag, sen.name());
	    sen.assertAttributes(attributes.getMap());
        return sen;
	}

    /**
	 * asserts that the next node is a start element node
	 * matching expected name and attributes.
	 * @param tag the expected name of the start element node
	 * @param attributes the expected attributes of the start
	 * element node
	 * @param message the error message to be shown if the assertion fails
	 * @return validated start element node
	 */	
	public StartElementNode assertStart(String tag, Attributes attributes,
			String message)
	{
	    Node node = next();
	    node.lazyAssertEquals(message, "<" + tag + ">", node.toShortString());
	    StartElementNode sen = (StartElementNode)node;
	    node.lazyAssertEquals(tag, sen.name());
	    sen.assertAttributes(attributes.getMap());
        return sen;
	}

	/**
	 * asserts that the next node is a start element node
	 * matching expected name and attributes.
	 * @param tag the expected name of the start element node
	 * @param attributes the expected attributes of the start
	 * element node
	 * @return validated start element node
	 */
	public StartElementNode assertStart(String tag, Map attributes)
	{
	    Node node = next();
	    node.lazyAssertEquals("<" + tag + ">", node.toShortString());
	    StartElementNode sen = (StartElementNode)node;
	    node.lazyAssertEquals(tag, sen.name());
	    sen.assertAttributes(attributes);
        return sen;
	}

	/**
	 * asserts that the next node is an end node.
	 * @param tag the expected name of the next node.
	 */
	public StartElementNode assertEnd(String tag)
	{
	    Node node = next();
	    node.lazyAssertEquals("</" + tag + ">", node.toShortString());
	    EndElementNode end = (EndElementNode)node;
	    node.lazyAssertEquals(tag, end.name());
	    return end.getMatchingStartTag();
	}

	/**
	 * asserts that the next node is an end node, and that its
	 * corresponding start node has expected attributes.
     * @deprecated pass an Attributes object rather than a String array
	 * @param tag the expected name of the next node.
	 * @param attributes the attributes of the start
	 * node corresponding to the expected end node.
	 */
	public void assertEnd(String tag, String[][] attributes)
	{
        assertEnd(tag, new Attributes(attributes));
	}

    /**
     * asserts that the next node is an end node, and that its
     * corresponding start node has expected attributes.
     * @param tag the expected name of the next node.
     * @param attributes the attributes of the start
     * node corresponding to the expected end node.
     */
    public void assertEnd(String tag, Attributes attributes)
    {
        Node node = next();
        node.lazyAssertEquals("</" + tag + ">", node.toShortString());
        EndElementNode end = (EndElementNode)node;
        node.lazyAssertEquals(tag, end.name());
        end.getMatchingStartTag().assertAttributes(attributes.getMap());
    }

	/**
	 * Asserts that the next node is a text node with the 
	 * specified text. All white space including newlines is canonicalised
	 * to a single space. Httpunit converts nbsp entities to spaces.
	 * @param text the expected text. null and the empty string are ignored.
	 */
	public void assertText(String text)
	{
		if (text == null) return;
		if (text.equals("")) return;
		// httpunit doesn't consistantly remove or keep nbsp entities
		// if (text.equals("&nbsp;")) return;
		text = text.replaceAll("\\s+", " ");
	    Node node = next();
	    node.lazyAssertEquals("Text: " + text, node.toShortString());
	    TextNode textNode = (TextNode)node;
	    node.lazyAssertEquals(text, textNode.text());
	}

	public void skipText()
	{
	    Node node;
	    
	    do
	    {
	        node = (Node)m_Tags.get(m_NextTagIndex);
	        if (node.skip() || node instanceof TextNode)
	        {
	        	m_NextTagIndex++;
	        }
	        else
	        {
	        	break;
	        }
	    }
	    while (true);
	}
	
	/**
     * Asserts that the next node is a text node with the specified raw text,
     * with literal white space and entities as they appear in the document.
     * @param text the expected text.
     */
    public void assertRawText(String text)
    {
        Node node = next();
        node.lazyAssertEquals(TextNode.class.getName(),
            node.getClass().getName());
        TextNode textNode = (TextNode)node;
        node.lazyAssertEquals(text, textNode.rawText());
    }

	/**
	 * asserts that the next node is the last node in the
	 * document.
	 */
	public void assertEndDocument()
	{
        Node node = next();
        assertEquals("End of document", node.toShortString());
	}
	
	/**
	 * asserts that the next node matches the next expected
	 * node, with no attributes, and contains a single text 
	 * node with the specified text.
	 * @param tag the expected name of the next node 
	 * @param text the expected text of the next node
	 */
	public StartElementNode assertSimple(String tag, String text)
	{
	    return assertSimple(tag, text, s_NoAttributes);
	}
	
    /**
     * asserts that the next node matches the next expected 
     * node, and contains a single text node with the 
     * specified text.
     * @deprecated pass an Attributes object rather than a String array
     * @param tag the expected name of the next node.
     * @param text the expected text of the contained node.
     * @param attributes the expected attributes of the next node.
     */
    public StartElementNode assertSimple(String tag, String text, String [][] attributes)
    {
        return assertSimple(tag, text, new Attributes(attributes));
    }

    /**
	 * asserts that the next node matches the next expected 
	 * node, and contains a single text node with the 
	 * specified text.
	 * @param tag the expected name of the next node.
	 * @param text the expected text of the contained node.
	 * @param attributes the expected attributes of the next node.
	 */
	public StartElementNode assertSimple(String tag, String text, Attributes attributes)
	{
	    StartElementNode start = assertStart(tag, attributes);
	    
	    if (text == null)
	    {
	        assertText("null");
	    }
	    else if (text.length() > 0)
	    {
	        assertText(text);
	    }
	    
	    assertEnd(tag);
	    
	    return start;
	}

	   /**
	 * asserts that the next node matches the next expected 
	 * node, and contains a single text node with the 
	 * specified text.
	 * @param tag the expected name of the next node.
	 * @param text the expected text of the contained node.
	 * @param attributes the expected attributes of the next node.
	 * @param message the error message to be shown if the assertion fails
	 */
	public void assertSimple(String tag, String text, Attributes attributes,
			String message)
	{
	    assertStart(tag, attributes, message);
	    
	    if (text == null)
	    {
	        assertText("null");
	    }
	    else if (text.length() > 0)
	    {
	        assertText(text);
	    }
	    
	    assertEnd(tag);
	}
	
    /**
     * Asserts that the next node is an element node with the specified
     * type, containing just the specified raw text.
     * @param tag the expected element name of the next node.
     * @param text the expected raw text contained in the element node.
     */
    public void assertElementRaw(String tag, String rawText)
    {
        assertElementRaw(tag, rawText, s_NoAttributes);
    }

    /**
     * Asserts that the next node is an element node with the specified
     * type and attributes, containing just the specified raw text.
     * @deprecated pass an Attributes object rather than a String array
     * @param tag the expected element name of the next node.
     * @param text the expected raw text contained in the element node.
     * @param attributes the expected attributes of the element node.
     */
    public void assertElementRaw(String tag, String rawText,
        String [][] attributes)
    {
        assertElementRaw(tag, rawText, new Attributes(attributes));
    }

    /**
     * Asserts that the next node is an element node with the specified
     * type and attributes, containing just the specified raw text.
     * @param tag the expected element name of the next node.
     * @param text the expected raw text contained in the element node.
     * @param attributes the expected attributes of the element node.
     */
    public void assertElementRaw(String tag, String rawText,
        Attributes attributes)
    {
        assertStart(tag, attributes);
        
        if (rawText == null)
        {
            assertText("null");
        }
        else if (rawText.length() > 0)
        {
            assertRawText(rawText);
        }
        
        assertEnd(tag, attributes);
    }

    /**
     * Asserts that the next node is an element node with the specified
     * type and attributes, containing just the specified raw text.
     * @param tag the expected element name of the next node.
     * @param text the expected raw text contained in the element node.
     * @param attributes the expected attributes of the element node.
     */
    public void assertElementRaw(String tag, String rawText,
        Map attributes)
    {
        assertStart(tag, attributes);
        
        if (rawText == null)
        {
            assertText("null");
        }
        else if (rawText.length() > 0)
        {
            assertRawText(rawText);
        }
        
        assertEnd(tag);
    }

	private static final String [][] s_NoAttributes = new String[][]{};

	/**
	 * asserts that the next node has no attributes, contains
	 * no further nodes, and matches the expected next node. 
	 * @param tag the expected next node name.
	 */
    public StartElementNode assertEmpty(String tag)
    {
    	StartElementNode start = assertStart(tag, s_NoAttributes);
        assertEnd(tag);
        return start;
    }

	/**
	 * asserts that the next node contains no further nodes
	 * and matches the expected next node
     * @deprecated pass an Attributes object rather than a String array
	 * @param tag the expected next node name.
	 * @param attributes the expected attributes of the 
	 * next node.
	 */
    public StartElementNode assertEmpty(String tag, String [][] attributes)
    {
        StartElementNode start = assertStart(tag, attributes);
        assertEnd(tag);
        return start;
    }

    /**
     * asserts that the next node contains no further nodes
     * and matches the expected next node
     * @param tag the expected next node name.
     * @param attributes the expected attributes of the 
     * next node.
     */
    public StartElementNode assertEmpty(String tag, Attributes attributes)
    {
        StartElementNode start = assertStart(tag, attributes);
        assertEnd(tag);
        return start;
    }
    
    public StartElementNode assertSubmit(String name, String value)
    {
        return assertEmpty("input", new Attributes()
    		.type("submit").name(name).value(value));

    }
    
    public StartElementNode assertSubmit(String name, String value, 
    		Attributes attributes)
    {
        return assertEmpty("input", new Attributes(attributes)
        	.type("submit").name(name).value(value));
    }
    
    public void assertSelectMulti(String name, List options, List selected)
    {
        if (selected == null)
        {
            selected = new ArrayList();
        }
        
        assertStart("select", new String[][]{
            new String[]{"name", name},
            new String[]{"multiple", "1"}});
        
        for (Iterator j = options.iterator(); j.hasNext(); )
        {
            String [] row = (String[])j.next();
            
            StartElementNode start = assertStart("option", new String[][]{
                new String[]{"value", row[0]}});
            
            start.assertAttribute("selected", 
                selected.contains(row[0]) ? "1" : null);
            
            if (row[1].length() > 0)
            {
                assertText(row[1]);
            }
            
            assertEnd("option");
        }
        
        assertEnd("select", new String[][]{new String[]{"name", name}});
    }

    interface ColumnInterface
    {
        void assertTableHeader(HtmlIterator i, String linkUrl);
        void assertEditField(HtmlIterator i, Object value) throws Exception;
        void assertStaticField(HtmlIterator i, String value) throws Exception;
    }

    class SimpleColumn implements ColumnInterface
    {
        private String m_Label;
        private List m_Options;
        
        public SimpleColumn(String label, List options)
        {
            m_Label = label;
            m_Options = options;
        }
        
        public void assertTableHeader(HtmlIterator i, String linkUrl)
        {
            if (linkUrl != null)
            {
                i.assertStart("th");
                i.assertSimple("a", m_Label, new Attributes().href(linkUrl));
                i.assertEnd("th");
            }
            else
            {
                i.assertSimple("th", m_Label);
            }
        }
        
        public void assertEditField(HtmlIterator i, Object value)
        throws Exception
        {
            new SelectBox(m_Label, m_Options).assertField(i);
        }

        public void assertStaticField(HtmlIterator i, String value)
        throws Exception
        {
            i.assertText(value);
        }
    }
    
    public void assertSelectTable(String name, List options, List selected)
    throws Exception
    {
        assertSelectTable(name, options, selected,
            new ColumnInterface[]{new SimpleColumn(name, options)});
    }
    
    private void assertSelectTableHeadings(String action,
        ColumnInterface [] columns)
    {
        assertStart("tr");
        if (columns.length == 1)
        {
            // obvious what is being listed, and want to save space
            // because "Add" and "Remove" are much wider than a checkbox
            assertSimple("th", action).assertAttribute("colspan", 2);
        }
        else
        {
            // with multiple columns, we'd better show column headings
            assertSimple("th", action);
            for (int ci = 0; ci < columns.length; ci++)
            {
                columns[ci].assertTableHeader(this, null);
            }
        }
        assertEnd("tr");
    }
    
    public void assertSelectTable(String name, List options, List selected,
        ColumnInterface [] columns)
    throws Exception
    {
    	if (selected == null)
    	{
    		selected = new ArrayList();
    	}
    	
    	List unselectedOptions = new ArrayList();
    	for (Iterator i = options.iterator(); i.hasNext();)
    	{
    		String [] row = (String[])i.next();
    		if (!selected.contains(row[0]))
    		{
    			unselectedOptions.add(row);
    		}
    	}
    	
    	if (selected.size() > 0)
    	{
            assertSelectTableHeadings("Remove", columns);
    	
	    	for (Iterator j = options.iterator(); j.hasNext(); )
	    	{
	    		String [] values = (String[])j.next();
                assertEquals(columns.length + 1, values.length);

                String optionId = values[0];
                if (!selected.contains(optionId))
                {
                    continue;
                }

                assertStart("tr");
                assertStart("td");
                assertEmpty("input", new String[][]{
                        new String[]{"type", "hidden"},
                        new String[]{"name", name + "_old"},
                        new String[]{"value", optionId},
                });
                assertEmpty("input", new String[][]{
                        new String[]{"type", "checkbox"},
                        new String[]{"name", name + "_del"},
                        new String[]{"value", optionId}
                });
                assertEnd("td");

                for (int ci = 0; ci < columns.length; ci++)
                {
                    assertStart("td");
                    columns[ci].assertStaticField(this, values[ci + 1]);
                    assertEnd("td");
                }

                assertEnd("tr");
            }
    	}
    	
        assertSelectTableHeadings("Add", columns);

    	assertStart("tr");
    	assertStart("td");
    	assertEmpty("input", new String[][]{
    			new String[]{"type", "checkbox"},
    			new String[]{"name", name + "_en"},
    			new String[]{"id",   name + "_en"},
    			new String[]{"value", "1"},
    			new String[]{"onclick", "return enable_" + name + "()"},
    	});
    	assertEnd("td");
    	assertStart("td");
    	assertSelectFromList(name + "_add", (String)null, 
    			unselectedOptions, false, 0);
    	assertStart("script", new String[][]{
    			new String[]{"type", "text/javascript"},
    	});
    	assertText("// function enable_" + name + "() { " +
    			"document.getElementById('" + name + "_add').disabled = " +
    			"!document.getElementById('" + name + "_en').checked; " +
    			"return true; } enable_" + name + "(); //");
    	assertEnd("script");
    	assertEnd("td");
    	assertEnd("tr");
    }

    public void assertSelectFromList(String name, Map params,
    		List options, boolean fixAmpersands, int maxLength)
    {
    	assertSelectFromList(name, (String)params.get(name), options,
    			fixAmpersands, maxLength);
    }
    
    public String getRowValue(Object row)
    {
        if (row instanceof String)
        {
            return (String)row;
        }
        else
        {
            return ((String[])row)[0];
        }
    }

    public String getRowLabel(Object row)
    {
        if (row instanceof String)
        {
            return (String)row;
        }
        else
        {
            return ((String[])row)[1];
        }
    }

    /**
     * Assert a select box (a drop-down list) with the given options.
     * Passing <code>null</code> is not equivalent to expecting the first
     * item of the list to be selected. If you want that, do it yourself.
     * 
     * @param name The name expected to appear in the select element's
     * name attribute.
     * @param value The value of the select box which is expected to have
     * the "selected" attribute. 
     * @param options The list of available options in the select box,
     * usually a list of String arrays, each of which has two elements:
     * the value and the corresponding label. 
     * @param fixAmpersands Converts entities in the list to their original
     * unescaped form, mainly a hack to work around broken sites which
     * sometimes escape entities properly and sometimes don't.
     * @param maxLength The maximum length of each entry, after which
     * it will be truncated with an ellipsis.
     */
    public void assertSelectFromList(String name, String value,
    		List options, boolean fixAmpersands, int maxLength)
    {
        assertStart("select", new String[][]{new String[]{"name", name}});
        
        for (Iterator j = options.iterator(); j.hasNext(); )
        {
            Object row = j.next();
            
            StartElementNode start = assertStart("option", new String[][]{
                new String[]{"value", getRowValue(row)}});
            
            start.assertAttribute("selected", 
            		getRowValue(row).equals(value) ? "selected" : null,	
                	name);
            
            if (getRowLabel(row).length() > 0)
            {
                // since we are checking the raw text, it will include
                // entities unescaped.
                assertRawText(EditField.escapeEntities(getRowLabel(row), 
                		fixAmpersands, maxLength));
            }
            
            assertEnd("option");
        }
        
        assertEnd("select", new String[][]{new String[]{"name", name}});
    }
	
    
    public void assertYesNoUndef(String name, Map fields)
    {
    	assertYesNoUndef(name, (String)fields.get(name));
    }

    public void assertYesNoUndef(String name, String value)
    {
        List options = new ArrayList();
        options.add(new String[]{"",""});
        options.add(new String[]{"1","Yes"});
        options.add(new String[]{"0","No"});
        assertSelectFromList(name, value, options, false, 0);
    }
    
    public void assertYesNo(String name, String value)
    {
        List options = new ArrayList();
        options.add(new String[]{"1","Yes"});
        options.add(new String[]{"0","No"});
        assertSelectFromList(name, value, options, false, 0);
    }
    
    public StartElementNode assertInput(String name, String value)
    {
        if (value == null)
        {
            value = "";
        }
        
        return assertEmpty("input", new String[][]{
            new String[]{"name", name},
            new String[]{"value", value}});
    }

    public StartElementNode assertInput(String name, Map values)
    {
        if (values.containsKey(name))
        {
            return assertInput(name, (String)values.get(name));
        }
        StartElementNode node = assertEmpty("input", 
        		new Attributes().name(name));
        String value = node.getAttribute("value");
        assertTrue(value, value == null || value.equals(""));
        return node;
    }
    
    public void assertHtml(String html)
    throws Exception
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setValidating(false);
        SAXParser parser = spf.newSAXParser();
        parser.getXMLReader().setFeature("http://apache.org/xml/" +
        		"features/nonvalidating/load-external-dtd", false);
	    parser.parse(
	        new InputSource(new StringReader("<html><body>" + html + 
	        		"</body></html>")),
	        new AssertingHandler());
    }
    
    public StartElementNode assertFormSubmit(String name, String value)
    {
        return assertEmpty("input").assertAttribute("name", name)
            .assertAttribute("value", value)
            .assertAttribute("type", "submit");
    }
    
    public StartElementNode assertFormTextField(String name, String value)
    {
        return assertEmpty("input").assertAttribute("name", name)
            .assertAttribute("value", value)
            .assertAttribute("type", "text");
    }
    
    public StartElementNode assertFormHidden(String name, String value)
    {
        return assertEmpty("input").assertAttribute("name", name)
            .assertAttribute("value", value)
            .assertAttribute("type", "hidden");
    }
    
    public StartElementNode assertFormTextArea(String name, String value)
    {
        return assertSimple("textarea", value).assertAttribute("name", name);
    }
    
    public StartElementNode assertFormCheckbox(String name,
    	String checkedValue, boolean isChecked)
    {
        return assertEmpty("input").assertAttribute("type", "checkbox")
	        .assertAttribute("name", name)
	        .assertAttribute("value", checkedValue)
	        .assertAttribute("checked", isChecked ? "checked" : null);       
    }
}