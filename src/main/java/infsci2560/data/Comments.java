/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infsci2560.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kolobj
 */
@Entity
@Table(name = "comments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comments.findAll", query = "SELECT c FROM Comments c"),
    @NamedQuery(name = "Comments.findByAuthor", query = "SELECT c FROM Comments c WHERE c.author = :author"),
    @NamedQuery(name = "Comments.findByContent", query = "SELECT c FROM Comments c WHERE c.content = :content"),
    @NamedQuery(name = "Comments.findByApproved", query = "SELECT c FROM Comments c WHERE c.approved = :approved"),
    @NamedQuery(name = "Comments.findBySubmissionDate", query = "SELECT c FROM Comments c WHERE c.submissionDate = :submissionDate")})
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
//    @Lob
//    @Column(name = "comment_uuid")
    @Column(name = "comment_uuid", columnDefinition="UUID")    
    private Object commentUuid;
    @Column(name = "author")
    private String author;
    @Column(name = "content")
    private String content;
    @Column(name = "approved")
    private Boolean approved;
    @Column(name = "submission_date")
    @Temporal(TemporalType.DATE)
    private Date submissionDate;
    @JoinColumn(name = "post_uuid", referencedColumnName = "post_uuid")
    @ManyToOne
    private Posts postUuid;

    public Comments() {
    }

    public Comments(Object commentUuid) {
        this.commentUuid = commentUuid;
    }

    public Object getCommentUuid() {
        return commentUuid;
    }

    public void setCommentUuid(Object commentUuid) {
        this.commentUuid = commentUuid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Posts getPostUuid() {
        return postUuid;
    }

    public void setPostUuid(Posts postUuid) {
        this.postUuid = postUuid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (commentUuid != null ? commentUuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comments)) {
            return false;
        }
        Comments other = (Comments) object;
        if ((this.commentUuid == null && other.commentUuid != null) || (this.commentUuid != null && !this.commentUuid.equals(other.commentUuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "helloworld.data.Comments[ commentUuid=" + commentUuid + " ]";
    }
    
}
