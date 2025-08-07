# RetryHelper

A reusable Java retry library with customizable backoff strategies, result and exception predicates, and optional fallback support.

---

## What is RetryHelper?

RetryHelper is a lightweight Java utility library designed to simplify retry logic in your applications. It lets you automatically retry operations that fail due to exceptions or undesired results, with configurable:

- Maximum retry attempts
- Exception or result-based retry conditions (predicates)
- Backoff strategies (fixed, exponential, incremental)
- Retry event listeners for logging or metrics
- Fallback suppliers for graceful degradation when retries are exhausted

This library is ideal for handling transient failures in network calls, database queries, or any operation that may intermittently fail or produce unwanted results.

---

## Features

- Retry on exceptions matching a customizable predicate  
- Retry based on result value predicate  

- Listener callback support on retry attempts  
- Support for fallback values when retry fails  
- Easy to configure with a fluent builder API  

---

## How to Add RetryHelper to Your Project

Since RetryHelper is not published to Maven Central, add it manually:

### Option 1: Add the JAR directly

1. Clone or download this repo.  
2. Build the project with Maven:  
   ```bash
   mvn clean install
3. Locate the JAR file at target/RetryHelper-1.0.jar.
4. Copy the JAR file to your project's libs folder or appropriate location.
5. Add the JAR to your project’s classpath.

### Option 2: Install as a local Maven dependency

1. Build the project as described in Option 1.

2. Run the following command to install the JAR into your local Maven repository:

   ```bash
   mvn install:install-file -Dfile=target/RetryHelper-1.0.jar -DgroupId=org.vanivska -DartifactId=RetryHelper -Dversion=1.0 -Dpackaging=jar

3. Add the dependency to your project's pom.xml:
  ```xml
   <dependency>
       <groupId>org.vanivska</groupId>
       <artifactId>RetryHelper</artifactId>
       <version>1.0</version>
   </dependency>
```


## Usage Examples
### Retry on Exception
```java
RetryPolicy<String> policy = new RetryPolicy.Builder<String>()
    .maxAttempts(5)
    .predicate(ex -> ex instanceof IOException)
    .backOffStrategy(new FixedBackOffStrategy(Duration.ofSeconds(1)))
    .retryListener((attempt, exception) -> 
        System.out.println("Attempt " + attempt + " failed: " + exception.getMessage()))
    .build();

Callable<String> task = () -> {
    if (Math.random() < 0.7) throw new IOException("Temporary failure");
    return "Success";
};

try {
    String result = RetryExecutor.retryOnException(task, policy);
    System.out.println("Result: " + result);
} catch (Exception e) {
    System.err.println("Operation failed after retries: " + e.getMessage());
}
```
### Retry on Result 
```java
RetryPolicy<Integer> policy = new RetryPolicy.Builder<Integer>()
    .maxAttempts(3)
    .result(result -> result > 0) // retry if result <= 0
    .backOffStrategy(new FixedBackOffStrategy(Duration.ofMillis(500)))
    .retryListener((attempt, ex) -> 
        System.out.println("Attempt " + attempt + " returned invalid result"))
    .build();

Callable<Integer> task = () -> {
    int value = (int) (Math.random() * 3) - 1; // could be negative, zero, or positive
    System.out.println("Generated value: " + value);
    return value;
};

Supplier<Integer> fallback = () -> -999;

try {
    int result = RetryExecutor.retryOnResult(task, policy, fallback);
    System.out.println("Final result: " + result);
} catch (Exception e) {
    System.err.println("Operation failed: " + e.getMessage());
}
```
