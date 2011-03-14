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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Implements queue of tasks.
 * 
 * 
 * @author kulikov
 */
public class TaskQueue {
    private final static int QUEUE_SIZE = 1500;

    //inner holder for tasks
    private List<Task> list;

    //priority comparator
    private Comparator comparator = new PriorityComparator();

    public TaskQueue() {
        //intitalize task list
        list = new ArrayList(QUEUE_SIZE);
    }

    public Collection<Task> getTasks() {
        return list;
    }

    /**
     * Shows if this queue is empty.
     * 
     * @return true if queue is currently empty
     */
    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }
    
    /**
     * Queues specified task using tasks dead line time.
     * 
     * @param task the task to be queued.
     * @return TaskExecutor for the scheduled task.
     */
    public synchronized void accept(Task task) {
        //the smaller priority value means that task will reside
        //closer to the head of queue.

        //initialy check head and tail of queue and if task doesn't fit
        //search place in the middle of the queue using bisection method.
        if (list.isEmpty()) {
            list.add(task);
            return;
        }

        //check that task is in the middle of the queue
        if (comparator.compare(task, list.get(0)) <= 0) {
            if (comparator.compare(task, list.get(0)) < 0) {
                list.add(0, task);
            } else {
                list.add(1, task);
            }
            return;
        }

        if (comparator.compare(task, list.get(list.size() - 1)) >= 0) {
            list.add(task);
            return;
        }

        //the task should be somewhere in the middle
        int N = list.size();
        int m = N / 2;

        int l = 1;
        int r = N - 1;

        while ((r - l) > 1) {
            if (comparator.compare(list.get(m), task) < 0) {
                l = m;
                m += (r - l) / 2;
            } else {
                r = m;
                m -= (r - l) / 2;
            }
        }

        if (comparator.compare(list.get(r), task) <= 0) {
            list.add(r + 1, task);
        } else if (comparator.compare(list.get(l), task) <= 0) {
            list.add(l + 1, task);
        } else {
            list.add(l, task);
        }

        return;
    }
    
    /**
     * Retrieves the task with earliest dead line and removes it from queue.
     * 
     * @return task which has earliest dead line
     */
    public synchronized Task poll() {
        return list.isEmpty() ? null : list.remove(0);
    }

    /**
     * Retrieves but do not remove earliest dead line task.
     * 
     * @return task.
     */
    public synchronized Task peek() {
        return list.get(0);
    }

    public Object getMonitor() {
        return list.isEmpty() ? new Integer(0) : list.get(0);
    }
    
    /**
     * Clean the queue.
     */
    public synchronized void clear() {
        list.clear();
    }
    
    /**
     * Gets the size of this queue.
     * 
     * @return the size of the queue.
     */
    public int size() {
        return list.size();
    }

    protected synchronized void remove(Task task) {
        list.remove(task);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Queue[");
        
        int len = Math.min(30, list.size());
        for (int i = 0; i < len -1; i++) {
            sb.append(list.get(i).getPriority());
            sb.append(",");
        }
        
        sb.append(list.get(len - 1).getPriority());
        sb.append("]");
        return sb.toString();
    }
}
