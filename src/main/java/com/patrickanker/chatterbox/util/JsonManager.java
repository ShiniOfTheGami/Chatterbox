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

package com.patrickanker.chatterbox.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class JsonManager {
  
  public static final Map<String, Object> loadJSON(final File file) throws FileNotFoundException {
    if (!file.exists())
      throw new FileNotFoundException();
    
    final BufferedReader in = new BufferedReader(new FileReader(file), (8*1024));
    final Gson gson = new Gson();
    
    Map<String, Object> map = new HashMap<>();
    map = gson.fromJson(in, map.getClass());
    return map;
  }
  
  public static final void saveJSON(Map<String, Object> map, File file) throws FileNotFoundException, IOException {
    if (!file.exists())
      throw new FileNotFoundException();
    
    final Gson gson = new Gson();
    final JsonWriter out = new JsonWriter(new FileWriter(file));
    
    Type type = new TypeToken<Map<String, Object>>(){}.getType();
    gson.toJson(map, type, out);
  }
  
}
