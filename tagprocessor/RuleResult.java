/*
  Copyright (c) 2000: The Trustees of Columbia University and the City of New York.
  All Rights Reserved.
*/

package psl.tagprocessor;

import java.util.*;
import org.jdom.Element;
import siena.AttributeValue;

import psl.tagprocessor.parser.ExpParser;

/**
 * The object that represents a "result" in the rule set.
 *
 * @author: Simin Wang
 * @version: 1.0
 *
 * $Id$
 */

public class RuleResult {

    /**
     * an expression element in the doc tree.
     */
    Element _expression;
    
    /**
     * an element that represents an effect.
     */
    Element _effects;

    /**
     * the string representation of the element.
     */
    String _str;

    /**
     * the expression parser.
     */
    ExpParser _expParser;

    /**
     * the global hashtable that stores the results
     */
    Hashtable _hashtable;

    /**
     * constructor.
     * 
     * @param element   the element used to contruct an RuleResult object
     */
    public RuleResult(Element element) {
        _expression = element.getChild("expression");
        _effects = element.getChild("effects");
        _str = toString((Element)_expression.getChildren().get(0));
    }

    /**
     * return effects name.
     *
     * @return   the effect name
     */
    public String getEffectsName() {
        return _effects.getAttributeValue("name");
    }

    /**
     * given an effect, put relevant information into the hashtable.
     *
     * @param hashtable   teh global hashtable
     */
    public void hash(Hashtable hashtable) {
        List list = _effects.getChildren("effect");
        String s;
        String s1;
        String s2;
        Iterator iterator = list.iterator();
	while(iterator.hasNext()) {
	    Element element = (Element)iterator.next();
            s = element.getChild("key").getText();
            s1 = element.getChild("type").getText();
            s2 = element.getChild("value").getText();
	    //System.out.println(s1 + " " + s + ";");
	    if(!hashtable.containsKey(s)) {
		_expParser.parse(s1 + " " + s + ";");
	    }
	    _expParser.parse(s + "=" + s2 + ";");
	    //hashtable.put(s, convert(s1, s2));
	}
    }

    /**
     * set the hashtable.
     *
     * @param ht   the hashtable
     */
    public void setHashtable(Hashtable ht) {
	_hashtable = ht;
	_expParser = new ExpParser(_hashtable);
    }

    /**
     * given the type and the value, convert it to
     * a siena AttributeValue object.
     *
     * @param s   type
     * @param s1  value
     * @return    an AttributeValue object
     */
    private AttributeValue convert(String s, String s1) {
	AttributeValue value = _expParser.parse(s1 + ";");
	return value;
	/* 
        if(s.equals("boolean"))
        {
            boolean flag;
            if(s1.equals("true"))
                flag = true;
            else
                flag = false;
            return new AttributeValue(flag);
        }
        if(s.equals("long"))
        {
            long l = -1L;
            try
            {
                l = Long.parseLong(s1);
            }
            catch(NumberFormatException numberformatexception)
            {
                numberformatexception.printStackTrace();
            }
            return new AttributeValue(l);
        }
        if(s.equals("double"))
        {
            double d = -1D;
            try
            {
                d = Double.parseDouble(s1);
            }
            catch(NumberFormatException numberformatexception1)
            {
                numberformatexception1.printStackTrace();
            }
            return new AttributeValue(d);
        }
        if(s.equals("byte"))
            return new AttributeValue(s1.getBytes());
        else
            return new AttributeValue(s1);
	*/
    }

    /**
     * get the string representation of the object.
     *
     * @return   string
     */
    public String toString() {
        return _str;
    }

    /**
     * given an element, convert it to a string.
     *
     * @param element   the element
     * @return          the string representation of the element
     */
    private String toString(Element element) {
        if(element.getName().equals("id"))
            return element.getTextTrim();
        List list = element.getChildren();
        java.util.ListIterator listiterator = list.listIterator();
        String s = "";
        if(element.getName().equals("and"))
            s = "&";
        else
        if(element.getName().equals("or"))
            s = "|";
        String s1 = "(";
        if(listiterator.hasNext())
            s1 = s1 + toString((Element)listiterator.next());
        while(listiterator.hasNext()) 
            s1 = s1 + s + toString((Element)listiterator.next());
        s1 = s1 + ")";
        return s1;
    }

    /**
     * given a set of rule ids, check if any of it matches the rule.
     *
     * @param hashset  the set that contains rule ids
     * @return         <code>true</code> if any id matches
     *                 <code>false</code> otherwise
     */
    public boolean isMatched(HashSet hashset) {
        return isMatched(hashset, (Element)_expression.getChildren().get(0));
    }

    /**
     * given an element and a set of ids, check if there is a match.
     *
     * @param hashset  the set of rule ids   
     * @param element  the element that describes a rule
     * @return         <code>true</code> if they match
     *                 <code>false</code> otherwise
     */
    private boolean isMatched(HashSet hashset, Element element) {
        if(element.getName().equals("id"))
            return hashset.contains(element.getTextTrim());
        List list = element.getChildren();
        java.util.ListIterator listiterator = list.listIterator();
        if(list.size() == 0)
            return false;
        if(element.getName().equals("and")) {
            while(listiterator.hasNext()) 
                if(!isMatched(hashset, (Element)listiterator.next()))
                    return false;
            return true;
        }else if(element.getName().equals("or")) {
            while(listiterator.hasNext()) 
                if(isMatched(hashset, (Element)listiterator.next()))
                    return true;
            return false;
        } else {
            return false;
        }
    }

}






