/*
 * Copyright (c) 2015 Patrick Anker and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.patrickanker.chatterbox.event;

import com.patrickanker.chatterbox.util.Debugger;
import java.util.HashMap;
import java.util.LinkedList;
import org.jdeferred.FailCallback;
import org.jdeferred.impl.DefaultDeferredManager;
import org.jdeferred.multiple.OneReject;

public class EventCenter {
  
  private static EventCenter defaultCenter;
  private final HashMap<String, LinkedList<Runnable>> eventMap = new HashMap<>();
  
  public static EventCenter defaultCenter() {
    if (defaultCenter == null) {
      defaultCenter = new EventCenter();
    }
    
    return defaultCenter;
  }
  
  public boolean on(String event, Runnable r) {
    if ((event == null || event.equals("")) || r == null) {
      Debugger.debug("Attempted to add a null event handler");
      return false;
    }
    
    if (eventMap.containsKey(event)) {
      if (!eventMap.get(event).contains(r)) {
        LinkedList<Runnable> l = eventMap.get(event);
        l.add(r);
        eventMap.remove(event);
        eventMap.put(event, l);
        return true;
      } else {
        Debugger.debug("Attempted to add duplicate entry in event map");
        return false;
      }
    } else {
      LinkedList<Runnable> l = new LinkedList<>();
      l.add(r);
      eventMap.put(event, l);
      return true;
    }
  }
  
  public void call(String event) {
    if (event == null || event.equals("")) {
      Debugger.debug("Cannot call a null event");
      return;
    }
    
    LinkedList<Runnable> l = eventMap.get(event);
    assert l != null;
    
    Runnable[] selectors = new Runnable[l.size()];
    selectors = l.toArray(selectors);
    
    (new DefaultDeferredManager()).when(selectors).fail(new FailCallback<OneReject>() {

      @Override
      public void onFail(OneReject result) {
        Throwable t = (Throwable) result.getReject();
        Debugger.debug("Error running event: " + t.getMessage());
      }
    });
  }
}