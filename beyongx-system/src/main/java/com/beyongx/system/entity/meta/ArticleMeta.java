package com.beyongx.system.entity.meta;

public class ArticleMeta {
    
    public static enum Status {
        CRAWLED(3), //已入库
        WAREHOUSED(-4), //已抓取

        DELETED(-1), //删除
        DRAFT(0), //草稿
        PUBLISHING(1), //申请发布
        FIRST_AUDIT_REJECT(2), //初审拒绝
        FIRST_AUDITED(3), //初审通过
        SECOND_AUDIT_REJECT(4), //终审拒绝
        PUBLISHED(5); //已发布

        private int code;
        private Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
