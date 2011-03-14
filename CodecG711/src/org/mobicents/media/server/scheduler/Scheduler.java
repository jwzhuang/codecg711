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

import java.util.concurrent.CountDownLatch;

/**
 * Implements scheduler with multi-level priority queue.
 *
 * This scheduler implementation follows to uniprocessor model with "super" thread.
 * The "super" thread includes IO bound thread and one or more CPU bound threads
 * with equal priorities.
 *
 * The actual priority is assigned to task instead of process and can be
 * changed dynamicaly at runtime using the initial priority level, feedback
 * and other parameters.
 *
 *
 * @author kulikov
 */
public class Scheduler  {
    //The clock for time measurement
    private Clock clock;

    //priority queue
    protected TaskQueue taskQueue = new TaskQueue();

    //CPU bound threads
    private CpuThread[] cpuThread;
    
    //flag indicating state of the scheduler
    private boolean isActive;

    //locks start and waits when all threads will start.
    private CountDownLatch latch;

    /** the amount of tasks missed their deadline */
    private volatile long missCount;

    /** the number of total tasks executed */
    private volatile long taskCount;

    /** The allowed time jitter */
    private long tolerance = 3000000L;

    //The most worst execution time detected
    private long wet;

    /**
     * Creates new instance of scheduler.
     */
    public Scheduler(int cpuNum) {
        latch = new CountDownLatch(cpuNum);
        
        cpuThread = new CpuThread[cpuNum];
        for (int i = 0; i < cpuThread.length; i++) {
            cpuThread[i] = new CpuThread(String.format("Scheduler[CPU-%s]", i));
        }
    }

    /**
     * Sets clock.
     *
     * @param clock the clock used for time measurement.
     */
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    /**
     * Gets the clock used by this scheduler.
     *
     * @return the clock object.
     */
    public Clock getClock() {
        return clock;
    }

    /**
     * Queues task for execution following to its priority.
     *
     * @param task the task to be executed
     * @param cpuID the CPU number on wich this task shell be executed
     */
    public void submit(Task task, int cpuID) {
        taskQueue.accept(task);        
        cpuThread[cpuID].wakeup();
    }

    /**
     * Queues task for execution according to its priority.
     *
     * @param task the task to be executed.
     */
    public void submit(Task task) {
        task.activate();
        taskQueue.accept(task);
    }

    /**
     * Starts scheduler.
     */
    public void start() {
        if (clock == null) {
            throw new IllegalStateException("Clock is not set");
        }

        this.isActive = true;
        for (int i = 0; i < cpuThread.length; i++) {
            cpuThread[i].start();
        }

        //wait when threads start
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
    }

    /**
     * Stops scheduler.
     */
    public void stop() {
        if (!this.isActive) {
            return;
        }

        latch = new CountDownLatch(cpuThread.length);
        this.isActive = false;

        for (int i = 0; i < cpuThread.length; i++) {
            cpuThread[i].shutdown();
        }

        //wait when threads stop
        try {
            latch.await();
        } catch (InterruptedException e) {
        }

        taskQueue.clear();
    }

    /**
     * Shows the miss rate.
     * 
     * @return the miss rate value;
     */
    public double getMissRate() {
        return taskCount > 0 ? (double)missCount/(double)taskCount : 0D;
    }

    public long getWorstExecutionTime() {
        return wet;
    }

    /**
     * Executor thread.
     */
    private class CpuThread extends Thread {
        private Task t;
        private volatile boolean active;

        private volatile boolean isRunning;

        public CpuThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            this.active = true;
            latch.countDown();
            while (active) {
                //park thread if nothing to do.
                //the will remaining at parking till wake up or
                //shutdown will be called
                if (taskQueue.isEmpty()) {
                    park();
                }

                //shutdown was called.
                //terminate it then completely.
                if (!active) {
                    break;
                }

                //wakeup was called.
                //switch lights on.
                this.isRunning = true;

                //load task with highest priority and execute it.
                t = taskQueue.poll();
                if (t == null) {
                    continue;
                }

                //System.out.println("This here spins couple of times per MILLISECOND " + System.currentTimeMillis());
                //to Vladimir:
                //see Spuri, M.: 1996b, "Holistic Analysis for Deadline Scheduled Real-Time Distributed
                //Systems". Technical Report RR-2873, INRIA, France
                
                try {
                    //update miss rate countor
                    long now = clock.getTime();

                    //increment task countor
                    taskCount++;

                    //check for missing dead line.
                    if (now - t.getDeadLine() > tolerance) {
                        missCount++;
                    }

                    //execute task
                    t.run();

                    //determine worst execution time
                    long duration = clock.getTime() - now;
                    if (duration > wet) {
                        wet = duration;
                    }
                } catch (Exception e) {
                }
            }

            latch.countDown();
        }

        /**
         * Parks thread.
         */
        private void park() {
            synchronized (this) {
                try {
                    if (active) {
                        this.isRunning = false;
                        wait();
                    }
                } catch (InterruptedException e) {
                }
            }
        }

        /**
         * Wakeups thread
         */
        private void wakeup() {
            synchronized(this) {
                notify();
            }
        }

        /**
         * Terminates thread.
         */
        private void shutdown() {
            this.active = false;
            this.wakeup();
        }
    }

}
