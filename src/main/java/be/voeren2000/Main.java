package be.voeren2000;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import org.apache.commons.cli.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Options options = new Options();
        options.addOption("o","output", true, "The output of the merged file");
        Option inputOption = new Option("i", "input", true, "The input(s) of the files to merge");
        inputOption.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(inputOption);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.getOptionValues('i') == null) {
            System.err.println("No input files provided");
            System.exit(1);
        }
        if (cmd.getOptionValues('o') == null) {
            System.err.println("No output file provided");
            System.exit(1);
        }
        try (Document document = new Document()) {
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(cmd.getOptionValue('o')));
            document.open();
            for (String file : cmd.getOptionValues('i')) {
                PdfReader reader = new PdfReader(file);
                int pages = reader.getNumberOfPages();
                for (int i = 1; i <= pages; i++) {
                    PdfImportedPage page = copy.getImportedPage(reader, i);
                    copy.addPage(page);
                }
                copy.freeReader(reader);
            }
            copy.close();
        }
    }
}
