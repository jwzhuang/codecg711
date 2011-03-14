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
 * @author amit bhayani
 */
public enum ConnectionState {

    NULL(0, "NULL", -1),
    HALF_OPEN(1, "HALF_OPEN", 5),
    OPEN(2, "OPEN", 30 * 60);

    private int code;
    private String stateName;
    private long timeout;

    private ConnectionState(int code, String stateName, long timeout) {
        this.stateName = stateName;
        this.code = code;
        this.timeout = timeout;
    }
    
    public static ConnectionState getInstance(String name) {
        if (name.equalsIgnoreCase("NULL")) {
            return NULL;
        } else if(name.equalsIgnoreCase("HALF_OPEN")){
            return HALF_OPEN;
        } else if(name.equalsIgnoreCase("OPEN")){
            return OPEN;
        } else{
            throw new IllegalArgumentException("There is no media type for: "+name);
        }
    }    
    
    @Override
    public String toString() {
        return stateName;
    }
    
    public int getCode() {
        return code;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
