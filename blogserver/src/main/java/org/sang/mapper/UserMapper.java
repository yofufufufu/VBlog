package org.sang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sang.bean.Role;
import org.sang.bean.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sang on 2017/12/17.
 */
@Repository
// 将mapper交由spring管理，在服务类中自动注入(原本需要在XML中配置扫描)
@Mapper
public interface UserMapper {

    // 传递多个参数时需要使用@Param注解
    User loadUserByUsername(@Param("username") String username);

    long reg(User user);

    int updateUserEmail(@Param("email") String email, @Param("id") Long id);

    List<User> getUserByNickname(@Param("nickname") String nickname);

    List<Role> getAllRole();

    int updateUserEnabled(@Param("enabled") Boolean enabled, @Param("uid") Long uid);

    int deleteUserById(Long uid);

    int deleteUserRolesByUid(Long id);

    int setUserRoles(@Param("rids") Long[] rids, @Param("id") Long id);

    User getUserById(@Param("id") Long id);
}
