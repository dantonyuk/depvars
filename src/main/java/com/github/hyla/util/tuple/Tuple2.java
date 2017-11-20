package com.github.hyla.util.tuple;

import java.util.function.Function;

public class Tuple2<T1, T2> {

    public final T1 _1;
    public final T2 _2;

    private Tuple2(T1 _1, T2 _2){
        this._1 = _1;
        this._2 = _2;
    }

    public static <T1, T2> Tuple2<T1, T2> of(T1 _1, T2 _2) {
        return new Tuple2<>(_1, _2);
    }

    public <C> Tuple2<C, T2> map1(Function<T1, C> fun) {
        return Tuple2.of(fun.apply(_1), _2);
    }

    public <C> Tuple2<T1, C> map2(Function<T2, C> fun) {
        return Tuple2.of(_1, fun.apply(_2));
    }
}

