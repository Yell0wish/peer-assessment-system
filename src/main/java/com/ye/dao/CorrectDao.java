package com.ye.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ye.pojo.ClassInformationPublicPojo;
import com.ye.pojo.CorrectPojo;
import com.ye.pojo.HomeworkPojo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CorrectDao extends BaseMapper<CorrectPojo> {
    @Select("select uuid, homeworkid, userid_c, userid_o, score, time, comment from correct where  userid_c = #{userid}")
    @Results({
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "homeworkid", property = "homeworkID"),
            @Result(column = "userID_o", property = "userO"),
            @Result(column = "userid_c", property = "userC"),
            @Result(column = "score", property = "score"),
            @Result(column = "time", property = "time"),
            @Result(column = "comment", property = "comment"),

            // 指定其他列与字段的映射关系
    })
    List<CorrectPojo> getAllocatedList(@Param("userid") int userid);

    @Select("select * from correct where  userid_o = #{studentid} and homeworkid = #{homeworkid} and userid_c <> #{teacherid}")
    @Results({
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "userid_c", property = "userC"),
            @Result(column = "score", property = "score"),
            // 指定其他列与字段的映射关系
    })
    List<CorrectPojo> getStudentScores(@Param("homeworkid") int homeworkid, @Param("studentid") int studentid, @Param("teacherid") int teacherid);

//    @Select("select * from correct where  userid_o = #{studentid} and homeworkid = #{homeworkid} and userid_c <> #{teacherid}")
//    @Results({
//            @Result(column = "uuid", property = "uuid"),
//            @Result(column = "userid_c", property = "userC"),
//            @Result(column = "score", property = "score"),
//            // 指定其他列与字段的映射关系
//    })
//    List<CorrectPojo> getAllocatedList(@Param("teacherid") int teacherid);
}
