package com.jumkid.like.graphql;

import com.jumkid.like.dao.LikeDao;
import com.jumkid.like.model.Like;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LikeMutationDataFetcher {

    private final LikeDao likeDao;

    @Autowired
    public LikeMutationDataFetcher(LikeDao likeDao) {
        this.likeDao = likeDao;
    }

    /**
     * Add count of like for the key for client id, entity name and entity id
     *
     * @return data fetcher function
     */
    public DataFetcher<Like> addLike() {
        return dataFetchingEnvironment -> {
            String clientId = dataFetchingEnvironment.getArgument(Like.Fields.CLIENT_ID.value());
            String entityName = dataFetchingEnvironment.getArgument(Like.Fields.ENTITY_NAME.value());
            String entityId = dataFetchingEnvironment.getArgument(Like.Fields.ENTITY_ID.value());

            long value = likeDao.increase(clientId, entityName, entityId);

            return new Like(clientId, entityName, entityId, value);
        };
    }

    /**
     * Subtract count of like for the key for client id, entity name and entity id
     *
     * @return data fetcher function
     */
    public DataFetcher<Like> subtractLike() {
        return dataFetchingEnvironment -> {
            String clientId = dataFetchingEnvironment.getArgument(Like.Fields.CLIENT_ID.value());
            String entityName = dataFetchingEnvironment.getArgument(Like.Fields.ENTITY_NAME.value());
            String entityId = dataFetchingEnvironment.getArgument(Like.Fields.ENTITY_ID.value());

            long value = likeDao.decrease(clientId, entityName, entityId);

            return new Like(clientId, entityName, entityId, value);
        };
    }

    public DataFetcher<Like> removeLike() {
        return dataFetchingEnvironment -> {
            String clientId = dataFetchingEnvironment.getArgument(Like.Fields.CLIENT_ID.value());
            String entityName = dataFetchingEnvironment.getArgument(Like.Fields.ENTITY_NAME.value());
            String entityId = dataFetchingEnvironment.getArgument(Like.Fields.ENTITY_ID.value());

            long value = likeDao.removeLike(clientId, entityName, entityId);

            return new Like(clientId, entityName, entityId, -1L);
        };
    }

}
