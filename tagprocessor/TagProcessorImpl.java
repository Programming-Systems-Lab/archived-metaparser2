/*
  Copyright (c) 2000: The Trustees of Columbia University and the City of New York.
  All Rights Reserved.
*/

package psl.tagprocessor;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.JarURLConnection;
import java.util.*;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.*;
import org.apache.xerces.parsers.SAXParser;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import siena.AttributeValue;

import psl.tagprocessor.util.JarFileLoader;

/**
 * Currently the only implementation of <code>TagProcessor</code> Interface.
 *
 * @author: Simin Wang
 * @version: 1.0
 *
 * $Id$
 */

// Referenced classes of package psl.tagprocessor:
//            JarResources, RuleResult, TagProcessor

public class TagProcessorImpl implements TagProcessor {

    /** 
     * the contentHandler for handling XML documents parsed by metaparser. 
     */
    TransformerHandler _contentHandler;
    
    /**
     * the global hashtable used to store results
     */
    Hashtable _ht;

    /**
     * The dom tree that contains the rules for matching intermediate
     * results produced by XSL transformation.
     */
    Document _rulesetDoc;

    /**
     * The list of RuleResult objects
     */
    List _resultList;

    /**
     * The set of rule ids
     */
    HashSet _set;

    /**
     * the input source of XSL rules
     */
    InputSource _rule;

    /**
     * the input stream of rule sets
     */
    InputStream _ruleset;

    /**
     * the jar path for classes and rules
     */
    String _jarPath;

    /**
     * the name for the properties file
     */
    static final String PROPERTIES = "properties.txt";

    /**
     * the name of the key for rule template file
     */
    static final String TEMPLATE = "rule_template";

    /**
     * the name of the key for the ruleset file
     */
    static final String RULESET = "rule_set";
    
    /**
     * a SAX feature
     */
    static final String FEATURE_A = "http://javax.xml.transform.sax.SAXSource/feature";
    
    /**
     * another SAX feature
     */
    static final String FEATURE_B = "http://javax.xml.transform.sax.SAXResult/feature";

    /**
     * empty constructor.
     */
    public TagProcessorImpl() {
	
    }

