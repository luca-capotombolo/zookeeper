package org.apache.zookeeper.server.deletenodetests;

import org.apache.zookeeper.server.DataTree;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class TestDataTreeDeleteInvalidNode {
    private String path;
    private long zxid;
    private DataTree dt;

    public TestDataTreeDeleteInvalidNode(String path, long zxid) {
        this.configure(path, zxid);
    }

    private void configure(String path, long zxid){
        this.path = path;
        this.zxid = zxid;
        this.dt = new DataTree();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
                {"/a", 10000},
                {null, 0},
                {"", -2}
        });
    }

    @Test
    public void testDeleteInvalidNode(){
        Exception error  =null;

        try{
            this.dt.deleteNode(this.path, this.zxid);
        }catch (Exception e){
            error = e;
        }

        Assert.assertNotNull(error);
    }


}
