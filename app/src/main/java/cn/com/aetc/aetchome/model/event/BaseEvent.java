package cn.com.aetc.aetchome.model.event;

import cn.com.aetc.aetchome.common.Constants;

/**
 * Created by chenliang3 on 2016/7/30.
 */
public class BaseEvent {

    protected Constants.Result result;

    public Constants.Result getResult() {
        return result;
    }

    public void setResult(Constants.Result result) {
        this.result = result;
    }
}
