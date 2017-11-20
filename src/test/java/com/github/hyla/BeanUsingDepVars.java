package com.github.hyla;

import com.github.hyla.depvar.DependentVar;
import com.github.hyla.depvar.DependentVarProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BeanUsingDepVars {

    @Autowired
    DependentVarProvider dependentVarProvider;

    private DependentVar<InternalState> state;

    @PostConstruct
    public void init() {
        state = dependentVarProvider
                .observeValues("first", "second", "third")
                .map(InternalState::new);
    }

    public String getState() {
        return state.get().toString();
    }

    private static class InternalState {
        private final String f1;
        private final String f2;
        private final String f3;

        InternalState(String f1, String f2, String f3) {
            this.f1 = f1;
            this.f2 = f2;
            this.f3 = f3;
        }

        @Override
        public String toString() {
            return f1 + "," + f2 + "," + f3;
        }
    }
}
