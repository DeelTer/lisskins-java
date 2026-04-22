# Lisskins Java Client

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/Java-17%2B-orange)](https://adoptium.net/)
[![JitPack](https://img.shields.io/jitpack/v/com.github.DeelTer/lisskins-java)](https://jitpack.io/#DeelTer/lisskins-java)

A modern, type-safe Java client for the [LIS-SKINS API](https://lis-skins.com).  
Supports synchronous and asynchronous operations, WebSocket real-time events, automatic rate-limit retries, and clean DTOs with Jackson.

## Disclaimer
This is an unofficial client library and is not affiliated with, endorsed by,
or connected to LIS-SKINS. The API service itself is owned and operated by LIS-SKINS.

## Features

- Full coverage of LIS-SKINS Public API v1
- Synchronous and asynchronous (`CompletableFuture`) methods
- WebSocket support via Centrifugo (optional)
- Automatic retry on `429 Too Many Requests` with `Retry-After` header
- Clean model classes with Lombok and `Instant` support
- Extensive JavaDoc with JSON examples
- String constants for games, sort orders, statuses, and error codes

## Requirements

- Java 17 or higher
- Maven 3.6+ (or Gradle)

## Installation

### Maven

Add the JitPack repository and the dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.DeelTer</groupId>
        <artifactId>lisskins-java</artifactId>
        <version>1.0.4</version>
    </dependency>
</dependencies>
```

### Gradle (Groovy DSL)

Add the JitPack repository and the dependency to your build.gradle:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.DeelTer:lisskins-java:1.0.4'
}
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.DeelTer:lisskins-java:1.0.4")
}
```

## Quick Start

```java
import ru.deelter.lisskins.LisskinsClient;
import ru.deelter.lisskins.constants.ApiConstants;
import ru.deelter.lisskins.model.*;

import java.util.List;

public class Example {
	
    public static void main(String[] args) {
        LisskinsClient client = LisskinsClient.builder()
                .apiKey("your-api-key")
                .debug(true)          // enable logging
                .userId("12345")      // optional, for WebSocket
                .build();

        // Get balance
        BalanceResponse balance = client.getBalance();
        System.out.println("Balance: " + balance.getData().getBalance());

        // Search for CS2 skins
        SearchRequest search = SearchRequest.builder()
                .game(ApiConstants.Game.CSGO)
                .priceFrom(1.0)
                .priceTo(100.0)
                .perPage(50)
                .sortBy(ApiConstants.SortBy.LOWEST_PRICE)
                .build();
        SearchResponse result = client.search(search);
        result.getData().forEach(skin ->
                System.out.println(skin.getName() + " - $" + skin.getPrice()));

        // Buy a skin (synchronous)
        BuyRequest buy = BuyRequest.builder()
                .ids(List.of(125345))
                .partner("123456789")
                .token("abcdef")
                .maxPrice(2.50)
                .customId("order-123")
                .build();
        BuyResponse purchase = client.buy(buy);
        System.out.println("Purchase ID: " + purchase.getData().getPurchaseId());

        // Asynchronous example
        client.getBalanceAsync()
                .thenAccept(b -> System.out.println("Async balance: " + b.getData().getBalance()))
                .exceptionally(ex -> { ex.printStackTrace(); return null; });
    }
}
```

### WebSocket (Real-time events)

```java
LisskinsClient client = LisskinsClient.builder()
        .apiKey("...")
        .userId("12345")
        .build();

LisskinsWebSocketManager ws = client.getWebSocketManager();
ws.setOnObtainedSkinListener(json -> {
    // Parse JSON: new skin, price change, or deleted
    System.out.println("Market event: " + json);
});
ws.setOnPurchaseUpdateListener(json -> {
    // Purchase status changed (e.g., wait_accept -> accepted)
    System.out.println("Purchase update: " + json);
});

ws.connect();   // starts connection and subscribes

// Don't forget to disconnect on shutdown
Runtime.getRuntime().addShutdownHook(new Thread(ws::disconnect));
```

### Constants
```java

import static ru.deelter.lisskins.constants.ApiConstants.Game.*;
import static ru.deelter.lisskins.constants.ApiConstants.PurchaseStatus.*;
import static ru.deelter.lisskins.constants.ApiConstants.SortBy.*;
import static ru.deelter.lisskins.constants.ApiConstants.ErrorCode.*;
import static ru.deelter.lisskins.constants.ApiConstants.ReturnReason.*;
import static ru.deelter.lisskins.constants.ApiConstants.WebSocketEvent.*;

SearchRequest req = SearchRequest.builder()
        .game(CSGO)
        .sortBy(NEWEST)
        .build();

if (skin.getStatus().equals(WAIT_ACCEPT)) {
    // ...
}
```

### Error Handling

All API methods throw LisskinsApiException when a request fails. The exception contains the HTTP status code and the error body (if available).

```java
try {
    client.buy(buyRequest);
} catch (LisskinsApiException e) {
    System.err.println("HTTP " + e.getCode() + ": " + e.getMessage());
    System.err.println("Error body: " + e.getErrorBody());
}
```

### Logging

The library uses SLF4J for logging. Add a binding like slf4j-simple or logback-classic to your classpath to see the logs.

# Documentation
* [Official LIS-SKINS API Documentation](https://lis-skins.stoplight.io/docs/lis-skins/dzq78x3edc19r-api-overview)
* JavaDoc is included in the source code for all public classes and methods.