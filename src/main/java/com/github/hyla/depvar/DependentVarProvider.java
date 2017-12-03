package com.github.hyla.depvar;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface DependentVarProvider {

    DependentVar<List<Optional<String>>> observeVars(String... varNames);

    default DependentVar<String> observeVal(String varName) {
        return observeVal(varName, () -> null);
    }

    default DependentVar<String> observeVal(String varName, Supplier<String> defaultValue) {
        return this.<String>observeVars(varName).map(l -> l.get(0).orElseGet(defaultValue));
    }

    default DependentVar<Optional<String>> observeOptional(String varName) {
        return this.<Optional<String>>observeVars(varName).map(l -> l.get(0));
    }

    DependentVar.DepVar2<Optional<String>, Optional<String>> observe(String name1, String name2);

    DependentVar.DepVar2<String, String> observeValues(String name1, String name2);

    DependentVar.DepVar3<Optional<String>, Optional<String>, Optional<String>> observe(String name1, String name2, String name3);

    DependentVar.DepVar3<String, String, String> observeValues(String name1, String name2, String name3);

    DependentVar.DepVar4<Optional<String>, Optional<String>, Optional<String>, Optional<String>> observe(String name1, String name2, String name3, String name4);

    DependentVar.DepVar4<String, String, String, String> observeValues(String name1, String name2, String name3, String name4);

    DependentVar.DepVar5<Optional<String>, Optional<String>, Optional<String>, Optional<String>, Optional<String>> observe(String name1, String name2, String name3, String name4, String name5);

    DependentVar.DepVar5<String, String, String, String, String> observeValues(String name1, String name2, String name3, String name4, String name5);
}
