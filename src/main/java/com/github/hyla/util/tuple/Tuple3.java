package com.github.hyla.util.tuple;

import java.util.function.Function;

public class Tuple3<T1, T2, T3> {

    public final T1 _1;
    public final T2 _2;
    public final T3 _3;

    private Tuple3(T1 _1, T2 _2, T3 _3){
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }

    public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 _1, T2 _2, T3 _3) {
        return new Tuple3<>(_1, _2, _3);
    }

    public <C> Tuple3<C, T2, T3> map1(Function<T1, C> fun) {
        return Tuple3.of(fun.apply(_1), _2, _3);
    }

    public <C> Tuple3<T1, C, T3> map2(Function<T2, C> fun) {
        return Tuple3.of(_1, fun.apply(_2), _3);
    }

    public <C> Tuple3<T1, T2, C> map3(Function<T3, C> fun) {
        return Tuple3.of(_1, _2, fun.apply(_3));
    }
}

