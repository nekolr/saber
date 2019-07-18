package com.nekolr.saber.service.impl;

import com.nekolr.saber.dao.IdSeqRepository;
import com.nekolr.saber.entity.IdSeq;
import com.nekolr.saber.service.IdSeqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class IdSeqServiceImpl implements IdSeqService {

    @Autowired
    private IdSeqRepository idSeqRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdSeq save(IdSeq idSeq) {
        return idSeqRepository.save(idSeq);
    }
}
