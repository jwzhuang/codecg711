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
package org.mobicents.media.server.spi;

/**
 *
 * @author kulikov
 */
public enum MediaType {
    AUDIO(0, "audio", 0x01), VIDEO(1, "video", 0x02);
    
    private int code;
    private String name;
    private int mask;

    MediaType(int code, String name, int mask) {
        this.code = code;
        this.name = name;
        this.mask = mask;
    }
    
    public static MediaType getInstance(String name) {
        if (name.equalsIgnoreCase("audio")) {
            return AUDIO;
        } else if(name.equalsIgnoreCase("video")){
            return VIDEO;
        }else
        {
        	throw new IllegalArgumentException("There is no media type for: "+name);
        }
    }

    public static MediaType getMediaType(int code) {
        if (code == 0) {
            return AUDIO;
        } else {
            return VIDEO;
        }
    }
    
    public int getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public int getMask(){
    	return mask;
    }
    
}
