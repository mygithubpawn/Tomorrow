package com.pawn.blog.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.bytebuddy.asm.Advice;

/**
 * <p>
 * 文章实体类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_article")
@ApiModel(value = "Article对象", description = "")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @TableField(condition = SqlCondition.LIKE)
    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "分类")
    private String categoryId;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "类型（1表示富文本，2表示markdown）")
    private String type;

    @ApiModelProperty(value = "审核状态（1表示待审核，2表示审核通过,3表示打回）")
    private String review;

    //逻辑删除（相当于执行修改方法）
    @TableLogic
    @ApiModelProperty(value = "文章状态（0表示删除，1表示正常）")
    private String state = "1";

    @ApiModelProperty(value = "发布状态（1表示发布，2表示草稿，3表示置顶）")
    private String status = "2";

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "发布位置（1表示发布到社区，2表示发布到本地博客）")
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

    @ApiModelProperty(value = "标签列表，不为数据库字段")
    @TableField(exist = false)
    private List<String> label = new ArrayList<>();

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public String getLabels() {
        //打散到集合
        this.label.clear();
        if (this.labels != null) {
            if (!this.labels.contains("-")) {
                this.label.add(this.labels);
                System.out.println("好的哈哈哈哈哈换行" + this.label);
            } else {
                String[] split = this.labels.split("-");
                List<String> strings = Arrays.asList(split);
                this.label.addAll(strings);
            }
        }
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", userId='" + userId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", state='" + state + '\'' +
                ", review='" + review + '\'' +
                ", status='" + status + '\'' +
                ", cover='" + cover + '\'' +
                ", position='" + position + '\'' +
                ", summary='" + summary + '\'' +
                ", labels='" + labels + '\'' +
                ", viewCount=" + viewCount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", label=" + label +
                '}';
    }
}
