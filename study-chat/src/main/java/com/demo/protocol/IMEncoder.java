package com.demo.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;


/**
 * User: lanxinghua
 * Date: 2019/10/10 16:46
 * Desc:
 */
public class IMEncoder extends MessageToByteEncoder<IMMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, IMMessage msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(new MessagePack().write(msg));
    }

    // 对msg进行格式化
    public String encode(IMMessage msg){
        if(null == msg){ return ""; }
        String prex = "[" + msg.getCmd() + "]" + "[" + msg.getTime() + "]";
        if(IMP.LOGIN.getName().equals(msg.getCmd()) ||
                IMP.CHAT.getName().equals(msg.getCmd()) ||
                IMP.FLOWER.getName().equals(msg.getCmd())){
            prex += ("[" + msg.getSender() + "]");
        }else if(IMP.SYSTEM.getName().equals(msg.getCmd())){
            prex += ("[" + msg.getOnlineCount() + "]");
        }
        if(!(null == msg.getMsg() || "".equals(msg.getMsg()))){
            prex += (" - " + msg.getMsg());
        }
        return prex;
    }
}
