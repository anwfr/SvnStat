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

import java.util.ArrayList;
import java.util.List;


public class SvnRecord {

    private String       date;
    private String       time;
    private String       user;
    private int          added;
    private int          modified;
    private int          deleted;
    private List<String> files = new ArrayList<String>();

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getAdded() {
        return this.added;
    }

    public void setAdded(int added) {
        this.added = added;
    }

    public int getDeleted() {
        return this.deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getModified() {
        return this.modified;
    }

    public void setModified(int modified) {
        this.modified = modified;
    }

    public void addFile(String filename){
        this.files.add(filename);
    }

    public List<String> getFiles() {
        return this.files;
    }

}
