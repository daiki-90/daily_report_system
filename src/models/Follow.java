package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Table(name = "follows")
@NamedQueries({
        @NamedQuery(
                name = "getMyAllFollowers",
                query = "SELECT f.followed_employee_id FROM Follow AS f WHERE f.follower_employee_id = :login_employee ORDER BY f.id DESC"
                ),
        @NamedQuery(
                name = "checkFollowerAndFollowed",
                query = "SELECT COUNT(f) FROM Follow AS f WHERE f.follower_employee_id = :login_employee AND f.followed_employee_id = :target_employee"
                ),
        @NamedQuery(
                name = "getSingleFollow",
                query = "SELECT f.id FROM Follow AS f WHERE f.follower_employee_id = :login_employee AND f.followed_employee_id = :target_employee"
                )
})
@Entity
public class Follow {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // フォローしている社員
    @ManyToOne
    @JoinColumn(name = "follower_employee_id", nullable = false)
    private Employee follower_employee_id;
    // フォローされた社員
    @ManyToOne
    @JoinColumn(name = "followed_employee_id", nullable = false)
    private Employee followed_employee_id;

    // 以下getter,setter
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Employee getFollower_employee_id() {
        return follower_employee_id;
    }
    public void setFollower_employee_id(Employee follower_employee_id) {
        this.follower_employee_id = follower_employee_id;
    }
    public Employee getFollowed_employee_id() {
        return followed_employee_id;
    }
    public void setFollowed_employee_id(Employee followed_employee_id) {
        this.followed_employee_id = followed_employee_id;
    }


}
