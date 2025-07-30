package com.quanxiaoha.xiaohashu.auth;

import com.quanxiaoha.xiaohashu.auth.domain.dataobject.RoleDO;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.RolePermissionDO;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.UserDO;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.RoleDOMapper;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.RolePermissionDOMapper;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.UserDOMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MybatisTests {
    @Resource
    UserDOMapper userDOMapper;
    @Resource
    RoleDOMapper roleDOMapper;
    @Resource
    RolePermissionDOMapper rolePermissionDOMapper;
    @Test
    void selectByPrimaryKey() {
        RoleDO roleDO = roleDOMapper.selectEnabledList().get(0);
        UserDO  userDO = userDOMapper.selectByPrimaryKey(1L);
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        List<RolePermissionDO>  rolePermissionDO = rolePermissionDOMapper.selectByRoleIds(roleIds);
        System.out.println(Arrays.toString(rolePermissionDO.toArray()));
        System.out.println(roleDO);
        System.out.println(userDO);
    }
}
