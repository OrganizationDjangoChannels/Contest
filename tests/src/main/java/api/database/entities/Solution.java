package api.database.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "solution")
public class Solution {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "file")
    private String file;

    @Column(name = "lang")
    private String lang;

    @Column(name = "points")
    private Double points;

    @Column(name = "status")
    private String status;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "passed_tests")
    private Integer passedTests;

}
