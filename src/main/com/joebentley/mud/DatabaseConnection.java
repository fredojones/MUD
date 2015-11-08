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
    public String getOrElse(String key, String orElse) {
        if (!connection.exists(key)) {
            connection.set(key, orElse);
        }

        return connection.get(key);
    }
}
