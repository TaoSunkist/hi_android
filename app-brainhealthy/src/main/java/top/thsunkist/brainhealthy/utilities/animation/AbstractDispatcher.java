package top.thsunkist.brainhealthy.utilities.animation;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import top.thsunkist.brainhealthy.utilities.animation.QueueModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksbk on 2017/5/17.
 */

public abstract class AbstractDispatcher<T> implements ImmViewDispatcher {

    public boolean isBinding = false;
    private int DELAY_TIME = 3000;

    private ViewGroup layout;//绑定数据的布局
    private Context context;
    protected List<ViewHolder> addedViewHolderList;//已添加子项结构
    private List<ViewHolder> emptyViewHolderList;//未添加子项结构
    protected int sumNumber;//总项数

    private boolean isDelayFix = true;//是否使用固定的延时


    protected QueueModel<T> queueModel;//数据队列

    private static class MyHandler<T> extends Handler {
        private WeakReference<AbstractDispatcher> adwr;

        public MyHandler(AbstractDispatcher<T> abstractDispatcher) {
            adwr = new WeakReference<>(abstractDispatcher);
        }

        @Override
        public void handleMessage(Message msg) {
            if (adwr.get() == null) return;
            if (msg.what != 0) {
                //view显示到时
                adwr.get().remove(msg);
            } else {
                //获取到新数据
                adwr.get().get(msg);

            }
        }
    }

    private void remove(Message msg) {
//        LogUtil.w("handleMessage1" + msg.what);
        ViewHolder holder = (ViewHolder) msg.obj;
        remove(holder);
        removeTiming(holder);
//        LogUtil.w("handleMessage2" + msg.what);
        queueModel.takeValue();
    }

    private void get(Message msg) {
        //获取到新数据
        T obj = (T) msg.obj;
        if (isBinding) {
            judgeDataStatus(obj);
        } else {
            queueModel.takeValue();
        }
    }

    private MyHandler<T> handler = new MyHandler<>(this);


    public AbstractDispatcher(Context context, int sumNumber) {
        this.sumNumber = sumNumber;
        this.context = context;
//        modelViews = new ArrayList<>(sumNumber);
        addedViewHolderList = new ArrayList<>(sumNumber);
        emptyViewHolderList = new ArrayList<>(sumNumber);
        queueModel = new QueueModel<>();
        queueModel.setHandler(handler);
        for (int i = 0; i < sumNumber; i++) {
            queueModel.takeValue();
        }

    }

    public void offer(T obj) {
        if (!isBinding || obj == null) return;
        queueModel.offer(obj);
    }

    /**
     * @param time 队列轮询的时间间隔
     */
    public void setQueueDelayTime(long time) {
        queueModel.setDelayTime(time);
    }

    /**
     * 绑定布局
     *
     * @param layout
     */
    public void bind(Context context, ViewGroup layout) {
        if (isBinding) {
//            LogUtil.e("未解绑布局1");
            return;
        }
//        LogUtil.d("effect_bindLayout");
        this.context = context;
        this.layout = layout;
        addedViewHolderList.clear();
        emptyViewHolderList.clear();
        for (int i = 0; i < sumNumber; i++) {
            ViewHolder view = createViewHolder(layout);
            view.setTag(i + 1);
            emptyViewHolderList.add(view);
        }
        isBinding = true;

    }

    /**
     * 解绑布局
     */
    public void unBind() {
        if (layout != null) layout.removeAllViews();
        this.layout = null;
        emptyViewHolderList.clear();
        addedViewHolderList.clear();
        queueModel.getQueue().clear();
        isBinding = false;
    }

    public void clear() {
        emptyViewHolderList.clear();
        addedViewHolderList.clear();
        queueModel.clear();
        queueModel.getQueue().clear();
    }

    public void clearQueue() {
        emptyViewHolderList.clear();
        addedViewHolderList.clear();
        queueModel.getQueue().clear();
    }

    public void clearHandlerMessage() {
        if (handler != null) {
            for (int i = 0; i <= sumNumber + 1; i++) {
                handler.removeMessages(i);
            }
        }
    }


    /**
     * 判断新数据与当前数据关系
     *
     * @param obj
     */
    private void judgeDataStatus(T obj) {
        //先判断是否与已显示的view有关
        for (ViewHolder viewHolder : addedViewHolderList) {
            if (isSameView(viewHolder.value, obj)) {
                //修改
                viewHolder.setValue(obj, true);
                modify(viewHolder);
                return;
            }
        }

        //添加新view
        if (!emptyViewHolderList.isEmpty()) {
            ViewHolder viewHolder = emptyViewHolderList.get(0);
            add(viewHolder);
            viewHolder.setValue(obj, false);
        }
    }

    public void emptyQueue() {

    }

    /**
     * 两个view是否相关联
     *
     * @param oldObj
     * @param newObj
     * @return true则关联, view刷新而不更换
     */
    protected abstract boolean isSameView(T oldObj, T newObj);


    /**
     * 添加view
     */
    protected void add(ViewHolder viewHolder) {
        if (layout != null && viewHolder.itemView.getParent() == null) {
            layout.addView(viewHolder.itemView);
        }
        addedViewHolderList.add(viewHolder);
        emptyViewHolderList.remove(viewHolder);
        startTiming(viewHolder);
    }

    /**
     * 修改view
     */
    private void modify(ViewHolder viewHolder) {
        viewHolder.itemView.invalidate();
        queueModel.takeValue();
        resetTiming(viewHolder);
    }

    /**
     * 删除view
     */
    private void remove(ViewHolder viewHolder) {
        if (layout != null) {
            layout.removeView(viewHolder.itemView);
        }
        emptyViewHolderList.add(viewHolder);
        addedViewHolderList.remove(viewHolder);
        if (queueModel.getQueue().isEmpty()) emptyQueue();
    }

    public QueueModel<T> getQueueModel() {
        return queueModel;
    }

    /**************************控制显示时间**********************/

    private void startTiming(ViewHolder viewHolder) {
        if (isDelayFix) {
            Message message = new Message();
            message.what = viewHolder.tag;
            message.obj = viewHolder;
//            LogUtil.w("startTiming " + message.what);
            handler.sendMessageDelayed(message, DELAY_TIME);
        }

    }

    protected void resetTiming(ViewHolder viewHolder) {
        removeTiming(viewHolder);
        startTiming(viewHolder);
    }

    protected void notifyRemoveView(ViewHolder viewHolder) {
        Message message = new Message();
        message.what = viewHolder.tag;
        message.obj = viewHolder;
//        LogUtil.w("notifyRemoveView " + message.what);
        handler.sendMessage(message);
    }

    public void setDelayFix(boolean delayFix) {
        isDelayFix = delayFix;
    }

    private void removeTiming(ViewHolder viewHolder) {
        handler.removeMessages(viewHolder.tag);
//        LogUtil.w("removeTiming " + viewHolder.tag);
    }


    public void setDELAY_TIME(int DELAY_TIME) {
        this.DELAY_TIME = DELAY_TIME;
    }

    /**
     * 创建布局
     *
     * @param viewGroup
     * @return
     */
    public abstract ViewHolder createViewHolder(ViewGroup viewGroup);


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public abstract class ViewHolder {
        protected View itemView;
        T value;
        int tag;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value, boolean isSame) {
            this.value = value;
            bindView(value, isSame);
        }

        public int getTag() {
            return tag;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }

        public abstract void bindView(T value, boolean isSame);
    }
}



