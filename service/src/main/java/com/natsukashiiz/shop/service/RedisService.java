package com.natsukashiiz.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class RedisService {

    @Resource private RedisTemplate<String, String> redisTemplate;

    public void setValueByKey(String key, String value, long expire) {
        log.debug("SetValueByKey-[next]. key:{}, value:{}, expire:{}", key, value, expire);
//        this.deleteByKey(key);
        this.redisTemplate.opsForValue().set(key, value);
        this.redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS);
    }

    public String getValueByKey(String key) {
        log.debug("GetValueByKey-[next]. key:{}", key);
        return this.redisTemplate.opsForValue().get(key);
    }

    public Boolean hasKey(String key) {
        log.debug("HasKey-[next]. key:{}", key);
        return this.redisTemplate.hasKey(key);
    }

    public void deleteByKey(String key) {
        log.debug("DeleteByKey-[next]. key:{}", key);
        this.redisTemplate.delete(key);
    }
}
