package com.ayuan.staging.common.util.autoid;

public abstract class IdStrategy {

    private final AutoIdHelper autoIdHelper = new AutoIdHelper(1, 1);

    /**
     * @Description 默认实现  雪花算法
     * @return
     */
    public Object product(){
        return autoIdHelper.nextId();
    }

}
