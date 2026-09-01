package com.gm.ace.common.fill;

import com.gm.ace.common.base.BaseEntity;
import com.gm.ace.tenant.LoginUserContext;
import com.mybatisflex.annotation.UpdateListener;

import java.time.LocalDateTime;

/**
 * 更新自动填充：修改人、修改时间
 *
 * @author guoym
 */
public class BaseEntityUpdateListener implements UpdateListener {

    @Override
    public void onUpdate(Object entity) {
        if (!(entity instanceof BaseEntity be)) {
            return;
        }
        Long uid = LoginUserContext.get();
        if (uid != null) {
            be.setUpdateBy(uid);
        }
        be.setUpdateTime(LocalDateTime.now());
    }
}
