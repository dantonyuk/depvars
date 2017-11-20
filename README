This is a proof-of-concept implementation of dependent variables.

Dependent variable is just a variable which value is changed depending on the changes of some other variables.

E.g. we have dependent variable like this:

```java
DependentVar<HttpClient> httpClient = depVarContext
        .observe(TIMEOUT, MAX_CONN, MAX_TOTAL_CONN)
        .map1(Integer::valueOf)
        .map2(Integer::valueOf)
        .map3(Integer::valueOf)
        .map((timeout, maxConn, maxTotalConn) -> {
            MultiThreadedHttpConnectionManager connectionMgr = new MultiThreadedHttpConnectionManager();
            HttpConnectionManagerParams params = new HttpConnectionManagerParams();
            params.setSoTimeout(timeout * 1000);
            params.setConnectionTimeout(timeout * 1000);
            params.setDefaultMaxConnectionsPerHost(maxConn);
            params.setMaxTotalConnections(maxTotalConn);
            connectionMgr.setParams(params);
            httpClient = new HttpClient(connectionMgr);
            httpClient.getParams().setAuthenticationPreemptive(true);
            return httpClient
        });
```

We can use it this way:

```java
httpClient.get().executeMethod(getMethod);
```

The value of this variable will not be reinitialized till any of primary variables, such as`timeout`, `maxConn` or 
`maxTotalConn` is changed. As soon as any if primary variable is change, a dependent variable will be changed as well.
This change is transparent for the client code, so we can use same `depVar.get()`.
