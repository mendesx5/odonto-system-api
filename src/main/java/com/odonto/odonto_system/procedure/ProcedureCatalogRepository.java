package com.odonto.odonto_system.procedure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProcedureCatalogRepository extends JpaRepository<ProcedureCatalog, UUID> {

    List<ProcedureCatalog> findByActiveTrue();

}
