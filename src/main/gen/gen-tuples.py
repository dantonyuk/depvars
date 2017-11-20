from __future__ import print_function

TUPLES_NUM=5

for NUM in range(2, TUPLES_NUM + 1):
    def nums(till=NUM):
        return range(1, till + 1)
    
    def commasep(pat, till=NUM):
        return ", ".join([pat.format(i) for i in nums(till)])

    with open("Tuple{0}.java".format(NUM), "w") as java_file:
        def p(s=""):
            print(s, file=java_file)

        p("package com.github.hyla.util.tuple;")
        p()
        p("import java.util.function.Function;")
        p()
        p("public class Tuple{dim}<{types}> {{".format(
            dim=NUM, types=commasep("T{0}")))
        p()
        for i in nums():
            p("    public final T{num} _{num};".format(num=i))
        p()
        p("    private Tuple{dim}({args}){{".format(
            dim=NUM, args=commasep("T{0} _{0}")))
        for i in nums():
            p("        this._{num} = _{num};".format(num=i))
        p("    }")
        p()
        p("    public static <{types}> Tuple{dim}<{types}> of({args}) {{".format(
            dim=NUM, types=commasep("T{0}"), args=commasep("T{0} _{0}")))
        p("        return new Tuple{dim}<>({params});".format(
            dim=NUM, params=commasep("_{0}")))
        p("    }")
        for i in nums():
            p()
            p("    public <C> Tuple{dim}<{types}> map{num}(Function<T{num}, C> fun) {{".format(
                dim=NUM, num=i, types=commasep("T{0}").replace("T{0}".format(i), "C")))
            p("        return Tuple{dim}.of({params});".format(
                dim=NUM, params=commasep("_{0}")
                    .replace("_{0}".format(i), "fun.apply(_{0})".format(i))))
            p("    }")
        p("}")
        p()
