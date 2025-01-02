package control.invest.IC.controller;

import control.invest.IC.models.IrpfModel;
import control.invest.IC.services.ImageToTextService;
import control.invest.IC.services.IrpfService;
import control.invest.IC.services.PdfToImageService;
import control.invest.IC.services.StringExtractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PdfController {
    private final PdfToImageService pdfToImageService;
    private final ImageToTextService imageToTextService;


    public PdfController(PdfToImageService pdfToImageService, ImageToTextService imageToTextService) {
        this.pdfToImageService = pdfToImageService;
        this.imageToTextService = imageToTextService;
    }

    @PostMapping("/irpf/extract-pdf")
    public ResponseEntity<Map<String, Object>> extractPdf(@RequestPart("file") MultipartFile file) {
        IrpfModel irpfModel = new IrpfModel();
        IrpfService irpfService = new IrpfService();
        //if (irpfService.extensionVerify(file.getName())) {
        try {
            File tempPdf = File.createTempFile("uploaded", ".pdf");
            file.transferTo(tempPdf);

            Rectangle regionValores = new Rectangle(510, 230, 70, 450);
            List<File> imageFilesValores = pdfToImageService.convertPdfToImages(tempPdf, regionValores, "valores");
            String extractedValor = imageToTextService.extractTextFromImages(imageFilesValores);


            Rectangle regionCabecalho = new Rectangle(30, 125, 420, 80);
            List<File> imageFilesCabecalho = pdfToImageService.convertPdfToImages(tempPdf, regionCabecalho, "cabecalho");
            String extractedCabecalho = imageToTextService.extractTextFromImages(imageFilesCabecalho);


            Rectangle regionPagamentos = new Rectangle(15, 655, 520, 100);
            List<File> imageFilespagamentos = pdfToImageService.convertPdfToImages(tempPdf, regionPagamentos, "pagamentos");


            tempPdf.delete();
            StringExtractService stringExtractService = new StringExtractService();

            irpfModel = stringExtractService.extrairValores(extractedValor);
            Map<String, Object> rendimentos = new HashMap<>();
            Map<String, Object> rendimentosIsentos = new HashMap<>();
            Map<String, Object> rendimentosExclusivos = new HashMap<>();
            Map<String, Object> rendimentosAcumulados = new HashMap<>();
            Map<String, Object> pagamentosEfetuados = new HashMap<>();
            Map<String, Object> dados = new HashMap<>();
            Map<String, Object> response = new HashMap<>();


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
                rendimentosIsentos.put("provApos-e-AcidenteTrabalho", irpfModel.getAcidenteTrabalho());
                rendimentosIsentos.put("lucros-e-dividendos", irpfModel.getLucroDividendo());
                rendimentosIsentos.put("pagamentosAoTitular", irpfModel.getPagamentosRecebidos());
                rendimentosIsentos.put("rescisaoContratoDeTrabalho", irpfModel.getRescisao());
                rendimentosIsentos.put("JurosDeMora", irpfModel.getJurosMora());
                rendimentosIsentos.put("Outros", irpfModel.getOutrosRendimentosIsentos());
                //Rendimentos Exclusivos
                rendimentosExclusivos.put("decTercSal", irpfModel.getDecTercSal());
                rendimentosExclusivos.put("impRetidoDecTerc", irpfModel.getImpRetDecTerc());
                rendimentosExclusivos.put("outrosRendExclusivos", irpfModel.getOutrosRendExclusivo());
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
                dados.put("cpf", irpfModel.getCpf());
                dados.put("pessoaFisica", irpfModel.getNomePessoaFisica());
            }

            response.put("dados", dados);
            response.put("rendimento", rendimentos);
            response.put("rendimentosIsentos",rendimentosIsentos);
            response.put("rendimentosAcumulados",rendimentosAcumulados);
            response.put("rendimentosExclusivos", rendimentosExclusivos);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

