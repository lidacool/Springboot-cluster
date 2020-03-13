package com.lee.util.jredis.pipeline;

import redis.clients.jedis.MultiKeyPipelineBase;

public interface PipelineCommand {
    void execute(MultiKeyPipelineBase var1);
}
