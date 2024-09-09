package com.backend.backend.controller;

import java.net.URI;

import java.util.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.util.UriComponentsBuilder;

import com.backend.backend.dto.PessoaDTO;
import com.backend.backend.model.Pessoa;
import com.backend.backend.repository.PessoaRepository;



@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private PessoaRepository pessoaRepository;

    @PostMapping("/post")
    public ResponseEntity<Void> criaPessoa(@RequestBody PessoaDTO pessoaDTO, UriComponentsBuilder ucb) {
       
        //cria pessoa 
        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome(pessoaDTO.getNome());
        novaPessoa.setTelefone(pessoaDTO.getTelefone().replace("(","").replace(")", "").replace(" ", "").replace("-", ""));
        novaPessoa.setCpf(pessoaDTO.getCpf().replace(".", "").replace("-",""));
       
        //salva no banco de dados
        Pessoa savePessoa = pessoaRepository.save(novaPessoa);
        
        //cria a location na header com a posicao da pessoa criada
        URI posicaoNovaPessoa = ucb.path("api/{id}").buildAndExpand(savePessoa.getId()).toUri();
        
        // retorna o status 201 e a location na header
        return ResponseEntity.created(posicaoNovaPessoa).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> findById(@PathVariable("id") Long requestedId){
        //acha a pessoa por id
        Optional<Pessoa> pessoaOpcional = pessoaRepository.findById(requestedId);
        //verifica se contem algo 
        if(pessoaOpcional.isPresent()){
            //se sim, retorna status 200 e a pessoa
            return ResponseEntity.ok(pessoaOpcional.get());
        }else{
            //se nao, status 404
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> updatePessoa(@PathVariable("id") Long id, @RequestBody Pessoa pessoaAtualizada) {
        //acha a pessoa por id
        Optional<Pessoa> pessoaOpcional = pessoaRepository.findById(id);
        //verifica se contem algo
        if (pessoaOpcional.isPresent()) {
            //se sim, salva os novos dados
            Pessoa pessoa = pessoaOpcional.get();
            pessoa.setNome(pessoaAtualizada.getNome());
            pessoa.setTelefone(pessoaAtualizada.getTelefone().replace("(","").replace(")", "").replace(" ", "").replace("-", ""));
            pessoa.setCpf(pessoaAtualizada.getCpf().replace(".", "").replace("-",""));
            pessoaRepository.save(pessoa);
            //retorna status 200
            return ResponseEntity.ok(pessoa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePessoa(@PathVariable("id") Long id) {
        //acha a pessoa por id
        Optional<Pessoa> pessoaExistente = pessoaRepository.findById(id);
        //verifica se existe
        if (pessoaExistente.isPresent()) {
            //se sim, deleta
            pessoaRepository.deleteById(id);
            //retorna status 204
            return ResponseEntity.noContent().build();
        } else {
            //retorna status 404
            return ResponseEntity.notFound().build();
        }
    }
}
