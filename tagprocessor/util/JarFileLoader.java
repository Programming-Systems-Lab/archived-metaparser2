/*
  Copyright (c) 2000: The Trustees of Columbia University and the City of New York.
  All Rights Reserved.
*/

package psl.tagprocessor.util;

import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.io.*;

/**
 * extract files from a jar file.
 *
 * @author: Simin Wang
 * @version: 1.0
 */
public class JarFileLoader {
   
    /**
     * the jar file
     */
    JarFile _jarFile;

    /**
     * construct a JarFile given the file name
     *
     * @param name  the jar file name
     */
    public JarFileLoader(String name) {
    System.err.println (name);
	try {
	    _jarFile = new JarFile(name);
	} catch(IOException e) {
	    e.printStackTrace();
	}
	}

	/**
     * encapsulate a given JarFile
     *
     * @param theJar  the jar file
     */
	public JarFileLoader(JarFile theJar) {
		_jarFile = theJar;
	}
	

    /**
     * return the input stream associated with the file.
     *
     * @param filename   the file name
     * @return           the input stream
     */
    public InputStream getInputStream(String filename) {
	try {
	    ZipEntry ze = _jarFile.getEntry(filename);
	    return _jarFile.getInputStream(ze);
	} catch(IOException e) {
	    e.printStackTrace();
	}
	return null;
    }
}
