package co.bharat.sudarshansaur.util;

import co.bharat.sudarshansaur.entity.Dealers;
import co.bharat.sudarshansaur.entity.Stockists;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;


import javax.servlet.http.HttpServletResponse;


import java.io.IOException;

public class AgreementFactory {
    public static void generateStockistAgreement(HttpServletResponse response, Stockists stockist) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font fontTiltle = FontFactory.getFont(FontFactory.COURIER_BOLD,  18, Font.UNDERLINE);
        Font fontPara = FontFactory.getFont(FontFactory.COURIER,  12, Font.NORMAL);
        Font fontParaBoldUnderline = FontFactory.getFont(FontFactory.COURIER_BOLD,  12, Font.UNDERLINE);
        Font fontParaBold = FontFactory.getFont(FontFactory.COURIER_BOLD,  12, Font.NORMAL);

        document.add(new Phrase("\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));

        document.add(new Paragraph("AGREEMENT WITH DEALER", fontTiltle));
        document.add(new Phrase("\n"));
        document.add(new Paragraph(StockistAgreementText.para1(stockist), fontPara));
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("AND\n", fontParaBoldUnderline));
        document.add(new Paragraph("\nM/S\n", fontParaBold));
        document.add(new Paragraph(StockistAgreementText.para2(stockist), fontPara));
        document.add(new Paragraph("\n(Hereinafter referred as “the Dealer” of the second part)\n", fontParaBold));
        document.add(new Paragraph(StockistAgreementText.para3(), fontParaBold));
        document.add(new Paragraph(StockistAgreementText.para4(stockist), fontPara));
        document.add(new Paragraph(StockistAgreementText.para5(stockist), fontPara));
        document.add(new Phrase("\n"));
        document.add(new Paragraph("TERMS AND CONDITIONS", fontParaBoldUnderline));
        document.add(new Phrase("\n"));
        document.add(new Phrase("\n"));
        document.add(new Paragraph(StockistAgreementText.para6(), fontPara));
        document.add(new Paragraph("Authorized Person:", fontPara));
        document.add(new Paragraph("For SUDARSHAN SAUR SHAKTI PVT. LTD.\n\n\n\n\n", fontPara));
        document.add(new Paragraph("Name: _________________________   Signature: ______________\n\n\n\n", fontPara));
        document.add(new Paragraph(StockistAgreementText.para7(stockist), fontPara));
        document.add(new Phrase("\n"));
        document.add(new Phrase("\n"));
        document.add(new Paragraph("This is electronically generated does not require any signature and stamp.",fontPara));
        document.close();
    }

    public static void generateDealerAgreement(HttpServletResponse response, Dealers dealers) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font fontTiltle = FontFactory.getFont(FontFactory.COURIER_BOLD,  18, Font.UNDERLINE);
        Font fontPara = FontFactory.getFont(FontFactory.COURIER,  12, Font.NORMAL);
        Font fontParaBoldUnderline = FontFactory.getFont(FontFactory.COURIER_BOLD,  12, Font.UNDERLINE);
        Font fontParaBold = FontFactory.getFont(FontFactory.COURIER_BOLD,  12, Font.NORMAL);

        document.add(new Phrase("\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));

        document.add(new Paragraph("AGREEMENT WITH DEALER", fontTiltle));
        document.add(new Phrase("\n"));
        document.add(new Paragraph(DealerAgreementText.para1(dealers), fontPara));
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("AND\n", fontParaBoldUnderline));
        document.add(new Paragraph("\nM/S\n", fontParaBold));
        document.add(new Paragraph(DealerAgreementText.para2(dealers), fontPara));
        document.add(new Paragraph("\n(Hereinafter referred as “the Dealer” of the second part)\n", fontParaBold));
        document.add(new Paragraph(DealerAgreementText.para3(), fontParaBold));
        document.add(new Paragraph(DealerAgreementText.para4(dealers), fontPara));
        document.add(new Paragraph(DealerAgreementText.para5(dealers), fontPara));
        document.add(new Phrase("\n"));
        document.add(new Paragraph("TERMS AND CONDITIONS", fontParaBoldUnderline));
        document.add(new Phrase("\n"));
        document.add(new Phrase("\n"));
        document.add(new Paragraph(DealerAgreementText.para6(), fontPara));
        document.add(new Paragraph("Authorized Person:", fontPara));
        document.add(new Paragraph("For SUDARSHAN SAUR SHAKTI PVT. LTD.\n\n\n\n\n", fontPara));
        document.add(new Paragraph("Name: _________________________   Signature: ______________\n\n\n\n", fontPara));
        document.add(new Paragraph(DealerAgreementText.para7(dealers), fontPara));
        document.add(new Phrase("\n"));
        document.add(new Phrase("\n"));
        document.add(new Paragraph("This is electronically generated does not require any signature and stamp.",fontPara));
        document.close();
    }

}
