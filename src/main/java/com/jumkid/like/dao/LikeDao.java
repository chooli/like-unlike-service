package com.jumkid.like.dao;

import com.jumkid.like.model.Like;
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
     * @return integer value
     */
    public long increase(String clientId, String entityName, String entityId){
        var key = buildKey(clientId, entityName);

        if(redisTemplate.opsForHash().hasKey(key, entityId)) {
            Long val = (Long)redisTemplate.opsForHash().get(key, entityId) + 1;
            redisTemplate.opsForHash().put(key, entityId, Long.valueOf(val));
            logger.debug("increased {} {} {}", key, " ", entityId);
            return val;
        } else {
            redisTemplate.opsForHash().put(key, entityId, Long.valueOf(1L));
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
            Long val = (Long)redisTemplate.opsForHash().get(key, entityId);
            val = val > 0 ? val - 1 : val;
            redisTemplate.opsForHash().put(key, entityId, Long.valueOf(val));
            logger.debug("increased {} {} {}", key, " ", entityId);
            return val;
        } else {
            redisTemplate.opsForHash().put(key, entityId, Long.valueOf(0));
            logger.debug("created {} {} {}", key, " ", entityId);
            return 0;
        }
    }

    /**
     * Get the like counter map by given client id, entity name and entity unique id
     *
     * @param clientId
     * @param entityName
     * @param entityId
     * @return Optional of long value
     */
    public Optional<Like> get(String clientId, String entityName, String entityId) {
        var key = buildKey(clientId, entityName);
        logger.debug("get {} {} {}", key, " ", entityId);
        Long value = (Long)redisTemplate.opsForHash().get(key, entityId);
        return Optional.of(new Like(clientId, entityName, entityId, value));
    }

    private String buildKey(String clientId, String entityName){
        return NS_LIKE + NS_SEPARATOR + clientId + NS_SEPARATOR + entityName ;
    }

    /**
     * Get the like counter map by given client id, entity name and entity unique id
     *
     * @param clientId
     * @param entityName
     * @param entityId
     * @return
     */
    public long removeLike(String clientId, String entityName, String entityId) {
        var key = buildKey(clientId, entityName);
        logger.debug("remove {} {} {}", key, " ", entityId);
        return redisTemplate.opsForHash().delete(key, entityId);
    }

}
