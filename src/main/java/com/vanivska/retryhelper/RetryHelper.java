package com.vanivska.retryhelper;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.lang.Math.pow;

public class RetryHelper {
    public static <T> T retry(Callable<T> task, int maxAttemps , Duration time) throws Exception {
        if(task ==null){
            throw new NullPointerException("task is null");
        }
        if(maxAttemps <= 0 ){
            throw new IllegalArgumentException("maxAttemps must be greater than 0");
        }
        if(time == null || time.isNegative()){
            throw new IllegalArgumentException("time must be greater than or equal to 0");
        }
        for (int i = 1; i <= maxAttemps; i++) {
            try {
                return task.call();
            } catch (Exception e) {
                if (i == maxAttemps) {
                    throw e;
                }
                try {
                    Thread.sleep(time.toMillis());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw ie;
                }
            }
        }
        throw new IllegalStateException("Unreachable: retry logic exited without return or throw");
    }


    public static <T> T retry(Callable<T> task, int maxAttemps , Duration time, Predicate<Throwable> predicate) throws Exception {
        if(task ==null){
            throw new NullPointerException("task is null");
        }
        if(maxAttemps <= 0 ){
            throw new IllegalArgumentException("maxAttemps must be greater than 0");
        }
        if(time == null || time.isNegative()){
            throw new IllegalArgumentException("time must be greater than or equal to 0");
        }
        if(predicate == null){
            throw new NullPointerException("predicate is null");
        }

        for(int i = 1; i <= maxAttemps; i++) {
            try{
                return task.call();
            }catch(Exception e){
                if(!predicate.test(e)) {
                    throw e;
                }

                if(i == maxAttemps) {
                    throw e;
                }

                try {
                    Thread.sleep(time.toMillis());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw ie;
                }
            }
        }
        throw new IllegalStateException("Unreachable: retry logic exited without return or throw");
    }

    public static <T> T retry(Callable<T> task, int maxAttemps ,Duration time, Predicate<Throwable> predicate, BiConsumer<Integer, Exception> onRetry) throws Exception {
        if(task ==null){
            throw new NullPointerException("task is null");
        }
        if(maxAttemps <= 0 ){
            throw new IllegalArgumentException("maxAttemps must be greater than 0");
        }
        if(time == null || time.isNegative()){
            throw new IllegalArgumentException("time must be greater than or equal to 0");
        }

        for(int i = 1; i <= maxAttemps; i++) {
            try{
                return task.call();
            }catch(Exception e){
                boolean shouldRetry = (predicate == null) || predicate.test(e);

                if(!shouldRetry) {
                    throw e;
                }
                if(i == maxAttemps) {
                    throw e;
                }

                if (onRetry != null) {
                    onRetry.accept(i, e);
                }

                try {
                    Thread.sleep(time.toMillis());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw ie;
                }
            }
        }

        throw new IllegalStateException("Unreachable: retry logic exited without return or throw");
    }
    public static <T> T exponentialRetry(Callable<T> task, int maxAttemps ) throws Exception {
        if(task ==null){
            throw new NullPointerException("task is null");
        }
        if(maxAttemps <= 0 ){
            throw new IllegalArgumentException("maxAttemps must be greater than 0");
        }
        for(int i = 1; i <= maxAttemps; i++) {
            try{
                return task.call();
            }
            catch(Exception e){
                if(i == maxAttemps) {
                    throw e;
                }
                try{
                    long baseDelay = 1000;
                    long delayMillis = baseDelay * (long) Math.pow(2, i - 1);
                    System.out.println(delayMillis);
                    Thread.sleep(delayMillis);
                }catch(InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw ie;
                }
            }
        }
        throw new IllegalStateException("Unreachable: retry logic exited without return or throw");
    }

}