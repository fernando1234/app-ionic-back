package com.prototipo.tcc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prototipo.tcc.domain.enums.Perfil;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message="Preenchimento obrigatório")
    @Length(min=5, max=120, message="O tamanho deve ser entre 5 e 120 caracteres")
    private String nome;

    @Column(unique = true)
    @Email(message="Email inválido")
    @NotEmpty(message="Preenchimento obrigatório")
    private String email;

    @JsonIgnore
    private String senha;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PERFIS")
    private Set<Integer> perfis = new HashSet<>();

    public Usuario() {
        addPerfil(Perfil.CLIENTE);
    }

    public Usuario(Integer id, String nome, String email, String senha) {
        super();
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        addPerfil(Perfil.CLIENTE);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Perfil> getPerfis() {
        return perfis.stream().map(Perfil::toEnum).collect(Collectors.toSet());
    }

    public void addPerfil(Perfil perfil) {
        perfis.add(perfil.getCod());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) &&
                Objects.equals(nome, usuario.nome) &&
                Objects.equals(email, usuario.email) &&
                Objects.equals(senha, usuario.senha) &&
                Objects.equals(perfis, usuario.perfis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, email, senha, perfis);
    }
}
