package com.danger.utils;

import org.springframework.stereotype.Service;

//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis 工具类
 *
 * @author 文希
 * @create 2019-08-16 17:22
 */
@Service
public class RedisUtil {

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    /**********************key******************************/
//
//    /**
//     * 删除key
//     *
//     * @param key
//     */
//    public void del(String key) {
//        stringRedisTemplate.delete(key);
//    }
//
//    /**
//     * 批量删除key
//     *
//     * @param keys
//     */
//    public void del(Collection<String> keys) {
//        stringRedisTemplate.delete(keys);
//    }
//
//    /**
//     * 检查给定 key 是否存在
//     *
//     * @param key
//     * @return
//     */
//    public Boolean exists(String key) {
//        return stringRedisTemplate.hasKey(key);
//    }
//
//    /**
//     * 设置过期时间
//     *
//     * @param key
//     * @param timeout
//     * @return
//     */
//    public Boolean expire(String key, long timeout) {
//        return stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 设置过期时间
//     */
//    public Boolean expire(String key, long timeout, TimeUnit timeUtit) {
//        return stringRedisTemplate.expire(key, timeout, timeUtit);
//    }
//
//
//    /**
//     * 设置过期时间
//     *
//     * @param key
//     * @param date
//     * @return
//     */
//    public Boolean expireAt(String key, Date date) {
//        return stringRedisTemplate.expireAt(key, date);
//    }
//
//    /**
//     * 返回给定 key 的剩余生存时间，以秒为单位
//     *
//     * @param key
//     * @return
//     */
//    public Long ttl(String key) {
//        return stringRedisTemplate.getExpire(key);
//    }
//
//    /******************String************************/
//
//    /**
//     * 将 key 所存储的值加上增量delta，返回增加后的值
//     *
//     * @param key
//     * @param delta
//     * @return
//     */
//    public Long incrBy(String key, long delta) {
//        return stringRedisTemplate.opsForValue().increment(key, delta);
//    }
//
//    /**
//     * 将字符串值 value 关联到 key
//     */
//    public void set(String key, String value) {
//        stringRedisTemplate.opsForValue().set(key, value);
//    }
//
//    /**
//     * 将字符串值 value 关联到 key
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public void setex(String key, String value, long timeout, TimeUnit unit) {
//        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
//    }
//
//    /**
//     * 将 key的值设为 value ，当且仅当 key 不存在
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Boolean setnx(String key, String value) {
//        return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
//    }
//
//    /**
//     * 关联到 key
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public void mset(Map<String, String> map) {
//        stringRedisTemplate.opsForValue().multiSet(map);
//    }
//
//    /**
//     * 返回 key所关联的字符串
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public String get(String key) {
//        return stringRedisTemplate.opsForValue().get(key);
//    }
//
//    /******************* Hash **********************/
//
//    /**
//     * 删除哈希表 key中的一个或多个指定域，不存在的域将被忽略
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long hdel(String key, Object... hashKeys) {
//        return stringRedisTemplate.opsForHash().delete(key, hashKeys);
//    }
//
//    /**
//     * 将哈希表 key中的域 field 的值设为 value
//     *
//     * @param
//     * @author caiLinFeng
//     * @Description
//     * @date 2018年1月30日
//     */
//    public void hset(String key, String hashKey, String hashValue) {
//        stringRedisTemplate.opsForHash().put(key, hashKey, hashValue);
//    }
//
//    /**
//     * 同时将多个 field-value (域-值)对设置到哈希表 key 中
//     *
//     * @param
//     * @param map
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public void hmset(String key, Map<String, Object> map) {
//        stringRedisTemplate.opsForHash().putAll(key, map);
//    }
//
//    /**
//     * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Boolean hsetnx(String key, String hashKey, String hashValue) {
//        return stringRedisTemplate.opsForHash().putIfAbsent(key, hashKey, hashValue);
//    }
//
//    /**
//     * 返回哈希表 key 中给定域 field 的值
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public String hget(String key, String hashKey) {
//        return (String) stringRedisTemplate.opsForHash().get(key, hashKey);
//    }
//
//    /**
//     * 返回哈希表 key 中，所有的域和值
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Map<Object, Object> hgetAll(String key) {
//        return stringRedisTemplate.opsForHash().entries(key);
//    }
//
//    /**
//     * 返回哈希表 key 中的所有域
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Set<Object> hkeys(String key) {
//        return stringRedisTemplate.opsForHash().keys(key);
//    }
//
//    /**
//     * 返回哈希表 key 中所有域的值
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public List<Object> hvals(String key) {
//        return stringRedisTemplate.opsForHash().values(key);
//    }
//
//    /**
//     * 为哈希表 key 中的域 field 的值加上增量 delta
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long hincrBy(String key, String hashKey, long delta) {
//        return stringRedisTemplate.opsForHash().increment(key, hashKey, delta);
//    }
//
//    /**
//     * 查看哈希表 key 中，给定域 field 是否存在
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Boolean hexists(final String key, String hashKey) {
//        return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
//    }
//
//    /******************* List **********************/
//
//    /**
//     * 删除并获取列表中的第一个元素
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public String lpop(String key) {
//        return stringRedisTemplate.opsForList().leftPop(key);
//    }
//
//    /**
//     * 删除并获取列表中的第一个元素，或阻塞，直到有一个元素可用
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public String blpop(String key, long timeout, TimeUnit unit) {
//        return stringRedisTemplate.opsForList().leftPop(key, timeout, unit);
//    }
//
//    /**
//     * 删除并获取列表中的最后一个元素
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public String rpop(String key) {
//        return stringRedisTemplate.opsForList().rightPop(key);
//    }
//
//    /**
//     * 删除并获取列表中的最后一个元素，或阻塞，直到有一个元素可用
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public String brpop(String key, long timeout, TimeUnit unit) {
//        return stringRedisTemplate.opsForList().rightPop(key, timeout, unit);
//    }
//
//    /**
//     * 返回列表 key 的长度
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long llen(String key) {
//        return stringRedisTemplate.opsForList().size(key);
//    }
//
//    /**
//     * 将value 插入到列表 key 的表头
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long lpush(String key, String value) {
//        return stringRedisTemplate.opsForList().leftPush(key, value);
//    }
//
//    /**
//     * 将值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long lpushx(String key, String value) {
//        return stringRedisTemplate.opsForList().leftPushIfPresent(key, value);
//    }
//
//    /**
//     * 将value 插入到列表 key 的表尾
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long rpush(String key, String value) {
//        return stringRedisTemplate.opsForList().rightPush(key, value);
//    }
//
//    /**
//     * 将值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long rpushx(String key, String value) {
//        return stringRedisTemplate.opsForList().rightPushIfPresent(key, value);
//    }
//
//    /******************* Set **********************/
//
//    /**
//     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long sadd(String key, String... values) {
//        return stringRedisTemplate.opsForSet().add(key, values);
//    }
//
//    /**
//     * 返回集合 key 的基数(集合中元素的数量)
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long scard(String key) {
//        return stringRedisTemplate.opsForSet().size(key);
//    }
//
//    /**
//     * 返回一个集合的全部成员，该集合是所有给定集合之间的差集
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Set<String> sdiff(String key, String otherKey) {
//        return stringRedisTemplate.opsForSet().difference(key, otherKey);
//    }
//
//    /**
//     * 返回一个集合的全部成员，该集合是所有给定集合之间的差集
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Set<String> sdiff(String key, Collection<String> otherKeys) {
//        return stringRedisTemplate.opsForSet().difference(key, otherKeys);
//    }
//
//    /**
//     * 返回一个集合的全部成员，该集合是所有给定集合的交集
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Set<String> sinter(String key, String otherKey) {
//        return stringRedisTemplate.opsForSet().intersect(key, otherKey);
//    }
//
//    /**
//     * 返回一个集合的全部成员，该集合是所有给定集合的交集
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Set<String> sinter(String key, Collection<String> otherKeys) {
//        return stringRedisTemplate.opsForSet().intersect(key, otherKeys);
//    }
//
//    /**
//     * 判断 member 元素是否集合 key 的成员
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Boolean sismember(String key, String member) {
//        return stringRedisTemplate.opsForSet().isMember(key, member);
//    }
//
//    /**
//     * 返回集合 key 中的所有成员
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Set<String> smembers(String key) {
//        return stringRedisTemplate.opsForSet().members(key);
//    }
//
//    /**
//     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long srem(String key, Object... values) {
//        return stringRedisTemplate.opsForSet().remove(key, values);
//    }
//
//    /**
//     * 返回一个集合的全部成员，该集合是所有给定集合的并集
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Set<String> sunion(String key, String otherKey) {
//        return stringRedisTemplate.opsForSet().union(key, otherKey);
//    }
//
//    /**
//     * 返回一个集合的全部成员，该集合是所有给定集合的并集
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Set<String> sunion(String key, Collection<String> otherKeys) {
//        return stringRedisTemplate.opsForSet().union(key, otherKeys);
//    }
//
//    /******************* Zset **********************/
//
//    /**
//     * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中v
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Boolean zadd(String key, String value, double score) {
//        return stringRedisTemplate.opsForZSet().add(key, value, score);
//    }
//
//    /**
//     * 返回有序集 key 的基数
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long zcard(String key) {
//        return stringRedisTemplate.opsForZSet().zCard(key);
//    }
//
//    /**
//     * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max)的成员的数量
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long zcount(String key, double min, double max) {
//        return stringRedisTemplate.opsForZSet().count(key, min, max);
//    }
//
//    /**
//     * 为有序集 key 的成员 member 的 score 值加上增量 delta
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Double zincrby(String key, String value, double delta) {
//        return stringRedisTemplate.opsForZSet().incrementScore(key, value, delta);
//    }
//
//    /**
//     * 返回有序集 key 中，指定区间内的成员,其中成员的位置按 score 值递增(从小到大)来排序
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Set<String> zrange(String key, long start, long end) {
//        return stringRedisTemplate.opsForZSet().range(key, start, end);
//    }
//
//    /**
//     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max)的成员。有序集成员按
//     * score,值递增(从小到大)次序排列
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Set<String> zrangeByScore(String key, double min, double max) {
//        return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
//    }
//
//    /**
//     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。排名以 0 为底
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long zrank(String key, String member) {
//        return stringRedisTemplate.opsForZSet().rank(key, member);
//    }
//
//    /**
//     * 移除有序集 key 中，指定排名(rank)区间内的所有成员
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long zremrangeByRank(String key, long start, long end) {
//        return stringRedisTemplate.opsForZSet().removeRange(key, start, end);
//    }
//
//    /**
//     * 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long zremrangeByScore(String key, double min, double max) {
//        return stringRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
//    }
//
//    /**
//     * 返回有序集 key 中，指定区间内的成员。其中成员的位置按 score 值递减(从大到小)来排列。
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Set<String> zrevrange(String key, long start, long end) {
//        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
//    }
//
//    /**
//     * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min)的所有的成员。有序集成员按
//     * score,值递减(从大到小)的次序排列
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Set<String> zrevrangeByScore(String key, double min, double max) {
//        return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
//    }
//
//    /**
//     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。排名以 0 为底
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Long zrevrank(String key, String member) {
//        return stringRedisTemplate.opsForZSet().reverseRank(key, member);
//    }
//
//    /**
//     * 返回有序集 key 中，成员 member 的 score 值
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public Double zscore(String key, String member) {
//        return stringRedisTemplate.opsForZSet().score(key, member);
//    }
//
//    /******************* Pub/Sub **********************/
//
//    /**
//     * 将信息 message 发送到指定的频道 channel
//     *
//     * @param channel
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public void publish(String channel, String message) {
//        stringRedisTemplate.convertAndSend(channel, message);
//    }
//
//    /******************* serial **********************/
//
//    /**
//     * 获取redisTemplate的序列化
//     *
//     * @param
//     * @author caiLinFeng
//     * @date 2018年1月30日
//     */
//    public RedisSerializer<String> getSerializer() {
//        return stringRedisTemplate.getStringSerializer();
//    }
}
