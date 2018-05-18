package com.jianxun.hook.thread;

import com.jianxun.hook.component.ReleaseManager;
import lombok.Data;

@Data
public class MyRunnable implements Runnable{

    private ReleaseManager ReleaseManager;
    public MyRunnable(ReleaseManager releaseManager) {
        ReleaseManager = releaseManager;
    }
    @Override
    public void run() {
        ReleaseManager.startCounterIncrement();
        while (!ReleaseManager.getStopping()) {
            System.out.println(Thread.currentThread().getId() + "正在运行...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getId() + "已经结束...");
        ReleaseManager.stopCounterIncrement();
    }
}
