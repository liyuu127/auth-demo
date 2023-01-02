package cn.liyu.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 在线用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {

    /**
     * 用户名
     */
    private Integer userId;

    private String userName;




    /**
     * token
     */
    private String key;

    private String ip;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;


}
