package com.myy.system.mapper;

import com.myy.system.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    /**
     * 递归删除菜单/按钮
     *
     * @param menuId menuId
     */
    void deleteRoleMenus(String menuId);
}
