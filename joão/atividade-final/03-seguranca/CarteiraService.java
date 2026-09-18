package com.financas.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.financas.backend.exception.NaoEncontradoExcecao;
import com.financas.backend.model.Carteira;
import com.financas.backend.repository.CarteiraRepository;

/**
 * Controle de acesso (OWASP A01) e injeção de SQL (A03) na prática.
 *
 * REGRA DE OURO: nunca confie no id que veio da URL. O usuário pode trocar
 * /api/carteira/7 por /api/carteira/8 e tentar ver a carteira de outra pessoa.
 * Por isso toda busca leva junto o e-mail de quem está logado.
 */
@Service
public class CarteiraService {

    @Autowired
    private CarteiraRepository carteiraRepository;

    /** O e-mail foi colocado aqui pelo FiltroJwt quando validou o token. */
    private String emailDoUsuarioLogado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public List<Carteira> listarMinhas() {
        return carteiraRepository.buscarPorMembro(emailDoUsuarioLogado());
    }

    /**
     * ERRADO (o que quase todo mundo faz):
     *   return carteiraRepository.findById(id).orElseThrow(...);
     *   -> qualquer usuário logado enxerga a carteira de qualquer outro.
     *
     * CERTO: a consulta já filtra pelo dono/membro.
     */
    public Carteira buscarPorId(Long id) {
        return carteiraRepository.buscarPorIdEMembro(id, emailDoUsuarioLogado())
                .orElseThrow(() -> new NaoEncontradoExcecao("Carteira não encontrada"));
    }

    public void excluir(Long id) {
        // reaproveita a busca protegida: se não for dele, nem chega no delete
        carteiraRepository.delete(buscarPorId(id));
    }
}

/*
 * ---------------------------------------------------------------------------
 * O repositório, com as consultas seguras (parâmetros nomeados = sem injeção):
 * ---------------------------------------------------------------------------
 *
 * public interface CarteiraRepository extends JpaRepository<Carteira, Long> {
 *
 *     @Query("select c from Carteira c join c.membros m where m.email = :email")
 *     List<Carteira> buscarPorMembro(@Param("email") String email);
 *
 *     @Query("select c from Carteira c join c.membros m where c.id = :id and m.email = :email")
 *     Optional<Carteira> buscarPorIdEMembro(@Param("id") Long id, @Param("email") String email);
 * }
 *
 * Por que isso é seguro contra SQL Injection?
 * Porque o valor vai como PARÂMETRO, separado do comando. O banco recebe
 * "... where m.email = ?" e o valor depois; ele nunca interpreta o texto como comando.
 *
 * O jeito ERRADO seria montar a query concatenando:
 *   "select ... where m.email = '" + email + "'"
 * Aí alguém digita:  ' or '1'='1
 * e a consulta vira "where m.email = '' or '1'='1'" -> devolve as carteiras de todo mundo.
 */
