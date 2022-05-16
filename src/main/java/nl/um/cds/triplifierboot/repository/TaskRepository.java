package nl.um.cds.triplifierboot.repository;

import nl.um.cds.triplifierboot.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    TaskEntity getTopByStatusOrderByDateCreatedAsc(TaskEntity.Status queue);

}
