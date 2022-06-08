package org.apache.zookeeper.common;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class TestPathUtilsValid {
    private final String inputValidPath;

    public TestPathUtilsValid(String inputValidPath){
        this.inputValidPath = inputValidPath;
    }

    @Parameterized.Parameters
    public static Collection<?> primeNumbers() {
        return Arrays.asList(new Object[][] {
                {"/znode1"},
                {"/a/b"},
                {"/a/b/c"},
                {"/a"},
                //next iteration
                {"/abc.d"},
                {"/.abcd"},
                {"/abcd."},
                {"/"}
        });
    }

    @Test
    public void testValidPath(){

        IllegalArgumentException error = null;

        try{
            PathUtils.validatePath(this.inputValidPath);
        }catch (IllegalArgumentException e){
            error = e;
        }

        Assert.assertNull(error);
    }
}
