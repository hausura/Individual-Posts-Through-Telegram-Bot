package com.io.project.service.Pusher.TiktokPusher;

import com.io.project.service.Pusher.TiktokPusher.Strategy.TiktokKeywordPusher;
import com.io.project.service.Pusher.TiktokPusher.Strategy.TiktokLinkPusher;
import com.io.project.service.Pusher.TiktokPusher.Strategy.TiktokPushStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TiktokPushContext {
    private final TiktokPushStrategy tiktokPushStrategy;
    private static final Logger logger = LoggerFactory.getLogger(TiktokPushContext.class);

    public TiktokPushContext(String action) {
        logger.info("Tiktok post action:{}",action);
        if (action.equals("add-url")){
            this.tiktokPushStrategy = new TiktokLinkPusher();
        }
        else this.tiktokPushStrategy = new TiktokKeywordPusher();
    }

    public String push(String target_id_or_url,String action){
        return this.tiktokPushStrategy.push(target_id_or_url,action);
    }
}
