package com.ye.controller;

import com.ye.pojo.ClassPojo;
import com.ye.pojo.UserPojo;
import com.ye.service.ClassService;
import com.ye.service.UserService;
import com.ye.utils.PasswordHash;
import com.ye.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ClassController {
    @Autowired
    ClassService classService;

    @Autowired
    UserService userService;

    // 创建班级
    @RequestMapping(value = "/createClass", method = RequestMethod.POST)
    public String createClass(@RequestParam("userid") int userid,
                              @RequestParam("token") String token,
                              @RequestParam("className") String className) {
        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            int classid = classService.createClass(userid, className);
            Map<String, Object> map = new HashMap<>();
            map.put("classid", classid);
            return Result.success("成功创建班级", map);
        }
    }

    // 获取班级邀请码
    @RequestMapping(value = "/getAccessCode", method = RequestMethod.GET)
    public String selectClassByID(@RequestParam("classid") int classid,
                                  @RequestParam("userid") int userid,
                                  @RequestParam("token") String token) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            ClassPojo classPojo = classService.selectClassByID(classid);
            if (classPojo == null) {
                return Result.defeat("班级ID不存在");
            } else if (classPojo.getUserid() != userid) {
                return Result.defeat("班级非该用户所有");
            } else {
                if (classPojo.getAccessCode() == null || classPojo.getAccessCode().isEmpty()) {
                    classService.getAccessCode(classPojo);
                }
                Map<String, Object> map = new HashMap<>();
                map.put("accessCode", classPojo.getAccessCode());
                return Result.success("成功获取邀请码", map);
            }
        }
    }


    // 获取所教授的课程
    @RequestMapping(value = "/selectAllTeachingClasses", method = RequestMethod.GET)
    public String selectAllTeachingClasses(@RequestParam("userid") int userid,
                                           @RequestParam("token") String token) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            List<ClassPojo> classPojos = classService.selectAllTeachingClasses(userid);

            Map<String, Object> map = new HashMap<>();
            map.put("classes", classPojos);
            return Result.success("成功获取信息", map);
        }
    }

    // todo 删除课程

    @RequestMapping(value = "/deleteTeachingClass", method = RequestMethod.DELETE)
    public String deleteTeachingClass(@RequestParam("userid") int userid,
                                      @RequestParam("token") String token,
                                      @RequestParam("classid") int classid) {

        UserPojo userPojo = userService.selectByID(userid);
        if (userPojo == null) {
            return Result.defeat("用户ID不存在");
        } else if (!token.equals(PasswordHash.getInstance().getMD5(userPojo.getEmail()))) {
            return Result.defeat("token不正确");
        } else {
            ClassPojo classPojo = classService.selectClassByID(classid);
            if (classPojo == null) {
                return Result.defeat("课程不存在");
            } else if (classPojo.getUserid() == userid) {
                classService.deleteTeachingClass(classid);
                return Result.success("您已成功删除班级");
            } else {
                return Result.defeat("您不是班级拥有者");
            }
        }
    }


}