package com.nekolr.saber.service.impl;

import com.nekolr.saber.dao.IdSeqRepository;
import com.nekolr.saber.entity.IdSeq;
import com.nekolr.saber.service.IdSeqService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
public class IdSeqServiceImpl implements IdSeqService {

    @Resource
    private IdSeqRepository idSeqRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IdSeq save(IdSeq idSeq) {
        return idSeqRepository.save(idSeq);
    }
}
