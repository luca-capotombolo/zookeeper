package org.apache.zookeeper.server.createnodetests;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.server.DataTree;
import org.junit.Assert;
import org.junit.Test;

//n°8/°9

public class TestDateTreeChildEphemeral {

    //@Test
    public void testEphemeral() throws KeeperException.NoNodeException, KeeperException.NodeExistsException {
        DataTree dt = new DataTree();
        Exception error = null;

        //new ephemeral node
        dt.createNode("/a", new byte[300], ZooDefs.Ids.CREATOR_ALL_ACL, 2, dt.getNode("/").stat.getCversion(), 1, 1);

        try {
            dt.createNode("/a/b", new byte[1000], ZooDefs.Ids.OPEN_ACL_UNSAFE, 0, dt.getNode("/a").stat.getCversion(),1,1);
        }catch (Exception e){
            error = e;
        }

        Assert.assertEquals(0, dt.getNode("/a").getChildren().size());
        Assert.assertNotNull(error);
    }


    //@Test
    public void testChildEphemeral() throws KeeperException.NoNodeException, KeeperException.NodeExistsException {
        DataTree dt = new DataTree();
        Exception error = null;

        //new ephemeral node
        dt.createNode("/a", new byte[300], ZooDefs.Ids.CREATOR_ALL_ACL, 2, dt.getNode("/").stat.getCversion(), 1, 1);
        try {
            dt.createNode("/a/b", new byte[1000], ZooDefs.Ids.READ_ACL_UNSAFE, 2, dt.getNode("/a").stat.getCversion(),1,1);
        }catch (Exception e){
            error = e;
        }

        Assert.assertEquals(0, dt.getNode("/a").getChildren().size());
        Assert.assertNotNull(error);
    }

}