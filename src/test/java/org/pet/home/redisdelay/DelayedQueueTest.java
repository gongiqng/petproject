package org.pet.home.redisdelay;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/

@SpringBootTest
public class DelayedQueueTest {
    @Autowired
    private DelayedQueueService delayedQueueService;

    @Test
    public void testDelayedQueue() {
        // 推送任务到延时队列
        delayedQueueService.pushJob("job1", 5000);  // 5秒后执行
        delayedQueueService.pushJob("job2", 10000);  // 10秒后执行

        // 等待一段时间，确保定时任务有足够的时间来处理延时队列中的任务
        try {
            Thread.sleep(15000);  // 等待15秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
