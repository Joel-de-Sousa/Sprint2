package Sprint.WsProjeto.repositories;


import Sprint.WsProjeto.datamodel.JDBC.ProjetoJDBC;
import Sprint.WsProjeto.datamodel.JDBC.assembler.ProjetoJDBCDomainDataAssembler;
import Sprint.WsProjeto.datamodel.JPA.ProjetoJPA;
import Sprint.WsProjeto.datamodel.JPA.assembler.ProjetoDomainDataAssembler;
import Sprint.WsProjeto.domain.entities.Projeto;
import Sprint.WsProjeto.repositories.IRepository.IProjetoRepository;
import Sprint.WsProjeto.repositories.JDBC.ProjetoJDBCRepository;
import Sprint.WsProjeto.repositories.JPA.ProjetoJPARepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjetoRepository implements IProjetoRepository {

    @Autowired
    ProjetoJPARepository projetoJPARepository;

    @Autowired
    ProjetoJDBCRepository projetoJDBCRepository;

    @Autowired
    ProjetoDomainDataAssembler projetoDomainDataAssembler;
    @Autowired
    ProjetoJDBCDomainDataAssembler projetoJDBCDataAssembler;



    public Projeto save(Projeto projeto) {
        ProjetoJPA projetoJPA = projetoDomainDataAssembler.toData(projeto);

        ProjetoJPA savedProjetoJPA = projetoJPARepository.save(projetoJPA);

        return projetoDomainDataAssembler.toDomain(savedProjetoJPA);
    }

    public Optional<Projeto> findById(int codProjeto) {
        Optional<ProjetoJPA> opProjeto = projetoJPARepository.findById(codProjeto);

        if ( opProjeto.isPresent() ) {
            Projeto projeto = projetoDomainDataAssembler.toDomain(opProjeto.get());
            return Optional.of( projeto );
        }
        else
            return Optional.empty();
    }

    public Optional<Projeto> findByCodEstudante(int codEstudante) {
        Optional<ProjetoJPA> opProjeto = projetoJPARepository.findByCodEstudante (codEstudante);

        if ( opProjeto.isPresent() ) {
            Projeto projeto = projetoDomainDataAssembler.toDomain(opProjeto.get());
            return Optional.of( projeto );
        }
        else
            return Optional.empty();
    }

    public List<Projeto> findProjetosConcluidos() throws SQLException {
        List<ProjetoJDBC> listProjetosJDBC = projetoJDBCRepository.findProjetosConcluidos();
        List<Projeto> listProjetos =new ArrayList<>();
        for (ProjetoJDBC p:listProjetosJDBC) {
            listProjetos.add(projetoJDBCDataAssembler.toDomain(p));
        }
        return listProjetos;
    }

    public List<Projeto> findProjetosDatasAvaliacao (Date fromDate, Date toDate) throws SQLException {
        List<ProjetoJDBC> listProjetosJDBC = projetoJDBCRepository.findProjetosDatasAvaliacao(fromDate, toDate);
        List<Projeto> listProjetos =new ArrayList<>();
        for (ProjetoJDBC p:listProjetosJDBC) {
            listProjetos.add(projetoJDBCDataAssembler.toDomain(p));
        }
        return listProjetos;
    }

    public List<Projeto> findProjetosByCodDocente(int codDocente) throws SQLException {
        List<ProjetoJDBC> listProjetosJDBC = projetoJDBCRepository.findProjetosByCodDocente(codDocente);
        List<Projeto> listProjetos =new ArrayList<>();
        for (ProjetoJDBC p:listProjetosJDBC) {
            listProjetos.add(projetoJDBCDataAssembler.toDomain(p));
        }
        return listProjetos;
    }

    public Projeto findProjetoByCodProposta(int codProposta) throws SQLException {
        Optional<ProjetoJDBC> opProjeto = projetoJDBCRepository.findProjetoByCodProposta(codProposta);

        if ( opProjeto.isPresent() ) {
            Projeto projeto = projetoJDBCDataAssembler.toDomain(opProjeto.get());
            return  projeto ;
        }
        else
            return null;
    }

    public List<Projeto> findProjetosComDeterminadoMACompleto(int codMA) throws SQLException {
        List<ProjetoJDBC> listProjetosJDBC = projetoJDBCRepository.findProjetosComDeterminadoMACompleto(codMA);
        List<Projeto> listProjetos =new ArrayList<>();
        for (ProjetoJDBC p:listProjetosJDBC) {
            listProjetos.add(projetoJDBCDataAssembler.toDomain(p));
        }
        return listProjetos;
    }
}
