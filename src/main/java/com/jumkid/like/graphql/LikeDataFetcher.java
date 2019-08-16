package com.jumkid.like.graphql;

import com.jumkid.like.dao.LikeDao;
import com.jumkid.like.model.Like;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LikeDataFetcher {

    private final LikeDao likeDao;

    @Autowired
    public LikeDataFetcher(LikeDao likeDao) {
        this.likeDao = likeDao;
    }

    public DataFetcher<Like> getLike(){
        return dataFetchingEnvironment -> {
            String clientId = dataFetchingEnvironment.getArgument(Like.Fields.CLIENT_ID.value());
            String entityName = dataFetchingEnvironment.getArgument(Like.Fields.ENTITY_NAME.value());
            String entityId = dataFetchingEnvironment.getArgument(Like.Fields.ENTITY_ID.value());

            Optional<Like> opt = likeDao.get(clientId, entityName, entityId);
            if(opt.isPresent()) {
                return opt.get();
            } else {
                return new Like(clientId, entityName, entityId, null);
            }
        };
    }

}
