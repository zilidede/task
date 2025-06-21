// ProducerConsumerWithTask.java
package com.zl.task.generator;

import com.zl.dao.generate.LocalTaskDO;
import com.zl.task.craw.base.x.DefaultCrawSeleniumInternalCityWeather;
import com.zl.task.craw.keyword.DefaultCrawSeleniumDouHot;
import com.zl.task.impl.ExecutorTaskService;
import com.zl.task.impl.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.impl.taskResource.TaskResource;
import com.zl.task.impl.taskResource.TaskResourceFactory;
import com.zl.task.save.syn.DefaultSynTaskData;
import com.zl.task.vo.task.TaskVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

// 修改日志：
// 修改时间：2024-07-23
// 修改内容：本地主机计划任务执行器
// 修改时间：2024-12-16
//修改内容：
// 一个生产者线程 -生产任务
//
//
// ，多个消费者线程
public class TaskScheduler {
    private static final Logger logger = LoggerFactory.getLogger(TaskScheduler.class);
    private static final int NUM_CONSUMERS = 2; // 消费者线程数量
    private static final int TASKS_PER_ROUND = 3; // 每轮生产任务数量
    private static final BlockingQueue<TaskVO> queue = new LinkedBlockingQueue<>(); // 任务队列
    private static CountDownLatch tasksLatch = new CountDownLatch(TASKS_PER_ROUND); // 任务计数器
    private static final AtomicBoolean shouldRestart = new AtomicBoolean(false); // 是否重新启动生产者线程
    private static final Map<String, Boolean> producedTasks = new ConcurrentHashMap<>();


    public static void main(String[] args) throws InterruptedException {
        logger.info("任务资质初始化");
        DefaultTaskResourceCrawTabList.getTaskResource();
        logger.info("生产者消费者模型启动");
        // 启动生产者线程
        new Thread(new Producer()).start();
        // 启动消费者线程
        new Thread(new ConsumerExecutor()).start(); //多线程同时执行
        // 等待所有任务被消费，然后重新开始生产
        while (true) {
            tasksLatch.await();
            logger.info("所有任务被消费，重启生产者线程");
            RestartSysRes(); // 重新开始生产 休眠1分钟
            tasksLatch = new CountDownLatch(TASKS_PER_ROUND);
            shouldRestart.set(true);
        }
    }

    public static void RestartSysRes() throws InterruptedException {
        Thread.sleep(1000 * 60);
    }

    static class Producer implements Runnable {
        private int taskId = 0;

