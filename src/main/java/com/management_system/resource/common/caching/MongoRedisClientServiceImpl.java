package com.management_system.resource.common.caching;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.entities.database.facility.Facility;
import com.management_system.resource.entities.database.ingredient.Category;
import com.management_system.resource.entities.database.ingredient.Ingredient;
import com.management_system.resource.entities.database.menu.Menu;
import com.management_system.resource.infrastucture.feign.redis.RedisServiceClient;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.core.redis.CachingProcessHandler;
import com.management_system.utilities.core.redis.RedisClientService;
import com.management_system.utilities.core.redis.RedisRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.database.DbEntity;
import com.management_system.utilities.entities.database.MongoDbEntity;
import com.management_system.utilities.entities.exceptions.DataNotFoundException;
import com.management_system.utilities.entities.exceptions.InvalidDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MongoRedisClientServiceImpl implements RedisClientService {
    final RedisServiceClient redisServiceClient;
    final MongoTemplate mongoTemplate;


    /**
     * Get data from Redis if it exist, or else get data from database and save it to Redis,
     * then trigger a logic before returning the result
     *
     * @param dbEntityClass  the table entity class we want to proceed
     * @param cachingProcessHandlers the logic we want to execute between returning the result and caching data to Redis
     * @param id         the id of data we want to get
     * @return Mongo database entity after query
     * @throws InvalidDataException  when TableName is invalid
     * @throws DataNotFoundException when data does not exist in database
     * @implNote Use this when you only need to get data from a single document only from MongoDB
     */
    @Override
    public MongoDbEntity getAndCacheDataFromOneTable(Class<? extends DbEntity> dbEntityClass, String id, List<Class<? extends CachingProcessHandler>> cachingProcessHandlers) {
        ApiResponse redisRes;
        ObjectMapper objectMapper = new ObjectMapper();

        TableName tableName = getTableNameFromEntityClass(dbEntityClass);

        redisRes = redisServiceClient.find(tableName, id);

        Object contentObj = redisRes.getContent();
        HttpStatus status = redisRes.getStatus();

        if (status.equals(HttpStatus.OK) && contentObj != null) {
            return (MongoDbEntity) objectMapper.convertValue(contentObj, dbEntityClass);
        } else {
            MongoDbEntity entity = (MongoDbEntity) mongoTemplate.findById(id, dbEntityClass);

            if (entity == null) {
                throw new DataNotFoundException("Can not find data from " + tableName.name().toLowerCase() + " with ID " + id);
            } else {
                CompletableFuture.runAsync(() -> {
                    try {
                        redisServiceClient.save(
                                objectMapper.writeValueAsString(
                                        RedisRequest.builder()
                                                .type(tableName)
                                                .data(objectMapper.convertValue(entity, Map.class))
                                                .build()
                                )
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });

                if(cachingProcessHandlers != null) {
                    executeHandlers(cachingProcessHandlers);
                }

                return entity;
            }
        }
    }

    @Override
    public TableName getTableNameFromEntityClass(Class<? extends DbEntity> entityClass) {
        if (entityClass.equals(Menu.class)) {
            return TableName.MENU;
        } else if (entityClass.equals(Ingredient.class)) {
            return TableName.INGREDIENT;
        } else if (entityClass.equals(Facility.class)) {
            return TableName.FACILITY;
        } else if (entityClass.equals(Category.class)) {
            return TableName.CATEGORY;
        } else throw new InvalidDataException("Table entity class is invalid");
    }


    private void executeHandlers(List<Class<? extends CachingProcessHandler>> handlers) {
        try {
            for (Class<? extends CachingProcessHandler> handler: handlers) {
                CachingProcessHandler processHandler = handler.getConstructor().newInstance();
                processHandler.process();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
