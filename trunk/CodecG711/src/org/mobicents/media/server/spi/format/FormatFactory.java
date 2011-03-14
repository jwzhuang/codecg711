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

import org.mobicents.media.server.spi.format.audio.DTMFFormat;
import org.mobicents.media.server.utils.Text;

/**
 * Constructs format descriptor object.
 * 
 * @author kulikov
 */
public class FormatFactory {
    private static Text DTMF = new Text("telephone-event");

    /**
     * Creates new audio format descriptor.
     *
     * @param name the encoding name.
     */
    public static AudioFormat createAudioFormat(EncodingName name) {
        //check name and create specific
        if (name.equals(DTMF)) {
            return new DTMFFormat();
        }

        //default format
        return new AudioFormat(name);
    }

    /**
     * Creates new format descriptor
     *
     * @param name the encoding
     * @param sampleRate sample rate value in Hertz
     * @param sampleSize sample size in bits
     * @param channels number of channels
     */
    public static AudioFormat createAudioFormat(EncodingName name, int sampleRate, int sampleSize, int channels) {
        AudioFormat fmt = createAudioFormat(name);
        fmt.setSampleRate(sampleRate);
        fmt.setSampleSize(sampleSize);
        fmt.setChannels(channels);
        return fmt;
    }

    /**
     * Creates new format descriptor
     *
     * @param name the encoding
     * @param sampleRate sample rate value in Hertz
     * @param sampleSize sample size in bits
     * @param channels number of channels
     */
    public static AudioFormat createAudioFormat(String name, int sampleRate, int sampleSize, int channels) {
        AudioFormat fmt = createAudioFormat(new EncodingName(name));
        fmt.setSampleRate(sampleRate);
        fmt.setSampleSize(sampleSize);
        fmt.setChannels(channels);
        return fmt;
    }

    /**
     * Creates new format descriptor
     *
     * @param name the encoding
     * @param sampleRate sample rate value in Hertz
     */
    public static AudioFormat createAudioFormat(String name, int sampleRate) {
        AudioFormat fmt = createAudioFormat(new EncodingName(name));
        fmt.setSampleRate(sampleRate);
        return fmt;
    }

    /**
     * Creates new format descriptor.
     *
     * @param name format encoding name.
     * @param  frameRate the number of frames per second.
     */
    public static VideoFormat createVideoFormat(EncodingName name, int frameRate) {
        //TODO : implement specific format here
        return new VideoFormat(name, frameRate);
    }

    /**
     * Creates new format descriptor.
     *
     * @param name format encoding name.
     */
    public static VideoFormat createVideoFormat(EncodingName name) {
        return new VideoFormat(name);
    }

    /**
     * Creates new format descriptor.
     *
     * @param name format encoding name.
     */
    public static VideoFormat createVideoFormat(String name) {
        return new VideoFormat(name);
    }

    /**
     * Creates new format descriptor.
     *
     * @param name format encoding name.
     * @param  frameRate the number of frames per second.
     */
    public static VideoFormat createVideoFormat(String name, int frameRate) {
        return new VideoFormat(name, frameRate);
    }

}
