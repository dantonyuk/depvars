package com.github.hyla;

import com.github.hyla.depvar.DependentVar;
import com.github.hyla.depvar.DependentVarContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = DepVarConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DepVarTest {

    @Autowired
    private DependentVarContext dependentVarContext;

    @Autowired
    private BeanUsingDepVars beanUsingDepVars;

    @Test
    public void testValues() {
        dependentVarContext.setValue("first", "1");
        dependentVarContext.setValue("second", "2");
        dependentVarContext.setValue("third", "3");

        assertEquals("1,2,3", beanUsingDepVars.getState());
        assertEquals("1,2,3", beanUsingDepVars.getState());
        assertEquals("1,2,3", beanUsingDepVars.getState());
        assertEquals("1,2,3", beanUsingDepVars.getState());

        dependentVarContext.setValue("first", "4");
        dependentVarContext.setValue("second", "5");
        dependentVarContext.setValue("third", "6");

        assertEquals("4,5,6", beanUsingDepVars.getState());
    }

    @Test
    public void testCallNumbers() {
        DependentVarContext context = new DependentVarContext();

        int[] callCount = {0};
        DependentVar<Integer> state = context
                .observeValues("first", "second", "third")
                .map1(Integer::parseInt)
                .map2(Integer::parseInt)
                .map3(Integer::parseInt)
                .map((c1, c2, c3) -> {
            callCount[0]++;
            return c1 + c2 + c3;
        });

        context.setValues(new HashMap<String, String>(){{
            put("first", "1");
            put("second", "2");
            put("third", "3");
        }});

        assertEquals(0, callCount[0]);
        assertEquals(Integer.valueOf(6), state.get());
        assertEquals(1, callCount[0]);

        context.setValues(new HashMap<String, String>(){{
            put("first", "4");
            put("second", "5");
            put("third", "6");
        }});

        assertEquals(Integer.valueOf(15), state.get());
        assertEquals(2, callCount[0]);

        state.get();
        state.get();
        state.get();

        // no more calls to supplier
        assertEquals(2, callCount[0]);
    }
}
