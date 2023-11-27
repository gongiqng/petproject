package org.pet.home.net;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public class NetCode {
    /**
     * 创建部门失败
     */
    public static final int CREAT_DEPARTMENT_ERROR = 0x10;
    /**
     * 移除部门失败
     */
    public static final int REMOVE_DEPARTMENT_ERROR =0x11 ;

    /**
     * 更新部门失败
     */
    public static final int UPDATE_DEPARTMENT_ERROR =0x12;

    public static final int CREATE_DEPARTMENT_ERROR =0x13 ;
    /**
     * 手机号不能为空
     */
    public static final int PHONE_INVALID =0x14 ;
    /**
     * 用户名不能为空
     */
    public static final int USERNAME_INVALID =0x15 ;
    /**
     * 邮箱不能为空
     */
    public static final int EMAIL_INVALID = 0x16;
    /**
     * 验证did的
     */
    public static final int DEPARTMENT_ID_INVALID = 0x17;
    /**
     * 删除人员
     */
    public static final int REMOVE_EMPLOYEE_ERROR =0x18 ;
    /**
     * 修改人员
     */
    public static final int UPDATE_EMPLOYEE_ERROR = 0x19;
    /**
     * 店铺名不能为空
     */
    public static final int SHOP_NAME_INVALID = 0x20;
    /**
     * 地址不能为空
     */
    public static final int SHOP_ADDRESS_INVALID =0x21 ;
    /**
     * logo不能为空
     */
    public static final int SHOP_LOGO_INVALID = 0x22;
    /**
     * token失效，过期
     */
    public static final int TOKEN_INVALID =0x23 ;
    /**
     * token错误，非法请求
     */
    public static final int TOKEN_NOT_EXIST =0x24 ;
    /**
     * 密码不能为空
     */
    public static final int PASSWORD_INVALID = 0x25;
    public static final int AGE_INVALID =0x26 ;
    public static final int CODE_ERROR = 0x27;
    public static final int CODE_LAPSE =0x28 ;
    public static final int PET_NAME_INVALID =0X29 ;
    public static final int ADDRESS_INVALID = 0X30;
    public static final int SEX_INVALID = 0X31;
    public static final int ISINOCULATION_INVALID =0X32;
    public static final int BIRTH_INVALID = 0X33;
    public static final int ID_INVALID = 0x34;
    public static final int PET_TYPE_ERROR =0x35 ;
    public static final int USER_LIST_IS_NULL =0x36 ;
    public static final int LOGIN_ERROR = 0x37;
    public static final int TYPE_ERROR =0x39 ;
    public static final int STATE_ERROR =0X40 ;
    public static final int PET_PROCESS_ERROR = 0x41;
    public static final int SHOP_LIST_IS_NULL =0x42 ;
    public static final int SHOP_IS_NULL = 0x43;
    public static final int SELL_PRICE_INVALID =0x44 ;
    public static final int PETSHOP_ERROR = 0x45;
    public static final int PETSHOP_IS_BUY =0x46 ;
    public static final int BUY_ERROR =0x47 ;
    public static final int PRODUCT_IS_NULL = 0X48;
    public static final int STATE_INVALID = 0x49;
    public static final int COUNT_ERROR =0X48 ;
    public static int Price_INVALID=0x50;
    public static int Count_INVALID=0x51;
}
