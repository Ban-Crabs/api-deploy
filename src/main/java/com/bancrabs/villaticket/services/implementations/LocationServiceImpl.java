package com.bancrabs.villaticket.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancrabs.villaticket.models.dtos.save.SaveLocationDTO;
import com.bancrabs.villaticket.models.entities.Location;
import com.bancrabs.villaticket.repositories.LocationRepository;
import com.bancrabs.villaticket.services.LocationService;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Boolean save(SaveLocationDTO data) throws Exception {
        try {
            Location check = locationRepository.findByIdOrName(data.getId(), data.getName());
            if(data.getIsAvailable() != null){
                if (check == null) {
                    locationRepository.save(new Location(data.getId(), data.getName(), data.getIsAvailable()));
                } else {
                    check.setId(data.getId());
                    check.setName(data.getName());
                    check.setIsAvailable(data.getIsAvailable());
                    locationRepository.save(check);
                }
                return true;
            }
            else{
                if (check == null) {
                    locationRepository.save(new Location(data.getId(), data.getName()));
                } else {
                    check.setId(data.getId());
                    check.setName(data.getName());
                    check.setIsAvailable(true);
                    locationRepository.save(check);
                }
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Boolean deleteLocation(String id) throws Exception {
        try {
            Location toDelete = locationRepository.findByIdOrName(id, id);
            if (toDelete == null) {
                throw new Exception("Location not found");
            }
            locationRepository.delete(toDelete);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Boolean setAvailable(String id) throws Exception {
        try{
            Location toUpdate = locationRepository.findByIdOrName(id, id);
            if (toUpdate == null) {
                throw new Exception("Location not found");
            }
            toUpdate.setIsAvailable(true);
            locationRepository.save(toUpdate);
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Boolean setUnavailable(String id) throws Exception {
        try{
            Location toUpdate = locationRepository.findByIdOrName(id, id);
            if (toUpdate == null) {
                throw new Exception("Location not found");
            }
            toUpdate.setIsAvailable(false);
            locationRepository.save(toUpdate);
            return true;
        }
        catch(Exception e){
            throw e;
        }
    }

    @Override
    public Location findByIdOrName(String id) {
        return locationRepository.findByIdOrName(id, id);
    }

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

}
