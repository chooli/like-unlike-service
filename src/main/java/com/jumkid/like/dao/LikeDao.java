package com.jumkid.like.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.jumkid.like.util.Constants.*;

@Repository
public class LikeDao {

    private static Logger logger = LoggerFactory.getLogger(LikeDao.class);

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public LikeDao(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Increase the value for the like map by given client id, entity name and entity unique id
     * If the like map does not exist, create one with initial value 1
     *
     * @param clientId
     * @param entityName
     * @param entityId
     * @return long value
     */
    public long increase(String clientId, String entityName, String entityId){
        var key = buildKey(clientId, entityName);

        if(redisTemplate.opsForHash().hasKey(key, entityId)) {
            long val = redisTemplate.opsForHash().increment(key, entityId, 1);
            logger.debug("increased {} {} {}", key, " ", entityId);
            return val;
        } else {
            redisTemplate.opsForHash().put(key, entityId, 1);
            logger.debug("created {} {} {}", key, " ", entityId);
            return 1;
        }
    }

    /**
     * Decrease the value for the like map by given client id, entity name and entity unique id
     * If the like map does not exist, create one with initial value 0
     *
     * @param clientId
     * @param entityName
     * @param entityId
     * @return long value
     */
    public long decrease(String clientId, String entityName, String entityId){
        var key = buildKey(clientId, entityName);

        if(redisTemplate.opsForHash().hasKey(key, entityId)) {
            long val = redisTemplate.opsForHash().increment(key, entityId, -1);
            logger.debug("increased {} {} {}", key, " ", entityId);
            return val;
        } else {
            redisTemplate.opsForHash().put(key, entityId, 0);
            logger.debug("created {} {} {}", key, " ", entityId);
            return 0;
        }
    }

    /**
     * Get the like map given client id, entity name and entity unique id
     *
     * @param clientId
     * @param entityName
     * @param entityId
     * @return Optional of long value
     */
    public Optional<Long> get(String clientId, String entityName, String entityId) {
        var key = buildKey(clientId, entityName);

        return Optional.of((Long)redisTemplate.opsForHash().get(key, entityId));
    }

    private String buildKey(String clientId, String entityName){
        return NS_LIKE + NS_SEPARATOR + clientId + NS_SEPARATOR + entityName ;
    }

}
