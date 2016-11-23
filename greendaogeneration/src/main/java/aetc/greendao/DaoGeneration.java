package aetc.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by chenliang3 on 2016/8/6.
 */
public class DaoGeneration {

    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(1, "cn.com.aetc.greendao");
        addNews(schema);
        new DaoGenerator().generateAll(schema, "../AetcHome/app/src/main/java-gen");
    }

    public static void addNews(Schema schema){
        Entity news = schema.addEntity("GreenNews");
        news.addIdProperty();
        news.addStringProperty("date");
        news.addStringProperty("newslist");
    }

}
