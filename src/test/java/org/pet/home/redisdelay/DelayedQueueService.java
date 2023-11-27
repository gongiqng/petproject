package org.pet.home.redisdelay;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Service
public class DelayedQueueService {
    private final RedisTemplate<String, String> redisTemplate;

    public DelayedQueueService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void pushJob(String job, long delay) {
        long timestamp = System.currentTimeMillis() + delay;
        redisTemplate.opsForZSet().add("delayed_queue", job, timestamp);
    }

    public Set<String> popJobs() {
        long currentTimestamp = System.currentTimeMillis();
        Set<String> jobs = redisTemplate.opsForZSet().rangeByScore("delayed_queue", 0, currentTimestamp);
        if (jobs != null) {
            redisTemplate.opsForZSet().remove("delayed_queue", jobs.toArray());
        }
        return jobs;
    }
}
