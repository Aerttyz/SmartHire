package com.smarthire.resume.service;

import com.smarthire.resume.domain.DTO.EmpresaRequestDTO;
import com.smarthire.resume.domain.DTO.EmpresaResponseDTO;
import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.exception.ItemNotFoundException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import com.smarthire.resume.security.AuthUtils;

@AllArgsConstructor
@Service
public class EmpresaService {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Transactional
    public Empresa salvar(Empresa empresa) {
        boolean cnpjEmUso = empresaRepository.findByCnpj(empresa.getCnpj())
                .filter(e-> !e.equals(empresa))
                .isPresent();
        if (cnpjEmUso) {
            throw new BusinessRuleException("CNPJ já cadastrado no sistema.");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaCriptografada = encoder.encode(empresa.getSenha());
        empresa.setSenha(senhaCriptografada);
        return empresaRepository.save(empresa);
    }

    public List<Empresa> listarTodas(){
        List<Empresa> empresas = empresaRepository.findAll();
        if (empresas.isEmpty()) {
            throw new BusinessRuleException("Nenhuma empresa encontrada.");
        }
        return empresas;
    }

    public List<Empresa> listarPorNome(String nomeEmpresa) {
        List<Empresa> empresas = empresaRepository.findByNomeIgnoreCase(nomeEmpresa);
        if (empresas.isEmpty()) {
            throw new BusinessRuleException("Nenhuma empresa encontrada.");
        }
        return empresas;
    }

    public EmpresaResponseDTO buscarEmpresa() {
        UUID empresaId = AuthUtils.getEmpresaId();
        Optional<Empresa> empresa = empresaRepository.findById(empresaId);
        if (empresa.isEmpty()) {
            throw new UsernameNotFoundException("Empresa não encontrada.");
        }
        return new EmpresaResponseDTO(empresa.get());
    }

    public Empresa atualizarEmpresaPorId(EmpresaRequestDTO data) {
        UUID id = AuthUtils.getEmpresaId();
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Empresa", id));
        empresa.setNome(data.nome());
        empresa.setCnpj(data.cnpj());
        empresa.setEmail(data.email());
        empresa.setTelefone(data.telefone());
        
        return empresaRepository.save(empresa);
    }

    @Transactional
    public void excluir() {
        UUID id = AuthUtils.getEmpresaId();
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Empresa não encontrada."));
        vagaRepository.deleteAllByEmpresa(empresa);
        empresaRepository.delete(empresa);
    }
}
