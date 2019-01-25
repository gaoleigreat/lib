package com.survey.lib.common.vo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenVo {
    private String token;
    private Date expireTime;
}
