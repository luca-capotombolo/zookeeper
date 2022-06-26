package org.apache.zookeeper.server.deletenodetests;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.server.DataNode;
import org.apache.zookeeper.server.DataTree;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.*;


@RunWith(Parameterized.class)
public class TestDateTreeDeleteValidNodePzxid {
    private String path;
    private long zxid;
    private DataTree dt;

    public TestDateTreeDeleteValidNodePzxid(String path, long zxid) throws KeeperException.NoNodeException, KeeperException.NodeExistsException {
        configure(path, zxid);
    }

    private String [] splitPath(String path){
        String [] pathElements;
        pathElements = path.split("/");
        return pathElements;
    }

    private void configure(String path, long zxid) throws KeeperException.NoNodeException, KeeperException.NodeExistsException {
        String newPath;
        String oldPath = "/";
        String parent = "/";
        this.dt = new DataTree();
        this.path = path;
        this.zxid = zxid;
        int count = 1;

        String [] pathElements = splitPath(path);
        //Create Tree all ephemeral nodes
        for(String pathElement: pathElements){
            newPath =  oldPath  + pathElement;
            this.dt.createNode(newPath, new byte[1000], ZooDefs.Ids.CREATOR_ALL_ACL, 0, dt.getNode(parent).stat.getCversion(), 0, 1);
            oldPath = newPath;
            parent = newPath;
            if(count!=1){
                oldPath += "/";
                count++;
            }
            count++;
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {"/a/b/node1/node2/c", -1},
                {"/a.b.c.d/e.f.g.h", 5},
                {"/a/b", 0},
                {"/node1", 10000000},
                {"/a/b/c", -1234567}
        });
    }

    @Test
    public void testCheckPzxid() throws KeeperException.NoNodeException {
        int last = this.path.lastIndexOf('/');
        String parentString = this.path.substring(0, last);
        DataNode parent = dt.getNode(parentString);
        long n = parent.stat.getPzxid();
        dt.deleteNode(this.path, this.zxid);
        parent = dt.getNode(parentString);
        if(n < this.zxid)
            Assert.assertEquals(this.zxid, parent.stat.getPzxid());
        else
            Assert.assertEquals(n, parent.stat.getPzxid());
    }
}