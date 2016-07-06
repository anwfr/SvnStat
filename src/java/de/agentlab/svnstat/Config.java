/*
 * Copyright © 2006 Juergen Lind (jli@agentlab.de), 2014 Joe Egan (J0e3gan@gmail.com).
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 */

package de.agentlab.svnstat;

import java.util.Enumeration;
import java.util.PropertyResourceBundle;


public class Config {

    private static PropertyResourceBundle config;

    public static void init(PropertyResourceBundle config) {
        Config.config = config;
    }

    public static String getProperty(String key) {
        try {
            return config.getString(key);
        } catch (RuntimeException e) {
            return "";
        }
    }

    public static String getProperty(String key, String defaultValue) {
        String result = config.getString(key);
        if (result != null) {
            return result;
        } else {
            return defaultValue;
        }
    }

    public static int getIntProperty(String key, int defaultValue) {
        String result = config.getString(key);
        if (result != null) {
            return Integer.parseInt(result);
        } else {
            return defaultValue;
        }
    }

    public static Enumeration<String> getKeys() {
        return config.getKeys();
    }

}
