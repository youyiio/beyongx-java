package com.beyongx.system.entity.meta;

public class LinkMeta {
    
    public static enum Status {
        OFFLINE(0), //下线
        ONLINE(1); //上线

        private int code;
        private Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
