package com.smarthire.resume.service;

import com.smarthire.resume.domain.DTO.EmpresaRequestDTO;
import com.smarthire.resume.domain.DTO.EmpresaResponseDTO;
import com.smarthire.resume.domain.model.Empresa;
import com.smarthire.resume.domain.repository.EmpresaRepository;
import com.smarthire.resume.exception.BusinessRuleException;
import com.smarthire.resume.exception.ItemNotFoundException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    public Empresa atualizarEmpresaPorId(UUID id, EmpresaRequestDTO data) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Empresa", id));

        empresa.setNome(data.nome());
        empresa.setCnpj(data.cnpj());
        empresa.setEmail(data.email());
        empresa.setTelefone(data.telefone());
        
        return empresaRepository.save(empresa);
    }

    public EmpresaResponseDTO atualizarEmpresaPorIdEncapsulado(EmpresaRequestDTO data, Authentication authentication) {
        String emailEmpresa = authentication.getName(); // pegando do JWT aq
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Empresa empresa = empresaRepository.findByEmail(emailEmpresa)
          .orElseThrow(() -> new UsernameNotFoundException("Empresa não encontrada (método de busca: findByEmail()."));

        empresa.setNome(data.nome());
        empresa.setCnpj(data.cnpj());
        empresa.setTelefone(data.telefone());
        empresa.setEmail(data.email());

        if (data.senha() != null && !data.senha().isEmpty()) {
            empresa.setSenha(encoder.encode(data.senha()));
        }

        empresaRepository.save(empresa);
        return new EmpresaResponseDTO(empresa);

    }


    @Transactional
    public void excluir(UUID id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Empresa não encontrada."));
        empresaRepository.delete(empresa);
    }

    @Transactional
    public void excluirPorEmail(String email) {
        Empresa empresa = empresaRepository.findByEmail(email)
          .orElseThrow(() -> new BusinessRuleException("Empresa autenticad anão encontrada."));
        empresaRepository.delete(empresa);
    }

    public Empresa getEmpresaLogada(Authentication authentication) {
        String email = authentication.getName();
        return empresaRepository.findByEmail(email)
          .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }



}
