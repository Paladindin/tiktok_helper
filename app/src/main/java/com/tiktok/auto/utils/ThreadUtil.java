package com.tiktok.auto.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by Administrator on 2017/9/10.
 */

/**
 * 使用方法:
 * 1.ThreadUtil.get_instance().init(context,handler).startThreadInPool( new Task(){ doTask(){//提交给线程池的操作}})
 * 2.ThreadUtil.startThread(new Task(){doTask(){//提交给线程的操作}})
 * 3.new Runnable(){ run(){ if(isMainThread(){// 如果是主线程进行xxx操作 })}}
 */

public class ThreadUtil {

    private static final byte[] writeLock = new byte[0];
    private static ThreadUtil _instance = null;

    private static Handler handler;

    private static ThreadPoolExecutor _pool;

    public interface Task{
        void doTask();
    }

    public void init(Handler handler){
        ThreadUtil.handler = handler;
    }

    private static ThreadPoolExecutor getPool(){

        if ( _pool == null || _pool.isTerminated()){
            _pool = new ThreadPoolExecutor(10, 50, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20),
                    new RejectedExecutionHandler() {
                        @Override
                        public void rejectedExecution(final Runnable runnable, final ThreadPoolExecutor threadPoolExecutor) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!threadPoolExecutor.isShutdown()){
                                        threadPoolExecutor.execute(runnable);
                                    }
                                }
                            },5);
                        }
                    });
        }
        return _pool;
    }

    public synchronized ThreadPoolExecutor createNewPool(){
        ThreadPoolExecutor pool = null;
        pool = new ThreadPoolExecutor(20, 100, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(final Runnable runnable, final ThreadPoolExecutor threadPoolExecutor) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!threadPoolExecutor.isShutdown()){
                                    threadPoolExecutor.execute(runnable);
                                }
                            }
                        },5);
                    }
                });
        return pool;
    }

    public static void startThread(final Task task){
        new Thread(new Runnable() {
            @Override
            public void run() {
                task.doTask();
            }
        }).start();
    }

    public static void startThreadInPool(final Task task){
        getPool();
        if (_pool != null && !_pool.isTerminated()){
            _pool.submit(new Runnable() {
                @Override
                public void run() {
                    task.doTask();
                }
            });
        }
    }

    public static boolean isMainThread(){
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void AssertInMainThread(){
        if (!isMainThread()){
            throw  new RuntimeException("Main thread assertion failed");
        }
    }

    public static void AssertInNoMainThread(){
        if (isMainThread()){
            throw  new RuntimeException("NoMain thread assertion failed");
        }
    }

    private ThreadUtil(){}
    public static ThreadUtil get_instance(){
        if (_instance == null){
            synchronized (writeLock){
                if (_instance == null){
                    _instance = new ThreadUtil();
                }
            }
        }
        return _instance;
    }

}
