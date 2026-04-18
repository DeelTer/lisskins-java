# Lisskins Java Client

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/Java-17%2B-orange)](https://adoptium.net/)
[![Maven Central](https://img.shields.io/maven-central/v/ru.deelter.lisskins/lisskins-client)](https://search.maven.org/)

A modern, type-safe Java client for the [LIS-SKINS API](https://lis-skins.com).  
Supports synchronous and asynchronous operations, WebSocket real-time events, automatic rate‑limit retries, and clean DTOs with Jackson.

## ✨ Features

- ✅ Full coverage of LIS-SKINS Public API v1
- ⚡ Synchronous and asynchronous (`CompletableFuture`) methods
- 📡 WebSocket support via Centrifugo (optional)
- 🔁 Automatic retry on `429 Too Many Requests` with `Retry-After` header
- 📦 Clean model classes with Lombok and `Instant` support
- 📖 Extensive JavaDoc with JSON examples
- 🧩 String constants for games, sort orders, statuses, and error codes

## 📋 Requirements

- Java 17 or higher
- Maven 3.6+ (or Gradle)

## 📦 Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>ru.deelter.lisskins</groupId>
    <artifactId>lisskins-client</artifactId>
    <version>1.0.0</version>
</dependency>

Add dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>ru.deelter.lisskins</groupId>
    <artifactId>lisskins-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 🚀 Quick Start
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
		
        BalanceResponse balance = client.getBalance();
        System.out.println("Balance: " + balance.getData().getBalance());

        // Search for CS:GO skins
        SearchRequest search = SearchRequest.builder()
                .game(ApiConstants.Game.CSGO)
                .priceFrom(1.0)
                .priceTo(100.0)
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
## 🔌 Websocket (Real-time events)
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
        // Purchase status changed (e.g., wait_accept → accepted)
        System.out.println("Purchase update: " + json);
    });
    
    ws.connect();   // starts connection and subscribes
    
    // Don't forget to disconnect on shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(ws::disconnect));
```
## Constants
```java 
    import static ru.deelter.lisskins.constants.ApiConstants.Game.*;
    import static ru.deelter.lisskins.constants.ApiConstants.PurchaseStatus.*;
    
    SearchRequest req = SearchRequest.builder()
            .game(CSGO)
            .sortBy(ApiConstants.SortBy.NEWEST)
            .build();
    
    if (skin.getStatus().equals(WAIT_ACCEPT)) {
        // ...
    }
```