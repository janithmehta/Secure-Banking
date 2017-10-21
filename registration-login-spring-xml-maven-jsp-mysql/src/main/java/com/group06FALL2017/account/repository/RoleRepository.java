package com.group06FALL2017.account.repository;

import com.group06FALL2017.account.model.ExternalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<ExternalUser, Long>{
}
