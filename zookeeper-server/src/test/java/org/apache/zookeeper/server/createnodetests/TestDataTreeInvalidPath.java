package org.apache.zookeeper.server.createnodetests;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.zookeeper.server.DataTree;

@RunWith(Parameterized.class)
public class TestDataTreeInvalidPath {
    private  String path;
    private  byte[] data;
    private  List<ACL> acl;
    private  long ephemeralOwner;
    private  int parentCVersion;
    private  long zxid;
    private  long time;
    private DataTree dt;

    public TestDataTreeInvalidPath(String path, byte[] data, List<ACL> acl, long ephemeralOwner, int parentCVersion,
                                 long zxid, long time){
        configure(path,data,acl,ephemeralOwner, parentCVersion, zxid,time);
    }

    private void configure(String path, byte[] data, List<ACL> acl, long ephemeralOwner, int parentCVersion, long zxid, long time) {
        this.path = path;
        this.data = data;
        this.acl = acl;
        this.ephemeralOwner = ephemeralOwner;
        this.parentCVersion = parentCVersion;
        this.zxid = zxid;
        this.time = time;
        this.dt = new DataTree();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                //{"a", new byte[200], new ArrayList<ACL>(), 0, 0, 0, 0},
                {"node1/node2", new byte[30000], null, 0xFF00000000000001L, 0, -1, 1},
                {"/../node", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, 0x8000000000000000L, 0, 1, 300000},
                {"/a/b", new byte[4000], ZooDefs.Ids.OPEN_ACL_UNSAFE, 0, 1, 10000, 1},
                {"/a/b", new byte[4000], ZooDefs.Ids.READ_ACL_UNSAFE, 23, 1, 1, 1}
        });
    }

    @Test
    public void testInvalidPath()  {
        Exception error = null;

        try {
            this.dt.createNode(this.path, this.data, this.acl, this.ephemeralOwner, this.parentCVersion,this.zxid,this.time);
        }catch (KeeperException.NoNodeException | KeeperException.NodeExistsException | IllegalArgumentException e){
            error = e;
        }

        Assert.assertNotNull(error);
    }

}