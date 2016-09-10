/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jason.multythread;

import java.util.concurrent.CyclicBarrier;

/**
 * TODO
 *
 * @author linjiedeng
 * @date 16/9/10 下午7:49
 * @since TODO
 * 与CountDownLatch类似,也是等待某些线程都做完以后再执行,区别在于,这个计数器可以反复使用
 * 假设我们将计数器设置为10,那么凑齐一批10个线程后计数器就会清零,然后接着凑齐下一批10个线程
 * 这里说的凑齐10个线程是指凑齐10个线程等待到barrier状态,即10个线程调用了await方法
 */
public class CyclicBarrierTest implements Runnable {
    private String soldier;

    private final CyclicBarrier cyclic;

    public CyclicBarrierTest(String soldier, CyclicBarrier cyclic) {
        this.soldier = soldier;
        this.cyclic = cyclic;
    }

    public void run() {
        try {
            cyclic.await(); //等待10个线程调用了await方法后,就会做一件事,这件事就是执行BarrierRun对象的run方法
            dowork();
            cyclic.await();
        } catch (Exception e) {

        }
    }

    private void dowork() {
        try {
            Thread.sleep(3000);
        }catch (Exception e) {

        }
        System.out.println(soldier + ":done");
    }

    public static class BarrierRun implements Runnable {
        boolean flag;
        int n;

        public BarrierRun(boolean flag, int n) {
            super();
            this.flag = flag;
            this.n = n;
        }


        public void run() {
            if(flag) {
                System.out.println(n + "个任务完成");
            } else {
                System.out.println(n + "个集合完成");
                flag = true;
            }
        }
    }

    public static void main(String[] args) {
        final int n = 10;
        Thread[] threads = new Thread[n];
        boolean flag = false;
        CyclicBarrier barrier = new CyclicBarrier(n, new BarrierRun(flag, n));
        System.out.println("集合");
        for(int i = 0; i < n; i++) {
            System.out.println(i + "报道");
            threads[i] = new Thread(new CyclicBarrierTest("士兵" + i, barrier));
            threads[i].start();
        }
    }
}
