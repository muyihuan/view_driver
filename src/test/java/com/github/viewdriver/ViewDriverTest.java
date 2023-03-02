package com.github.viewdriver;

import com.github.case2.domain.modela.model.ModelA;
import com.github.case2.view.ViewA;
import com.github.viewdriver.builder.ViewDriverBuilder;
import org.junit.jupiter.api.Test;

/**
 * 单元测试
 *
 * @author yanghuan
 */
class ViewDriverTest {

    ViewDriverBuilder defViewDriver = new ViewDriverBuilder();

    @Test
    void testMeta() {
        defViewDriver.viewBindModel(ViewA.class, ModelA.class);

        System.out.println("hello");
    }

    @Test
    void testViewTree() {

    }

    @Test
    void testLoadModel() {

    }

    @Test
    void testMapView() {

    }
}
