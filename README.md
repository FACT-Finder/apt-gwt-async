# Java Annotation Processor GWT Async Services 

[![Build Status](https://travis-ci.org/FACT-Finder/apt-gwt-async.svg?branch=master)](https://travis-ci.org/FACT-Finder/apt-gwt-async)
[![](https://jitpack.io/v/fact-finder/apt-gwt-async.svg)](https://jitpack.io/#fact-finder/apt-gwt-async)


## GWT-RPC

The client side of the [GWT-RPC][gwtrpc] mechanism requires an asynchronous variant of the service interface. 
An example synchronous service from the official documentation:


```java
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("stockPrices")
public interface StockPriceService extends RemoteService {

  StockPrice[] getPrices(String[] symbols);
}
```

... must be turned into ...

```java
package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StockPriceServiceAsync {

  void getPrices(String[] symbols, AsyncCallback<StockPrice[]> callback);
}
```

in order to have a type which fits the asynchronous (callback-based) programming model of the client. 
This annotation processor can make this transformation automatically, and therefore has the following benefits:

* no longer requires developers to hand-code async services,
* no longer requires manual intervention to generate async services using IDE facilities,
* no longer requires to keep the generated sources under version control,
* is not tied to a specific build tool (as opposed to [GWT Maven Plugin][mavenplugin], for instance).

## Usage

Every service interface annotated with `@RemoteServiceRelativePath` has its async variant generated automatically. If this annotation is absent, async service generation can be turned on using `@AsyncService`:

```java
import net.omikron.apt.gwt.AsyncService;

@AsyncService
public interface MyService { ... }
```

## Dependency Metadata

Grab the most recent release from [JitPack][jitpack]. An complete Gradle configuration is shown below:

```groovy
repositories {
  mavenCentral()
  maven { url "https://jitpack.io" }
}

apply plugin: 'java'

dependencies {
  compileOnly 'com.github.fact-finder.apt-gwt-async:apt-gwt-async-annotation:1.0'
  annotationProcessor 'com.github.fact-finder.apt-gwt-async:apt-gwt-async-processor:1.0'
}
```



[gwtrpc]: http://www.gwtproject.org/doc/latest/DevGuideServerCommunication.html
[mavenplugin]: https://gwt-maven-plugin.github.io/gwt-maven-plugin/user-guide/async.html
[jitpack]: https://jitpack.io/
