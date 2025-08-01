package com.quanxiaoha.xiaohashu.auth.service.Impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.google.common.base.Preconditions;
import com.quanxiaoha.framework.common.response.Response;
//import com.quanxiaoha.biz.context.holder.LoginUserContextHolder;
import com.quanxiaoha.framework.common.util.JsonUtils;
import com.quanxiaoha.xiaohashu.auth.constant.RedisKeyConstants;
import com.quanxiaoha.xiaohashu.auth.constant.RoleConstants;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.RoleDO;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.UserDO;
import com.quanxiaoha.xiaohashu.auth.domain.dataobject.UserRoleRelDO;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.RoleDOMapper;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.UserDOMapper;
import com.quanxiaoha.xiaohashu.auth.domain.mapper.UserRoleRelDOMapper;
import com.quanxiaoha.xiaohashu.auth.enums.RegisterTypeEnum;
import com.quanxiaoha.xiaohashu.auth.enums.UserStatusEnum;
//import com.quanxiaoha.xiaohashu.auth.filter.LoginUserContextHolder;
import com.quanxiaoha.biz.context.holder.LoginUserContextHolder;
import com.quanxiaoha.xiaohashu.auth.model.RegisterVO;
import com.quanxiaoha.xiaohashu.auth.model.UserVO;
import com.quanxiaoha.xiaohashu.auth.model.user.UpdatePasswordVO;
import com.quanxiaoha.xiaohashu.auth.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
//import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDOMapper userDOMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private PasswordEncoder passwordEncoder;

    private static final String USER_REGISTER_CODE_PREFIX = "user:register:code:";
    private static final int CODE_EXPIRE_TIME = 5; // Verification code expiry time in minutes
    @Autowired
    private UserRoleRelDOMapper userRoleRelDOMapper;
    @Autowired
    private RoleDOMapper roleDOMapper;

    @Override
    public Response<String> login(UserVO userVO) {

        Preconditions.checkArgument(StringUtils.hasText(userVO.getPhone()),"手机号不能为空");
        // Input validation
        if (!StringUtils.hasText(userVO.getPhone()) || !StringUtils.hasText(userVO.getPassword())) {
            return Response.fail("手机号或密码不能为空");
        }
        String phone = userVO.getPhone();
        String password = userVO.getPassword();
        // Find user by phone number
        UserDO existUser = userDOMapper.selectByPhone(phone);
        if (existUser == null) {
            return Response.fail("用户不存在");
        }
        // Verify password (in production, use proper password hashing)
        if (!passwordEncoder.matches(password, existUser.getPassword())) {
            return Response.fail("密码错误");
        }
        // Check if user is active
        if (existUser.getStatus() != 0) {
            return Response.fail("账号已被禁用");
        }
        // Login with Sa-Token
        Long userId = existUser.getId();
        RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);
        List<String> roles = new ArrayList<>(1);
        roles.add(roleDO.getRoleKey());
        String userRolesKey = RedisKeyConstants.buildUserRoleKey(userId);
        redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));
        StpUtil.login(userId);
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        return Response.success(saTokenInfo.tokenValue);
    }

    @Override
    public Response<?> logout() {
        Long userId = LoginUserContextHolder.getUserId();
//         退出登录 (指定用户 ID)
        StpUtil.logout(userId);
        return Response.success();
    }

    @Override
    public Response<String> register(RegisterVO  registerVO) {
        if(registerVO==null){
            return Response.fail("手机号不能为空");
        }
        String phone = registerVO.getPhone();
        UserDO existUser = userDOMapper.selectByPhone(phone);
        if (existUser != null) {
            return Response.fail("该手机号已被注册");
        }

        if(registerVO.getRegisterType()== RegisterTypeEnum.PASSWORD.getCode()){
            return registerWithPassword(registerVO);
        }else if (registerVO.getRegisterType()==RegisterTypeEnum.SMS.getCode()){
            return registerWithSms(registerVO);
        }else {
            return Response.fail("不支持的注册方式");
        }
    }

    @Override
    public Response<Boolean> sendVerificationCode(String phone) {
        if(!StringUtils.hasText(phone)){
            return Response.fail("手机号不能为空");
        }
        UserDO existUser = userDOMapper.selectByPhone(phone);
        if(existUser == null){
            return Response.fail("该手机已被注册");

        }
        String code = String.valueOf((int)(Math.random()*9+1)*100000);
        redisTemplate.opsForValue().set(USER_REGISTER_CODE_PREFIX + phone, code,CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        return Response.success(true);
    }

    @Override
    public Response<?> updatePassword(UpdatePasswordVO updatePasswordVO) {
        String newPassword = updatePasswordVO.getNewPassword();

        String encoderPassword = passwordEncoder.encode(newPassword);

        Long userId = LoginUserContextHolder.getUserId();

        UserDO userDO = UserDO.builder()
                .id(userId)
                .password(encoderPassword)
                .updateTime(LocalDateTime.now())
                .build();
        userDOMapper.updateByPrimaryKeySelective(userDO);
        return Response.success();
    }

    private String generateUniqueId() {
        return "XHS" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Response<String> registerWithPassword(RegisterVO registerVO) {
        if(!StringUtils.hasText(registerVO.getPassword())){
            return Response.fail("密码不能为空");
        }
        UserDO newUser = createNewUser(registerVO);
        newUser.setPassword(passwordEncoder.encode(registerVO.getPassword()));
        userDOMapper.insertSelective(newUser);

        UserRoleRelDO userRoleRelDO = UserRoleRelDO.builder()
                .userId(newUser.getId())
                .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .isDeleted(false)
                .build();
        userRoleRelDOMapper.insert(userRoleRelDO);
        StpUtil.login(newUser.getId());
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        return Response.success(saTokenInfo.tokenValue);
    }

    private Response<String> registerWithSms(RegisterVO registerVO){
        if(!StringUtils.hasText(registerVO.getPassword())){
            return Response.fail("验证码不能为空");
        }
        String cacheCode = (String) redisTemplate.opsForValue().get(USER_REGISTER_CODE_PREFIX + registerVO.getPhone());
        if(cacheCode == null || !cacheCode.equals(registerVO.getVerificationCode())){
            return Response.fail("验证码错误或已过期");
        }
        redisTemplate.delete(USER_REGISTER_CODE_PREFIX + registerVO.getPhone());
        UserDO newUser = createNewUser(registerVO);
        // Generate a random initial password
        String initialPassword = UUID.randomUUID().toString().substring(0, 8);
        newUser.setPassword(initialPassword);

        // Save user to database

        // Auto-login after registration
        StpUtil.login(newUser.getId());
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();

        return Response.success(saTokenInfo.tokenValue+"请尽快设置您的密码");
        }

    private UserDO createNewUser(RegisterVO registerVO){
        UserDO newUser = new UserDO();
        newUser.setPhone(registerVO.getPhone());
        if(StringUtils.hasText(registerVO.getNickname())){
            newUser.setNickname(registerVO.getNickname());
        }else {
            newUser.setNickname("用户"+registerVO.getPhone().substring(0,6));
        }
            newUser.setXiaohashuId(generateUniqueId());
            LocalDateTime localDateTime = LocalDateTime.now();
            newUser.setCreateTime(localDateTime);
            newUser.setUpdateTime(localDateTime);
            newUser.setStatus(UserStatusEnum.ACTIVE.getCode());
            newUser.setIsDeleted(false);


            return newUser;
        }

    }
