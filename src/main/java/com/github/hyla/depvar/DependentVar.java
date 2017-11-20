package com.github.hyla.depvar;

import com.github.hyla.util.function.Function2;
import com.github.hyla.util.function.Function3;
import com.github.hyla.util.function.Function4;
import com.github.hyla.util.function.Function5;
import com.github.hyla.util.tuple.Tuple2;
import com.github.hyla.util.tuple.Tuple3;
import com.github.hyla.util.tuple.Tuple4;
import com.github.hyla.util.tuple.Tuple5;

import java.util.function.Function;

public interface DependentVar<T> {

    T get();

    <R> DependentVar<R> map(Function<? super T, ? extends  R> fun);

//    <R> DependentVar<R> flatMap(Function<? super T, ? extends DependentVar<R>> fun);

    interface DepVar2<T1, T2> extends DependentVar<Tuple2<T1, T2>> {

        <C1, C2> DepVar2<C1, C2> mapArgs(Function<Tuple2<T1, T2>, Tuple2<C1, C2>> fun);

        default <R> DependentVar<R> map(Function2<T1, T2, R> fun) {
            return map(t -> fun.apply(t._1, t._2));
        }

        default <C> DepVar2<C, T2> map1(Function<T1, C> fun) {
            return mapArgs(t -> t.map1(fun));
        }

        default <C> DepVar2<T1, C> map2(Function<T2, C> fun) {
            return mapArgs(t -> t.map2(fun));
        }
    }

    interface DepVar3<T1, T2, T3> extends DependentVar<Tuple3<T1, T2, T3>> {

        <C1, C2, C3> DepVar3<C1, C2, C3> mapArgs(Function<Tuple3<T1, T2, T3>, Tuple3<C1, C2, C3>> fun);

        default <R> DependentVar<R> map(Function3<T1, T2, T3, R> fun) {
            return map(t -> fun.apply(t._1, t._2, t._3));
        }

        default <C> DepVar3<C, T2, T3> map1(Function<T1, C> fun) {
            return mapArgs(t -> t.map1(fun));
        }

        default <C> DepVar3<T1, C, T3> map2(Function<T2, C> fun) {
            return mapArgs(t -> t.map2(fun));
        }

        default <C> DepVar3<T1, T2, C> map3(Function<T3, C> fun) {
            return mapArgs(t -> t.map3(fun));
        }
    }

    interface DepVar4<T1, T2, T3, T4> extends DependentVar<Tuple4<T1, T2, T3, T4>> {

        <C1, C2, C3, C4> DepVar4<C1, C2, C3, C4> mapArgs(Function<Tuple4<T1, T2, T3, T4>, Tuple4<C1, C2, C3, C4>> fun);

        default <R> DependentVar<R> map(Function4<T1, T2, T3, T4, R> fun) {
            return map(t -> fun.apply(t._1, t._2, t._3, t._4));
        }

        default <C> DepVar4<C, T2, T3, T4> map1(Function<T1, C> fun) {
            return mapArgs(t -> t.map1(fun));
        }

        default <C> DepVar4<T1, C, T3, T4> map2(Function<T2, C> fun) {
            return mapArgs(t -> t.map2(fun));
        }

        default <C> DepVar4<T1, T2, C, T4> map3(Function<T3, C> fun) {
            return mapArgs(t -> t.map3(fun));
        }

        default <C> DepVar4<T1, T2, T3, C> map4(Function<T4, C> fun) {
            return mapArgs(t -> t.map4(fun));
        }
    }

    interface DepVar5<T1, T2, T3, T4, T5> extends DependentVar<Tuple5<T1, T2, T3, T4, T5>> {

        <C1, C2, C3, C4, C5> DepVar5<C1, C2, C3, C4, C5> mapArgs(Function<Tuple5<T1, T2, T3, T4, T5>, Tuple5<C1, C2, C3, C4, C5>> fun);

        default <R> DependentVar<R> map(Function5<T1, T2, T3, T4, T5, R> fun) {
            return map(t -> fun.apply(t._1, t._2, t._3, t._4, t._5));
        }

        default <C> DepVar5<C, T2, T3, T4, T5> map1(Function<T1, C> fun) {
            return mapArgs(t -> t.map1(fun));
        }

        default <C> DepVar5<T1, C, T3, T4, T5> map2(Function<T2, C> fun) {
            return mapArgs(t -> t.map2(fun));
        }

        default <C> DepVar5<T1, T2, C, T4, T5> map3(Function<T3, C> fun) {
            return mapArgs(t -> t.map3(fun));
        }

        default <C> DepVar5<T1, T2, T3, C, T5> map4(Function<T4, C> fun) {
            return mapArgs(t -> t.map4(fun));
        }

        default <C> DepVar5<T1, T2, T3, T4, C> map5(Function<T5, C> fun) {
            return mapArgs(t -> t.map5(fun));
        }
    }
}
