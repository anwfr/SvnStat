/*
 * Copyright © 2006 Juergen Lind (jli@agentlab.de).
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


public class AutoList extends ArrayList {

    public Object get(int index) {
        Object result = null;
        try {
            result = super.get(index);
        } catch (IndexOutOfBoundsException e) {
            for (int i = super.size(); i <= index; i++) {
                super.add(new Object());
            }

            result = super.get(index);
        }
        return result;
    }

    public Object set(int index, Object element) {
        this.get(index);
        return super.set(index, element);
    }

}
