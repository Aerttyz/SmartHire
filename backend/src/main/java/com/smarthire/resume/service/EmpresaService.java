package com.smarthire.resume.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.smarthire.resume.domain.DTO.EmpresaPatchRequestDto;
import com.smarthire.resume.domain.DTO.EmpresaResponseDTO;
import com.smarthire.resume.domain.repository.EmpresaRepositoryJpa;
import com.smarthire.resume.domain.repository.VagaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.exception.ItemNotFoundException;
import com.smarthirepro.core.security.AuthUtils;
import com.smarthirepro.domain.model.Empresa;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EmpresaService {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private EmpresaRepositoryJpa empresaRepository;

    @Transactional
    public Empresa salvar(Empresa empresa) {
        boolean cnpjEmUso = empresaRepository.findByCnpj(empresa.getCnpj())
                .filter(e -> !e.equals(empresa))
                .isPresent();
        if (cnpjEmUso) {
            throw new BusinessRuleException("CNPJ já cadastrado no sistema.");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaCriptografada = encoder.encode(empresa.getSenha());
        empresa.setSenha(senhaCriptografada);
        return empresaRepository.save(empresa);
    }

    public List<EmpresaResponseDTO> listarTodas() {
        List<Empresa> empresas = empresaRepository.findAll();
        if (empresas.isEmpty()) {
            throw new BusinessRuleException("Nenhuma empresa encontrada.");
        }
        return empresas.stream()
                .map(EmpresaResponseDTO::new)
                .toList();
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

    public Empresa atualizarEmpresaPorId(EmpresaPatchRequestDto data) {
        UUID id = AuthUtils.getEmpresaId();
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Empresa", id));
        if (data.nome() != null && !data.nome().isBlank())
            empresa.setNome(data.nome());
        if (data.cnpj() != null && !data.cnpj().isBlank())
            empresa.setCnpj(data.cnpj());
        if (data.email() != null && !data.email().isBlank())
            empresa.setEmail(data.email());
        if (data.senha() != null && !data.senha().isBlank())
            empresa.setSenha(new BCryptPasswordEncoder().encode(data.senha()));

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
