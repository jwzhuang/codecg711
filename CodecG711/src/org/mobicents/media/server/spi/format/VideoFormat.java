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

/**
 *
 * @author kulikov
 */
public class VideoFormat extends Format implements Cloneable {
    //number of frames per second
    private int frameRate;

    /**
     * Creates new format descriptor.
     *
     * @param name format encoding name.
     */
    protected VideoFormat(EncodingName name) {
        super(name);
    }

    /**
     * Creates new format descriptor.
     *
     * @param name format encoding name.
     * @param  frameRate the number of frames per second.
     */
    protected VideoFormat(EncodingName name, int frameRate) {
        super(name);
        this.frameRate = frameRate;
    }

    /**
     * Creates new format descriptor.
     *
     * @param name format encoding name.
     */
    protected VideoFormat(String name) {
        super(new EncodingName(name));
    }

    /**
     * Creates new format descriptor.
     *
     * @param name format encoding name.
     * @param  frameRate the number of frames per second.
     */
    protected VideoFormat(String name, int frameRate) {
        super(new EncodingName(name));
        this.frameRate = frameRate;
    }

    /**
     * Gets the frame rate.
     *
     * @return the number of frames per second.
     */
    public int getFrameRate() {
        return frameRate;
    }

    /**
     * Modifies frame rate.
     *
     * @param frameRate the new value in frames per second.
     */
    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    @Override
    protected VideoFormat clone() {
        return new VideoFormat(getName(), frameRate);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AudioFormat[");
        builder.append(getName().toString());

        builder.append(",");
        builder.append(frameRate);

        builder.append("]");
        return builder.toString();
    }

}
