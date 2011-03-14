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

import java.io.IOException;



/**
 *
 * @author Oleg Kulikov
 * @author amit bhayani
 */
public interface Connection {
    /**
     * Gets the identifier of this connection.
     *
     * @return hex view of the integer.
     */
    public String getId();
    
    /**
     * Returns state of this connection
     * @return
     */
    public ConnectionState getState();

    /**
     * Gets the current mode of this connection.
     *
     * @return integer constant indicating mode.
     */
    public ConnectionMode getMode(MediaType mediaType);

    /**
     * Modify mode of this connection.
     *
     * @param mode the new value of the mode.
     */
    public void setMode(ConnectionMode mode, MediaType mediaType) throws ModeNotSupportedException;
    
    /**
     * Modify mode of this connection for all known media types.
     * 
     * @param mode the new mode of the connection.
     */
    public void setMode(ConnectionMode mode) throws ModeNotSupportedException;
    
    /**
     * Gets the endpoint which executes this connection.
     *
     * @return the endpoint object.
     */
    public Endpoint getEndpoint();

    /**
     * Gets the descriptor of this connection in SDP format.
     * 
     * @return SDP descriptor as text string.
     */
    public String getDescriptor();

    /**
     * Joins endpoint wich executes this connection with other party.
     *
     * @param other the connection executed by other party endpoint.
     * @throws IOException
     */
    public void setOtherParty(Connection other) throws IOException;

    /**
     * Joins endpoint wich executes this connection with other party.
     *
     * @param descriptor the SDP descriptor of the other party.
     * @throws IOException
     */
    public void setOtherParty(byte[] descriptor) throws IOException;

    /**
     * Adds connection state listener.
     * 
     * @param listener to be registered
     */
    public void addListener(ConnectionListener listener);

    /**
     * Removes connection state listener.
     * 
     * @param listener to be unregistered
     */
    public void removeListener(ConnectionListener listener);

    /**
     * The number of packets of the specified media type received .
     * 
     * @param media the media type.
     * @return the number of packets.
     */
    public long getPacketsReceived(MediaType media);
    
    /**
     * The number of bytes of the specified media type received .
     * 
     * @param media the media type.
     * @return the number of bytes.
     */
    public long getBytesReceived(MediaType media);
    
    /**
     * The total number of bytes  received .
     * 
     * @return the number of bytes.
     */
    public long getBytesReceived();
    
    /**
     * The number of packets of the specified media type transmitted.
     * 
     * @param media the media type 
     * @return the number of packets.
     */
    public long getPacketsTransmitted(MediaType media);

    /**
     * The number of bytes of the specified media type transmitted.
     * 
     * @param media the media type 
     * @return the number of bytes.
     */
    public long getBytesTransmitted(MediaType media);
    
    
    /**
     * The total number of bytes transmitted.
     * 
     * @return the number of bytes.
     */
    public long getBytesTransmitted();
    
    /**
     * The interarrival jitter for the specific media type.
     * 
     * @param media the media type
     * @return jitter value
     */
    public double getJitter(MediaType media);
    
    /**
     * The average jitter value accross all media types.
     * 
     * @return average jitter value. 
     */
    public double getJitter();
    
}
