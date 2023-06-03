package com.fiap.connectfood.services;

import com.fiap.connectfood.model.DoacaoModel;
import com.fiap.connectfood.repository.DoacaoRepository;
import com.fiap.connectfood.services.exceptions.DatabaseException;
import com.fiap.connectfood.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DoacaoService {

    @Autowired
    DoacaoRepository doacaoRepository;

    public void registerDonation(DoacaoModel doacaoModel) {
        doacaoRepository.save(doacaoModel);
    }

    public void deleteDonation(int id)       {
        try {
            doacaoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }

    }

    public DoacaoModel editDonation(int id, DoacaoModel doacaoModel) {
        try {
            DoacaoModel entity = doacaoRepository.getReferenceById(id);
            updateData(entity, doacaoModel);
            return doacaoRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    public List<DoacaoModel> getAllDonations() {
        return doacaoRepository.findAll();
    }

    public DoacaoModel getDonationById(int id) {
        Optional<DoacaoModel> doacaoModel = doacaoRepository.findById(id);
        return doacaoModel.orElseThrow(() -> new ResourceNotFoundException(id));

    }

    public List<DoacaoModel> getDonationByRestaurante(String restaurante) {
        return doacaoRepository.findDonationByRestaurante(restaurante);
    }

    public List<DoacaoModel> getDonationByInstituicao(String instituicao) {
        return doacaoRepository.findDonationByInstituicao(instituicao);
    }

    public List<DoacaoModel> getDonationByDate(Date date) {
        return doacaoRepository.findDonationByDate(date);
    }

    public List<DoacaoModel> getDonationByDateBetween(Date startDate, Date endDate) {
        return doacaoRepository.findDoacaoByDateBetween(startDate, endDate);
    }

    public List<DoacaoModel> findByDoacaoFinalizada(String cnpj) {
        return doacaoRepository.findByDoacaoFinalizada(true);
    }
    public void finalizarDoacao(Integer id) {
        DoacaoModel doacaoModel = doacaoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Doação inexistente"));
        doacaoModel.setDoacaoFinalizada(true);
        doacaoRepository.save(doacaoModel);
    }

    public List<DoacaoModel> findByDoacaoAgendada(String cnpj) {
        return doacaoRepository.findByDoacaoFinalizada(false);
    }

    private void updateData(DoacaoModel entity, DoacaoModel obj) {
        entity.setDate(obj.getDate());
        entity.setDescricao(obj.getDescricao());
    }


}

