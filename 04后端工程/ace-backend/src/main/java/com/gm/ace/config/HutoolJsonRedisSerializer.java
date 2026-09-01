package com.gm.ace.config;

import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

/**
 * 基于 Hutool JSON 的 Redis 序列化器（与 Jackson 解耦，规避 Boot 4 的 Jackson 3 冲突）
 *
 * @param <T> 值类型
 * @author guoym
 */
public class HutoolJsonRedisSerializer<T> implements RedisSerializer<T> {

    private final Class<T> type;

    public HutoolJsonRedisSerializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public byte[] serialize(T value) throws SerializationException {
        if (value == null) {
            return new byte[0];
        }
        return JSONUtil.toJsonStr(value).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return JSONUtil.toBean(new String(bytes, StandardCharsets.UTF_8), type);
    }
}
