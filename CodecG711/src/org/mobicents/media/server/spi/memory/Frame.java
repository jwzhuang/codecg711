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

package org.mobicents.media.server.spi.memory;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import org.mobicents.media.server.spi.format.Format;

/**
 *
 * @author kulikov
 */
public class Frame implements Delayed {
    private Partition partition;
    private byte[] data;

    private volatile int offset;
    private volatile int length;

    private volatile long timestamp;
    private volatile long duration = Long.MAX_VALUE;
    private volatile long sn;

    private volatile boolean eom;
    private volatile Format format;

    public Frame(Partition partition, byte[] data) {
        this.partition = partition;
        this.data = data;
    }

    protected void reset() {
        this.timestamp = 0;
        this.duration = 0;
        this.sn = 0;
        this.eom = false;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDuration() {
        return duration;
    }
    
    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSequenceNumber(){
        return sn;
    }

    public void setSequenceNumber(long sn) {
        this.sn = sn;
    }

    public boolean isEOM() {
        return this.eom;
    }

    public void setEOM(boolean value) {
        this.eom = value;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
    
    public long getDelay(TimeUnit unit) {
        return unit.convert(timestamp + duration +60000000L- Memory.clock.getTime(), TimeUnit.NANOSECONDS);
    }

    public int compareTo(Delayed o) {
        if (this.getDelay(TimeUnit.NANOSECONDS) < o.getDelay(TimeUnit.NANOSECONDS)) {
            return -1;
        }
        if (this.getDelay(TimeUnit.NANOSECONDS) > o.getDelay(TimeUnit.NANOSECONDS)) {
            return 1;
        }
        return 0;
    }

    public void recycle() {
        partition.recycle(this);
    }

    @Override
    public Frame clone() {
        Frame frame = Memory.allocate(data.length);
        System.arraycopy(data, offset, frame.data, offset, length);
        frame.offset = offset;
        frame.length = length;
        frame.duration = duration;
        frame.sn = sn;
        frame.eom = eom;
        frame.format = format;
        frame.timestamp = timestamp;
        return frame;
    }
}
