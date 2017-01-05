package learn.dagger2;

/**
 * Created by weiyanying on 2016/12/28.
 * 作为加热器，你应该要做一个加热器应该做的事，比如加热
 */
public interface Heater {
    /**
     * 使用我，你就能实现加热功能
     */
    void on();

    /**
     * 使用我，你就能关闭加热器，不然等着炸吧
     */
    void off();

    void isHot();
}
