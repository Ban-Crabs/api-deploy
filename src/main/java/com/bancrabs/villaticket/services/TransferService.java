package com.bancrabs.villaticket.services;

import java.util.List;
import java.util.UUID;

import com.bancrabs.villaticket.models.dtos.save.SaveTransferDTO;
import com.bancrabs.villaticket.models.entities.Transfer;

public interface TransferService {
    Transfer save(SaveTransferDTO data) throws Exception;
    Boolean save(Transfer data) throws Exception;
    Boolean delete(UUID id) throws Exception;

    Transfer findById(UUID id);
    List<Transfer> findAll();
    List<Transfer> findBySenderId(UUID senderId);
    List<Transfer> findByReceiverId(UUID receiverId);
    List<Transfer> findBySenderIdAndReceiverId(UUID senderId, UUID receiverId);
    List<Transfer> findByResultIsNotNull();
    List<Transfer> findByResultIsNull();
    List<Transfer> findByResultIsTrue();
    List<Transfer> findByResultIsFalse();
}
