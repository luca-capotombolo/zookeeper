package org.apache.zookeeper.common;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class TestPathUtilsInvalidUnicode {
    private final String inputInvalidPath;

    public TestPathUtilsInvalidUnicode(String inputInvalidPath){
        this.inputInvalidPath = inputInvalidPath;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {"/\u0001"},
                {"/\u0019"},
                {"/\u007F"},
                {"/\u009F"},
                {"/\ud800"},
                {"/\uF8FF"},
                {"/\uFFF0"},
                {"/\uFFFF"}
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