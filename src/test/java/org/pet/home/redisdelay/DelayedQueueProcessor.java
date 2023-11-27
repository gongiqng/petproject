package org.pet.home.redisdelay;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Component
public class DelayedQueueProcessor {
    private final DelayedQueueService delayedQueueService;

    public DelayedQueueProcessor(DelayedQueueService delayedQueueService) {
        this.delayedQueueService = delayedQueueService;
    }

    @Scheduled(fixedDelay = 1000)  // 每秒执行一次
    public void processDelayedJobs() {
        delayedQueueService.popJobs().forEach(job -> {
            // 处理任务
            System.out.println("处理任务：" + job);
        });
    }
}
