package learn.dagger2;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by weiyanying on 2016/12/28.
 */
public class CoffeeApp {
    /**
     * //一杯coffee的制造需要{@link DripCoffeeModule}来进行制造
     */
    @Singleton
    @Component(modules = DripCoffeeModule.class)
    public interface Coffee {
        /**
         * 出售Coffee
         */
        Coffee maker();
    }

    public static void main(String[] args) {

    }
}
