/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cn.liyu.security.security;

import cn.hutool.core.util.RandomUtil;
import cn.liyu.security.model.JwtUser;
import cn.liyu.security.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static cn.liyu.security.constant.SecurityConstant.USER_CACHE_KEY;

/**
 * @author Zheng Jie
 * @description 用户缓存管理
 * @date 2022-05-26
 **/
@Component
public class UserCacheManager {

    @Resource
    private RedisUtils redisUtils;
    @Value("${login.user-cache.idle-time}")
    private long idleTime;

    /**
     * 返回用户缓存
     * @param userName 用户名
     * @return JwtUserDto
     */
    public JwtUser getUserCache(String userName) {
        if (StringUtils.isNotEmpty(userName)) {
            // 获取数据
            Object obj = redisUtils.hget(USER_CACHE_KEY, userName);
            if(obj != null){
                return (JwtUser)obj;
            }
        }
        return null;
    }

    /**
     *  添加缓存到Redis
     * @param userName 用户名
     */
    @Async
    public void addUserCache(String userName, JwtUser user) {
        if (StringUtils.isNotEmpty(userName)) {
            // 添加数据, 避免数据同时过期
            long time = idleTime + RandomUtil.randomInt(900, 1800);
            redisUtils.hset(USER_CACHE_KEY, userName, user, time);
        }
    }

    /**
     * 清理用户缓存信息
     * 用户信息变更时
     * @param userName 用户名
     */
    @Async
    public void cleanUserCache(String userName) {
        if (StringUtils.isNotEmpty(userName)) {
            // 清除数据
            redisUtils.hdel(USER_CACHE_KEY, userName);
        }
    }
}