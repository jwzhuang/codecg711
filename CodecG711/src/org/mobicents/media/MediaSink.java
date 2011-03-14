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

package org.mobicents.media;

import org.mobicents.media.server.scheduler.Scheduler;
import org.mobicents.media.server.spi.format.Formats;
import org.mobicents.media.server.spi.io.Pipe;


/**
 * Implements the media consumer.
 * 
 * @author Oleg Kulikov\
 * @author baranowb
 */
public interface MediaSink extends Component {

    /**
     * Starts media processing.
     */
    public void start();
    
    /**
     * Terminates media processing.
     */
    public void stop();
    
    
    /**
     * Get possible formats which this consumer can handle.
     * 
     * @return an array of Format objects.
     */
    public Formats getFormats();
    
    /**
     * Joins this media sink with media source.
     * The concrete media sink can allow to join with multiple sources
     * 
     * @param source the media source to join with.
     */
    public void connect(Pipe pipe);
    
    /**
     * Breaks connection with media source.
     * The concrete media sink can allow to join with multiple sources so
     * this method requires the explicit source for disconnection.
     * 
     * @param source the source to disconnect from.
     */
    public void disconnect(Pipe pipe);
    
    /**
     * Gets the state of the component.
     * 
     * @return  true if component is connected to other component.
     */
    public boolean isConnected();
    
    /**
     * Gets true if component is able to receive media.
     * 
     * @return true if component is able to receive media.
     */
    public boolean isStarted();
    
    /**
     * Shows the number of packets received by this medis sink since last start.
     * 
     * @return the number of packets.
     */
    public long getPacketsReceived();
    
    /**
     * Shows the number of bytes received by this sink since last start;
     * 
     * @return the number of bytes.
     */
    public long getBytesReceived();
    
	
}
