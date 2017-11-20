LIMIT = 5

for dim in range(1, LIMIT + 1):
    def nums(till=dim):
        return range(1, till + 1)

    def commasep(pat, till=dim):
        return ", ".join([pat.format(i) for i in nums(till)])

    print("""
    interface DepVar{dim}<{types}> extends DependentVar<Tuple{dim}<{types}>> {{

        <{ctypes}> DepVar{dim}<{ctypes}> mapArgs(Function<Tuple{dim}<{types}>, Tuple{dim}<{ctypes}>> fun);

        default <R> DependentVar<R> map(Function{dim}<{types}, R> fun) {{
            return map(t -> fun.apply({funParams}));
        }}""".format(dim=dim, types=commasep("T{0}"), ctypes=commasep("C{0}"), funParams=commasep("t._{0}")))

    for i in nums():
        print("""
        default <C> DepVar{dim}<{types}> map{num}(Function<T{num}, C> fun) {{
            return mapArgs(t -> t.map{num}(fun));
        }}""".format(dim=dim, num=i, types=commasep("T{0}").replace("T{0}".format(i), "C")))

    print("    }")
