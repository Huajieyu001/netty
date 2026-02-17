package top.huajieyu001.chapter2.chatroom.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Author huajieyu
 * @Date 2026/2/17 20:35
 * @Version 1.0
 * @Description TODO
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {
    public ProtocolFrameDecoder() {
        super(1024, 12, 4, 0, 0);
    }
}
