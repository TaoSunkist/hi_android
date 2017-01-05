package learn.dagger2;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by weiyanying on 2016/12/28.
 * 做一杯coffee需要的机器之一，我提供coffee的加热功能
 */
@Module
public class DripCoffeeModule {
    /**
     * 对外提供加热的功能，因为@Singleton的原因，我始终提供一个加热器对外使用
     *
     * @return
     */
    @Provides
    @Singleton
    Heater provideHeater() {
        return new EletricHeater();
    }
}
