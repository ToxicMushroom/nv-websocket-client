/*
 * Copyright (C) 2015-2018 Neo Visionaries Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neovisionaries.ws.client;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;


abstract class PeriodicalFrameSender
{
    private final WebSocket mWebSocket;
    private String mTimerName;
    private Timer mTimer;
    private boolean mScheduled;
    private long mInterval;
    private PayloadGenerator mGenerator;

    private ReentrantLock reentrantLock = new ReentrantLock();


    public PeriodicalFrameSender(
            WebSocket webSocket, String timerName, PayloadGenerator generator)
    {
        mWebSocket = webSocket;
        mTimerName = timerName;
        mGenerator = generator;
    }


    public void start()
    {
        setInterval(getInterval());
    }


    public void stop()
    {
        reentrantLock.lock();
        try {
            if (mTimer == null)
            {
                return;
            }

            mScheduled = false;
            mTimer.cancel();
        } finally {
            reentrantLock.unlock();
        }
    }


    public long getInterval()
    {
        reentrantLock.lock();
        try {
            return mInterval;
        } finally {
            reentrantLock.unlock();
        }
    }


    public void setInterval(long interval)
    {
        if (interval < 0)
        {
            interval = 0;
        }

        reentrantLock.lock();
        try {
            mInterval = interval;
        } finally {
            reentrantLock.unlock();
        }

        if (interval == 0)
        {
            return;
        }

        if (mWebSocket.isOpen() == false)
        {
            return;
        }

        reentrantLock.lock();
        try {
            if (mTimer == null) {
                if (mTimerName == null) {
                    mTimer = new Timer();
                } else {
                    mTimer = new Timer(mTimerName);
                }
            }

            if (mScheduled == false) {
                mScheduled = schedule(mTimer, new Task(), interval);
            }
        } finally {
            reentrantLock.unlock();
        }
    }


    public PayloadGenerator getPayloadGenerator()
    {
        reentrantLock.lock();
        try {
            return mGenerator;
        } finally {
            reentrantLock.unlock();
        }
    }


    public void setPayloadGenerator(PayloadGenerator generator)
    {
        reentrantLock.lock();
        try {
            mGenerator = generator;
        } finally {
            reentrantLock.unlock();
        }
    }


    public String getTimerName()
    {
        return mTimerName;
    }


    public void setTimerName(String timerName)
    {
        reentrantLock.lock();
        try {
            mTimerName = timerName;
        } finally {
            reentrantLock.unlock();
        }
    }


    private final class Task extends TimerTask
    {
        @Override
        public void run()
        {
            doTask();
        }
    }


    private void doTask()
    {
        reentrantLock.lock();
        try {
            if (mInterval == 0 || mWebSocket.isOpen() == false) {
                mScheduled = false;

                // Not schedule a new task.
                return;
            }

            // Create a frame and send it to the server.
            mWebSocket.sendFrame(createFrame());

            // Schedule a new task.
            mScheduled = schedule(mTimer, new Task(), mInterval);
        } finally {
            reentrantLock.unlock();
        }
    }


    private WebSocketFrame createFrame()
    {
        // Prepare payload of a frame.
        byte[] payload = generatePayload();

        // Let the subclass create a frame.
        return createFrame(payload);
    }


    private byte[] generatePayload()
    {
        if (mGenerator == null)
        {
            return null;
        }

        try
        {
            // Let the generator generate payload.
            return mGenerator.generate();
        }
        catch (Throwable t)
        {
            // Empty payload.
            return null;
        }
    }


    private static boolean schedule(Timer timer, Task task, long interval)
    {
        try
        {
            // Schedule the task.
            timer.schedule(task, interval);

            // Successfully scheduled the task.
            return true;
        }
        catch (RuntimeException e)
        {
            // Failed to schedule the task. Probably, the exception is
            // an IllegalStateException which is raised due to one of
            // the following reasons (according to the Javadoc):
            //
            //   (1) if task was already scheduled or cancelled,
            //   (2) timer was cancelled, or
            //   (3) timer thread terminated.
            //
            // Because a new task is created every time this method is
            // called and there is no code to call TimerTask.cancel(),
            // (1) cannot be a reason.
            //
            // In either case of (2) and (3), we don't have to retry to
            // schedule the task, because the timer that is expected to
            // host the task will stop or has stopped anyway.
            return false;
        }
    }


    protected abstract WebSocketFrame createFrame(byte[] payload);
}
