package com.gm.ace.module.system.dto.resp;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户分页出参
 *
 * @author guoym
 */
@Data
public class UserPageResp implements Serializable {

    private Long id;
    private String username;
    private String nickname;
    private String realName;
    private String email;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;
}
