LIMIT = 5

for dim in range(2, LIMIT + 1):
    def nums(till=dim):
        return range(1, till + 1)

    def commasep(pat, till=dim):
        return ", ".join([pat.format(i) for i in nums(till)])

    print("""
        @Override
        public DependentVar.DepVar{dim}<{opttypes}> observe({nameArgs}) {{
            DepVar{dim}Impl<{opttypes}> var = new DepVar{dim}Impl<>(
                    () -> Tuple{dim}.of({optTupleParams}));

            associateDepVarToPrimaryVars(var, Arrays.asList({nameParams}));

            return var;
        }}

        @Override
        public DependentVar.DepVar{dim}<{strtypes}> observeValues({nameArgs}) {{
            DepVar{dim}Impl<{strtypes}> var = new DepVar{dim}Impl<>(
                    () -> Tuple{dim}.of({tupleParams}));

            associateDepVarToPrimaryVars(var, Arrays.asList({nameParams}));

            return var;
        }}

        private class DepVar{dim}Impl<{types}> extends RegisteredVar<Tuple{dim}<{types}>> implements DependentVar.DepVar{dim}<{types}> {{

            DepVar{dim}Impl(Supplier<Tuple{dim}<{types}>> supplier) {{
                super(supplier);
            }}

            @Override
            public <{ctypes}> DepVar{dim}<{ctypes}> mapArgs(Function<Tuple{dim}<{types}>, Tuple{dim}<{ctypes}>> mapper) {{
                DepVar{dim}Impl<{ctypes}> var = new DepVar{dim}Impl<>(() -> mapper.apply(get()));
                associateDepVarToPrimaryVars(var, varSources.get(this));
                return var;
            }}
        }}""".format(dim=dim, types=commasep("T{0}"), ctypes=commasep("C{0}"),
               strtypes=commasep("String"), opttypes=commasep("Optional<String>"),
               nameArgs=commasep("String name{0}"), nameParams=commasep("name{0}"),
               optTupleParams=commasep("getPrimaryVar(name{0}).get()"),
               tupleParams=",".join(["\n                            getPrimaryVar(name{0}).get().orElse(null)".
                                    format(i) for i in nums()])))
