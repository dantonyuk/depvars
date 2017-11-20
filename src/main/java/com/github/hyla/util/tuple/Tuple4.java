package com.github.hyla.util.tuple;

import java.util.function.Function;

public class Tuple4<T1, T2, T3, T4> {

    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    public final T4 _4;

    private Tuple4(T1 _1, T2 _2, T3 _3, T4 _4){
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
    }

    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 _1, T2 _2, T3 _3, T4 _4) {
        return new Tuple4<>(_1, _2, _3, _4);
    }

    public <C> Tuple4<C, T2, T3, T4> map1(Function<T1, C> fun) {
        return Tuple4.of(fun.apply(_1), _2, _3, _4);
    }

    public <C> Tuple4<T1, C, T3, T4> map2(Function<T2, C> fun) {
        return Tuple4.of(_1, fun.apply(_2), _3, _4);
    }

    public <C> Tuple4<T1, T2, C, T4> map3(Function<T3, C> fun) {
        return Tuple4.of(_1, _2, fun.apply(_3), _4);
    }

    public <C> Tuple4<T1, T2, T3, C> map4(Function<T4, C> fun) {
        return Tuple4.of(_1, _2, _3, fun.apply(_4));
    }
}

