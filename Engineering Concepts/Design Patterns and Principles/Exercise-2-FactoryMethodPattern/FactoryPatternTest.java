public class FactoryPatternTest {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println(" Factory Method Pattern - Document Management ");
        System.out.println("==============================================");
        System.out.println();

        DocumentFactory wordFactory = new WordDocumentFactory();
        Document wordDoc = wordFactory.createDocument();

        DocumentFactory pdfFactory = new PdfDocumentFactory();
        Document pdfDoc = pdfFactory.createDocument();

        DocumentFactory excelFactory = new ExcelDocumentFactory();
        Document excelDoc = excelFactory.createDocument();

        wordDoc.open();     
        pdfDoc.open();      
        excelDoc.open();    

        System.out.println();
        System.out.println("==============================================");
        System.out.println(" All documents created and opened successfully!");
        System.out.println("==============================================");
    }
}
