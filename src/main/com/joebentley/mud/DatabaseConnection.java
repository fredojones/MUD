/*
 * Copyright (c) 2015
 *
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *
 */

package main.com.joebentley.mud;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class DatabaseConnection {
    protected static JedisPool pool = new JedisPool("localhost");

    protected Jedis connection;

    public DatabaseConnection() {
        connection = pool.getResource();
    }

    public Jedis getConnection() {
        return connection;
    }

    /**
     * Get the value at string key if it exists, or else set it to orElse
     *
     * @param key    key to find the value of
     * @param orElse value to set key to if it doesn't exist
     */
    public synchronized String getOrElse(String key, String orElse) {
        if (!connection.exists(key)) {
            connection.set(key, orElse);
        }

        return connection.get(key);
    }
}
