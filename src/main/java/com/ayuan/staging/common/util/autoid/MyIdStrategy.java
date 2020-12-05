package com.ayuan.staging.common.util.autoid;

/**
 * @author ayuan
 * @date 2020-12-05
 */
public class MyIdStrategy extends IdStrategy {

    @Override
    public Object product() {
        return ((Long)super.product()).intValue();
    }
}
