package org.apache.zookeeper.server.createnodetests;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.server.DataTree;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class TestDataTreeValidPath {
    private String path;
    private byte[] data;
    private List<ACL> acl;
    private long ephemeralOwner;
    private int parentCVersion;
    private long zxid;
    private long time;
    private int length;
    private DataTree dt;


    public TestDataTreeValidPath(String path, byte[] data, List<ACL> acl, long ephemeralOwner, int parentCVersion,
                                 long zxid, long time) throws KeeperException.NoNodeException, KeeperException.NodeExistsException {
        configure(path, data, acl, ephemeralOwner, parentCVersion, zxid, time);
    }

    private String [] splitPath(String path){
        String [] pathElements;
        pathElements = path.split("/");
        return pathElements;
    }

    private void configure(String path, byte[] data, List<ACL> acl, long ephemeralOwner, int parentCVersion,
                           long zxid, long time) throws KeeperException.NoNodeException, KeeperException.NodeExistsException {
        this.path = path;
        this.data = data;
        this.acl = acl;
        this.ephemeralOwner = ephemeralOwner;
        this.parentCVersion = parentCVersion;
        this.zxid = zxid;
        this.time = time;
        this.dt = new DataTree();
        String [] pathElements = splitPath(path);
        int n = pathElements.length;
        String newPath;
        String oldPath = "/";
        String parent = "/";
        int count = 1;

        //Create Tree all not ephemeral nodes
        for(String pathElement: pathElements){
            newPath =  oldPath  + pathElement;
            if(count==(n))
                break;
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
        this.length = n;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                //NO EPHEMERAL NODE
                {"/a", new byte[1], ZooDefs.Ids.CREATOR_ALL_ACL, 0, 3, 1, 1},
                //EPHEMERAL NODE
                {"/abc", new byte[30000], ZooDefs.Ids.OPEN_ACL_UNSAFE, 122, 0, 1, 1},
                //NO EPHEMERAL NODE
                {"/abcd56", new byte[0], ZooDefs.Ids.CREATOR_ALL_ACL, 0, -2, 1, 1},
                //EPHEMERAL NODE
                {"/a??9m", null, ZooDefs.Ids.READ_ACL_UNSAFE, 122, 0, 1, 1},
                //next iteration (2)
                //TTL NODE
                {"/node.1/node.2/node.3", new byte[10000], ZooDefs.Ids.CREATOR_ALL_ACL, 0xFF00000000000111L, 0, 0, 1},
                //TTL NODE
                {"/n1/n2/n3/n4", new byte[1000000], ZooDefs.Ids.READ_ACL_UNSAFE, 0xFF00000000000111L, 0, 0, 1},
                //CONTAINER NODE
                {"/n1/n2/n3/n4/n5/n6/n7/n8", new byte[1000000], ZooDefs.Ids.READ_ACL_UNSAFE, 0x8000000000000000L, 0, 0, 1}
        });
    }


    @Test
    public void test(){
        Exception error = null;

        try{
            this.dt.createNode(this.path, this.data, this.acl, this.ephemeralOwner, this.parentCVersion, this.zxid, this.time);
        }catch (Exception e){
            error = e;
        }
        Assert.assertNull(error);
        //""    "/zookeeper"    "/zookeeper/config"     "/zookeeper/quota"
        //this.length --> "/" YES
        Assert.assertEquals(this.length, this.dt.getNodeCount() - 4);
    }

    //next iteration
    @Test
    public void testAlreadyExists(){
        Exception error = null;

        try{
            this.dt.createNode(this.path, this.data, this.acl, this.ephemeralOwner, this.parentCVersion, this.zxid, this.time);
            this.dt.createNode(this.path, this.data, this.acl, this.ephemeralOwner, this.parentCVersion, this.zxid, this.time);
        }catch (KeeperException.NodeExistsException | KeeperException.NoNodeException e){
            error = e;
        }
        Assert.assertNotNull(error);
    }
}
