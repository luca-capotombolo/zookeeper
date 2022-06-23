package org.apache.zookeeper.server.createnodetests;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.server.DataNode;
import org.apache.zookeeper.server.DataTree;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class TestDateTreeValidPathCVersion {
    private  String path;
    private  byte[] data;
    private  List<ACL> acl;
    private  long ephemeralOwner;
    private  int parentCVersion;
    private  long zxid;
    private  long time;
    private DataTree dt;


    public TestDateTreeValidPathCVersion(String path, byte[] data, List<ACL> acl, long ephemeralOwner, int parentCVersion,
                                 long zxid, long time){
        configure(path, data, acl, ephemeralOwner, parentCVersion, zxid, time);
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
                {"/a", new byte[1], ZooDefs.Ids.CREATOR_ALL_ACL, 0, 3, 1, 1},
                {"/abc", new byte[30000], ZooDefs.Ids.OPEN_ACL_UNSAFE, 122, 0, 1, 1},
                {"/abcd56", new byte[0], ZooDefs.Ids.CREATOR_ALL_ACL, 0, -2, 1, 1},
                {"/a??9m", null, ZooDefs.Ids.READ_ACL_UNSAFE, 122, 0, 1, 1}
        });
    }

    @Test
    public void testCVersion() throws KeeperException.NoNodeException, KeeperException.NodeExistsException {
        int n;
        DataNode root = this.dt.getNode("/");
        n = root.stat.getCversion();
        this.dt.createNode(this.path, this.data, this.acl, this.ephemeralOwner, this.parentCVersion, this.zxid, this.time);
        root = this.dt.getNode("/");
        if(n <= this.parentCVersion)
            Assert.assertEquals(this.parentCVersion, root.stat.getCversion());
        if(n > this.parentCVersion) {
            Assert.assertNotEquals(this.parentCVersion, root.stat.getCversion());
            Assert.assertEquals(n, root.stat.getCversion());
        }
    }
}
