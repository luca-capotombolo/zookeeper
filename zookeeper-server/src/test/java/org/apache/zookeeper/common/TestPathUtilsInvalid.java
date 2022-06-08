package org.apache.zookeeper.common;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class TestPathUtilsInvalid {
    private final String inputInvalidPath;

    public TestPathUtilsInvalid(String inputInvalidPath){
        this.inputInvalidPath = inputInvalidPath;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {null},
                {"a/b"},
                {"a/b/c"},
                {""},
                //next iteration
                {"/./a"},
                {"/a/./b"},
                {"a/b/."},
                {"/."},
                //next iteration
                {"/.."},
                {"/a/.."},
                {"a/../b"},
                {"/a/b/.."},
                {"/a/b/78/"},
        });
    }

    @Test
    public void testInvalidPath(){

        IllegalArgumentException error = null;

        try{
            PathUtils.validatePath(this.inputInvalidPath);
        }catch (IllegalArgumentException e){
            error = e;
        }

        Assert.assertNotNull(error);
    }
}
