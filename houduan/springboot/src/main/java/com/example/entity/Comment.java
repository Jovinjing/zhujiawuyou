package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 评论
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 评论描述
     */
    private String commentDescribe;

    /**
     * 评论时间
     */
    private String commentTime;

    /**
     * 评论用户id
     */
    private Integer commentUserid;

    /**
     * 评论图片id
     */
    private Integer commentPictureid;


//    @Override
//    public boolean equals(Object that) {
//        if (this == that) {
//            return true;
//        }
//        if (that == null) {
//            return false;
//        }
//        if (getClass() != that.getClass()) {
//            return false;
//        }
//        Comment other = (Comment) that;
//        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
//            && (this.getCommentDescribe() == null ? other.getCommentDescribe() == null : this.getCommentDescribe().equals(other.getCommentDescribe()))
//            && (this.getCommentTime() == null ? other.getCommentTime() == null : this.getCommentTime().equals(other.getCommentTime()))
//            && (this.getCommentUserId() == null ? other.getCommentUserId() == null : this.getCommentUserId().equals(other.getCommentUserId()))
//            && (this.getCommentPictureId() == null ? other.getCommentPictureId() == null : this.getCommentPictureId().equals(other.getCommentPictureId()));
//    }
//
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
//        result = prime * result + ((getCommentDescribe() == null) ? 0 : getCommentDescribe().hashCode());
//        result = prime * result + ((getCommentTime() == null) ? 0 : getCommentTime().hashCode());
//        result = prime * result + ((getCommentUserId() == null) ? 0 : getCommentUserId().hashCode());
//        result = prime * result + ((getCommentPictureId() == null) ? 0 : getCommentPictureId().hashCode());
//        return result;
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(getClass().getSimpleName());
//        sb.append(" [");
//        sb.append("Hash = ").append(hashCode());
//        sb.append(", id=").append(id);
//        sb.append(", commentDescribe=").append(commentDescribe);
//        sb.append(", commentTime=").append(commentTime);
//        sb.append(", commentUserId=").append(commentUserId);
//        sb.append(", commentPictureId=").append(commentPictureId);
//        sb.append("]");
//        return sb.toString();
//    }
}