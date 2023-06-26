package com.bancrabs.villaticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.entities.QR;
import com.bancrabs.villaticket.repositories.QRRepository;
import com.bancrabs.villaticket.services.QRService;

import jakarta.transaction.Transactional;

@Service
public class QRServiceImpl implements QRService{

    @Autowired
    private QRRepository qrRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public QR save(String code) throws Exception {
        try{
            return qrRepository.save(new QR(code));
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean delete(UUID id) throws Exception {
        try{
            qrRepository.deleteById(id);
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public List<QR> findAll() {
        return qrRepository.findAll();
    }

    @Override
    public QR findById(UUID id) {
        return qrRepository.findById(id).orElse(null);
    }

    @Override
    public QR findByCode(String code) {
        return qrRepository.findByCode(code);
    } 
}
