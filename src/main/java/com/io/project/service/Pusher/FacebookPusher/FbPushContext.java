package com.io.project.service.Pusher.FacebookPusher;

import com.io.project.service.Pusher.FacebookPusher.Strategy.FbLinkPusher;
import com.io.project.service.Pusher.FacebookPusher.Strategy.FbPushStrategy;
import com.io.project.service.Pusher.FacebookPusher.Strategy.FbTargetIdPusher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FbPushContext {
    private final FbPushStrategy fbPushStrategy;
    private static final Logger logger = LoggerFactory.getLogger(FbPushContext.class);

    public FbPushContext(String action) {
        logger.info("Fb post action:{}",action);
        if (action.equals("add-url")){
            this.fbPushStrategy = new FbLinkPusher();
        }
        else this.fbPushStrategy = new FbTargetIdPusher();
    }

    public String push( String target_id_or_url){
        return this.fbPushStrategy.push(target_id_or_url);
    }
}
