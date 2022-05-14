package nl.um.cds.triplifierboot.repository;

import nl.um.cds.triplifierboot.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    TaskEntity getTopByStatusOrderByDateCreatedAsc(TaskEntity.Status queue);

}
