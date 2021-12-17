package com.beyongx.system.entity.meta;

public class MenuMeta {
    
    public static enum Status {
        DELETED(-1), //删除
        ACTIVE(1), //激活
        PAUSE(0); //暂停

        private int code;
        private Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public static enum Type {
        LINK(0), //网页链接
        MENU(1), //菜单组件
        ACTION(2); //动作组件
 
        private int code;
        private Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
