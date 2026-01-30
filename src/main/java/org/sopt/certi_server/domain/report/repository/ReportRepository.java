package org.sopt.certi_server.domain.report.repository;

import org.sopt.certi_server.domain.report.entity.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<CommentReport, Long> {
}
