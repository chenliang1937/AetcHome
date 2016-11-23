package cn.com.aetc.aetchome.model.event;

import cn.com.aetc.aetchome.common.Constants;
import cn.com.aetc.aetchome.model.News;

/**
 * Created by chenliang3 on 2016/7/30.
 */
public class NewsEvent extends BaseEvent {

    private News news;
    private Constants.Way way;

    public NewsEvent(News news, Constants.Way way, Constants.Result result){
        this.news = news;
        this.way = way;
        this.result = result;
    }

    public News getNews() {
        return news;
    }

    public Constants.Way getWay() {
        return way;
    }
}
