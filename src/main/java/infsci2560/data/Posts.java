/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infsci2560.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kolobj
 */
@Entity
@Table(name = "posts")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Posts.findAll", query = "SELECT p FROM Posts p"),
    @NamedQuery(name = "Posts.findByTitle", query = "SELECT p FROM Posts p WHERE p.title = :title"),
    @NamedQuery(name = "Posts.findByContent", query = "SELECT p FROM Posts p WHERE p.content = :content"),
    @NamedQuery(name = "Posts.findByPublishingDate", query = "SELECT p FROM Posts p WHERE p.publishingDate = :publishingDate")})
public class Posts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    //@Lob
//    @Column(name = "post_uuid")
    @Column(name = "post_uuid", columnDefinition="UUID")    
    private Object postUuid;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "publishing_date")
    @Temporal(TemporalType.DATE)
    private Date publishingDate;
    @OneToMany(mappedBy = "postUuid")
    private Collection<Comments> commentsCollection;

    public Posts() {
    }

    public Posts(Object postUuid) {
        this.postUuid = postUuid;
    }

    public Posts(Object postUuid, String title) {
        this.postUuid = postUuid;
        this.title = title;
    }

    public Object getPostUuid() {
        return postUuid;
    }

    public void setPostUuid(Object postUuid) {
        this.postUuid = postUuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {
        this.publishingDate = publishingDate;
    }

    @XmlTransient
    public Collection<Comments> getCommentsCollection() {
        return commentsCollection;
    }

    public void setCommentsCollection(Collection<Comments> commentsCollection) {
        this.commentsCollection = commentsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (postUuid != null ? postUuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Posts)) {
            return false;
        }
        Posts other = (Posts) object;
        if ((this.postUuid == null && other.postUuid != null) || (this.postUuid != null && !this.postUuid.equals(other.postUuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "helloworld.data.Posts[ postUuid=" + postUuid + " ]";
    }
    
}
