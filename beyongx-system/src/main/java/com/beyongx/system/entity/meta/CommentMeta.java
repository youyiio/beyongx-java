package com.beyongx.system.entity.meta;

public class CommentMeta {
    
    public static enum Status {
        DELETED(-1), //删除
        DRAFT(0), //草稿
        PUBLISHING(1), //申请发布
        REFUSE(2), //拒绝
        PUBLISHED(3); //已发布

        private int code;
        private Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
