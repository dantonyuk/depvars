package com.github.hyla.util;

import java.util.Collection;
import java.util.Set;

public interface MultiMap<K, V> {

    void add(K key, V value);

    void addAll(K key, Collection<V> values);

    Set<V> get(K key);
}
