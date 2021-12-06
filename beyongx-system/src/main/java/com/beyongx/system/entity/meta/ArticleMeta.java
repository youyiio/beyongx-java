package com.beyongx.system.entity.meta;

public class ArticleMeta {
    
    public static enum Status {
        CRAWLED(-3), //已入库
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

    public static enum MetaKey {
        IMAGE("image"), //图片
        FILE("file"), //文件
        TAG("tag"), //标签
        READ_IP("read_ip"), //阅读ip
        TIMING_PUBLISH("__timing_post__"), //定时发布
        BAIDU_INDEX("baidu_index"); //百度索引
 
        private String key;

        private MetaKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
