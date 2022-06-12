package org.apache.zookeeper.server.deletenodetests;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.server.DataTree;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.*;


@RunWith(Parameterized.class)
public class TestDataTreeDeleteValidNode {
    private String path;
    private long zxid;
    private DataTree dt;
    private DataTree dtStandard;
    private DataTree dtContainer;
    private DataTree dtTTL;

    public TestDataTreeDeleteValidNode(String path, long zxid) throws KeeperException.NoNodeException, KeeperException.NodeExistsException {
       configure(path, zxid);
    }

    private String [] splitPath(String path){
        String [] pathElements;
        pathElements = path.split("/");
        return pathElements;
    }

    private void configure(String path, long zxid) throws KeeperException.NoNodeException, KeeperException.NodeExistsException {
        this.dt = new DataTree();
        this.path = path;
        this.zxid = zxid;
        this.dtContainer = new DataTree();
        this.dtTTL = new DataTree();
        this.dtStandard = new DataTree();

        String newPath;
        String oldPath = "/";
        String parent = "/";
        int count = 1;
        String [] pathElements = splitPath(path);
        //Create Tree all not ephemeral nodes
        for(String pathElement: pathElements){
            newPath =  oldPath  + pathElement;
            this.dt.createNode(newPath, new byte[1000], ZooDefs.Ids.CREATOR_ALL_ACL, 0, this.dt.getNode(parent).stat.getCversion(), 0, 1);
            oldPath = newPath;
            parent = newPath;
            if(count!=1){
                oldPath += "/";
                count++;
                continue;
            }
            count++;
        }

        count = 1;
        oldPath = "/";
        parent = "/";
        //Last node is ephemeral standard
        for(String pathElement: pathElements){
            newPath =  oldPath  + pathElement;
            if(count==pathElements.length)
                this.dtStandard.createNode(newPath, new byte[1000], ZooDefs.Ids.CREATOR_ALL_ACL, 23, this.dtStandard.getNode(parent).stat.getCversion(), 0, 1);
            else
                this.dtStandard.createNode(newPath, new byte[1000], ZooDefs.Ids.CREATOR_ALL_ACL, 0, this.dtStandard.getNode(parent).stat.getCversion(), 0, 1);
            oldPath = newPath;
            parent = newPath;
            if(count!=1){
                oldPath += "/";
                count++;
                continue;
            }
            count++;
        }

        //Create last node as Container node
        count = 1;
        oldPath = "/";
        parent = "/";
        for(String pathElement: pathElements){
            newPath =  oldPath  + pathElement;
            if(count==pathElements.length)
                this.dtContainer.createNode(newPath, new byte[1000], ZooDefs.Ids.CREATOR_ALL_ACL, 0x8000000000000000L, this.dtContainer.getNode(parent).stat.getCversion(), 0, 1);
            else
                this.dtContainer.createNode(newPath, new byte[1000], ZooDefs.Ids.CREATOR_ALL_ACL, 0, this.dtContainer.getNode(parent).stat.getCversion(), 0, 1);
            oldPath = newPath;
            parent = newPath;
            if(count!=1){
                oldPath += "/";
                count++;
                continue;
            }
            count++;
        }

        //Create last node as TTL node
        System.setProperty("zookeeper.extendedTypesEnabled", "true");
        count = 1;
        oldPath = "/";
        parent = "/";
        for(String pathElement: pathElements){
            newPath =  oldPath  + pathElement;
            if(count==pathElements.length)
                this.dtTTL.createNode(newPath, new byte[1000], ZooDefs.Ids.CREATOR_ALL_ACL, 0xFF00000000000001L, this.dtTTL.getNode(parent).stat.getCversion(), 0, 1);
            else
                this.dtTTL.createNode(newPath, new byte[1000], ZooDefs.Ids.CREATOR_ALL_ACL, 0, this.dtTTL.getNode(parent).stat.getCversion(), 0, 1);
            oldPath = newPath;
            parent = newPath;
            if(count!=1){
                oldPath += "/";
                count++;
                continue;
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
    public void testDeleteValidNode(){
        Exception error = null;
        try{
            this.dt.deleteNode(this.path, this.zxid);
        }catch (KeeperException.NoNodeException e){
            error = e;
        }
        Assert.assertNull(error);
    }

    //next iteration (2)
    @Test
    public void testDeleteValidNodeEphStandard(){
        Exception error = null;
        try{
            this.dtStandard.deleteNode(this.path, this.zxid);
        }catch (KeeperException.NoNodeException e){
            error = e;
        }
        Assert.assertNull(error);
    }

    //next iteration (2)
    @Test
    public void testDeleteValidNodeContainer(){
        Exception error = null;

        try{
            this.dtContainer.deleteNode(this.path, this.zxid);
        }catch (KeeperException.NoNodeException e){
            error = e;
        }

        Assert.assertNull(error);
    }

    //next iteration (2)
    @Test
    public void testDeleteValidNodeTTL(){
        Exception error = null;

        try{
            this.dtTTL.deleteNode(this.path, this.zxid);
        }catch (KeeperException.NoNodeException e){
            error = e;
        }

        Assert.assertNull(error);
    }

}