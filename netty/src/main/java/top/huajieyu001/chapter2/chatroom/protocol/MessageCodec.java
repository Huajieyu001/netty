package top.huajieyu001.chapter2.chatroom.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import top.huajieyu001.chapter2.chatroom.message.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@Slf4j
//@ChannelHandler.Sharable
public class MessageCodec extends ByteToMessageCodec<Message> {

    public static final byte[] MAGIC = new byte[] { 1, 2, 3, 4 };

    public static final int VERSION = 1;

    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 1. -- 4 字节 魔数
        out.writeBytes(MAGIC);
        // 2. -- 1 字节 版本号
        out.writeByte(VERSION);
        // 3. -- 1 字节 序列化算法 自行约定0-jdk 1-json
        out.writeByte(0);
        // 4. -- 1 字节 指令类型
        out.writeByte(msg.getMessageType());
        // 5. -- 4 字节 请求序号
        out.writeInt(msg.getSequenceId());
        // 对齐填充
        out.writeByte(0xff);
        // 6. -- 4 字节 正文长度
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();
        out.writeInt(bytes.length);
        // 7. -- n 字节 消息正文
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 1. 魔数
        int magicNum = in.readInt();
        // 2. 版本号
        byte version = in.readByte();
        // 3. 序列化算法
        byte serializerType = in.readByte();
        // 4. 指令类型
        byte messageType = in.readByte();
        // 消息对齐部分，读取1字节，跳过
        in.readByte();
        // 5. 请求序号
        int sequenceId = in.readInt();
        // 6. 正文长度
        int length = in.readInt();
        // 7. 消息正文
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);

        // 8.反序列化
        Object o = null;
        if(serializerType == 0) {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            o = ois.readObject();
            out.add(o);
        }
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
        log.debug("{}", o);
    }
}