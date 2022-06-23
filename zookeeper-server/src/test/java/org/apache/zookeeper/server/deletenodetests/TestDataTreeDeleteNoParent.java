package org.apache.zookeeper.server.deletenodetests;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.server.DataTree;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class TestDataTreeDeleteNoParent {
    private String path;
    private long zxid;
    private DataTree dt;

    public TestDataTreeDeleteNoParent(String path, long zxid) throws KeeperException.NoNodeException, KeeperException.NodeExistsException {
        configure(path, zxid);
    }

    private String [] splitPath(String path){
        String [] pathElements;
        pathElements = path.split("/");
        return pathElements;
    }

    private void configure(String path, long zxid) throws KeeperException.NoNodeException, KeeperException.NodeExistsException {
        this.dt = new DataTree();
        String newPath;
        String oldPath = "/";
        String parent = "/";
        this.path = path;
        this.zxid = zxid;
        int count = 1;

        String [] pathElements = this.splitPath(path);

        //Create Tree all ephemeral nodes
        for(String pathElement: pathElements){
            newPath =  oldPath  + pathElement;
            this.dt.createNode(newPath, new byte[1000], ZooDefs.Ids.CREATOR_ALL_ACL, 0, dt.getNode(parent).stat.getCversion(), 0, 1);
            oldPath = newPath;
            parent = newPath;
            if(count!=1){
                oldPath += "/";
                count++;
                continue;
            }
            count++;
        }
        int lastSlash = path.lastIndexOf('/');
        String parentName = path.substring(0, lastSlash);
        dt.deleteNode(parentName, 0);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
                {"/a/b/c/d/e/f", 1},
                {"/a/b/c", 10000},
                {"/a/b", 0}
        });
    }

    @Test
    public void testDeleteInvalidNode(){
        Exception error  =null;
        try{
            this.dt.deleteNode(this.path, this.zxid);
        }catch (KeeperException.NoNodeException e){
            error = e;
        }

        Assert.assertNotNull(error);
    }


}
