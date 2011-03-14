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

package org.mobicents.media.server.scheduler;

/**
 * Schedulable task.
 * 
 * @author kulikov
 */
public abstract class Task {
    private volatile long deadline;
    protected Scheduler scheduler;

    private volatile boolean isActive = true;

    public Task(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Scheduler scheduler() {
        return this.scheduler;
    }
    
    /**
     * Current priority of this task.
     * 
     * @return the value of priority
     */
    public abstract long getPriority();
    
    /**
     * Dead line on absolute clock when this task must be executed
     * 
     * @return the value in nanoseconds.
     */
    public long getDeadLine() {
        return deadline;
    }
    
    /**
     * Modifies dead line of the task.
     * 
     * @param d the new dead line value.
     */
    public void setDeadLine(long d) {
        this.deadline = d;
    }
    
    /**
     * Worst execution time of this task.
     *  
     * @return duration expressed in nanoseconds.
     */
    public abstract long getDuration();

    /**
     * Executes task.
     * 
     * @return dead line of next execution
     */
    public abstract long perform();

    /**
     * Cancels task execution
     */
    public synchronized void cancel() {
        this.isActive = false;
        scheduler.taskQueue.remove(this);
    }

    protected synchronized void run() {
        if (this.isActive)  perform();
    }

    protected synchronized void activate() {
        this.isActive = true;
    }
}
