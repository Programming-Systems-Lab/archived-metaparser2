/*
  Copyright (c) 2000: The Trustees of Columbia University and the City of New York.
  All Rights Reserved.
*/

package psl.tagprocessor.parser;

import siena.AttributeValue;
import java.io.*;
import java.util.Hashtable;


/**
 *
 * The wrapper class of the expression parser.
 * 
 * @author: Simin Wang
 * @version: 0.1
 */

public class ExpParser {

    /** 
     * the actual parser used to evaluate expressions.
     */
    MyParser _parser;

    /**
     * the stream to be parsed.
     */
    StringReader _stream;

    /**
     * construct an ExpParser instance using a temperary hashtable.
     *
     * @param ht   the hashtable
     */
    public ExpParser(Hashtable ht) {
	_stream = new StringReader("");
	_parser = new MyParser(_stream);
	_parser.setHashtable(ht);
    }

    /**
     * evaluate the expression given.
     *
     * @param s   the expression
     * @return    a siena AttributeValue instance
     */
    public AttributeValue parse(String s) {
	try {
	    _stream = new StringReader(s);
	    _parser.ReInit(_stream);
	    return _parser.parseOneLine();
	} catch(Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

}

