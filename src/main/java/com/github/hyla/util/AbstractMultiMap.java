package com.github.hyla.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public abstract class AbstractMultiMap<K, V> implements MultiMap<K, V> {

    private Map<K, Set<V>> map;

    AbstractMultiMap() {
        this.map = createMap();
    }

    protected abstract Map<K,Set<V>> createMap();

    protected abstract Function<? super K, ? extends Set<V>> getEmptySet();

    @Override
    public void add(K key, V value) {
        get(key).add(value);
    }

    @Override
    public void addAll(K key, Collection<V> values) {
        get(key).addAll(values);
    }

    @Override
    public Set<V> get(K key) {
        return map.computeIfAbsent(key, getEmptySet());
    }
}
