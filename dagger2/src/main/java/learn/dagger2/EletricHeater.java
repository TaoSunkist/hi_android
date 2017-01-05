package learn.dagger2;

/**
 * Created by weiyanying on 2016/12/28.
 * 我是电子加热器，我的功能是提供加热
 */
public class EletricHeater implements Heater {
    private boolean isHeatering;

    @Override
    public void on() {
        //更改加热器的状态
        isHeatering = true;
        System.out.printf("加热器开始工作");
    }

    @Override
    public void off() {
        //更改加热器的状态
        isHeatering = true;
        System.out.printf("加热完毕");
    }

    @Override
    public void isHot() {
        System.out.printf("正在加热，请稍等");
    }
}
