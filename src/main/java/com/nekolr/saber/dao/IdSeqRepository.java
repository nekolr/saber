package com.nekolr.saber.dao;

import com.nekolr.saber.entity.IdSeq;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface IdSeqRepository extends JpaRepository<IdSeq, Long> {

}
