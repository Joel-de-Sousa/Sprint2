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
    ProjetoJDBCRepository projetoJDBCRepository;


    @Autowired
    ProjetoJDBCDomainDataAssembler projetoJDBCDomainDataAssembler;



    public Projeto save(Projeto projeto) throws SQLException {
        ProjetoJDBC projetoJDBC = projetoJDBCDomainDataAssembler.toJDBC(projeto);

        ProjetoJDBC savedProjetoJDBC = projetoJDBCRepository.save(projetoJDBC);

        return projetoJDBCDomainDataAssembler.toDomain(savedProjetoJDBC);
    }

    public Optional<Projeto> findById(int codProjeto) throws SQLException {
        Optional<ProjetoJDBC> opProjeto = projetoJDBCRepository.getById(codProjeto);

        if ( opProjeto.isPresent() ) {
            Projeto projeto = projetoJDBCDomainDataAssembler.toDomain(opProjeto.get());
            return Optional.of( projeto );
        }
        else
            return Optional.empty();
    }

    public Optional<Projeto> findByCodEstudante(int codEstudante) throws SQLException {
        Optional<ProjetoJDBC> opProjeto = projetoJDBCRepository.findProjetoByCodeEstudante(codEstudante);

        if ( opProjeto.isPresent() ) {
            Projeto projeto = projetoJDBCDomainDataAssembler.toDomain(opProjeto.get());
            return Optional.of( projeto );
        }
        else
            return Optional.empty();
    }

    public List<Projeto> findProjetosConcluidos() throws SQLException {
        List<ProjetoJDBC> listProjetosJDBC = projetoJDBCRepository.findProjetosConcluidos();
        List<Projeto> listProjetos =new ArrayList<>();
        for (ProjetoJDBC p:listProjetosJDBC) {
            listProjetos.add(projetoJDBCDomainDataAssembler.toDomain(p));
        }
        return listProjetos;
    }

    public List<Projeto> findProjetosDatasAvaliacao (Date fromDate, Date toDate) throws SQLException {
        List<ProjetoJDBC> listProjetosJDBC = projetoJDBCRepository.findProjetosDatasAvaliacao(fromDate, toDate);
        List<Projeto> listProjetos =new ArrayList<>();
        for (ProjetoJDBC p:listProjetosJDBC) {
            listProjetos.add(projetoJDBCDomainDataAssembler.toDomain(p));
        }
        return listProjetos;
    }
}
