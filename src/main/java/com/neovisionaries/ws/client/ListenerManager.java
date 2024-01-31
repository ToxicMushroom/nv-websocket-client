/*
 * Copyright (C) 2015-2016 Neo Visionaries Inc.
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


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;


class ListenerManager
{
    private final WebSocket mWebSocket;
    private final List<WebSocketListener> mListeners = new CopyOnWriteArrayList<>();

    public ListenerManager(WebSocket websocket)
    {
        mWebSocket = websocket;
    }


    public List<WebSocketListener> getListeners()
    {
        return mListeners;
    }


    public void addListener(WebSocketListener listener)
    {
        if (listener == null)
        {
            return;
        }

        mListeners.add(listener);
    }


    public void addListeners(List<WebSocketListener> listeners)
    {
        if (listeners == null)
        {
            return;
        }

        mListeners.addAll(listeners.stream().filter(Objects::nonNull).toList());
    }


    public void removeListener(WebSocketListener listener)
    {
        if (listener == null)
        {
            return;
        }

        mListeners.remove(listener);
    }


    public void removeListeners(List<WebSocketListener> listeners)
    {
        if (listeners == null)
        {
            return;
        }

        mListeners.removeAll(listeners.stream().filter(Objects::nonNull).toList());
    }


    public void clearListeners()
    {
        mListeners.clear();
    }

    public void callOnStateChanged(WebSocketState newState)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onStateChanged(mWebSocket, newState);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnConnected(Map<String, List<String>> headers)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onConnected(mWebSocket, headers);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnConnectError(WebSocketException cause)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onConnectError(mWebSocket, cause);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnDisconnected(
        WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
        boolean closedByServer)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onDisconnected(
                    mWebSocket, serverCloseFrame, clientCloseFrame, closedByServer);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnFrame(WebSocketFrame frame)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onFrame(mWebSocket, frame);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnContinuationFrame(WebSocketFrame frame)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onContinuationFrame(mWebSocket, frame);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnTextFrame(WebSocketFrame frame)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onTextFrame(mWebSocket, frame);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnBinaryFrame(WebSocketFrame frame)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onBinaryFrame(mWebSocket, frame);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnCloseFrame(WebSocketFrame frame)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onCloseFrame(mWebSocket, frame);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnPingFrame(WebSocketFrame frame)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onPingFrame(mWebSocket, frame);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnPongFrame(WebSocketFrame frame)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onPongFrame(mWebSocket, frame);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnTextMessage(String message)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onTextMessage(mWebSocket, message);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnTextMessage(byte[] data)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onTextMessage(mWebSocket, data);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnBinaryMessage(byte[] message)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onBinaryMessage(mWebSocket, message);
            }
            catch (Throwable t)
            {
                    callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnSendingFrame(WebSocketFrame frame)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onSendingFrame(mWebSocket, frame);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnFrameSent(WebSocketFrame frame)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onFrameSent(mWebSocket, frame);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnFrameUnsent(WebSocketFrame frame)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onFrameUnsent(mWebSocket, frame);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnThreadCreated(ThreadType threadType, Thread thread)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onThreadCreated(mWebSocket, threadType, thread);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnThreadStarted(ThreadType threadType, Thread thread)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onThreadStarted(mWebSocket, threadType, thread);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnThreadStopping(ThreadType threadType, Thread thread)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onThreadStopping(mWebSocket, threadType, thread);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnError(WebSocketException cause)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onError(mWebSocket, cause);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnFrameError(WebSocketException cause, WebSocketFrame frame)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onFrameError(mWebSocket, cause, frame);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnMessageError(WebSocketException cause, List<WebSocketFrame> frames)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onMessageError(mWebSocket, cause, frames);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnMessageDecompressionError(WebSocketException cause, byte[] compressed)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onMessageDecompressionError(mWebSocket, cause, compressed);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnTextMessageError(WebSocketException cause, byte[] data)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onTextMessageError(mWebSocket, cause, data);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnSendError(WebSocketException cause, WebSocketFrame frame)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onSendError(mWebSocket, cause, frame);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    public void callOnUnexpectedError(WebSocketException cause)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onUnexpectedError(mWebSocket, cause);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }


    private void callHandleCallbackError(WebSocketListener listener, Throwable cause)
    {
        try
        {
            listener.handleCallbackError(mWebSocket, cause);
        }
        catch (Throwable t)
        {
        }
    }


    public void callOnSendingHandshake(String requestLine, List<String[]> headers)
    {
        for (WebSocketListener listener : getListeners())
        {
            try
            {
                listener.onSendingHandshake(mWebSocket, requestLine, headers);
            }
            catch (Throwable t)
            {
                callHandleCallbackError(listener, t);
            }
        }
    }
}
