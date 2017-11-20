LIMIT = 5

for dim in range(2, LIMIT + 1):
    def nums(till=dim):
        return range(1, till + 1)

    def commasep(pat, till=dim):
        return ", ".join([pat.format(i) for i in nums(till)])

    print("""
    DependentVar.DepVar{dim}<{opttypes}> observe({args});

    DependentVar.DepVar{dim}<{strtypes}> observeValues({args});""".format(
        dim=dim, strtypes=commasep("String"), opttypes=commasep("Optional<String>"),
        args=commasep("String name{0}")))
