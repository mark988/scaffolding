package com.myy.system.service.impl;

import com.myy.common.authentication.ShiroRealm;
import com.myy.common.entity.MenuTree;
import com.myy.common.utils.TreeUtil;
import com.myy.system.entity.Menu;
import com.myy.system.mapper.MenuMapper;
import com.myy.system.mapper.RoleMenuMapper;
import com.myy.system.service.IMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private ShiroRealm shiroRealm;

    @Override
    public List<Menu> findUserPermissions(String username) {
        return this.baseMapper.findUserPermissions(username);
    }

    @Override
    public MenuTree<Menu> findUserMenus(String username) {
        List<Menu> menus = this.baseMapper.findUserMenus(username);
        List<MenuTree<Menu>> trees = this.convertMenus(menus);
        return TreeUtil.buildMenuTree(trees);
    }

    @Override
    public MenuTree<Menu> findMenus(Menu menu) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(menu.getMenuName())) {
            queryWrapper.lambda().like(Menu::getMenuName, menu.getMenuName());
        }
        queryWrapper.lambda().orderByAsc(Menu::getOrderNum);
        List<Menu> menus = this.baseMapper.selectList(queryWrapper);
        List<MenuTree<Menu>> trees = this.convertMenus(menus);

        return TreeUtil.buildMenuTree(trees);
    }

    @Override
    public List<Menu> findMenuList(Menu menu) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(menu.getMenuName())) {
            queryWrapper.lambda().like(Menu::getMenuName, menu.getMenuName());
        }
        queryWrapper.lambda().orderByAsc(Menu::getMenuId).orderByAsc(Menu::getOrderNum);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createMenu(Menu menu) {
        menu.setCreateTime(new Date());
        this.setMenu(menu);
        this.baseMapper.insert(menu);
    }


    @Override
    @Transactional
    public void updateMenu(Menu menu) {
        menu.setModifyTime(new Date());
        this.setMenu(menu);
        this.baseMapper.updateById(menu);

        shiroRealm.clearCache();
    }

    @Override
    @Transactional
    public void deleteMeuns(String menuIds) {
        String[] menuIdsArray = menuIds.split(StringPool.COMMA);
        for (String menuId : menuIdsArray) {
            // 递归删除这些菜单/按钮
            this.baseMapper.deleteMenus(menuId);
            this.roleMenuMapper.deleteRoleMenus(menuId);
        }

        shiroRealm.clearCache();
    }

    private List<MenuTree<Menu>> convertMenus(List<Menu> menus) {
        List<MenuTree<Menu>> trees = new ArrayList<>();
        menus.forEach(menu -> {
            MenuTree<Menu> tree = new MenuTree<>();
            tree.setId(String.valueOf(menu.getMenuId()));
            tree.setParentId(String.valueOf(menu.getParentId()));
            tree.setTitle(menu.getMenuName());
            tree.setIcon(menu.getIcon());
            tree.setHref(menu.getUrl());
            tree.setData(menu);
            trees.add(tree);
        });
        return trees;
    }

    private void setMenu(Menu menu) {
        if (menu.getParentId() == null)
            menu.setParentId(Menu.TOP_NODE);
        if (Menu.TYPE_BUTTON.equals(menu.getType())) {
            menu.setUrl(null);
            menu.setIcon(null);
        }
    }
}
