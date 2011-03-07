package org.aptivate.web;

import junit.framework.TestCase;

import org.aptivate.web.utils.EditField;
import org.aptivate.web.utils.HtmlIterator;

public class EditFieldTest extends TestCase
{
	protected void setUp() throws Exception 
	{
		super.setUp();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testEscapeEntitiesString()
	{
		assertEquals("IRNEM - Institut Fédératif de " +
				"Recherche Necker Enfants M",
				EditField.escapeEntities("IRNEM - Institut " +
						"F&eacute;d&eacute;ratif de Recherche " +
						"Necker Enfants M"));
		assertEquals("SciELO (México)",
				EditField.escapeEntities("SciELO (México)"));
		assertEquals("Swets &amp; Zeitlinger",
				EditField.escapeEntities("Swets &amp; Zeitlinger"));
		assertEquals("Taylor &amp; Francis",
				EditField.escapeEntities("Taylor & Francis"));
		assertEquals("Associação Brasileira de Psicologia " +
				"Escolar e Educacional",
				EditField.escapeEntities("Associação Brasileira " +
						"de Psicologia Escolar e Educacional"));
		assertEquals("Taylor &lt; Francis",
				EditField.escapeEntities("Taylor < Francis"));
		assertEquals("&gt; Taylor Francis",
				EditField.escapeEntities("> Taylor Francis"));
	}

	public void testEscapeEntitiesStringBooleanInt()
	{
		assertEquals("IRNEM - Institut Fédératif de ...",
				EditField.escapeEntities("IRNEM - Institut " +
						"F&eacute;d&eacute;ratif de Recherche " +
						"Necker Enfants M", true, 30));
		assertEquals("IRNEM - Institut F&amp;eacute;d&amp;ea...", 
				EditField.escapeEntities("IRNEM - Institut " +
						"F&eacute;d&eacute;ratif de Recherche " +
						"Necker Enfants M", false, 30));
		assertEquals("SciELO (México)",
				EditField.escapeEntities("SciELO (México)", true, 30));
		assertEquals("SciELO (México)",
				EditField.escapeEntities("SciELO (México)", false, 30));
	}

	/*
	public void testAttrib() {
		fail("Not yet implemented");
	}

	public void testText() {
		fail("Not yet implemented");
	}

	public void testHidden() {
		fail("Not yet implemented");
	}

	public void testSelect() {
		fail("Not yet implemented");
	}

	public void testSelectMulti() {
		fail("Not yet implemented");
	}

	public void testSelectTable() {
		fail("Not yet implemented");
	}

	public void testCheck() {
		fail("Not yet implemented");
	}

	public void testYesNo() {
		fail("Not yet implemented");
	}

	public void testYesNoUndef() {
		fail("Not yet implemented");
	}
	*/
    
    public void testSubmitButton()
    {
        assertEquals("<input type=\"submit\" name=\"foo\" id=\"foo\" " +
                "value=\"bar\" class=\"baz\" />",
                new EditField(null).submit("foo", "bar")
                .setAttribute("class", "baz").toString());
    }
}
