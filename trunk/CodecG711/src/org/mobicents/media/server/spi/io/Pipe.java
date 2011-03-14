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

package org.mobicents.media.server.spi.io;

import org.mobicents.media.MediaSink;
import org.mobicents.media.MediaSource;

/**
 * Defines the interface for unidirectional media transfer between source and sink.
 *
 * @author kulikov
 */
public interface Pipe {
    public final static int INPUT = 1;
    public final static int OUTPUT = 2;

    /**
     * Connects source to this pipe.
     * @param source
     */
    public void connect(MediaSource source);
    /**
     * Connects sinks to this pipe.
     * @param sink
     */
    public void connect(MediaSink sink);
    
    /**
     * Disconnects pipe.
     * @param source the termonation for disconnect.
     * <code>Pipe.INPUT</code> disconnects source from this pipe if connected.
     * <code>Pipe.OUTPUT</code> disconnects sink from this pipe if connected.
     */
    public void disconnect(int termination);

    /**
     * Disconnects source and sink.
     */
    public void disconnect();

    /**
     * Starts transmission from source to sink if both are assigned.
     * This method is equivalent to
     * <code>
     * source.start()
     * sink.start();
     * </code>
     */
    public void start();

    /**
     * Terminates transmission from source to sink if both are assigned.
     * This method is equivalent to
     * <code>
     * source.stop()
     * sink.stop();
     * </code>
     */
    public void stop();
}
