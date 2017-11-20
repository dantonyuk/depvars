package com.github.hyla.util;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class WeakValueMultiMap<K, V> extends AbstractMultiMap<K, V> {

    private static Function<Object, Set<Object>> EMPTY_SET = unused ->
            Collections.newSetFromMap(new WeakHashMap<>());

    @Override
    protected Map<K, Set<V>> createMap() {
        return new ConcurrentHashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Function<? super K, ? extends Set<V>> getEmptySet() {
        return (Function<? super K, ? extends Set<V>>) (Function) EMPTY_SET;
    }
}
