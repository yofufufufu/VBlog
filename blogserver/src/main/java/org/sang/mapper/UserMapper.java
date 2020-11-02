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


    User loadUserByUsername(@Param("username") String username);

    long reg(User user);

    // 传递多个参数时需要使用@Param注解
    int updateUserEmail(@Param("email") String email, @Param("id") Long id);

    // 如果在动态SQL中用到了参数作为判断条件，那也一定要加@Param注解
    List<User> getUserByNickname(@Param("nickname") String nickname);

    List<Role> getAllRole();

    int updateUserEnabled(@Param("enabled") Boolean enabled, @Param("uid") Long uid);

    int deleteUserById(Long uid);

    int deleteUserRolesByUid(Long id);

    // 传入list，array，collection类型都要加@param
    int setUserRoles(@Param("rids") Long[] rids, @Param("id") Long id);

    User getUserById(@Param("id") Long id);
}
