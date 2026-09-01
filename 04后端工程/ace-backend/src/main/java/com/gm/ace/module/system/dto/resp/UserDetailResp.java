package com.gm.ace.module.system.dto.resp;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户详情出参
 *
 * @author guoym
 */
@Data
public class UserDetailResp implements Serializable {

    private Long id;
    private String username;
    private String nickname;
    private String realName;
    private String email;
    private String phone;
    private Integer gender;
    private Integer status;
    private Long deptId;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<String> roles;
}
