package Sprint.WsProjeto.service;

import Sprint.WsProjeto.DTO.*;
import Sprint.WsProjeto.DTO.assembler.AvaliacaoDomainDTOAssembler;
import Sprint.WsProjeto.DTO.assembler.ProjetoDomainDTOAssembler;
import Sprint.WsProjeto.datamodel.REST.EdicaoRestDTO;
import Sprint.WsProjeto.datamodel.REST.PropostaRestDTO;
import Sprint.WsProjeto.datamodel.REST.UtilizadorRestDTO;
import Sprint.WsProjeto.domain.entities.Avaliacao;
import Sprint.WsProjeto.domain.entities.Projeto;
import Sprint.WsProjeto.domain.factories.IProjetoFactory;
import Sprint.WsProjeto.repositories.EdicaoWebRepository;
import Sprint.WsProjeto.repositories.ProjetoRepository;
import Sprint.WsProjeto.repositories.PropostaWebRepository;
import Sprint.WsProjeto.repositories.UtilizadorWebRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ProtocolException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    @Autowired
    ProjetoDomainDTOAssembler projetoDomainDTOAssembler;

    @Autowired
    IProjetoFactory projetoFactory;

    @Autowired
    ProjetoRepository projetoRepository;

    @Autowired
    UtilizadorWebRepository utilizadorWebRepository;

    @Autowired
    PropostaWebRepository propostaWebRepository;

    @Autowired
    EdicaoWebRepository edicaoWebRepository;

    @Autowired
    AvaliacaoService avaliacaoService;

    @Autowired
    AvaliacaoDomainDTOAssembler avaliacaoDomainDTOAssembler;


    public ProjetoService() {
    }

    public ProjetoDTO createAndSaveProjeto(NewProjetoInfoDto projetoInfoDto) throws Exception {


        Optional<PropostaRestDTO> propostaRestDTO = propostaWebRepository.findPropostaByCode(projetoInfoDto.getCodProposta());
        if (propostaRestDTO.isPresent()) {
            Projeto projeto = projetoFactory.createProjeto(projetoInfoDto.getCodProposta(), projetoInfoDto.getCodEstudante());

            Projeto oProjetoSaved = projetoRepository.save(projeto);


            Optional<EdicaoRestDTO> edicaoRestDTO = edicaoWebRepository.findEdicaoByCode(propostaRestDTO.get().getCodEdicao());
            List<MomentoAvaliacaoDTO> momentoAvaliacaoList = edicaoRestDTO.get().getMomentoAvaliacaoList();
            for (MomentoAvaliacaoDTO momentoAvaliacaoDTO : momentoAvaliacaoList) {

                Avaliacao avaliacao = avaliacaoService.createAndSaveAvaliacao(momentoAvaliacaoDTO.getCodMomentoAvaliacao(),oProjetoSaved.getCodProjeto());

            }
            ProjetoDTO oProjetoDTO = projetoDomainDTOAssembler.toDto(oProjetoSaved);

            return oProjetoDTO;
        } else
            throw new ProtocolException("A Proposta não existe");


    }

    public ProjetoDTO findProjetoByCode(int codProjeto) throws SQLException {

        Optional<Projeto> opProjeto = projetoRepository.findById(codProjeto);

        if (opProjeto.isPresent()) {
            Projeto oProjeto = opProjeto.get();
            ProjetoDTO oProjetoDTO = projetoDomainDTOAssembler.toDto(oProjeto);

            return oProjetoDTO;
        } else return null;
    }

    public ProjetoDTO findProjetoByCodeEstudante(int codEstudante) throws SQLException {

        Optional<Projeto> opProjeto = projetoRepository.findByCodEstudante(codEstudante);

        if (opProjeto.isPresent()) {
            Projeto oProjeto = opProjeto.get();
            ProjetoDTO oProjetoDTO = projetoDomainDTOAssembler.toDto(oProjeto);

            return oProjetoDTO;
        } else return null;
    }


    public Optional<UtilizadorRestDTO> findUtilizadorByCode(int codUtilizador) {

        Optional<UtilizadorRestDTO> oUtilizadorCode = utilizadorWebRepository.findUtilizadorByCode(codUtilizador);

        return oUtilizadorCode;
    }

    public Optional<PropostaRestDTO> findPropostaByCode(int codProposta) {

        Optional<PropostaRestDTO> oPropostaCode = propostaWebRepository.findPropostaByCode(codProposta);

        return oPropostaCode;
    }

    public List<ProjetoDTO> findProjetosPorCodigoRUC(int codRUC) throws Exception {
        List<ProjetoDTO> listProjeto = new ArrayList<>();
        List<EdicaoRestDTO> listEdicoes = edicaoWebRepository.getListaEdicoesByCodRUC(codRUC);
        for (EdicaoRestDTO edicao : listEdicoes) {
            List<PropostaRestDTO> listPropostas = propostaWebRepository.findAllPropostasAceitesByCodEdicao(edicao.getCodEdicao());
            for (PropostaRestDTO proposta : listPropostas) {
                Projeto projeto = projetoRepository.findProjetoByCodProposta(proposta.getCodProposta());
                ProjetoDTO projetoDTO = projetoDomainDTOAssembler.toDto(projeto);
                listProjeto.add(projetoDTO);
            }
        }
        return listProjeto;

    }

    public List<ProjetoDTO> findProjetosConcluidos(int codRuc) throws Exception {

        List<EdicaoRestDTO> edicaoRestDTO=edicaoWebRepository.getListaEdicoesByCodRUC(codRuc);
        if(!edicaoRestDTO.isEmpty()) {
        List<Projeto> listProjetos = projetoRepository.findProjetosConcluidos();

        List<ProjetoDTO> listProjetoDTO = new ArrayList<>();
        for (Projeto projeto : listProjetos) {
            ProjetoDTO projetoDTO = projetoDomainDTOAssembler.toDto(projeto);
            listProjetoDTO.add(projetoDTO);
        }
        return listProjetoDTO;}
        else
            throw new Exception("O Codigo introduzido não pertence a um RUC");

    }

    public List<ProjetoDTO> findProjetosByCodDocente(int codRuc, int codDocente) throws Exception {
        List<EdicaoRestDTO> edicaoRestDTO=edicaoWebRepository.getListaEdicoesByCodRUC(codRuc);
        if(!edicaoRestDTO.isEmpty()){
        List<Projeto> listProjetos = projetoRepository.findProjetosByCodDocente(codDocente);

        List<ProjetoDTO> listProjetoDTO = new ArrayList<>();
        for (Projeto projeto : listProjetos) {
            ProjetoDTO projetoDTO = projetoDomainDTOAssembler.toDto(projeto);
            listProjetoDTO.add(projetoDTO);
        }
        return listProjetoDTO;}
        else {
            throw new Exception("O Codigo introduzido não pertence a um RUC");
        }
    }

    public List<ProjetoDTO> findProjetosComDeterminadoMACompleto(int codRuc,int codMA) throws Exception {

        List<EdicaoRestDTO> edicaoRestDTO=edicaoWebRepository.getListaEdicoesByCodRUC(codRuc);
        if(!edicaoRestDTO.isEmpty()) {
            List<Projeto> listProjetos = projetoRepository.findProjetosComDeterminadoMACompleto(codMA);

            List<ProjetoDTO> listProjetoDTO = new ArrayList<>();
            for (Projeto projeto : listProjetos) {
                ProjetoDTO projetoDTO = projetoDomainDTOAssembler.toDto(projeto);
                listProjetoDTO.add(projetoDTO);
            }
            return listProjetoDTO;
        }
        else
            throw new Exception("O Codigo introduzido não pertence a um RUC");
    }
    
    public List<ProjetoDTO> findProjetosDatasAvaliacao(int codRuc,Date fromDate, Date toDate) throws Exception {
        List<EdicaoRestDTO> edicaoRestDTO=edicaoWebRepository.getListaEdicoesByCodRUC(codRuc);
        if(!edicaoRestDTO.isEmpty()) {

        List<Projeto> listProjetos = projetoRepository.findProjetosDatasAvaliacao(fromDate, toDate);

        List<ProjetoDTO> listProjetoDTO = new ArrayList<>();
        for (Projeto projeto : listProjetos) {
            ProjetoDTO projetoDTO = projetoDomainDTOAssembler.toDto(projeto);
            listProjetoDTO.add(projetoDTO);
        }
        return listProjetoDTO;}
        else
        throw new Exception("O Codigo introduzido não pertence a um RUC");
    }


    public List<ProjetoDTO> findProjetosByNifOrganizacao(long nifOrganizacao) throws Exception {

        List<ProjetoDTO> listFiltradaProjetos = new ArrayList<>();

        List<PropostaRestDTO> listPropostas = propostaWebRepository.findAllPropostasAceitesByNif (nifOrganizacao);
        for (PropostaRestDTO proposta : listPropostas) {
            Projeto projeto = projetoRepository.findProjetoByCodProposta(proposta.getCodProposta());
            ProjetoDTO projetoDTO = projetoDomainDTOAssembler.toDto(projeto);
            listFiltradaProjetos.add(projetoDTO);

        }return listFiltradaProjetos;

    }

    /*public void updateEstado(Projeto.Estado estado, int codProjeto) throws Exception {

        Optional<Projeto> opProjeto = projetoRepository.findById(codProjeto);

        if (opProjeto.isPresent()) {
            if (!opProjeto.get().getEstado().equals(Projeto.Estado.CONCLUIDO)) {

                opProjeto.get().setCodProjeto(codProjeto);
                opProjeto.get().setEstado(estado);

                Projeto projetoUpdated = projetoRepository.update(opProjeto.get());
                ProjetoDTO projetoUpdatedDTO = projetoDomainDTOAssembler.toDto(projetoUpdated);


            }
            throw new Exception("O projeto já está com estado concluído");
        }
        throw new Exception("O projeto não consta na base de dados");
    }*/

}