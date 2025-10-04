package com.example.user_srv.constant_;

public final class Constants {

    public static final int QUERY_BATCH_SIZE = 500;

    public interface DELETE {
        Boolean DELETED = false;
        Boolean NORMAL = true;
    }

    public interface IS_BLOCK {
        Long TRUE = 1L;
        Long FALSE = 0L;
    }

}
