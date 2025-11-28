package com.EIPplatform.repository.historyAction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.historyActions.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
   
}
