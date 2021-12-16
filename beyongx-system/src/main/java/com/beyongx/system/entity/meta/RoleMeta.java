package com.beyongx.system.entity.meta;

public class RoleMeta {
    
    public static enum Status {
        DELETED(-1), //删除
        ACTIVE(1),
        FREED(2);

        private int code;
        private Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

}
