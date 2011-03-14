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
 * Implemnets immutable task executor
 *
 * @author kulikov
 */
public final class TaskExecutor {
    private TaskQueue queue;
    protected Task task;
    protected long priority;
    
    protected boolean isActive = true;

    protected TaskExecutor(TaskQueue queue) {
        this.queue = queue;
    }

    public void cancel() {
        synchronized(task) {
            this.isActive = false;
            //queue.remove(this);
        }
    }

    public void perform() {
        synchronized(task) {
            if (task != null && this.isActive) task.perform();
        }
    }

}
