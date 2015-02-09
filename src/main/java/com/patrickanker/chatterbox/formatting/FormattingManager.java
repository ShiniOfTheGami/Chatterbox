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

package com.patrickanker.chatterbox.formatting;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.patrickanker.chatterbox.messenger.Messenger;
import com.patrickanker.chatterbox.util.Debugger;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class FormattingManager {
  
  private static FormattingManager defaultManager;
  
  protected final Multimap<String, Formatter> activeFormatters = LinkedListMultimap.create();
  
  public static FormattingManager defaultManager() {
    if (defaultManager == null) {
      defaultManager = new FormattingManager();
    }
    
    return defaultManager;
  }
  
  public void on(String event, Class<? extends Formatter> clazz) {
    if (clazz == null || (event == null || event.equals(""))) {
      Debugger.debug("Attempted to insert null Formatter");
      return;
    }
    
    Formatter format = null;
    
    try {
      format = (Formatter) clazz.newInstance();
      
      if (activeFormatters.containsEntry(event, format)) {
        Debugger.debug("Attempted to add duplicate Formatters");
      } else {
        activeFormatters.put(event, format);
      }
    } catch (Throwable t) {
      Debugger.debug("Error instancing Formatter: " + t.getMessage());
    } finally {
      if (format == null) {
        Debugger.debug("Formatter could not register; still null");
      } else {
        Debugger.debug("Successfully registered Formatter: " + clazz.getCanonicalName());
      }
    }
  }
  
  // As opposed to the event system, this bit is semi-synchronous
  public Future<String> call(String event, String message, Messenger messenger, String targetUUID) {
    ExecutorService executor = Executors.newCachedThreadPool();
    
    return ((Future<String>) executor.submit(new FormattingEventCallable(event, message, messenger, targetUUID)));
  }
  
  class FormattingEventCallable implements Callable {

    private final String event;
    private final String message;
    private final Messenger messenger;
    private final String targetUUID;   // The UUID of the target channel
    
    public FormattingEventCallable(String e, String msg, Messenger msngr, String trgtUUID) {
      event      = e;
      message    = msg;
      messenger  = msngr;
      targetUUID = trgtUUID;
    }
    
    @Override
    public String call() {
      String copy = message;
      
      for (Formatter formatter : activeFormatters.get(event)) {
        copy = formatter.format(message, messenger, targetUUID);
      }
      
      return copy;
    }
  }
}
