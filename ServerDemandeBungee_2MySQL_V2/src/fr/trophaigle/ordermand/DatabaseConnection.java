package fr.trophaigle.ordermand;

import java.util.logging.Level;
import java.util.logging.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class DatabaseConnection {

    private final Ordermand instance;
    public Thread reconnection;
    private JedisPool jedisPool;

    public DatabaseConnection(Ordermand instance)
    {
        this.instance = instance;
        this.connect();

        this.reconnection = new Thread(() -> {
            while (true)
            {
                try
                {
                    try
                    {
                        jedisPool.getResource().close();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        instance.getLogger().severe("Error redis connection, Try to reconnect!");
                        connect();
                    }
                    Thread.sleep(10 * 1000);
                } catch (Exception e)
                {
                    break;
                }
            }
        }, "Redis reconnect");
        reconnection.start();
    }

    public void connect()
    {
        this.instance.logInfo("Connecting to database...");

        JedisPoolConfig jedisConfiguration = new JedisPoolConfig();
        jedisConfiguration.setMaxTotal(-1);
        jedisConfiguration.setJmxEnabled(false);

        Logger logger = Logger.getLogger(JedisPool.class.getName());
        logger.setLevel(Level.OFF);

        this.jedisPool = new JedisPool(jedisConfiguration, "127.0.0.1", 0000, 0, "password");
        try
        {
            this.jedisPool.getResource().close();
        } catch (Exception e)
        {
            this.instance.logInfo("Can't connect to the database!");
            System.exit(8);
        }

        this.instance.logInfo("Connected to database.");
    }

    public void disconnect()
    {
        reconnection.interrupt();
    }

    public JedisPool getJedisPool()
    {
        return this.jedisPool;
    }

    public Jedis getResource()
    {
        return jedisPool.getResource();
    }
	
}
