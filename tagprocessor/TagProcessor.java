/*
  Copyright (c) 2000: The Trustees of Columbia University and the City of New York.
  All Rights Reserved.
*/


package psl.tagprocessor;

import java.util.Hashtable;
import java.net.URL;
import org.xml.sax.ContentHandler;

/**
 * The interface of tagprocessor.
 *
 *
 * @author: Simin Wang
 * @version: 1.0
 *
 * $Id$
 */

public interface TagProcessor {
    /**
     * initialize a tagprocessor and pass it a hashtable to store results.
     * 
     * @param hashtable   the hashtable used to store results
     */
    public abstract void init(Hashtable hashtable);

    /**
     * get the SAX contentHandler which tagprocessor uses to handle events.
     *
     * @return   <code>ContentHandler</code>
     */           
    public abstract ContentHandler getContentHandler();

    /**
     * process the XML documents.
     */
    public abstract void process();

    /**
     * tell the tagprocessor where to look for the jar files.
     *
     * In the future this method may not be necessary, 
     * because only two small text files will be sent from Oracle
     * 
     * @param path   the path of the jar files
     */ 
    public abstract void setResource(String path);
    
    /**
     * tell the tagprocessor where to look for a jar file.
     *
     * In the future this method may not be necessary, 
     * because only two small text files will be sent from Oracle
     * 
     * @param tpURL   the path of the jar file
     */ 
    public abstract void setResource (URL tpURL);
}
