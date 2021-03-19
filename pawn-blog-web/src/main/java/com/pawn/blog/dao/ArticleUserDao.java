package com.pawn.blog.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pawn.blog.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ArticleUser对象", description = "")
public class ArticleUserDao implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签列表")
    private List<String> labelsList = new ArrayList<>();

    @TableId(value = "id")
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户昵称")
    private String userName;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "分类ID")
    private String categoryId;

    @ApiModelProperty(value = "类型（1表示富文本，2表示markdown）")
    private String type;

    @ApiModelProperty(value = "文章状态（0表示删除，1表示正常）")
    private String state = "1";

    @ApiModelProperty(value = "审核状态（1表示待审核，2表示审核通过，3表示被打回）")
    private String review;

    @ApiModelProperty(value = "发布状态（1表示发布，2表示草稿，3表示置顶）")
    private String status = "2";

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "发布位置（1表示发布到社区，2表示发布到本地博客,）")
    private String position = "2";

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "标签")
    private String labels;

    @ApiModelProperty(value = "阅读数量")
    private Integer viewCount = 0;

    @ApiModelProperty(value = "发布时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    public String getLabels() {
        //打散到集合里
        this.labelsList.clear();
        if (this.labels != null) {
            //不包含直接添加
            if (!this.labels.contains("-")) {
                labelsList.add(this.labels);
            } else {
                //包含，需切割
                String[] split = this.labels.split("-");
                //数组转集合
                List<String> list = Arrays.asList(split);
                this.labelsList.addAll(list);

            }
        }
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public List<String> getLabelsList() {
        return labelsList;
    }

    public void setLabelsList(List<String> labelsList) {
        this.labelsList = labelsList;
    }
}
