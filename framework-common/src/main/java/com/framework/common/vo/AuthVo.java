package com.framework.common.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthVo {
    private String issUer;
    // 设备类型
    private String subject;
    private String audience;
    // 过期时间
    private Date expiration;
    // 当前时间
    private Date notBefore;
    private Date issuedAt;
    private CurrentVo currentVo;
    private String token;
}
