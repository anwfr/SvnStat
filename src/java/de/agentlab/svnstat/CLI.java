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

import java.util.HashMap;


/**
 * Simple command line interface.
 * 
 * @author Jürgen Lind (iteratec GmbH)
 * @version $Id: CLI.java,v 1.2 2006/06/17 20:46:32 jli Exp $
 */
public class CLI {

    private HashMap<String, Comparable> options;

    public CLI() {
        this.options = new HashMap<String, Comparable>();
    }

    /**
     * Parse the vector of command line arguments into options and store these options in a HashMap. A
     * command line option must start with a dash ("-") and may have an argument (which must not start
     * with a dash). Options without arguments are considered boolean. Note that it is not possible to
     * provide a list of non-option arguments.
     * 
     * @param argv the vector of command line arguments
     */
    public void parseOptions(String[] argv) {
        int i = 0;

        while (i < argv.length) {
            String arg = argv[i++];

            if (arg.startsWith("-")) {
                if (i != argv.length) {
                    // There may be an argument to this option.
                    String value = argv[i];

                    if (!value.startsWith("-")) {
                        // argument
                        this.options.put(arg.substring(1), value);
                        i++;
                    } else {
                        // no argument
                        this.options.put(arg.substring(1), new Boolean(true));
                    }
                } else {
                    // This is the last option: there cannot be an argument.
                    this.options.put(arg.substring(1), new Boolean(true));
                }
            }
        }
    }

    /**
     * Return the options HashMap.
     * 
     * @return the options HashMap
     */
    public HashMap<String, Comparable> getOptions() {
        return this.options;
    }

    /**
     * Returns an option value as String.
     * 
     * @param name the name of the option
     * 
     * @return the option value as String or <code>null</code> if the option is not provided by the
     *         user.
     */
    public String getStringOption(String name) {
        return (String) this.options.get(name);
    }

    public String getStringOption(String name, String defaultValue) {
        String result = (String) this.options.get(name);

        if (result != null) {
            return result;
        } else {
            return defaultValue;
        }
    }

    /**
     * Returns an option value as boolean value.
     * 
     * @param name the name of the option
     * 
     * @return <code>true</code> if the option is provided, <code>false</code> otherwise
     */
    public boolean getBooleanOption(String name) {
        Boolean value = ((Boolean) this.options.get(name));

        if (value != null) {
            return value.booleanValue();
        } else {
            return false;
        }
    }

    /**
     * Returns an option value as boolean value.
     * 
     * @param name the name of the option
     * 
     * @return <code>true</code> if the option is provided, <code>false</code> otherwise
     */
    public boolean getBooleanOption(String name, boolean defaultValue) {
        Boolean value = ((Boolean) this.options.get(name));

        if (value != null) {
            return value.booleanValue();
        } else {
            return defaultValue;
        }
    }

    public double getDoubleOption(String name, double defaultValue) {
        String result = (String) this.options.get(name);

        if (result != null) {
            return Double.parseDouble(result);
        } else {
            return defaultValue;
        }
    }

    public int getIntOption(String name, int defaultValue) {
        String result = (String) this.options.get(name);

        if (result != null) {
            return Integer.parseInt(result);
        } else {
            return defaultValue;
        }
    }

}
