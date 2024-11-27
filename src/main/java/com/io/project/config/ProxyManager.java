package com.io.project.config;

import com.io.project.ThreadManager.MyThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class ProxyManager  {
    private final BlockingQueue<Proxy> proxyQueue = new LinkedBlockingQueue<>();
    private final MyThreadPoolExecutor executor = new MyThreadPoolExecutor(1, this.getClass().getSimpleName());

    public static ProxyManager getInstance() {
        return LazyHolder.instance;
    }

    public Proxy poll() {
        if (!proxyQueue.isEmpty()) {
            return proxyQueue.poll();
        }
        return null;
    }

    public void init() {
        executor.submit(this::run);
        log.info("proxy-manager started!");
    }

    public void run() {
        while (true) {
            if (proxyQueue.isEmpty()) {
                try {
                    List<String> proxies = Files.readAllLines(Paths.get("src\\main\\resources\\config\\proxy.properties"), StandardCharsets.ISO_8859_1);
                    log.info("proxies: {}",proxies);
                    if (!proxies.isEmpty()) {
                        for (String proxy : proxies) {
                            String[] arr = proxy.split(":");
                            String host = arr[0].trim();
                            int port = Integer.parseInt(arr[1].trim());
                            proxyQueue.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)));
                        }
                    }
                } catch (Exception e) {
                    log.error("Init proxy fail:", e);
                }
            }
        }
    }

    private static class LazyHolder {
        private static ProxyManager instance = new ProxyManager();
    }
}