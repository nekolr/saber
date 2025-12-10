package com.nekolr.saber.service.impl;

import com.nekolr.saber.dao.IdSeqRepository;
import com.nekolr.saber.entity.IdSeq;
import com.nekolr.saber.service.IdSeqService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class IdSeqServiceImpl implements IdSeqService {

    private final IdSeqRepository idSeqRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdSeq save(IdSeq idSeq) {
        return idSeqRepository.save(idSeq);
    }
}
