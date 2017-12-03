package com.github.hyla.depvar;

import com.github.hyla.util.tuple.Tuple2;
import com.github.hyla.util.tuple.Tuple3;
import com.github.hyla.util.tuple.Tuple4;
import com.github.hyla.util.tuple.Tuple5;
import com.github.hyla.util.MultiMap;
import com.github.hyla.util.WeakKeyMultiMap;
import com.github.hyla.util.WeakValueMultiMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DependentVarContext implements DependentVarProvider {

    private Map<String, String> varValues = new HashMap<>();
    private Map<String, RegisteredVar<Optional<String>>> primaryVars = new HashMap<>();
    private MultiMap<String, RegisteredVar<?>> dependentVars = new WeakValueMultiMap<>();
    private MultiMap<RegisteredVar<?>, String> varSources = new WeakKeyMultiMap<>();

    public void setValues(Map<String, String> values) {
        varValues.putAll(values);
        values.keySet().stream()
                .map(this::getPrimaryVar)
                .forEach(RegisteredVar::invalidate);
        values.keySet().stream()
                .flatMap(key -> dependentVars.get(key).stream())
                .forEach(RegisteredVar::invalidate);
    }

    public void setValue(String key, String value) {
        setValues(Collections.singletonMap(key, value));
//        varValues.put(key, value);
//        getPrimaryVar(key).invalidate();
//        dependentVars.get(key).forEach(RegisteredVar::invalidate);
    }

    private RegisteredVar<Optional<String>> getPrimaryVar(String varName) {
        return primaryVars.computeIfAbsent(varName, name -> {
            RegisteredVar<Optional<String>> var = new RegisteredVar<>(
                    () -> Optional.ofNullable(varValues.get(name)));
            varSources.add(var, varName);
            return var;
        });
    }

    @Override
    public DependentVar<List<Optional<String>>> observeVars(String... varNames) {
        RegisteredVar<List<Optional<String>>> var = new RegisteredVar<>(
                () -> Arrays.stream(varNames)
                        .map(this::getPrimaryVar)
                        .map(DependentVar::get)
                        .collect(Collectors.toList()));

        associateDepVarToPrimaryVars(var, Arrays.asList(varNames));

        return var;
    }

    private <T> void associateDepVarToPrimaryVars(RegisteredVar<T> var, Collection<String> varNames) {
        varNames.forEach(name -> dependentVars.add(name, var));
        varSources.addAll(var, varNames);
    }

    private class RegisteredVar<T> implements DependentVar<T> {

        private T cached;
        private Supplier<T> supplier;

        RegisteredVar(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        void invalidate() {
            cached = null;
        }

        public T get() {
            if (cached != null) {
                return cached;
            }

            synchronized (this) {
                if (cached != null) {
                    return cached;
                }

                cached = supplier.get();
            }

            return cached;
        }

        @Override
        public <R> DependentVar<R> map(Function<? super T, ? extends  R> fun) {
            RegisteredVar<R> state = new RegisteredVar<>(() -> fun.apply(get()));
            associateDepVarToPrimaryVars(state, varSources.get(this));
            return state;
        }
    }


    @Override
    public DependentVar.DepVar2<Optional<String>, Optional<String>> observe(String name1, String name2) {
        DepVar2Impl<Optional<String>, Optional<String>> state = new DepVar2Impl<>(
                () -> Tuple2.of(getPrimaryVar(name1).get(), getPrimaryVar(name2).get()));

        associateDepVarToPrimaryVars(state, Arrays.asList(name1, name2));

        return state;
    }

    @Override
    public DependentVar.DepVar2<String, String> observeValues(String name1, String name2) {
        DepVar2Impl<String, String> state = new DepVar2Impl<>(
                () -> Tuple2.of(
                        getPrimaryVar(name1).get().orElse(null),
                        getPrimaryVar(name2).get().orElse(null)));

        associateDepVarToPrimaryVars(state, Arrays.asList(name1, name2));

        return state;
    }

    private class DepVar2Impl<T1, T2> extends RegisteredVar<Tuple2<T1, T2>> implements DependentVar.DepVar2<T1, T2> {

        DepVar2Impl(Supplier<Tuple2<T1, T2>> supplier) {
            super(supplier);
        }

        @Override
        public <C1, C2> DepVar2<C1, C2> mapArgs(Function<Tuple2<T1, T2>, Tuple2<C1, C2>> mapper) {
            DepVar2Impl<C1, C2> state = new DepVar2Impl<>(() -> mapper.apply(get()));
            associateDepVarToPrimaryVars(state, varSources.get(this));
            return state;
        }
    }

    @Override
    public DependentVar.DepVar3<Optional<String>, Optional<String>, Optional<String>> observe(String name1, String name2, String name3) {
        DepVar3Impl<Optional<String>, Optional<String>, Optional<String>> state = new DepVar3Impl<>(
                () -> Tuple3.of(getPrimaryVar(name1).get(), getPrimaryVar(name2).get(), getPrimaryVar(name3).get()));

        associateDepVarToPrimaryVars(state, Arrays.asList(name1, name2, name3));

        return state;
    }

    @Override
    public DependentVar.DepVar3<String, String, String> observeValues(String name1, String name2, String name3) {
        DepVar3Impl<String, String, String> state = new DepVar3Impl<>(
                () -> Tuple3.of(
                        getPrimaryVar(name1).get().orElse(null),
                        getPrimaryVar(name2).get().orElse(null),
                        getPrimaryVar(name3).get().orElse(null)));

        associateDepVarToPrimaryVars(state, Arrays.asList(name1, name2, name3));

        return state;
    }

    private class DepVar3Impl<T1, T2, T3> extends RegisteredVar<Tuple3<T1, T2, T3>> implements
            DependentVar.DepVar3<T1, T2, T3> {

        DepVar3Impl(Supplier<Tuple3<T1, T2, T3>> supplier) {
            super(supplier);
        }

        @Override
        public <C1, C2, C3> DepVar3<C1, C2, C3> mapArgs(Function<Tuple3<T1, T2, T3>, Tuple3<C1, C2, C3>> mapper) {
            DepVar3Impl<C1, C2, C3> state = new DepVar3Impl<>(() -> mapper.apply(get()));
            associateDepVarToPrimaryVars(state, varSources.get(this));
            return state;
        }
    }

    @Override
    public DependentVar.DepVar4<Optional<String>, Optional<String>, Optional<String>, Optional<String>> observe(String name1, String name2, String name3, String name4) {
        DepVar4Impl<Optional<String>, Optional<String>, Optional<String>, Optional<String>> state = new DepVar4Impl<>(
                () -> Tuple4.of(getPrimaryVar(name1).get(), getPrimaryVar(name2).get(), getPrimaryVar(name3).get(), getPrimaryVar(name4).get()));

        associateDepVarToPrimaryVars(state, Arrays.asList(name1, name2, name3, name4));

        return state;
    }

    @Override
    public DependentVar.DepVar4<String, String, String, String> observeValues(String name1, String name2, String name3, String name4) {
        DepVar4Impl<String, String, String, String> state = new DepVar4Impl<>(
                () -> Tuple4.of(
                        getPrimaryVar(name1).get().orElse(null),
                        getPrimaryVar(name2).get().orElse(null),
                        getPrimaryVar(name3).get().orElse(null),
                        getPrimaryVar(name4).get().orElse(null)));

        associateDepVarToPrimaryVars(state, Arrays.asList(name1, name2, name3, name4));

        return state;
    }

    private class DepVar4Impl<T1, T2, T3, T4> extends RegisteredVar<Tuple4<T1, T2, T3, T4>> implements
            DependentVar.DepVar4<T1, T2, T3, T4> {

        DepVar4Impl(Supplier<Tuple4<T1, T2, T3, T4>> supplier) {
            super(supplier);
        }

        @Override
        public <C1, C2, C3, C4> DepVar4<C1, C2, C3, C4> mapArgs(Function<Tuple4<T1, T2, T3, T4>, Tuple4<C1, C2, C3, C4>> mapper) {
            DepVar4Impl<C1, C2, C3, C4> state = new DepVar4Impl<>(() -> mapper.apply(get()));
            associateDepVarToPrimaryVars(state, varSources.get(this));
            return state;
        }
    }

    @Override
    public DependentVar.DepVar5<Optional<String>, Optional<String>, Optional<String>, Optional<String>, Optional<String>> observe(String name1, String name2, String name3, String name4, String name5) {
        DepVar5Impl<Optional<String>, Optional<String>, Optional<String>, Optional<String>, Optional<String>> state = new DepVar5Impl<>(
                () -> Tuple5.of(getPrimaryVar(name1).get(), getPrimaryVar(name2).get(), getPrimaryVar(name3).get(), getPrimaryVar(name4).get(), getPrimaryVar(name5).get()));

        associateDepVarToPrimaryVars(state, Arrays.asList(name1, name2, name3, name4, name5));

        return state;
    }

    @Override
    public DependentVar.DepVar5<String, String, String, String, String> observeValues(String name1, String name2, String name3, String name4, String name5) {
        DepVar5Impl<String, String, String, String, String> state = new DepVar5Impl<>(
                () -> Tuple5.of(
                        getPrimaryVar(name1).get().orElse(null),
                        getPrimaryVar(name2).get().orElse(null),
                        getPrimaryVar(name3).get().orElse(null),
                        getPrimaryVar(name4).get().orElse(null),
                        getPrimaryVar(name5).get().orElse(null)));

        associateDepVarToPrimaryVars(state, Arrays.asList(name1, name2, name3, name4, name5));

        return state;
    }

    private class DepVar5Impl<T1, T2, T3, T4, T5> extends RegisteredVar<Tuple5<T1, T2, T3, T4, T5>> implements
            DependentVar.DepVar5<T1, T2, T3, T4, T5> {

        DepVar5Impl(Supplier<Tuple5<T1, T2, T3, T4, T5>> supplier) {
            super(supplier);
        }

        @Override
        public <C1, C2, C3, C4, C5> DepVar5<C1, C2, C3, C4, C5> mapArgs(Function<Tuple5<T1, T2, T3, T4, T5>, Tuple5<C1, C2, C3, C4, C5>> mapper) {
            DepVar5Impl<C1, C2, C3, C4, C5> state = new DepVar5Impl<>(() -> mapper.apply(get()));
            associateDepVarToPrimaryVars(state, varSources.get(this));
            return state;
        }
    }
}
