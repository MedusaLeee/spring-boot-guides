package com.jianxun.hook.component;

import com.jianxun.hook.thread.MyRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ThreadManager implements CommandLineRunner {
    @Autowired
    private ReleaseManager releaseManager;
    @Override
    public void run(String... args) throws Exception {
        MyRunnable myRunnable = new MyRunnable(releaseManager);
        new Thread(myRunnable).start();
        MyRunnable myRunnable2 = new MyRunnable(releaseManager);
        new Thread(myRunnable2).start();
    }
}
