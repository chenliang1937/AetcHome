package cn.com.aetc.aetchome.service;

/**
 * Created by chenliang3 on 2016/7/29.
 * bindService call back
 */
public interface UpdateProgressListener {

    /**
     * download start
     */
    public void start();

    /**
     * update download progress
     * @param progress
     */
    public void update(int progress);

    /**
     * download success
     */
    public void success();

    /**
     * download error
     */
    public void error();

}
