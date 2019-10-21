package com.demo.redis;

/**
 * User: lanxinghua
 * Date: 2019/10/21 14:29
 * Desc:
 */
public class TestPrefix extends BasePrefix{

    public TestPrefix(String prefix) {
        super(prefix);
    }

    @Override
    public String getPrefix() {
        return new TestPrefix("test").getPrefix();
    }
}
