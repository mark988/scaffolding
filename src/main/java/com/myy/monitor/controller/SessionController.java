package com.myy.monitor.controller;

import com.myy.common.entity.FebsResponse;
import com.myy.monitor.entity.ActiveUser;
import com.myy.monitor.service.ISessionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("session")
public class SessionController {

    @Autowired
    private ISessionService sessionService;

    @GetMapping("list")
    @RequiresPermissions("online:view")
    public FebsResponse list(String username) {
        List<ActiveUser> list = sessionService.list(username);
        Map<String, Object> data = new HashMap<>();
        data.put("rows", list);
        data.put("total", CollectionUtils.size(list));
        return new FebsResponse().success().data(data);
    }

    @GetMapping("delete/{id}")
    @RequiresPermissions("user:kickout")
    public FebsResponse forceLogout(@PathVariable String id) {
        sessionService.forceLogout(id);
        return new FebsResponse().success();
    }
}
