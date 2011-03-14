/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.media.server.spi.format;

import java.util.ArrayList;

/**
 * Represents collection of formats.
 *
 * @author kulikov
 */
public class Formats {
    //the default size of format list
    public final static int DEFAULT_SIZE = 15;

    //backing array
    private ArrayList<Format> list;

    /**
     * Creates new collection with default size;
     */
    public Formats() {
        list = new ArrayList(DEFAULT_SIZE);
    }

    /**
     * Creates collection with specified size.
     *
     * @param size the size of the collection.
     */
    public Formats(int size) {
        list = new ArrayList(size);
    }

    /**
     * Adds specified format to this collection.
     *
     * @param format the format object to be added
     */
    public void add(Format format) {
        list.add(format);
    }

    /**
     * Adds multiple formats.
     * 
     * @param other the collection of formats to be added to this collection.
     */
    public void addAll(Formats other) {
        this.list.addAll(other.list);
    }

    /**
     * Removes specified format from this collection.
     *
     * @param format the format object to be removed.
     */
    public void remove(Format format) {
        list.remove(format);
    }

    /**
     * Gets the collection element.
     * 
     * @param i the position of the element in collection
     * @return format descriptor.
     */
    public Format get(int i) {
        return list.get(i);
    }

    /**
     * Checks that collection has specified format.
     *
     * @param format the format to be checked
     * @return true if collection contains specified format and false otherwise
     */
    public boolean contains(Format format) {
        for (Format f : list) {
            if (f.matches(format)) return true;
        }
        return false;
    }
    
    /**
     * Gets the number of formats contained inside collection.
     * 
     * @return the number of objects in the collection.
     */
    public int size() {
        return list.size();
    }

    /**
     * Checks collection's size.
     *
     * @return true if collection is empty and its size equals to zero.
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Removes all formats from the collection.
     */
    public void clean() {
        list.clear();
    }
    
    /**
     * Find the intersection between this collection and other
     * 
     * @param other the other collection
     * @param intersection the resulting collection.
     */
    public void intersection(Formats other, Formats intersection) {
        for (Format f1 : list) {
            for (Format f2 : other.list) {
                if (f1.matches(f2)) intersection.list.add(f2);
            }
        }
    }

}
