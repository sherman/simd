# Full hardware spec

https://linux-hardware.org/?probe=dfa95a4efd

# VM version

JDK 16.0.1, OpenJDK 64-Bit Server VM, 16.0.1+9-Ubuntu-120.04

# Task

The task is to check, whether an intersection between two sorted arrays.
Examples:

[1, 3, 4] and [2, 3, 5] => returns true

[2, 3, 4] and [1, 8, 10] => returns false

# How to build/run?

```bash
mvn clean install -DskipTests=true
```

```bash
/usr/lib/jvm/java-16-openjdk-amd64/bin/java --enable-preview --add-modules=jdk.incubator.vector -jar target/FunMicro-benchmarks.jar NoIntersectionBenchmark
```

# Benchmarks

## No intersection between the arrays (worst case)

```
Benchmark                                                  Mode  Cnt  Score   Error   Units
NoIntersectionBenchmark.hasNoIntersectionScalar           thrpt   10  2.029 ± 0.015  ops/us
NoIntersectionBenchmark.hasNoIntersectionVector           thrpt   10  3.394 ± 0.015  ops/us
NoIntersectionBenchmark.hasNoIntersectionVectorShuffling  thrpt   10  1.763 ± 0.004  ops/us    
```

## An intersection in the middle of the arrays

```
Benchmark                                                Mode  Cnt  Score   Error   Units
IntersectionOnTheMiddleBenchmark.hasIntersectionScalar  thrpt   10  3.730 ± 0.040  ops/us
IntersectionOnTheMiddleBenchmark.hasIntersectionVector  thrpt   10  7.295 ± 0.070  ops/us
```

## An intersection in the beginning of the arrays

```
Benchmark                                                   Mode  Cnt    Score   Error   Units
IntersectionOnTheBeginningBenchmark.hasIntersectionScalar  thrpt   10   73.002 ± 1.037  ops/us
IntersectionOnTheBeginningBenchmark.hasIntersectionVector  thrpt   10  104.260 ± 0.849  ops/us
```

