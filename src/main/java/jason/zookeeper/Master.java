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
package jason.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.omg.PortableInterceptor.Interceptor;

import java.sql.Connection;
import java.util.Random;

/**
 * TODO
 *
 * @author linjiedeng
 * @date 16/9/16 上午11:03
 * @since TODO
 */
public class Master implements Watcher {
    ZooKeeper zk;
    String hostPort;

    String serverId = Integer.toHexString(new Random().nextInt());
    private static boolean isLeader = false;

    Master(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZK() throws Exception {
        zk = new ZooKeeper(hostPort, 15000, this);
    }

    void stopZK() throws Exception{
        zk.close();
    }

    public void process(WatchedEvent event) {
        System.out.println(event);
    }

    public static void main(String[] args) throws Exception {
        Master m = new Master("127.0.0.1:2181");
        m.startZK();
        m.runForMaster();
        if(isLeader) {
            System.out.println("I'm the leader");
            Thread.sleep(60000);
        } else {
            System.out.println("someone else is the leader");
        }
        m.stopZK();
    }

    public void runForMaster() throws Exception{
        while(true) {
            try {
                zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                isLeader = true;
                break;
            } catch (KeeperException.NoNodeException e1) {
                isLeader = false;
            } catch (KeeperException.ConnectionLossException e2) {

            }
            if(checkMaster()) {
                break;
            }
        }
    }

    boolean checkMaster() throws Exception{
        while (true) {
            try {
                Stat stat = new Stat();
                byte data[] = zk.getData("/master", false, stat);
                isLeader = new String(data).equals(serverId);
                return true;
            } catch (KeeperException.NoNodeException e) {
                return false;
            } catch (KeeperException.ConnectionLossException e2) {

            }
        }
    }
}
