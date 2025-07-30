package com.quanxiaoha.xiaohashu.auth.constant;

public class RedisKeyConstants {
    /**
     * 验证码 KEY 前缀
     */
    private static final String VERIFICATION_CODE_KEY_PREFIX = "verification_code:";

    /**
     * 角色对应的权限集合KEY前缀
     */
    private static final String ROLE_PERMISSIONS_KEY_PREFIX = "role:permission:";
    private static final String USER_ROLES_KEY_PREFIX = "user:roles:";

    public static String buildUserRoleKey(Long userId) {
        return USER_ROLES_KEY_PREFIX + userId;
    }
    /**
     * 构建角色对应的权限集合KEY
     * @parm roleId
     * @return
     */

    public static String buildVerificationCodeKey(String phone) {
        return VERIFICATION_CODE_KEY_PREFIX + phone;
    }

    public static String buildRolePermissionsKey(String roleKey) {
        return ROLE_PERMISSIONS_KEY_PREFIX + roleKey;
    }
}
