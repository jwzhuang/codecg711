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

import org.mobicents.media.server.utils.Text;

/**
 * Media format descriptor.
 *
 * @author kulikov
 */
public class Format implements Cloneable {
    //encoding name
    private EncodingName name;

    //any specific options
    private Text options;

    /**
     * Creates new descriptor.
     *
     * @param name the encoding name
     */
    public Format(EncodingName name) {
        this.name = name;
    }

    
    /**
     * Gets the encoding name.
     *
     * @return the encoding name.
     */
    public EncodingName getName() {
        return name;
    }

    /**
     * Modifies encoding name.
     *
     * @param name new encoding name.
     */
    public void setName(EncodingName name) {
        this.name = name;
    }

    /**
     * Gets options
     *
     * @return options as text.
     */
    public Text getOptions() {
        return options;
    }

    /**
     * Modify options.
     *
     * @param options new options.
     */
    public void setOptions(Text options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    /**
     * Compares two format descriptors.
     *
     * @param fmt the another format descriptor
     * @return
     */
    public boolean matches(Format fmt) {
        return this.name.equals(fmt.name);
    }

}
