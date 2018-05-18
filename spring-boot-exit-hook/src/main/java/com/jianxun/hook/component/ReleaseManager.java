package com.jianxun.hook.component;


import javax.annotation.PreDestroy;
import java.util.concurrent.atomic.AtomicInteger;

public class ReleaseManager {
    private volatile Boolean isStopping = false;
    private AtomicInteger startCounter = new AtomicInteger(0);
    private AtomicInteger stopCounter = new AtomicInteger(0);

    public void startCounterIncrement() {
        startCounter.incrementAndGet();
    }

    public int getStartCounterValue() {
        return startCounter.get();
    }

    public void stopCounterIncrement() {
        stopCounter.incrementAndGet();
    }

    public int getStopCounterValue() {
        return stopCounter.get();
    }

    public synchronized Boolean getStopping() {
        return isStopping;
    }

    public synchronized void setStopping(Boolean stopping) {
        isStopping = stopping;
    }
    @PreDestroy
    public void preDestroy() throws InterruptedException {
        this.setStopping(true);
        while (this.startCounter.get() != this.stopCounter.get()) {
            System.out.println("正在退出中...");
            Thread.sleep(3000);
        }
    }
}