    /**
     * initialize the tagprocessor and set the global hashtable.
     *
     * @param hashtable   the global hashtable
     */
    public void init(Hashtable hashtable) {
        _ht = hashtable;
        buildResultTree();
        buildResultList("result");
        try {
            TransformerFactory transformerfactory = TransformerFactory.newInstance();
            
	    if(transformerfactory.getFeature(FEATURE_A) 
	       && transformerfactory.getFeature(FEATURE_B)) {
                SAXTransformerFactory saxtransformerfactory 
		    = (SAXTransformerFactory)transformerfactory;
                TemplatesHandler templateshandler 
		    = saxtransformerfactory.newTemplatesHandler();
                SAXParser saxparser = new SAXParser();
                saxparser.setContentHandler(templateshandler);
                saxparser.parse(_rule);
                javax.xml.transform.Templates templates 
		    = templateshandler.getTemplates();
                _set = new HashSet();
                _contentHandler 
		    = saxtransformerfactory.newTransformerHandler(templates);
                _contentHandler.setResult(new SAXResult(new RuleHandler()));
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * given the path, load various classes and files.
     *
     * @param filename   the file name for the jar file
     */
    public void setResource(String filename) {
	try {
	    JarFileLoader jfl = new JarFileLoader(filename);
	    Properties prop = new Properties();
	    prop.load(jfl.getInputStream(PROPERTIES));
	    String ruleTemplate = prop.getProperty(TEMPLATE);
	    String ruleSet = prop.getProperty(RULESET);
	    _rule = new InputSource(jfl.getInputStream(ruleTemplate));
	    _ruleset = jfl.getInputStream(ruleSet);
	} catch(Exception e) {
	    e.printStackTrace();
	}
        //JarResources jarresources = new JarResources(path);
        //_rule = new InputSource
	//(new ByteArrayInputStream(jarresources.getResource(RULE)));
        //_ruleset = new ByteArrayInputStream(jarresources.getResource(RESULT));
    }
    
    /**
     * given the URL, load various classes and files.
     * assumes a URL to a jar file
     *
     * @param tpURL   the URL for the jar file
     */
    public void setResource(URL tpURL) {
    	System.err.println (tpURL.toExternalForm());
    	try {
   	    	URL jarURL = new URL("jar:" + tpURL.toExternalForm() + "!/");
    		JarURLConnection urlCon= (JarURLConnection)jarURL.openConnection();
			urlCon.connect();
			//urlCon.getJarFile gives us the actual .jar file
			//there is an option here for caching it 
			JarFileLoader jfl = new JarFileLoader (urlCon.getJarFile());
			
			Properties prop = new Properties();
	    	prop.load(jfl.getInputStream(PROPERTIES));
	    	String ruleTemplate = prop.getProperty(TEMPLATE);
	    	String ruleSet = prop.getProperty(RULESET);
	    	_rule = new InputSource(jfl.getInputStream(ruleTemplate));
	    	_ruleset = jfl.getInputStream(ruleSet);	
    	} catch (Exception e) {
    		// needs better exception handling than this!
    		e.printStackTrace();    		
    	}    	
    }
    

    /**
     * build a DOM tree using rule sets.
     */
    private void buildResultTree() {
        try {
            SAXBuilder saxbuilder = new SAXBuilder();
            _rulesetDoc = saxbuilder.build(_ruleset);
        } catch(JDOMException jdomexception) {
            jdomexception.printStackTrace();
        }
    }

    /**
     * return the contentHandler, which will be used by metaparser.
     *
     * @return   <code>ContentHandler</code>
     */
    public ContentHandler getContentHandler() {
        return _contentHandler;
    }

    /**
     * match the rules against the XML documents being processed by metaparser
     */
    public void process() {
        //System.out.println(_set);
        for(ListIterator listiterator = _resultList.listIterator(); listiterator.hasNext();)
        {
            RuleResult ruleresult = (RuleResult)listiterator.next();
	    ruleresult.setHashtable(_ht);
            if(ruleresult.isMatched(_set))
                ruleresult.hash(_ht);
        }

    }

    /**
     * given the element name, build the list that contains the rule set
     *
     * @param s   the element name for each rule
     */
    private void buildResultList(String s) {
        Element element = _rulesetDoc.getRootElement();
        _resultList = new LinkedList();
        addResultList(s, element, _resultList);
    }

    /**
     * add the rules to the rule set list
     * 
     * @param s       the name of the element    
     * @param element a DOM Element
     * @param list    the list that has the rule set
     */
    private void addResultList(String s, Element element, List list) {
        List list1 = element.getChildren();
        if(list1.size() == 0)
            return;
        for(ListIterator listiterator = list1.listIterator(); listiterator.hasNext();) {
            Element element1 = (Element)listiterator.next();
            if(element1.getName().equals(s))
                list.add(new RuleResult(element1));
            else
                addResultList(s, element1, list);
        }
    }

    /**
     * this class handles the SAX events of resulting XML document
     * after XSL transformation using XSL templates.
     */
    class RuleHandler extends DefaultHandler {
	/** the string to be inserted in the hashtable. */
	String _insert;

	/**
	 * It handles the "startElement" event.
	 *
	 * @param uri         
	 * @param localName
	 * @param qName
	 * @param attributes
	 */
        public void startElement(String uri, String localName, 
				 String qName, Attributes attributes) {
            if(localName.equals("rule")) {
                String id = attributes.getValue("id");
                _set.add(id);
	    } else if(localName.equals("insert")) {
                _insert = new String("");
	    } else {
		String value = attributes.getValue("value");
		_ht.put(localName, new AttributeValue(value));
	    }
        }
	
	/**
	 * It handles the "endElement" event.
	 *
	 * @param uri
	 * @param localName
	 * @param qName
	 */
	public void endElement(String uri, String localName, String qName) {
	    if(localName.equals("insert")) {
		String key = _insert.substring(0, _insert.indexOf('='));
		String value = _insert.substring(_insert.indexOf('=')+1, _insert.length());
		_ht.put(key, new AttributeValue(value));
		_insert = null;
	    }
	}

	/**
	 * It handles characters data.
	 * 
	 * Append the characters to the string _insert.
	 *
	 * @param ch
	 * @param start
	 * @param length
	 */
	public void characters(char[] ch, int start, int length) {
	    if(_insert != null) {
		String s = new String(ch, start, length);
		_insert += s;
	    }
	}

	/**
	 * Empty Constructor.
	 */
        public RuleHandler(){

        }
    }

}








