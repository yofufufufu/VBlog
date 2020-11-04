package org.sang.service;

import org.sang.bean.Role;
import org.sang.bean.User;
import org.sang.mapper.RolesMapper;
import org.sang.mapper.UserMapper;
import org.sang.utils.Util;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//事务注解
@Transactional
public class UserService implements UserDetailsService {
    final UserMapper userMapper;
    final RolesMapper rolesMapper;
    // MyPasswordEncoder类是passwordEncoder接口的实现类，注入MyPasswordEncoder实例对象
    final PasswordEncoder passwordEncoder;

    // 构造方法注入
    public UserService(UserMapper userMapper, RolesMapper rolesMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.rolesMapper = rolesMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // loadUserByUsername要求返回UserDetails接口
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // User类时UserDetails接口的实现类
        User user = userMapper.loadUserByUsername(s);
        if (user == null) {
            //避免返回null，这里返回一个不含有任何值的User对象，在后期的密码比对过程中一样会验证失败
            return new User();
        }
        //查询用户的角色信息，并返回存入user中
        List<Role> roles = rolesMapper.getRolesByUid(user.getId());
        user.setRoles(roles);
        return user;
    }

    /**
     * @param user
     * @return 0表示成功
     * 1表示用户名重复
     * 2表示失败
     */
    public int reg(User user) {
        User loadUserByUsername = userMapper.loadUserByUsername(user.getUsername());
        //loadUserByUsername != null表明想注册的用户名已经存在(被占用),返回1
        if (loadUserByUsername != null) {
            return 1;
        }
        //插入用户,插入之前先对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);//用户可用
        //数据库insert操作,返回值为成功插入数据条数
        long result = userMapper.reg(user);
        //配置用户的角色，默认都只是普通用户,2是数据库roles表中普通用户的id
        String[] roles = new String[]{"2"};
        //user.getId()可以获得插入的数据在数据库中生成的主键，在UserMapper.xml<insert>中通过KeyProperty等配置
        int i = rolesMapper.addRoles(roles, user.getId());
        //result==1表明插入用户成功,i == roles.length表明设置角色成功
        boolean b = i == roles.length && result == 1;
        //注册成功返回0,失败返回2
        if (b) {
            return 0;
        } else {
            return 2;
        }
    }

    public int updateUserEmail(String email) {
        return userMapper.updateUserEmail(email, Util.getCurrentUser().getId());
    }

    public List<User> getUserByNickname(String nickname) {
        List<User> list = userMapper.getUserByNickname(nickname);
        return list;
    }

    public List<Role> getAllRole() {
        return userMapper.getAllRole();
    }

    public int updateUserEnabled(Boolean enabled, Long uid) {
        return userMapper.updateUserEnabled(enabled, uid);
    }

    public int deleteUserById(Long uid) {
        return userMapper.deleteUserById(uid);
    }

    public int updateUserRoles(Long[] rids, Long id) {
        int i = userMapper.deleteUserRolesByUid(id);
        return userMapper.setUserRoles(rids, id);
    }

    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }
}
