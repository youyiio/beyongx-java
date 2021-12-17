package com.beyongx.system.entity.meta;

public class ConfigMeta {
 
    public static enum Status {
        ONLINE(1), //启用
        OFFLINE(0); //关停

        private int code;
        private Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
