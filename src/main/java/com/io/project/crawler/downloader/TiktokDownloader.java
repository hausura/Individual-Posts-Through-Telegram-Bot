package com.io.project.crawler.downloader;

import com.io.project.config.ProxyManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.net.Proxy;

@Slf4j
public class TiktokDownloader {

    @SneakyThrows
    public static String download(String url){
        Proxy proxy;
        String res;
        HttpSupport httpSupport = HttpSupport.getInstances();
        int retry = 3;
        do {
            retry --;
            proxy= ProxyManager.getInstance().poll();
            res= httpSupport.sendGetRequest(url,proxy,"");
            log.info("Download tiktok content from url: {}, with proxy: {}",url,proxy);
            if (res!= null && proxy!=null) return res;
            else Thread.sleep(1000);
        }while (retry>0);
        return "";
    }

//    public static void main(String[] args) {
//        String res = TiktokDownloader.download("https://www.tiktok.com/@thamnhung1222");
//        System.out.println(res);
//    }
}
