package com.smarthire.resume.service;

import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@AllArgsConstructor
@Service
public class EmpresaService {

    private EmpresaRepository empresaRepository;

    @Transactional
    public Empresa salvar(Empresa empresa) {
        boolean cnpjEmUso = empresaRepository.findByCnpj(empresa.getCnpj())
                .filter(e-> !e.equals(empresa))
                .isPresent();
        if (cnpjEmUso) {
            throw new BusinessRuleException("CNPJ já cadastrado no sistema.");
        }
        return empresaRepository.save(empresa);
    }

    public List<Empresa> listar(Empresa empresa) {
         return empresaRepository.findAll();
    }

    @Transactional
    public void excluir(UUID id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Empresa não encontrada."));
        empresaRepository.delete(empresa);
    }

}
