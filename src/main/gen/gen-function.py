from __future__ import print_function

TUPLES_NUM=5

for dim in range(2, TUPLES_NUM + 1):
    def nums(till=dim):
        return range(1, till + 1)

    def commasep(pat, till=dim):
        return ", ".join([pat.format(i) for i in nums(till)])

    with open("Function{0}.java".format(dim), "w") as java_file:
        def p(s=""):
            print(s, file=java_file)

        p("""package com.github.hyla.util.function;

@FunctionalInterface
public interface Function{dim}<{types}, R> {{

    R apply({args});
}}
""".format(dim=dim, types=commasep("T{0}"), args=commasep("T{0} t{0}")))
