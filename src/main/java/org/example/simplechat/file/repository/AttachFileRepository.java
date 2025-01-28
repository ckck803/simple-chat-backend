package org.example.simplechat.file.repository;

import org.example.simplechat.file.entity.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    Optional<AttachFile> findBySavedFileName(String savedFileName);
}
