package control.invest.IC.controller;

import control.invest.IC.models.ContribuinteModel;
import control.invest.IC.models.IrpfModel;
import control.invest.IC.repositories.ContribuinteRepository;
import control.invest.IC.repositories.IrpfRepository;
import control.invest.IC.service.ImageToTextService;
import control.invest.IC.service.PdfToImageService;
import control.invest.IC.service.StringExtractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PdfController {
    private final PdfToImageService pdfToImageService;
    private final ImageToTextService imageToTextService;

    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    IrpfRepository irpfRepository;


    public PdfController(PdfToImageService pdfToImageService, ImageToTextService imageToTextService) {
        this.pdfToImageService = pdfToImageService;
        this.imageToTextService = imageToTextService;
    }

    @PostMapping("/irpf/saveIrpf/{cpfContribuinte}")
    public ResponseEntity<String> saveIrpf(@RequestBody IrpfModel irpfModel, @PathVariable String cpfContribuinte) {
        ContribuinteModel result = contribuinteRepository.findByCpf(cpfContribuinte);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CPF informado " + cpfContribuinte + " não foi encontrado, verifique o CPF e tente novamente!");
        }
        irpfModel.setContribuinteModel(result);
        irpfRepository.save(irpfModel);

        return ResponseEntity.status(HttpStatus.OK).body("Informe de rendimentos salvo com sucesso!");
    }

    @PostMapping("/irpf/extract-pdf")
    public ResponseEntity<Map<String, Object>> extractPdf(@RequestPart("file") MultipartFile file) {
        IrpfModel irpfModel = new IrpfModel();
        try {
            File tempPdf = File.createTempFile("uploaded", ".pdf");
            file.transferTo(tempPdf);

            Rectangle regionValores = new Rectangle(510, 230, 70, 450);
            List<File> imageFilesValores = pdfToImageService.convertPdfToImages(tempPdf, regionValores, "valores");
            String extractedValor = imageToTextService.extractTextFromImages(imageFilesValores);


            Rectangle regionCabecalho = new Rectangle(30, 125, 420, 80);
            List<File> imageFilesCabecalho = pdfToImageService.convertPdfToImages(tempPdf, regionCabecalho, "cabecalho");
            String extractedCabecalho = imageToTextService.extractTextFromImages(imageFilesCabecalho);


            Rectangle regionPagamentos = new Rectangle(15, 675, 520, 100);
            List<File> imageFilespagamentos = pdfToImageService.convertPdfToImages(tempPdf, regionPagamentos, "pagamentos");
            String extractedPagamentos = imageToTextService.extractTextFromImages(imageFilespagamentos);

            tempPdf.delete();
            StringExtractService stringExtractService = new StringExtractService();

            irpfModel = stringExtractService.extrairValores(extractedValor);
            Map<String, Object> rendimentos = new LinkedHashMap<>();
            Map<String, Object> rendimentosIsentos = new LinkedHashMap<>();
            Map<String, Object> rendimentosAcumulados = new LinkedHashMap<>();
            Map<String, Object> pagamentosEfetuados = new LinkedHashMap<>();
            Map<String, Object> dados = new LinkedHashMap<>();
            Map<String, Object> response = new LinkedHashMap<>();


            if (irpfModel != null) {
                //rendimentos isentos e deduções
                rendimentos.put("rendimentosTotais", irpfModel.getRendimentosTotais());
                rendimentos.put("prevSocial", irpfModel.getPrevSocial());//dedução
                rendimentos.put("impostoRetido", irpfModel.getImpostoRetido());//recebe este valor de volta no final do calculo, não entra como dedução
                rendimentos.put("decTercSal", irpfModel.getDecTercSal());
                rendimentos.put("impRendDecTerc", irpfModel.getImpRetDecTerc());

                //rendimentos isentos
                rendimentosIsentos.put("parcIsentaApos", irpfModel.getParcelaIsentaApos());
                rendimentosIsentos.put("decTercApos", irpfModel.getParcelaIsentaDecTerc());
                rendimentosIsentos.put("ajudaDeCusto", irpfModel.getAjudaCusto());
                rendimentosIsentos.put("acidenteTrabalho", irpfModel.getAcidenteTrabalho());
                rendimentosIsentos.put("dividendos", irpfModel.getLucroDividendo());
                rendimentosIsentos.put("pagamentosTitular", irpfModel.getPagamentosRecebidos());
                rendimentosIsentos.put("rescisaoContrato", irpfModel.getRescisao());
                rendimentosIsentos.put("jurosMora", irpfModel.getJurosMora());
                rendimentosIsentos.put("outrosRendimentosIsentos", irpfModel.getOutrosRendimentosIsentos());


                //rendimentos recebidos acumuladamente
                rendimentosAcumulados.put("totalRendimentosTrib", irpfModel.getTotalRendTributavel());
                rendimentosAcumulados.put("despesasJudiciais", irpfModel.getDespesaAcaoJud());
                rendimentosAcumulados.put("contribPrevSocial", irpfModel.getContribPrevSocial());
                rendimentosAcumulados.put("pensaoRecebida", irpfModel.getPensaoRecebida());
                rendimentosAcumulados.put("impRetidoRendRec", irpfModel.getImpostoRetidoRendRec());
                rendimentosAcumulados.put("rendIsentos", irpfModel.getRendIsentos());
            }
            irpfModel = stringExtractService.extrairCabecalho(extractedCabecalho);
            if (irpfModel != null) {
                dados.put("cnpj", irpfModel.getFontePagadoraCnpj());
                dados.put("nomeEmpresa", irpfModel.getFontePagadoraNomeEmpresa());
            }
            ArrayList<String> pagamentos = stringExtractService.extrairPagamentos(extractedPagamentos);
            ArrayList<Double> pagamentosValores = stringExtractService.extrairPagamentosValores(extractedPagamentos);

            response.put("dados", dados);

            response.put("rendimento", rendimentos);
            if (!pagamentos.isEmpty() && !pagamentosValores.isEmpty()) {
                for (int i = 0; i < pagamentos.size(); i++) {
                    pagamentosEfetuados.put("pagamento " + (i + 1), pagamentos.get(i));
                    pagamentosEfetuados.put("valor " + (i + 1), pagamentosValores.get(i));
                }
            } else {
                pagamentosEfetuados.put("mensagem", "Nenhum pagamento realizado!");
            }
            response.put("rendimentosIsentos", rendimentosIsentos);
            response.put("rendimentosAcumulados", rendimentosAcumulados);
            response.put("PagamentosEfetuados", pagamentosEfetuados);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