        @Override
        public void run() {
            while (true) {
                if (shouldRestart.getAndSet(false)) {
                    taskId = 0;
                    producedTasks.clear(); // 清除已生产的任务记录
                }
                if (taskId > TASKS_PER_ROUND) {
                    continue;
                }

                try {
                    List<TaskVO> tasks = generateUniqueTask();
                    for (TaskVO task : tasks) {
                        if (addTask(task)) { // 只有当任务成功添加时才递增taskId
                            taskId++;
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private List<TaskVO> generateUniqueTask() throws Exception {
            List<LocalTaskDO> vos = LocalTaskCommon.getUnExeTaskList();
            List<TaskVO> tasks = new ArrayList<>();
            for (LocalTaskDO vo : vos) {
                if (vo == null) {
                    return null;
                }
                TaskVO task = new TaskVO(taskId++, vo.getName());

                task.setTaskUuid(vo.getId());
                task.setTaskDesc(vo.getContent());
                // 根据任务类型选择执行器
                task.setExecutor(getExecutor(vo));
                if (task.getTaskName().indexOf("爬") >= 0) {
                    task.setTaskType("craw");
                } else if (task.getTaskName().indexOf("保存") >= 0) {
                    task.setTaskType("save");
                }
                //根据任务类型选择初始资源
                TaskResource res = TaskResourceFactory.createTaskResource(task.getTaskType());
                task.setTaskResource(res);
                tasks.add(task);
            }
            return tasks;
        }

        public ExecutorTaskService getExecutor(LocalTaskDO vo) throws Exception {
            if (vo.getName().equals("文件同步任务")) {
                return DefaultSynTaskData.getInstance();
            } else if (vo.getName().equals("爬取抖店罗盘商品榜单")) {
                vo.setType("craw");
                return DefaultCrawSeleniumDouHot.getInstance();
            } else if (vo.getName().equals("爬取全国城市天气")) {
                vo.setType("craw");
                return DefaultCrawSeleniumInternalCityWeather.getInstance();
            } else if (vo.getName().equals("爬取巨量云图大盘搜索词")) {
                vo.setType("craw");
                // return task.setExecutor(DefaultCrawOceanSearchKeyWords.getInstance());
            } else if (vo.getName().equals("爬取抖店罗盘类目")) {
                vo.setType("craw");
                // return taskDefaultCrawSeleniumDouYinCategoryList.getInstance();
            } else if (vo.getName().equals("爬取热点宝搜索词")) {
                vo.setType("craw");
                //  return DefaultCrawSeleniumDouHot.getInstance();
            } else if (vo.getName().equals("爬取巨量算数")) {
                vo.setType("craw");
                //  return DefaultCrawOceanSearchKeyWords.getInstance();
            } else if (vo.getName().equals("爬取关注列表账号")) {
                vo.setType("craw");
                // return DefaultCrawOceanSearchKeyWords.getInstance();
            } else if (vo.getName().equals("测试线程")) {
                //return DefaultCraw.getInstance();
            } else
                return null;
            return null;
        }

        public synchronized boolean addTask(TaskVO task) {
            if (task == null)
                return false;
            if (producedTasks.containsKey(task.getTaskUuid())) {
                return false;
            } else {
                producedTasks.put(task.getTaskUuid(), true);
                if (queue.offer(task)) {
                    logger.info("生产者线程生产任务: " + task);
                    try {
                        Thread.sleep(200); // 模拟生产时间
                    } catch (InterruptedException e) {
                        logger.info("生产者线程被中断，异常原因: " + e.getMessage());
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
            return true;
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    TaskVO task = queue.take();
                    if (task == null) {
                        continue;
                    }
                    logger.info("消费者线程消费任务" + "Consumed: " + task);
                    tasksLatch.countDown();
                    long startTime = System.currentTimeMillis();
                    // 执行你的代码
                    task.setExecuteTime(new Date());
                    task.getExecutor().run(task);
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;  // 结果以毫秒为单位
                    System.out.println("程序运行时间: " + duration + " ms");
                    logger.info(task.getTaskName() + "任务执行时间: " + duration / 1000 + "s");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class ConsumerExecutor implements Runnable {
        private static final ExecutorService executor = Executors.newFixedThreadPool(5); // 调整线程池大小

        @Override
        public void run() {
            while (true) {
                try {
                    // 定义要获取的任务数量
                    int numberOfTasksToTake = 5; // 这里可以调整
                    // 创建一个任务列表来存储从队列中取出的任务
                    List<TaskVO> tasks = new ArrayList<>();
                    // 使用 drainTo 方法尝试从队列中取出尽可能多的任务，但不超过 numberOfTasksToTake
                    int numTaken = queue.drainTo(tasks, numberOfTasksToTake);

                    // 对于每个获取到的任务，提交给线程池以并发执行
                    for (TaskVO task : tasks) {
                        if (task == null) {
                            continue;
                        }
                        logger.info("消费者线程消费任务: " + task);
                        tasksLatch.countDown();
                        executor.submit(() -> executeTask(task));
                    }

                    // 如果没有从队列中获取到任何任务，则让线程短暂休眠
                    if (numTaken == 0) {
                        Thread.sleep(1000); // 休眠 1 秒
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void executeTask(TaskVO task) {
            try {
                long startTime = System.currentTimeMillis();
                task.setExecuteTime(new Date());
                task.getExecutor().run(task);
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;  // 结果以毫秒为单位
                System.out.println("程序运行时间: " + duration + " ms");
                logger.info(task.getTaskName() + "任务执行时间: " + duration / 1000 + "s");
            } catch (Exception e) {
                logger.error("Error executing task", e);
            }
        }
    }
}