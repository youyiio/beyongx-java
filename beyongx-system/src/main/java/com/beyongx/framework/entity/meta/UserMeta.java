package com.beyongx.framework.entity.meta;

public class UserMeta {

    public static enum Status {
        DELETED(-1), //删除
        APPLY(1), //申请
        ACTIVE(2),
        FREED(3);

        private int code;
        private Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
