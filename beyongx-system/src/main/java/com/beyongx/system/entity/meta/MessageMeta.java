package com.beyongx.system.entity.meta;

public class MessageMeta {
    
    public static enum Status {
        DELETED(-1), //删除
        DRAFT(0), //草稿
        APPLY(1), //提交
        SEND(2), //已发送
        READ(3); //已阅读

        private int code;
        private Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public static enum UID {
        ALL("all"), //表示uid为所有的人
        SYSTEM("sys"); //表示uid为系统
 
        private String key;

        private UID(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public static enum Type {
        SYSTEM("system"), //系统消息
        MAIL("mail"), //站内信
        COMMENT("comment"); //评论消息
 
        private String key;

        private Type(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
