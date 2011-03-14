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
package org.mobicents.media.server.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Case insensitive text block representation.
 *
 * This class provides time deterministic and fast methods for creating
 * and comparing short character strings like format names, attribute values, etc.
 *
 * @author kulikov
 */
public class Text implements CharSequence {
    //reference on the local buffer
    protected byte[] chars;

    //the start position
    protected int pos;

    //the length of the string
    protected int len;

    /** points to the line separator */
    private int linePointer;

    /**
     * Create an empty text object.
     *
     * This object later can be strained on some memory area
     */
    public Text() {
    }

    /**
     * Creates new object with specified text.
     *
     * @param another the text object.
     */
    protected Text(Text another) {
        this.chars = another.chars;
        this.pos = another.pos;
        this.len = another.len;
    }
    
    /**
     * Creates new instance with specified text.
     *
     * @param s the text value.
     */
    public Text(String s) {
        this.chars = s.getBytes();
        this.pos = 0;
        this.len = chars.length;
    }

    /**
     * Creates new instance and straing it into memory.
     * @param data memory
     * @param pos initial position
     * @param len the length from the initial position
     */
    private Text(byte[] data, int pos, int len) {
        this.chars = data;
        this.pos = pos;
        this.len = len;
    }

    /**
     * Strains this object into the memory area.
     *
     * @param data the memory area
     * @param pos the initial position
     * @param len the length of area to use
     */
    public void strain(byte[] data, int pos, int len) {
        this.chars = data;
        this.pos = pos;
        this.len = len;
    }

    /**
     * (Non Java-doc.)
     * @see java.lang.CharSequence#length()
     */
    public int length() {
        return len;
    }

    /**
     * (Non Java-doc.)
     * @see java.lang.CharSequence#charAt(int)
     */
    public char charAt(int index) {
        return (char) chars[pos + index];
    }

    /**
     * (Non Java-doc.)
     * @see java.lang.CharSequence#subSequence(int, int);
     */
    public CharSequence subSequence(int start, int end) {
        return new Text(chars, start, end - start);
    }

    /**
     * Splits text into partitions.
     * 
     * @param separator character used for partitioning
     * @return array of text strings.
     */
    public Collection<Text> split(char separator) {
        int pointer = pos;
        int limit = pos + len;
        int mark = pointer;

        ArrayList<Text> tokens = new ArrayList();
        while (pointer < limit) {
            if (chars[pointer] == separator) {
                tokens.add(new Text(chars, mark, pointer - mark));
                mark = pointer + 1;
            }
            pointer++;
        }
        
        tokens.add(new Text(chars, mark, limit - mark));        
        return tokens;
    }

    /**
     * Removes whitespaces from the head and tail of the string.
     * 
     */
    public void trim() {
        //cut white spaces from the head
        while (chars[pos] == ' ') {
            pos++;
            len--;
        }

        //cut from the end
        while (chars[pos + len - 1] == ' ') {
            len--;
        }
    }

    /**
     * Extracts next line from this text.
     * 
     * @return
     */
    public Text nextLine() {
        if (linePointer == 0) {
            linePointer = pos;
        } else {
            linePointer++;
        }

        int mark = linePointer;
        int limit = pos + len;

        while (linePointer < limit && chars[linePointer] != '\n') {
            linePointer++;
        }
        
        return new Text(chars, mark, linePointer - mark);
    }

    /**
     * Shows is this text contains more lines.
     *
     * @return true if there are more lines 
     */
    public boolean hasMoreLines() {
        return linePointer < pos + len;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!(other instanceof Text)) {
            return false;
        }

        Text t = (Text) other;
        if (this.len != t.len) {
            return false;
        }

        return compareChars(t.chars, t.pos);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Arrays.hashCode(this.chars);
        return hash;
    }

    /**
     * Compares specified character string with current string.
     * The upper and lower case charactes are considered as equals.
     *
     * @param chars the string to be compared
     * @return true if specified string equals to current
     */
    private boolean compareChars(byte[] chars, int pos) {
        for (int i = 0; i < len; i++) {
            if (differentChars((char) this.chars[i + this.pos], (char) chars[i + pos])) return false;
        }
        return true;
    }

    /**
     * Compares two chars.
     *
     * @param c1 the first char
     * @param c2 the second char
     * @return tru if first and second chars are different.
     */
    private boolean differentChars(char c1, char c2) {
        if (65 <= c1 && c1 < 97) {
            c1 +=32;
        }

        if (65 <= c2 && c2 < 97) {
            c2 +=32;
        }
        return c1 != c2;
    }

    @Override
    public String toString() {
        return new String(chars, pos, len);
    }

    /**
     * Converts string value to integer
     * @return ineger value
     */
    public int toInteger() {
        int res = 0;
        for (int i = 0; i < len; i++) {
            res += digit(pos + i) * pow(len - i - 1);
        }
        return res;
    }

    /**
     * 10^a
     * 
     * @param a the power
     * @return the value of 10^a
     */
    private int pow(int a) {
        int res = 1;
        for (int i = 0; i < a; i++) {
            res *= 10;
        }
        return res;
    }

    /**
     * Converts character to digit.
     *
     * @param pos the position of the character
     * @return the digit value
     */
    private int digit(int pos) {
        return chars[pos] - 48;
    }
}
