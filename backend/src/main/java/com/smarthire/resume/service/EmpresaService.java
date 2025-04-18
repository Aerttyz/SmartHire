package com.smarthire.resume.service;

import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void excluir(Empresa empresa) {
        empresaRepository.save(empresa);
    }

}
