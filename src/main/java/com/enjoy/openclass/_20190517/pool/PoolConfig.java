package com.enjoy.openclass._20190517.pool;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/17 21:29<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 数据库连接池配置类<br/>
 * <b>Description</b>:
 */
//@Configuration
public class PoolConfig {

    //@Bean
    public MySqlPool mySqlPool() {
        MySqlPool pool = new MySqlPoolImpl();
        pool.init();
        return pool;
    }
}
