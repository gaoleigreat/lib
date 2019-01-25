package com.survey.lib.common.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;
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
    private String subject;
    private String audience;
    private Date expiration;
    private Date notBefore;
    private Date issuedAt;
    private String id;
    private String role;
    private String userName;
    private String userId;
    private List<String> permissions;
}
