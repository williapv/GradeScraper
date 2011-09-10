/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williapv.grades.gradesscraper;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author paul
 */
public class PropertiesFileFilter implements FilenameFilter{

    public boolean accept(File file, String string) {
        if(string.endsWith(".properties"))
        {
            return true;
        }
        return false;
    }
    
}
