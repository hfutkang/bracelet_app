package sctek.cn.ysbracelet.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kang on 16-3-5.
 */
public class ThreadManager {

    private final static String TAG = ThreadManager.class.getSimpleName();

    private ExecutorService mExecutorService;

    private ThreadManager mInstance = new ThreadManager();

    public ThreadManager getInstance() {return mInstance; }

    private ThreadManager() {
        mExecutorService = Executors.newCachedThreadPool();
    }

    public void submit(Runnable runnable) {
        if(runnable == null)
            return;
        mExecutorService.execute(runnable);
    }

    public void stopAll() {
        mExecutorService.shutdownNow();
    }
}
