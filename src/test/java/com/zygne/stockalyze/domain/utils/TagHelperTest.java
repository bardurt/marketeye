package com.zygne.stockalyze.domain.utils;

import org.junit.Assert;
import org.junit.Test;

public class TagHelperTest {

    /**
     * Given : a valid XML Tags of size 2
     * When : extracting a specific TAG value
     * Then : return correct TAG value
     */
    @Test
    public void test1() {
        String xmlLine = "<tag1>TAG_1</tag1><tag2>TAG_2</tag2>";

        Assert.assertEquals("TAG_1", TagHelper.getValueFromTagName("tag1", xmlLine));
    }

    /**
     * Given : a valid XML Tags of size 1
     * When : extracting a specific TAG value
     * Then : return correct TAG value
     */
    @Test
    public void test2() {
        String xmlLine = "<tag1>TAG_1</tag1>";

        Assert.assertEquals("TAG_1", TagHelper.getValueFromTagName("tag1", xmlLine));
    }

    /**
     * Given : an empty String
     * When : extracting a specific TAG value
     * Then : return empty String
     */
    @Test
    public void tes3() {
        String xmlLine = "";

        Assert.assertEquals("", TagHelper.getValueFromTagName("tag1", xmlLine));
    }

    /**
     * Given : a null value
     * When : extracting a specific TAG value
     * Then : return empty String
     */
    @Test
    public void tes4() {
        Assert.assertEquals("", TagHelper.getValueFromTagName("tag1", null));
    }

    /**
     * Given : a null value
     * When : extracting  null
     * Then : return empty String
     */
    @Test
    public void tes5() {
        Assert.assertEquals("", TagHelper.getValueFromTagName(null, null));
    }

    /**
     * Given : a invalid XML Tags of size 2
     * When : extracting a specific TAG value
     * Then : return correct TAG value
     */
    @Test
    public void test6() {
        String xmlLine = "<tag1>TAG_1<tag1><tag2>TAG_2<tag2>";

        Assert.assertEquals("", TagHelper.getValueFromTagName("tag1", xmlLine));
    }

    /**
     * Given : duplicate XML Tags
     * When : extracting a specific TAG value
     * Then : return correct TAG value
     */
    @Test
    public void test7() {
        String xmlLine = "<tag1>TAG_1</tag1><tag1>TAG_2</tag1>";

        Assert.assertEquals("", TagHelper.getValueFromTagName("tag1", xmlLine));
    }

}
